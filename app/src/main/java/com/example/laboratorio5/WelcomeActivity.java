package com.example.laboratorio5;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tvUserInfo;
    private Button btnCreateUser, btnOpenMaps, btnAddSpecialUser; // Added btnAddSpecialUser
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

        setContentView(R.layout.activity_welcome);

        sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);

        initViews();
        displayUserInfo();
        setupClickListeners();
    }

    private void initViews() {
        tvUserInfo = findViewById(R.id.tvUserInfo);
        btnCreateUser = findViewById(R.id.btnCreateUser);
        btnOpenMaps = findViewById(R.id.btnOpenCalculator); // Renamed in XML, but kept here for consistency for now
        btnAddSpecialUser = findViewById(R.id.btnAddSpecialUser); // Initialize the new button
    }

    private void displayUserInfo() {
        String userEmail = getIntent().getStringExtra("user_email");
        boolean isSpecialUser = getIntent().getBooleanExtra("is_special_user", false);

        String name = "";
        String cedula = "";
        String userTypeString = "";

        if (isSpecialUser) {
            // Read special user data from file
            String[] specialUserData = readSpecialUserFromFile();
            if (specialUserData != null && specialUserData.length == 5) {
                name = specialUserData[0];
                cedula = specialUserData[1];
                userEmail = specialUserData[2]; // Use the email from the file
                int userType = Integer.parseInt(specialUserData[4]);
                userTypeString = getUserTypeDescription(userType);
                // Show add special user button only if admin
                if (userType == 1) {
                    btnAddSpecialUser.setVisibility(View.VISIBLE);
                } else {
                    btnAddSpecialUser.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(this, "Error al cargar datos del usuario especial", Toast.LENGTH_SHORT).show();
                btnAddSpecialUser.setVisibility(View.GONE);
            }
        } else {
            // Read regular user data from SharedPreferences
            name = sharedPreferences.getString(userEmail + "_name", "N/A");
            cedula = sharedPreferences.getString(userEmail + "_cedula", "N/A");
            int userType = sharedPreferences.getInt(userEmail + "_type", 0);
            userTypeString = getUserTypeDescription(userType);

            // Show add special user button only if admin
            if (userType == 1) {
                btnAddSpecialUser.setVisibility(View.VISIBLE);
            } else {
                btnAddSpecialUser.setVisibility(View.GONE);
            }
        }

        String userInfo = "Nombre: " + name + "\n" +
                "Cédula: " + cedula + "\n" +
                "Grupo: Desarrollo Android\n" +
                "Lab: #5 - Ciclo de Vida Android\n" +
                "Email: " + userEmail + "\n" +
                "Tipo de Usuario: " + userTypeString;

        tvUserInfo.setText(userInfo);
    }

    private void setupClickListeners() {
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, CreateUserActivity.class);
                startActivity(intent);
            }
        });

        btnOpenMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGoogleMaps();
            }
        });

        btnAddSpecialUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSpecialUserFile();
            }
        });
    }

    private void openGoogleMaps() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("geo:0,0?q=Panama"));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(android.net.Uri.parse("http://maps.google.com/0")); // Corrected URL
                startActivity(intent);
            } catch (Exception ex) {
                Toast.makeText(this, "No se puede abrir Google Maps", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getUserTypeDescription(int userType) {
        switch (userType) {
            case 1:
                return "Administrador";
            case 2:
                return "Usuario Normal";
            case 3:
                return "Registrador";
            default:
                return "Desconocido";
        }
    }

    private void createSpecialUserFile() {
        // Example special user data
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
            Toast.makeText(this, "Usuario especial creado en archivo: special@user.com", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al crear usuario especial en archivo", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.welcome_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_create_user) {
            startActivity(new Intent(this, CreateUserActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}