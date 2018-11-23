package com.example.littlewolf_pc.app.utils;

import com.example.littlewolf_pc.app.model.UsuarioDTO;

public class UsuarioSingleton {
    private static final UsuarioSingleton INSTANCE = new UsuarioSingleton();
    private UsuarioDTO usuario;

    private UsuarioSingleton(){}

    public static UsuarioSingleton getInstance(){
        return INSTANCE;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioDTO usuario) {
        this.usuario = usuario;
    }
}
