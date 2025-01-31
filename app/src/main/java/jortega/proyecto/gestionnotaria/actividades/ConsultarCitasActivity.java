package jortega.proyecto.gestionnotaria.actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jortega.proyecto.gestionnotaria.R;
import jortega.proyecto.gestionnotaria.adaptadores.CitasAdapter;
import jortega.proyecto.gestionnotaria.adaptadores.SpinnerAdapter;
import jortega.proyecto.gestionnotaria.modelos.Cita;
import jortega.proyecto.gestionnotaria.modelos.Despacho;
import jortega.proyecto.gestionnotaria.modelos.SpinnerItem;
import jortega.proyecto.gestionnotaria.modelos.Usuario;
import jortega.proyecto.gestionnotaria.red.ApiService;
import jortega.proyecto.gestionnotaria.red.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultarCitasActivity extends AppCompatActivity {

    private ApiService apiService;
    private Spinner filtroSpinner, clienteNotarioSpinner;
    private RecyclerView citasRecyclerView;
    private List<SpinnerItem> clientesList, notariosList;
    private List<Cita> citasList;
    private String selectedFiltro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_citas);

        apiService = RetrofitClient.getClient("http://192.168.222.69:5000/").create(ApiService.class);

        filtroSpinner = findViewById(R.id.filtroSpinner);
        clienteNotarioSpinner = findViewById(R.id.clienteNotarioSpinner);
        citasRecyclerView = findViewById(R.id.citasRecyclerView);
        citasRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configurar el Spinner de filtro
        List<String> filtros = new ArrayList<>();
        filtros.add("Cliente");
        filtros.add("Notario");
        ArrayAdapter<String> filtroAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filtros);
        filtroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filtroSpinner.setAdapter(filtroAdapter);

        filtroSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFiltro = filtros.get(position);
                clienteNotarioSpinner.setVisibility(View.VISIBLE);
                if (selectedFiltro.equals("Cliente")) {
                    obtenerClientes();
                } else if (selectedFiltro.equals("Notario")) {
                    obtenerNotarios();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se hace nada
            }
        });

        clienteNotarioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerItem selectedItem = (SpinnerItem) clienteNotarioSpinner.getSelectedItem();
                int selectedId = selectedItem.getId();
                obtenerCitasFiltradas(selectedFiltro, selectedId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se hace nada
            }
        });
    }

    private void obtenerClientes() {
        Call<List<Usuario>> clientesCall = apiService.getClientes();
        clientesCall.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    clientesList = new ArrayList<>();
                    for (Usuario cliente : response.body()) {
                        clientesList.add(new SpinnerItem(cliente.getId(), cliente.getNombre() + " " + cliente.getApellido()));
                    }
                    SpinnerAdapter adapter = new SpinnerAdapter(ConsultarCitasActivity.this, clientesList);
                    clienteNotarioSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(ConsultarCitasActivity.this, "Error al obtener clientes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerNotarios() {
        Call<List<Usuario>> notariosCall = apiService.getNotarios();
        notariosCall.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    notariosList = new ArrayList<>();
                    for (Usuario notario : response.body()) {
                        notariosList.add(new SpinnerItem(notario.getId(), notario.getNombre() + " " + notario.getApellido()));
                    }
                    SpinnerAdapter adapter = new SpinnerAdapter(ConsultarCitasActivity.this, notariosList);
                    clienteNotarioSpinner.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(ConsultarCitasActivity.this, "Error al obtener notarios", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void obtenerCitasFiltradas(String filtro, int id) {
        Call<List<Cita>> citasCall;
        if (filtro.equals("Cliente")) {
            citasCall = apiService.getCitasPorCliente(id);
        } else {
            citasCall = apiService.getCitasPorNotario(id);
        }
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
                Toast.makeText(ConsultarCitasActivity.this, "Error al obtener citas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
