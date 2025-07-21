package com.example.laboratorio5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import java.io.BufferedReader;
import java.io.File; // Import File class
import java.io.FileInputStream;
import java.io.FileOutputStream; // Import FileOutputStream
import java.io.IOException; // Import IOException
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private SharedPreferences sharedPreferences;

    // File name for special user
    private static final String SPECIAL_USER_FILE = "special_user.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        // Call the method to pre-populate the special user file
        prepopulateSpecialUserFile();

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupClickListeners() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndLogin();
            }
        });
    }

    private void validateAndLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Try to validate with SharedPreferences
        if (sharedPreferences.contains(email + "_password")) {
            String storedPassword = sharedPreferences.getString(email + "_password", "");
            if (password.equals(storedPassword)) {
                // Login successful via SharedPreferences
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                intent.putExtra("user_email", email);
                startActivity(intent);
                finish();
                return;
            }
        }

        // 2. Try to validate with special user file
        String[] specialUserData = readSpecialUserFromFile();
        if (specialUserData != null && specialUserData.length == 5) {
            String specialUserEmail = specialUserData[2]; // email is at index 2
            String specialUserPassword = specialUserData[3]; // password is at index 3
            if (email.equals(specialUserEmail) && password.equals(specialUserPassword)) {
                // Login successful via special user file
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                intent.putExtra("user_email", email); // Send special user email
                intent.putExtra("is_special_user", true); // Indicate it's a special user
                startActivity(intent);
                finish();
                return;
            }
        }

        Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
    }

    private String[] readSpecialUserFromFile() {
        try {
            FileInputStream fis = openFileInput(SPECIAL_USER_FILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            br.close();
            isr.close();
            fis.close();
            if (line != null && !line.isEmpty()) {
                return line.split(","); // Assuming CSV format: name,cedula,email,password,type
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if the special user file exists. If not, it creates it and
     * pre-populates it with default special user credentials.
     */
    private void prepopulateSpecialUserFile() {
        File file = new File(getFilesDir(), SPECIAL_USER_FILE);
        if (!file.exists()) {
            // File does not exist, create and populate it
            String specialUserName = "Usuario Especial";
            String specialUserCedula = "87654321";
            String specialUserEmail = "special@user.com";
            String specialUserPassword = "specialpassword";
            int specialUserType = 2; // Example: normal user type for the special user

            String userData = specialUserName + "," + specialUserCedula + "," + specialUserEmail + "," + specialUserPassword + "," + specialUserType;

            try {
                FileOutputStream fos = openFileOutput(SPECIAL_USER_FILE, Context.MODE_PRIVATE);
                fos.write(userData.getBytes());
                fos.close();
                Toast.makeText(this, "Usuario especial pre-creado.", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al pre-crear usuario especial.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}