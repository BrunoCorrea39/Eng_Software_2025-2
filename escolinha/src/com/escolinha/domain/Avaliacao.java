package com.escolinha.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Avaliacao {
    private int id;
    private int alunoId;
    private int treinadorId;
    private LocalDate dataAvaliacao;
    private String comentarioTecnico;
    private String comentarioTatico;
    private String comentarioComportamental;

    public Avaliacao(int id, int alunoId, int treinadorId, LocalDate dataAvaliacao, String comentarioTecnico, String comentarioTatico, String comentarioComportamental) {
        this.id = id;
        this.alunoId = alunoId;
        this.treinadorId = treinadorId;
        this.dataAvaliacao = dataAvaliacao;
        this.comentarioTecnico = comentarioTecnico;
        this.comentarioTatico = comentarioTatico;
        this.comentarioComportamental = comentarioComportamental;
    }

    // --- Getters e Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAlunoId() { return alunoId; }
    public void setAlunoId(int alunoId) { this.alunoId = alunoId; }
    public int getTreinadorId() { return treinadorId; }
    public void setTreinadorId(int treinadorId) { this.treinadorId = treinadorId; }
    public LocalDate getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(LocalDate dataAvaliacao) { this.dataAvaliacao = dataAvaliacao; }
    public String getComentarioTecnico() { return comentarioTecnico; }
    public void setComentarioTecnico(String comentarioTecnico) { this.comentarioTecnico = comentarioTecnico; }
    public String getComentarioTatico() { return comentarioTatico; }
    public void setComentarioTatico(String comentarioTatico) { this.comentarioTatico = comentarioTatico; }
    public String getComentarioComportamental() { return comentarioComportamental; }
    public void setComentarioComportamental(String comentarioComportamental) { this.comentarioComportamental = comentarioComportamental; }

    // --- toString, equals, hashCode ---
    @Override
    public String toString() {
        return "Avaliacao [id=" + id + ", alunoId=" + alunoId + ", data=" + dataAvaliacao + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Avaliacao other = (Avaliacao) obj;
        return id == other.id;
    }
}