package com.example.chathub;

import android.content.Context;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.*;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Mensaje> messages = new ArrayList<>();
    private Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void setMessages(List<Mensaje> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Mensaje message = messages.get(position);
        holder.user.setText(message.getUsuario());
        holder.text.setText(message.getTexto());

        // Formatear hora
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String horaFormateada = sdf.format(new Date(message.getHora()));
        holder.hora.setText(horaFormateada);

        // Estilo según el usuario
        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        boolean isCurrentUser = message.getUsuario().equals(currentUser);

        // Cambiar alineación y color
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.container.getLayoutParams();
        params.gravity = isCurrentUser ? Gravity.END : Gravity.START;
        holder.container.setLayoutParams(params);

        int bgColor = isCurrentUser ? 0xFFD1E8FF : 0xFFEFEFEF;
        holder.container.setBackgroundColor(bgColor);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView user, text, hora;
        LinearLayout container;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.usuarioTextView);
            text = itemView.findViewById(R.id.mensajeTextView);
            hora = itemView.findViewById(R.id.horaTextView);
            container = itemView.findViewById(R.id.messageContainer);
        }
    }
}
