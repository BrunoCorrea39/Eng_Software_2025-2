package com.escolinha.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Fatura {
    private int id;
    private int alunoId;
    private int planoPagamentoId; // Opcional, se quiser referenciar o plano
    private BigDecimal valor;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento; // Pode ser null se PENDENTE/VENCIDA
    private StatusFatura status;

    public Fatura(int id, int alunoId, BigDecimal valor, LocalDate dataVencimento) {
        this.id = id;
        this.alunoId = alunoId;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.status = StatusFatura.PENDENTE; // Status inicial
        this.dataPagamento = null;
    }

    // --- Getters e Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getAlunoId() { return alunoId; }
    public void setAlunoId(int alunoId) { this.alunoId = alunoId; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public LocalDate getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDate dataPagamento) { this.dataPagamento = dataPagamento; }
    public StatusFatura getStatus() { return status; }
    public void setStatus(StatusFatura status) { this.status = status; }

    // --- toString, equals, hashCode ---
    @Override
    public String toString() {
        return "Fatura [id=" + id + ", alunoId=" + alunoId + ", valor=" + valor + ", vencimento=" + dataVencimento + ", status=" + status + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public int getPlanoPagamentoId() {
        return planoPagamentoId;
    }

    public void setPlanoPagamentoId(int planoPagamentoId) {
        this.planoPagamentoId = planoPagamentoId;
    }
    
    

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Fatura other = (Fatura) obj;
        return id == other.id;
    }
}