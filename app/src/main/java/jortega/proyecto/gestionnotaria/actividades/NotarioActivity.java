package jortega.proyecto.gestionnotaria.actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import jortega.proyecto.gestionnotaria.R;

public class NotarioActivity extends AppCompatActivity {
    private Button btnCreateCita, btnModifyCita, btnDeleteCita, btnConsultCitas, btnAddDespachos, btndeldespacho, btnregistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notario);

        btnCreateCita = findViewById(R.id.btn_create_cita);
        btnModifyCita = findViewById(R.id.btn_modify_cita);
        btnDeleteCita = findViewById(R.id.btn_delete_cita);
        btnConsultCitas = findViewById(R.id.btn_consult_citas);
        btnAddDespachos = findViewById(R.id.btn_add_despacho);
        btndeldespacho = findViewById(R.id.btn_del_despacho);
        btnregistro = findViewById(R.id.btn_register);

        btnregistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotarioActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnCreateCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotarioActivity.this, ReservaCitaActivity.class);
                startActivity(intent);
            }
        });

        btnModifyCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotarioActivity.this, ModificarCitaActivity.class);
                startActivity(intent);
            }
        });

        btnDeleteCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotarioActivity.this, EliminarCitaActivity.class);
                startActivity(intent);
            }
        });

        btnConsultCitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotarioActivity.this, ConsultarCitasActivity.class);
                startActivity(intent);
            }
        });

        btnAddDespachos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotarioActivity.this, AgregarDespachoActivity.class);
                startActivity(intent);
            }
        });

        btndeldespacho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotarioActivity.this, EliminarDespachoActivity.class);
                startActivity(intent);
            }
        });
    }
}
