package com.example.chathub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {

    public interface OnChatClickListener {
        void onChatClick(String roomName);
    }

    private final List<String> chatRooms;
    private final OnChatClickListener listener;

    public ChatListAdapter(List<String> chatRooms, OnChatClickListener listener) {
        this.chatRooms = chatRooms;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos EXACTAMENTE item_chat_room.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        String roomName = chatRooms.get(position);
        holder.chatNameTextView.setText(roomName);
        holder.itemView.setOnClickListener(v -> listener.onChatClick(roomName));
    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView chatNameTextView;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            // Aquí (y solo aquí) buscamos la vista con EXACTAMENTE ese ID
            chatNameTextView = itemView.findViewById(R.id.chatNameTextView);
        }
    }
}
