package jortega.proyecto.gestionnotaria.actividades;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import jortega.proyecto.gestionnotaria.utils.NotificationUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CrearCitaClienteActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NOTIFICATION = 1;
    private ApiService apiService;
    private Spinner notarios, despachos;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnCrearCita;
    private List<SpinnerItem> notariosList;
    private List<SpinnerItem> despachosList;
    private int clienteId; // ID del cliente autenticado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cita_cliente);

        apiService = RetrofitClient.getClient("http://192.168.222.69:5000/").create(ApiService.class);

        notarios = findViewById(R.id.notarios);
        despachos = findViewById(R.id.despachos);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        btnCrearCita = findViewById(R.id.btn_crear_cita);

        // Obtener el ID del cliente autenticado
        clienteId = getIntent().getIntExtra("clienteId", -1);

        if (clienteId == -1) {
            Toast.makeText(this, "ID de cliente no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Crear el canal de notificación
        NotificationUtils.createNotificationChannel(this);

        // Verificar permisos de notificación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION);
        }

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
                Toast.makeText(CrearCitaClienteActivity.this, "Error al obtener notarios", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(CrearCitaClienteActivity.this, "Error al obtener despachos", Toast.LENGTH_SHORT).show();
            }
        });

        btnCrearCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearCita();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, proceder con la creación de la cita
                crearCita();
            } else {
                // Permiso denegado, manejar el caso
                Toast.makeText(this, "Permiso de notificación denegado", Toast.LENGTH_SHORT).show();
            }
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

    private void crearCita() {
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
                    Toast.makeText(CrearCitaClienteActivity.this, "Cita creada", Toast.LENGTH_SHORT).show();
                    // Mostrar notificación
                    NotificationUtils.showCitaNotification(CrearCitaClienteActivity.this, "Nueva Cita", "Tienes una nueva cita programada.");
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CrearCitaClienteActivity.this, "Error al crear cita", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
