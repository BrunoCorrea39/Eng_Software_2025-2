package com.escolinha.domain;

// Poderia ser uma interface também
public abstract class Usuario {
    protected int id;
    protected String nome;
    protected String login; // Ou email
    // protected String senha; // Cuidado ao armazenar senhas diretamente

    public Usuario(int id, String nome, String login) {
        this.id = id;
        this.nome = nome;
        this.login = login;
    }

    public abstract TipoUsuario getTipoUsuario(); // Cada subclasse dirá quem é

    // --- Getters e Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    // --- toString, equals, hashCode ---
    // Implementar nas subclasses ou aqui se fizer sentido
}

// Exemplo de Subclasse (crie arquivos separados para elas)
/*
public class Treinador extends Usuario {
    // Atributos específicos do treinador, se houver
    public Treinador(int id, String nome, String login) {
        super(id, nome, login);
    }
    @Override
    public TipoUsuario getTipoUsuario() { return TipoUsuario.TREINADOR; }
}

public class Administrador extends Usuario {
    public Administrador(int id, String nome, String login) { super(id, nome, login); }
    @Override
    public TipoUsuario getTipoUsuario() { return TipoUsuario.ADMINISTRADOR; }
}

// Responsavel já existe, poderia herdar de Usuario se fizer sentido ter login para eles
*/