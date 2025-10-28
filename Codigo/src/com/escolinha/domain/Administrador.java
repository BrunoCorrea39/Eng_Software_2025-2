package com.escolinha.domain;

public class Administrador extends Usuario {
    public Administrador(int id, String nome, String login) {
        super(id, nome, login);
    }
    @Override
    public TipoUsuario getTipoUsuario() { return TipoUsuario.ADMINISTRADOR; }
}
