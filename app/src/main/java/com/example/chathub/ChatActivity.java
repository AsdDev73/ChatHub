package com.example.chathub;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import java.util.*;

public class ChatActivity extends AppCompatActivity {

    private EditText MensajeEnviado;
    private Button BtnEnviar;
    private RecyclerView recyclerView;
    private MessageAdapter adaptador;
    private FirebaseFirestore db;
    private CollectionReference messagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        MensajeEnviado = findViewById(R.id.mensaje);
        BtnEnviar = findViewById(R.id.botonEnviar);
        recyclerView = findViewById(R.id.recyclerView);

        db = FirebaseFirestore.getInstance();
        messagesRef = db.collection("messages");

        adaptador = new MessageAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptador);

        // Escuchar mensajes en tiempo real, ordenando por el campo "hora"
        messagesRef.orderBy("hora", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null) return;

                    List<Mensaje> messages = new ArrayList<>();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Mensaje mensaje = doc.toObject(Mensaje.class);
                        messages.add(mensaje);
                    }
                    adaptador.setMessages(messages);
                });

        BtnEnviar.setOnClickListener(v -> {
            String text = MensajeEnviado.getText().toString().trim();
            String user = FirebaseAuth.getInstance().getCurrentUser().getEmail();

            if (!text.isEmpty()) {
                // Asegúrate de pasar primero el usuario, luego el texto, y finalmente la hora
                Mensaje message = new Mensaje(user, text, System.currentTimeMillis());
                messagesRef.add(message);
                MensajeEnviado.setText("");
            }
        });
    }
}
