package com.example.chathub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private Button btnGeneral, btnFutbol, btnProgramacion, btnCine, btnVideojuegos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Asegúrate que este layout se llama así

        // Vinculamos los botones
        btnGeneral = findViewById(R.id.btnGeneral);
        btnFutbol = findViewById(R.id.btnFutbol);
        btnProgramacion = findViewById(R.id.btnProgramacion);
        btnCine = findViewById(R.id.btnCine);
        btnVideojuegos = findViewById(R.id.btnVideojuegos);

        // Asignamos listeners a cada botón
        btnGeneral.setOnClickListener(v -> openChat("general"));
        btnFutbol.setOnClickListener(v -> openChat("futbol"));
        btnProgramacion.setOnClickListener(v -> openChat("programacion"));
        btnCine.setOnClickListener(v -> openChat("cine"));
        btnVideojuegos.setOnClickListener(v -> openChat("videojuegos"));
    }

    // Método para abrir el ChatActivity con el nombre de la sala
    private void openChat(String roomName) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("roomName", roomName); // Enviamos el nombre de la sala
        startActivity(intent);
    }
}
