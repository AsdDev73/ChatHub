package com.example.chathub;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private EditText mensajeEnviado;
    private ImageButton botonEnviar, btnVolver;
    private RecyclerView recyclerView;
    private MessageAdapter adaptador;
    private TextView tituloTextView;
    private CollectionReference messagesRef;

    private String nombreUsuario ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mensajeEnviado = findViewById(R.id.mensaje);
        botonEnviar = findViewById(R.id.botonEnviar);
        recyclerView = findViewById(R.id.recyclerView);
        btnVolver = findViewById(R.id.btnVolver);
        tituloTextView = findViewById(R.id.titulo);

        String chatName = getIntent().getStringExtra("chatName");
        if (chatName == null) {
            chatName = getIntent().getStringExtra("roomName");
        }
        if (chatName == null) {
            chatName = "general";
        }

        String displayName = chatName.substring(0, 1).toUpperCase() + chatName.substring(1);
        tituloTextView.setText(displayName);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        messagesRef = db.collection("rooms").document(chatName).collection("messages");

        // Cargar el nombre del usuario y almacenarlo localmente
        db.collection("usuarios")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        nombreUsuario = documentSnapshot.getString("name");
                    }
                });

        adaptador = new MessageAdapter(this, new ArrayList<>(), () -> nombreUsuario);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);

        messagesRef.orderBy("hora", Query.Direction.ASCENDING)
                .addSnapshotListener((snapshots, error) -> {
                    if (error != null || snapshots == null) return;
                    List<Mensaje> lista = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots.getDocuments()) {
                        Mensaje m = doc.toObject(Mensaje.class);
                        if (m != null) lista.add(m);
                    }
                    adaptador.setMessages(lista);
                    recyclerView.scrollToPosition(lista.size() - 1);
                });

        botonEnviar.setOnClickListener(v -> {
            String texto = mensajeEnviado.getText().toString().trim();
            if (!texto.isEmpty() && nombreUsuario != null) {
                Mensaje m = new Mensaje(nombreUsuario, texto, System.currentTimeMillis());
                messagesRef.add(m)
                        .addOnSuccessListener(docRef -> mensajeEnviado.setText(""))
                        .addOnFailureListener(e -> Toast.makeText(this, "Error al enviar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        btnVolver.setOnClickListener(v -> finish());
    }
}
