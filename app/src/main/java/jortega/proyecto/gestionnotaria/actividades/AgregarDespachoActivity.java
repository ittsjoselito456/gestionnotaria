package jortega.proyecto.gestionnotaria.actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import jortega.proyecto.gestionnotaria.R;
import jortega.proyecto.gestionnotaria.modelos.Despacho;
import jortega.proyecto.gestionnotaria.red.ApiService;
import jortega.proyecto.gestionnotaria.red.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarDespachoActivity extends AppCompatActivity {

    private ApiService apiService;
    private EditText nombreDespacho, direccionDespacho;
    private Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_despacho);

        apiService = RetrofitClient.getClient("http://192.168.222.69:5000/").create(ApiService.class);

        nombreDespacho = findViewById(R.id.nombreDespacho);
        direccionDespacho = findViewById(R.id.direccionDespacho);
        btnAgregar = findViewById(R.id.btn_agregar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarDespacho();
            }
        });
    }

    private void agregarDespacho() {
        String nombre = nombreDespacho.getText().toString().trim();
        String direccion = direccionDespacho.getText().toString().trim();

        if (nombre.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(AgregarDespachoActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Despacho despacho = new Despacho();
        despacho.setNombre(nombre);
        despacho.setDireccion(direccion);

        Call<Void> addDespachoCall = apiService.addDespacho(despacho);
        addDespachoCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AgregarDespachoActivity.this, "Despacho agregado", Toast.LENGTH_SHORT).show();
                    nombreDespacho.setText("");
                    direccionDespacho.setText("");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AgregarDespachoActivity.this, "Error al agregar despacho", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
