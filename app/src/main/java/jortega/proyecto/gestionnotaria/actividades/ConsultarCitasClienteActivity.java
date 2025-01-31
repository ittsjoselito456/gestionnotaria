package jortega.proyecto.gestionnotaria.actividades;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import jortega.proyecto.gestionnotaria.R;
import jortega.proyecto.gestionnotaria.adaptadores.CitasAdapter;
import jortega.proyecto.gestionnotaria.modelos.Cita;
import jortega.proyecto.gestionnotaria.red.ApiService;
import jortega.proyecto.gestionnotaria.red.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultarCitasClienteActivity extends AppCompatActivity {

    private ApiService apiService;
    private RecyclerView citasRecyclerView;
    private List<Cita> citasList;
    private int clienteId; // ID del cliente autenticado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_citas_cliente);

        apiService = RetrofitClient.getClient("http://192.168.222.69:5000/").create(ApiService.class);

        citasRecyclerView = findViewById(R.id.citasRecyclerView);
        citasRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener el ID del cliente autenticado (puedes pasarlo desde la actividad anterior)
        clienteId = getIntent().getIntExtra("clienteId", -1);

        if (clienteId != -1) {
            obtenerCitasPorCliente(clienteId);
        } else {
            Toast.makeText(this, "ID de cliente no válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerCitasPorCliente(int clienteId) {
        Call<List<Cita>> citasCall = apiService.getCitasPorCliente(clienteId);
        citasCall.enqueue(new Callback<List<Cita>>() {
            @Override
            public void onResponse(Call<List<Cita>> call, Response<List<Cita>> response) {
                if (response.isSuccessful()) {
                    citasList = response.body();
                    CitasAdapter adapter = new CitasAdapter(citasList);
                    citasRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Cita>> call, Throwable t) {
                Toast.makeText(ConsultarCitasClienteActivity.this, "Error al obtener citas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
