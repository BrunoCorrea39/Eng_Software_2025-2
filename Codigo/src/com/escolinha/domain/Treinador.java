package com.escolinha.domain;

public class Treinador extends Usuario {
    // Adicione atributos específicos se houver (ex: String crefi)

    public Treinador(int id, String nome, String login) {
        super(id, nome, login);
    }

    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.TREINADOR;
    }

    // Getters/Setters para atributos específicos, se houver
    // toString, equals, hashCode (pode usar os da superclasse Usuario)
}