package com.example.chathub;

public class Mensaje {
    private String usuario;
    private String texto;
    private long hora;

    public Mensaje() {}

    public Mensaje(String usuario, String texto, long hora) {
        this.usuario = usuario;
        this.texto = texto;
        this.hora = hora;
    }

    // Getters y setters

    public long getHora() {
        return hora;
    }

    public void setHora(long hora) {
        this.hora = hora;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}

