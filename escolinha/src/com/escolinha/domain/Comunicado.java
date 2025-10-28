package com.escolinha.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comunicado {
    private int id;
    private String titulo;
    private String mensagem;
    private LocalDateTime dataPublicacao;
    private int autorId; // ID do Treinador ou Administrador que publicou
    private Integer turmaId; // Pode ser null se for um comunicado geral

    public Comunicado(int id, String titulo, String mensagem, LocalDateTime dataPublicacao, int autorId, Integer turmaId) {
        this.id = id;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.dataPublicacao = dataPublicacao;
        this.autorId = autorId;
        this.turmaId = turmaId;
    }

    // --- Getters e Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public LocalDateTime getDataPublicacao() { return dataPublicacao; }
    public void setDataPublicacao(LocalDateTime dataPublicacao) { this.dataPublicacao = dataPublicacao; }
    public int getAutorId() { return autorId; }
    public void setAutorId(int autorId) { this.autorId = autorId; }
    public Integer getTurmaId() { return turmaId; } // Integer permite null
    public void setTurmaId(Integer turmaId) { this.turmaId = turmaId; }

    // --- toString, equals, hashCode ---
    @Override
    public String toString() {
        return "Comunicado [id=" + id + ", titulo=" + titulo + ", data=" + dataPublicacao + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Comunicado other = (Comunicado) obj;
        return id == other.id;
    }
}