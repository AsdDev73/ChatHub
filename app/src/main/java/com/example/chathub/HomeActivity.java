package com.example.chathub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;
    private ImageView perfilIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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
    }

    private void cargarAvatarDesdeFirestore() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("usuarios")
                    .document(currentUser.getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String avatarName = documentSnapshot.getString("avatar");
                            if (avatarName != null) {
                                int avatarResId = getResources().getIdentifier(avatarName, "drawable", getPackageName());
                                if (avatarResId != 0) {
                                    perfilIcon.setImageResource(avatarResId);
                                }
                            }
                        }
                    })
                    .addOnFailureListener(Throwable::printStackTrace);
        }
    }
}
