package com.example.laboratorio5;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private TextView tvUserInfo;
    private Button btnCreateUser, btnOpenMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_welcome);

        initViews();
        displayUserInfo();
        setupClickListeners();
    }

    private void initViews() {
        tvUserInfo = findViewById(R.id.tvUserInfo);
        btnCreateUser = findViewById(R.id.btnCreateUser);
        btnOpenMaps = findViewById(R.id.btnOpenCalculator);
    }

    private void displayUserInfo() {
        String userEmail = getIntent().getStringExtra("user_email");
        String userInfo = "Nombre: Jean Solano\n" +
                "Cédula: 12345678\n" +
                "Grupo: Desarrollo Android\n" +
                "Lab: #5 - Ciclo de Vida Android\n" +
                "Email: " + userEmail;

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
    }

    private void openGoogleMaps() {
        try {
            // Intent para abrir Google Maps
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(android.net.Uri.parse("geo:0,0?q=Panama"));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        } catch (Exception e) {
            // Si Google Maps no está instalado, abrir en navegador web
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(android.net.Uri.parse("https://maps.google.com/"));
                startActivity(intent);
            } catch (Exception ex) {
                Toast.makeText(this, "No se puede abrir Google Maps", Toast.LENGTH_SHORT).show();
            }
        }
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