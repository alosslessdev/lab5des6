package com.example.laboratorio5;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CreateUserActivity extends AppCompatActivity {

    private EditText etName, etCedula, etEmail, etPassword, etUserType;
    private Button btnCreateUser, btnCancel;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_create_user);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etCedula = findViewById(R.id.etCedula);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etUserType = findViewById(R.id.etUserType); // New EditText for user type
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
        String userTypeStr = etUserType.getText().toString().trim(); // Get user type

        if (name.isEmpty() || cedula.isEmpty() || email.isEmpty() || password.isEmpty() || userTypeStr.isEmpty()) {
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

        int userType;
        try {
            userType = Integer.parseInt(userTypeStr);
            if (userType < 1 || userType > 3) {
                Toast.makeText(this, "El tipo de usuario debe ser 1 (Admin), 2 (Normal) o 3 (Registrador)", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El tipo de usuario debe ser un número (1, 2 o 3)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save user data to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(email + "_name", name);
        editor.putString(email + "_cedula", cedula);
        editor.putString(email + "_password", password);
        editor.putInt(email + "_type", userType);
        editor.apply();

        Toast.makeText(this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();

        // Optionally, you can send data back to MainActivity if needed, but for now just finish
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}