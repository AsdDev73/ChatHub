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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final Context context;
    private List<Mensaje> messages;
    private final ProveedorNombre proveedorNombre;

    public interface ProveedorNombre {
        String getNombreActual();
    }

    public MessageAdapter(Context context, List<Mensaje> messages, ProveedorNombre proveedorNombre) {
        this.context = context;
        this.messages = messages;
        this.proveedorNombre = proveedorNombre;
    }

    public void setMessages(List<Mensaje> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_chat, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Mensaje msg = messages.get(position);

        holder.user.setText(msg.getUsuario());
        holder.text.setText(msg.getTexto());

        String horaFormateada = new SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(new Date(msg.getHora()));
        holder.hora.setText(horaFormateada);

        String currentUserName = proveedorNombre.getNombreActual();
        boolean isMine = msg.getUsuario().equals(currentUserName);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)
                holder.container.getLayoutParams();
        params.gravity = isMine ? Gravity.END : Gravity.START;
        holder.container.setLayoutParams(params);

        holder.container.setBackgroundResource(
                isMine ? R.drawable.bg_message_mine : R.drawable.bg_message_other
        );
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container;
        TextView user, text, hora;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.messageContainer);
            user = itemView.findViewById(R.id.usuarioTextView);
            text = itemView.findViewById(R.id.mensajeTextView);
            hora = itemView.findViewById(R.id.horaTextView);
        }
    }
}
