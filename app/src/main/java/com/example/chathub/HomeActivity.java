package com.example.chathub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerViewChats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> chatRooms = Arrays.asList("General", "Futbol", "Programacion", "Cine", "Videojuegos");
        adapter = new ChatListAdapter(chatRooms, roomName -> {
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            intent.putExtra("roomName", roomName.toLowerCase());
            startActivity(intent);
        });
        ImageView perilIcon = findViewById(R.id.imageView2);
        perilIcon.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PerfilActivity.class);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

    }
}
