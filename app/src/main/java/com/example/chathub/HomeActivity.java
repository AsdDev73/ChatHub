package com.example.chathub;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private ShapeableImageView perfilIcon;
    private FloatingActionButton fabSolicitarChat;
    private EditText searchEditText;

    private List<String> chatRooms = Arrays.asList("General", "Fútbol", "Programación", "Cine", "Videojuegos", "Cocina");
    private List<String> filteredChatRooms = new ArrayList<>(chatRooms);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        recyclerView = findViewById(R.id.recyclerViewChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ChatListAdapter(filteredChatRooms, roomName -> {
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

        searchEditText = findViewById(R.id.editTextSearch);
        configurarBuscador();
    }
    // Método para configurar el buscador
    private void configurarBuscador() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarSalas(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }
    // Método para filtrar las salas de chat según el texto de búsqueda
    private void filtrarSalas(String texto) {
        filteredChatRooms.clear();
        for (String sala : chatRooms) {
            if (sala.toLowerCase().contains(texto.toLowerCase())) {
                filteredChatRooms.add(sala);
            }
        }
        adapter.notifyDataSetChanged();
    }
    // Método para cargar el avatar desde Firestore
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
    // Método para mostrar el diálogo de solicitud
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
    // Método para enviar la solicitud a Firestore
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
