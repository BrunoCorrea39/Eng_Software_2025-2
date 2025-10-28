package com.escolinha.domain;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

public class Turma {
    private int id;
    private String nome; // Ex: "Sub-10 Manhã"
    private int treinadorId; // ID do Treinador responsável (simplificado)
    private List<Aluno> alunos; // Lista de alunos na turma

    public Turma(int id, String nome, int treinadorId) {
        this.id = id;
        this.nome = nome;
        this.treinadorId = treinadorId;
        this.alunos = new ArrayList<>(); // Inicializa a lista vazia
    }

    // Método para adicionar aluno à turma
    public void adicionarAluno(Aluno aluno) {
        if (aluno != null && !this.alunos.contains(aluno)) {
            this.alunos.add(aluno);
            // Poderia adicionar lógica para setar a turma no aluno também
        }
    }

    // Método para remover aluno
    public void removerAluno(Aluno aluno) {
        this.alunos.remove(aluno);
    }

    // --- Getters e Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getTreinadorId() { return treinadorId; }
    public void setTreinadorId(int treinadorId) { this.treinadorId = treinadorId; }
    // Retorna uma cópia para proteger a lista interna
    public List<Aluno> getAlunos() { return new ArrayList<>(alunos); }
    // Não fornecer um setter direto para a lista inteira é mais seguro
    // public void setAlunos(List<Aluno> alunos) { this.alunos = alunos; }


    // --- toString, equals, hashCode ---
    @Override
    public String toString() {
        return nome + " (ID " + id + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Turma other = (Turma) obj;
        return id == other.id;
    }
}