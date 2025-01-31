package jortega.proyecto.gestionnotaria.actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import jortega.proyecto.gestionnotaria.R;
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

public class ReservaCitaActivity extends AppCompatActivity {

    private ApiService apiService;
    private Spinner clientes, notarios, despachos;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnReservar;
    private List<SpinnerItem> clientesList;
    private List<SpinnerItem> notariosList;
    private List<SpinnerItem> despachosList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_cita);

        apiService = RetrofitClient.getClient("http://192.168.222.69:5000/").create(ApiService.class);

        clientes = findViewById(R.id.clientes);
        notarios = findViewById(R.id.notarios);
        despachos = findViewById(R.id.despachos);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        btnReservar = findViewById(R.id.btn_reservar);

        // Llamar a la API para obtener clientes disponibles
        Call<List<Usuario>> clientesCall = apiService.getClientes();
        clientesCall.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    clientesList = new ArrayList<>();
                    for (Usuario cliente : response.body()) {
                        clientesList.add(new SpinnerItem(cliente.getId(), cliente.getNombre() + " " + cliente.getApellido()));
                    }
                    // Llenar el Spinner con los clientes disponibles
                    llenarSpinnerClientes();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(ReservaCitaActivity.this, "Error al obtener clientes", Toast.LENGTH_SHORT).show();
            }
        });

        // Llamar a la API para obtener notarios disponibles
        Call<List<Usuario>> notariosCall = apiService.getNotarios();
        notariosCall.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                if (response.isSuccessful()) {
                    notariosList = new ArrayList<>();
                    for (Usuario notario : response.body()) {
                        notariosList.add(new SpinnerItem(notario.getId(), notario.getNombre() + " " + notario.getApellido()));
                    }
                    // Llenar el Spinner con los notarios disponibles
                    llenarSpinnerNotarios();
                }
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
                Toast.makeText(ReservaCitaActivity.this, "Error al obtener notarios", Toast.LENGTH_SHORT).show();
            }
        });

        // Llamar a la API para obtener despachos disponibles
        Call<List<Despacho>> despachosCall = apiService.getDespachos();
        despachosCall.enqueue(new Callback<List<Despacho>>() {
            @Override
            public void onResponse(Call<List<Despacho>> call, Response<List<Despacho>> response) {
                if (response.isSuccessful()) {
                    despachosList = new ArrayList<>();
                    for (Despacho despacho : response.body()) {
                        despachosList.add(new SpinnerItem(despacho.getId(), despacho.getNombre()));
                    }
                    // Llenar el Spinner con los despachos disponibles
                    llenarSpinnerDespachos();
                }
            }

            @Override
            public void onFailure(Call<List<Despacho>> call, Throwable t) {
                Toast.makeText(ReservaCitaActivity.this, "Error al obtener despachos", Toast.LENGTH_SHORT).show();
            }
        });

        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservarCita();
            }
        });
    }

    private void llenarSpinnerClientes() {
        if (clientesList != null) {
            SpinnerAdapter adapter = new SpinnerAdapter(this, clientesList);
            clientes.setAdapter(adapter);
        }
    }

    private void llenarSpinnerNotarios() {
        if (notariosList != null) {
            SpinnerAdapter adapter = new SpinnerAdapter(this, notariosList);
            notarios.setAdapter(adapter);
        }
    }

    private void llenarSpinnerDespachos() {
        if (despachosList != null) {
            SpinnerAdapter adapter = new SpinnerAdapter(this, despachosList);
            despachos.setAdapter(adapter);
        }
    }

    private void reservarCita() {
        int clienteId = ((SpinnerItem) clientes.getSelectedItem()).getId();
        int notarioId = ((SpinnerItem) notarios.getSelectedItem()).getId();
        int despachoId = ((SpinnerItem) despachos.getSelectedItem()).getId();

        // Obtener la fecha y hora seleccionada
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        // Formatear la fecha y hora en un String
        String fechaCitaText = String.format("%04d-%02d-%02d %02d:%02d", year, month + 1, day, hour, minute);

        Cita cita = new Cita();
        cita.setCliente_id(clienteId);
        cita.setNotario_id(notarioId);
        cita.setDespacho_id(despachoId);
        cita.setFecha_cita(fechaCitaText);

        Call<Void> addCitaCall = apiService.addCita(cita);
        addCitaCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReservaCitaActivity.this, "Cita agregada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ReservaCitaActivity.this, "Error al agregar cita", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
