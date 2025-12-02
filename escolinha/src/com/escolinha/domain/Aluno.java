package com.escolinha.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects; // Import para equals/hashCode

public class Aluno {

    private int id; // Usaremos int para simplificar, 0 para novo
    private String nome;
    private LocalDate dataNascimento;
    private List<Responsavel> responsaveis; // Relação com outra classe (crie Responsavel.java também)
    private String observacoesMedicas;
    // Adicione outros atributos se necessário (ex: id da turma)

    // Construtor
    public Aluno(int id, String nome, LocalDate dataNascimento, List<Responsavel> responsaveis, String observacoesMedicas) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.responsaveis = responsaveis; // Idealmente, criar cópia defensiva
        this.observacoesMedicas = observacoesMedicas;
    }

    // Getters e Setters (Gerados pelo Eclipse)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        // Geralmente ID é setado pelo repositório, mas pode ser útil ter o setter
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public List<Responsavel> getResponsaveis() {
        return responsaveis; // Idealmente, retornar cópia imutável
    }

    public void setResponsaveis(List<Responsavel> responsaveis) {
        this.responsaveis = responsaveis;
    }

    public String getObservacoesMedicas() {
        return observacoesMedicas;
    }

    public void setObservacoesMedicas(String observacoesMedicas) {
        this.observacoesMedicas = observacoesMedicas;
    }

    // toString() (Gerado pelo Eclipse)
    @Override
    public String toString() {
        return nome; // Mostra apenas o nome
    }
    

    // equals() e hashCode() (Gerados pelo Eclipse, baseados no ID)
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Aluno other = (Aluno) obj;
        return id == other.id; // Compara apenas pelo ID
    }
}