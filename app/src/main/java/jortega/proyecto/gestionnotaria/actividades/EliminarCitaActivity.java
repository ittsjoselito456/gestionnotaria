package jortega.proyecto.gestionnotaria.actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import jortega.proyecto.gestionnotaria.R;
import jortega.proyecto.gestionnotaria.modelos.Cita;
import jortega.proyecto.gestionnotaria.red.ApiService;
import jortega.proyecto.gestionnotaria.red.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EliminarCitaActivity extends AppCompatActivity {

    private ApiService apiService;
    private ListView citasListView;
    private Button btnEliminar;
    private List<Cita> citasList;
    private int selectedCitaId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_cita);

        apiService = RetrofitClient.getClient("http://192.168.222.69:5000/").create(ApiService.class);

        citasListView = findViewById(R.id.citasListView);
        btnEliminar = findViewById(R.id.btn_eliminar);

        // Llamar a la API para obtener la lista de citas disponibles
        Call<List<Cita>> citasCall = apiService.getCitas();
        citasCall.enqueue(new Callback<List<Cita>>() {
            @Override
            public void onResponse(Call<List<Cita>> call, Response<List<Cita>> response) {
                if (response.isSuccessful()) {
                    citasList = response.body();
                    // Llenar el ListView con las citas disponibles
                    llenarListViewCitas();
                }
            }

            @Override
            public void onFailure(Call<List<Cita>> call, Throwable t) {
                Toast.makeText(EliminarCitaActivity.this, "Error al obtener citas", Toast.LENGTH_SHORT).show();
            }
        });

        citasListView.setOnItemClickListener((parent, view, position, id) -> {
            Cita selectedCita = citasList.get(position);
            selectedCitaId = selectedCita.getId();
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCitaId != -1) {
                    eliminarCita(selectedCitaId);
                } else {
                    Toast.makeText(EliminarCitaActivity.this, "Selecciona una cita para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void llenarListViewCitas() {
        if (citasList != null) {
            List<String> citasDescriptions = new ArrayList<>();
            for (Cita cita : citasList) {
                citasDescriptions.add("Cita ID: " + cita.getId() + " - Fecha: " + cita.getFecha_cita());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, citasDescriptions);
            citasListView.setAdapter(adapter);
        }
    }

    private void eliminarCita(int citaId) {
        Call<Void> deleteCitaCall = apiService.deleteCita(citaId);
        deleteCitaCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EliminarCitaActivity.this, "Cita eliminada", Toast.LENGTH_SHORT).show();
                    // Actualizar la lista de citas
                    Call<List<Cita>> citasCall = apiService.getCitas();
                    citasCall.enqueue(new Callback<List<Cita>>() {
                        @Override
                        public void onResponse(Call<List<Cita>> call, Response<List<Cita>> response) {
                            if (response.isSuccessful()) {
                                citasList = response.body();
                                // Llenar el ListView con las citas disponibles
                                llenarListViewCitas();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Cita>> call, Throwable t) {
                            Toast.makeText(EliminarCitaActivity.this, "Error al obtener citas", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EliminarCitaActivity.this, "Error al eliminar cita", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
