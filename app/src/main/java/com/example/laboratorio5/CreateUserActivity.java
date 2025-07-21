package com.example.laboratorio5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CreateUserActivity extends AppCompatActivity {

    private EditText etName, etCedula, etEmail, etPassword;
    private Button btnCreateUser, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_create_user);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etCedula = findViewById(R.id.etCedula);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnCreateUser = findViewById(R.id.btnCreateUser);
        btnCancel = findViewById(R.id.btnCancel);
    }

    private void setupClickListeners() {
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndCreateUser();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void validateAndCreateUser() {
        String name = etName.getText().toString().trim();
        String cedula = etCedula.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || cedula.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Ingrese un email válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear Bundle con datos del usuario
        Bundle userBundle = new Bundle();
        userBundle.putString("name", name);
        userBundle.putString("cedula", cedula);
        userBundle.putString("email", email);

        Intent resultIntent = new Intent();
        resultIntent.putExtras(userBundle);

        Toast.makeText(this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();
        finish();
    }
}
