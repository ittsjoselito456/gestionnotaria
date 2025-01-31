package jortega.proyecto.gestionnotaria.actividades;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import jortega.proyecto.gestionnotaria.R;
import jortega.proyecto.gestionnotaria.modelos.Usuario;
import jortega.proyecto.gestionnotaria.red.ApiService;
import jortega.proyecto.gestionnotaria.red.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private ApiService apiService;
    private EditText nombre, apellido, email, password;
    private Spinner rol;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        apiService = RetrofitClient.getClient("http://192.168.222.69:5000/").create(ApiService.class);

        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        rol = findViewById(R.id.rol);
        btnRegister = findViewById(R.id.btn_register);

        // Configurar el Spinner con las opciones disponibles
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rol.setAdapter(adapter);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String nombreText = nombre.getText().toString();
        String apellidoText = apellido.getText().toString();
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        String rolText = rol.getSelectedItem().toString();

        Usuario usuario = new Usuario();
        usuario.setNombre(nombreText);
        usuario.setApellido(apellidoText);
        usuario.setEmail(emailText);
        usuario.setPassword(passwordText);
        usuario.setRol(rolText);

        Call<Void> registerCall = apiService.register(usuario);
        registerCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
