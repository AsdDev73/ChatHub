package com.example.chathub;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private ShapeableImageView perfilIcon;
    private FloatingActionButton fabSolicitarChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    // Configurar el RecyclerView
        recyclerView = findViewById(R.id.recyclerViewChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    
        List<String> chatRooms = Arrays.asList("General", "Fútbol", "Programación", "Cine", "Videojuegos");
        adapter = new ChatListAdapter(chatRooms, roomName -> {
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            intent.putExtra("roomName", roomName.toLowerCase());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        perfilIcon = findViewById(R.id.imageView2);
        perfilIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
            startActivity(intent);
        });

        cargarAvatarDesdeFirestore();

        fabSolicitarChat = findViewById(R.id.fabSolicitarChat);
        fabSolicitarChat.setOnClickListener(v -> mostrarDialogoSolicitud());
    }
    // cargar el avatar desde la base de datos
    private void cargarAvatarDesdeFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("usuarios")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(doc -> {
                        String avatarName = doc.getString("avatar");
                        if (avatarName != null) {
                            int resId = getResources().getIdentifier(avatarName, "drawable", getPackageName());
                            if (resId != 0) perfilIcon.setImageResource(resId);
                        }
                    })
                    .addOnFailureListener(Throwable::printStackTrace);
        }
    }

    //abrir el dialogo de solicitud
    private void mostrarDialogoSolicitud() {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_solicitud_chat, null);
        EditText editTexto = dialogView.findViewById(R.id.editTextSolicitud);

        new AlertDialog.Builder(this)
                .setTitle("Solicitar nueva sala")
                .setView(dialogView)
                .setPositiveButton("Enviar", (dialog, which) -> {
                    String texto = editTexto.getText().toString().trim();
                    if (texto.isEmpty()) {
                        Toast.makeText(this, "Escribe tu solicitud", Toast.LENGTH_SHORT).show();
                    } else {
                        enviarSolicitudFirestore(texto);
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
    // metodo enviar la solicitud a la base de datos
    private void enviarSolicitudFirestore(String texto) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        Map<String, Object> solicitud = new HashMap<>();
        solicitud.put("uid", user.getUid());
        solicitud.put("texto", texto);
        solicitud.put("timestamp", Timestamp.now());

        FirebaseFirestore.getInstance()
                .collection("chat_requests")
                .add(solicitud)
                .addOnSuccessListener(docRef ->
                        Toast.makeText(this, "Solicitud enviada. Se revisará pronto.", Toast.LENGTH_LONG).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al enviar solicitud: " + e.getMessage(), Toast.LENGTH_LONG).show()
                );
    }
}
