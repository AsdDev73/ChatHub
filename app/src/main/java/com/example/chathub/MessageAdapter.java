package com.example.chathub;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final Context context;
    private List<Mensaje> messages;

    public MessageAdapter(Context context, List<Mensaje> messages) {
        this.context = context;
        this.messages = messages;
    }

    public void setMessages(List<Mensaje> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos el layout item_chat.xml
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_chat, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Mensaje msg = messages.get(position);

        // 1. Asignar texto y usuario
        holder.user.setText(msg.getUsuario());
        holder.text.setText(msg.getTexto());

        // 2. Formatear y asignar hora
        String horaFormateada = new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(new Date(msg.getHora()));
        holder.hora.setText(horaFormateada);

        // 3. Determinar si es mensaje propio
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        boolean isMine = msg.getUsuario().equals(currentUser);

        // 4. Ajustar LayoutParams del LinearLayout hijo (messageContainer)
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
                holder.container.getLayoutParams();
        params.gravity = isMine ? Gravity.END : Gravity.START;
        holder.container.setLayoutParams(params);

        // 5. Cambiar fondo según remitente
        holder.container.setBackgroundResource(
                isMine
                        ? R.drawable.bg_message_mine
                        : R.drawable.bg_message_other
        );
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;  // Cambiado a LinearLayout
        TextView user, text, hora;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            // 'messageContainer' es el LinearLayout dentro del FrameLayout
            container = itemView.findViewById(R.id.messageContainer);
            user      = itemView.findViewById(R.id.usuarioTextView);
            text      = itemView.findViewById(R.id.mensajeTextView);
            hora      = itemView.findViewById(R.id.horaTextView);
        }
    }
}
