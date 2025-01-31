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
import jortega.proyecto.gestionnotaria.modelos.Despacho;
import jortega.proyecto.gestionnotaria.red.ApiService;
import jortega.proyecto.gestionnotaria.red.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EliminarDespachoActivity extends AppCompatActivity {

    private ApiService apiService;
    private ListView despachosListView;
    private Button btnEliminar;
    private List<Despacho> despachosList;
    private int selectedDespachoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_despacho);

        apiService = RetrofitClient.getClient("http://192.168.222.69:5000/").create(ApiService.class);

        despachosListView = findViewById(R.id.despachosListView);
        btnEliminar = findViewById(R.id.btn_eliminar);

        // Llamar a la API para obtener la lista de despachos disponibles
        Call<List<Despacho>> despachosCall = apiService.getDespachos();
        despachosCall.enqueue(new Callback<List<Despacho>>() {
            @Override
            public void onResponse(Call<List<Despacho>> call, Response<List<Despacho>> response) {
                if (response.isSuccessful()) {
                    despachosList = response.body();
                    // Llenar el ListView con los despachos disponibles
                    llenarListViewDespachos();
                }
            }

            @Override
            public void onFailure(Call<List<Despacho>> call, Throwable t) {
                Toast.makeText(EliminarDespachoActivity.this, "Error al obtener despachos", Toast.LENGTH_SHORT).show();
            }
        });

        despachosListView.setOnItemClickListener((parent, view, position, id) -> {
            Despacho selectedDespacho = despachosList.get(position);
            selectedDespachoId = selectedDespacho.getId();
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDespachoId != -1) {
                    eliminarDespacho(selectedDespachoId);
                } else {
                    Toast.makeText(EliminarDespachoActivity.this, "Selecciona un despacho para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void llenarListViewDespachos() {
        if (despachosList != null) {
            List<String> despachosDescriptions = new ArrayList<>();
            for (Despacho despacho : despachosList) {
                despachosDescriptions.add("Despacho ID: " + despacho.getId() + " - Nombre: " + despacho.getNombre());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, despachosDescriptions);
            despachosListView.setAdapter(adapter);
        }
    }

    private void eliminarDespacho(int despachoId) {
        Call<Void> deleteDespachoCall = apiService.deleteDespacho(despachoId);
        deleteDespachoCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EliminarDespachoActivity.this, "Despacho eliminado", Toast.LENGTH_SHORT).show();
                    // Actualizar la lista de despachos
                    Call<List<Despacho>> despachosCall = apiService.getDespachos();
                    despachosCall.enqueue(new Callback<List<Despacho>>() {
                        @Override
                        public void onResponse(Call<List<Despacho>> call, Response<List<Despacho>> response) {
                            if (response.isSuccessful()) {
                                despachosList = response.body();
                                // Llenar el ListView con los despachos disponibles
                                llenarListViewDespachos();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<Despacho>> call, Throwable t) {
                            Toast.makeText(EliminarDespachoActivity.this, "Error al obtener despachos", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EliminarDespachoActivity.this, "Error al eliminar despacho", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
