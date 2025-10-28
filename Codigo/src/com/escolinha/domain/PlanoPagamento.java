package com.escolinha.domain;

import java.math.BigDecimal; // Ideal para valores monetários
import java.util.Objects;

public class PlanoPagamento {
    private int id;
    private String nome; // Ex: "Mensal", "Semestral"
    private BigDecimal valor;
    private int duracaoMeses; // Ex: 1, 6, 12

    public PlanoPagamento(int id, String nome, BigDecimal valor, int duracaoMeses) {
        this.id = id;
        this.nome = nome;
        this.valor = valor;
        this.duracaoMeses = duracaoMeses;
    }

    // --- Getters e Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public int getDuracaoMeses() { return duracaoMeses; }
    public void setDuracaoMeses(int duracaoMeses) { this.duracaoMeses = duracaoMeses; }

    // --- toString, equals, hashCode ---
    @Override
    public String toString() {
        return "PlanoPagamento [id=" + id + ", nome=" + nome + ", valor=" + valor + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlanoPagamento other = (PlanoPagamento) obj;
        return id == other.id;
    }
}