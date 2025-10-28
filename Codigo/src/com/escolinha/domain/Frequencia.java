package com.escolinha.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Frequencia {
    private int id; // Pode ser útil ter um ID para o registro
    private int alunoId;
    private int turmaId;
    private LocalDate dataAula;
    private boolean presente; // true = Presente, false = Ausente

    public Frequencia(int id, int alunoId, int turmaId, LocalDate dataAula, boolean presente) {
        this.id = id;
        this.alunoId = alunoId;
        this.turmaId = turmaId;
        this.dataAula = dataAula;
        this.presente = presente;
    }

    // --- Getters e Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAlunoId() { return alunoId; }
    public void setAlunoId(int alunoId) { this.alunoId = alunoId; }
    public int getTurmaId() { return turmaId; }
    public void setTurmaId(int turmaId) { this.turmaId = turmaId; }
    public LocalDate getDataAula() { return dataAula; }
    public void setDataAula(LocalDate dataAula) { this.dataAula = dataAula; }
    public boolean isPresente() { return presente; }
    public void setPresente(boolean presente) { this.presente = presente; }

    // --- toString, equals, hashCode ---
    @Override
    public String toString() {
        return "Frequencia [id=" + id + ", alunoId=" + alunoId + ", data=" + dataAula + ", presente=" + presente + "]";
    }

    @Override
    public int hashCode() {
        // Uma combinação de aluno, turma e data pode ser uma chave natural
        return Objects.hash(alunoId, turmaId, dataAula);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Frequencia other = (Frequencia) obj;
        return alunoId == other.alunoId && turmaId == other.turmaId && Objects.equals(dataAula, other.dataAula);
    }
}