package jortega.proyecto.gestionnotaria.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import jortega.proyecto.gestionnotaria.R;

public class ClienteActivity extends AppCompatActivity {

    private Button btnCrearCita, btnConsultarCitas;
    private int clienteId; // ID del cliente autenticado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        btnCrearCita = findViewById(R.id.btn_create_cita);
        btnConsultarCitas = findViewById(R.id.btn_consultar_citas);

        // Obtener el ID del cliente autenticado
        clienteId = getIntent().getIntExtra("clienteId", -1);

        if (clienteId == -1) {
            // Manejar el caso en que el ID del cliente no sea válido
            finish();
            return;
        }

        btnCrearCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de crear cita y pasar el ID del cliente
                Intent intent = new Intent(ClienteActivity.this, CrearCitaClienteActivity.class);
                intent.putExtra("clienteId", clienteId);
                startActivity(intent);
            }
        });

        btnConsultarCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de consultar citas y pasar el ID del cliente
                Intent intent = new Intent(ClienteActivity.this, ConsultarCitasClienteActivity.class);
                intent.putExtra("clienteId", clienteId);
                startActivity(intent);
            }
        });
    }
}
