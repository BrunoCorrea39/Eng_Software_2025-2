package com.escolinha.service;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.Fatura;
import com.escolinha.domain.PlanoPagamento;
import com.escolinha.domain.StatusFatura;
import com.escolinha.repository.AlunoRepository;
import com.escolinha.repository.FaturaRepository;
import com.escolinha.repository.PlanoPagamentoRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class FinanceiroService {

    private final FaturaRepository faturaRepository;
    private final PlanoPagamentoRepository planoPagamentoRepository;
    private final AlunoRepository alunoRepository;

    // vínculo em memória: alunoId -> planoId (lifetime do processo)
    private final Map<Integer, Integer> alunoParaPlano = new ConcurrentHashMap<>();

    // Observers (thread-safe)
    private final List<PagamentoObserver> observers = new CopyOnWriteArrayList<>();

    public FinanceiroService(FaturaRepository faturaRepository,
                             PlanoPagamentoRepository planoPagamentoRepository,
                             AlunoRepository alunoRepository) {
        this.faturaRepository = faturaRepository;
        this.planoPagamentoRepository = planoPagamentoRepository;
        this.alunoRepository = alunoRepository;
    }

    // ================= HU-01: Status Financeiro =================
    public Map<Aluno, StatusFatura> getStatusFinanceiroAlunos() {
        return alunoRepository.listarTodos().stream()
                .collect(Collectors.toMap(a -> a, a -> verificarStatusAluno(a.getId())));
    }

    private StatusFatura verificarStatusAluno(int alunoId) {
        LocalDate hoje = LocalDate.now();
        List<Fatura> faturas = faturaRepository.buscarPorAlunoId(alunoId);

        boolean temVencida = faturas.stream()
                .anyMatch(f -> f.getStatus() != StatusFatura.PAGA
                        && f.getDataVencimento().isBefore(hoje));
        if (temVencida) return StatusFatura.VENCIDA;

        boolean temPendente = faturas.stream()
                .anyMatch(f -> f.getStatus() == StatusFatura.PENDENTE);
        if (temPendente) return StatusFatura.PENDENTE;

        return StatusFatura.PAGA;
    }

    // ================= HU-02: Gerenciar Planos =================

    /**
     * Cria um novo plano de pagamento com validações de negócio.
     * (Usar na parte de correção de bugs/validação no README)
     */
    public PlanoPagamento criarPlano(String nome, BigDecimal valor, int duracaoMeses) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do plano não pode ser vazio.");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do plano deve ser maior que zero.");
        }
        if (duracaoMeses <= 0) {
            throw new IllegalArgumentException("Duração do plano deve ser positiva.");
        }

        PlanoPagamento novoPlano = new PlanoPagamento(0, nome.trim(), valor, duracaoMeses);
        return planoPagamentoRepository.salvar(novoPlano);
    }

    public List<PlanoPagamento> listarPlanos() {
        return planoPagamentoRepository.listarTodos();
    }

    public synchronized void atribuirPlanoAoAluno(int alunoId, int planoId) {
        // valida existência do aluno
        alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado (ID " + alunoId + ")."));

        // valida existência do plano
        PlanoPagamento plano = planoPagamentoRepository.buscarPorId(planoId)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado (ID " + planoId + ")."));

        // vincula e gera primeira fatura
        alunoParaPlano.put(alunoId, plano.getId());
        gerarPrimeiraFaturaDoPlano(alunoId, plano);
    }

    /**
     * TDD 2 – Lista apenas as faturas vencidas de um aluno.
     */
    public List<Fatura> listarFaturasVencidas(int alunoId) {
        LocalDate hoje = LocalDate.now();

        return faturaRepository.buscarPorAlunoId(alunoId).stream()
                .filter(f -> f.getStatus() != StatusFatura.PAGA)
                .filter(f -> f.getDataVencimento().isBefore(hoje))
                .collect(Collectors.toList());
    }

    public PlanoPagamento obterPlanoDoAluno(int alunoId) {
        Integer planoId = alunoParaPlano.get(alunoId);
        if (planoId == null) return null;
        return planoPagamentoRepository.buscarPorId(planoId).orElse(null);
    }

    // Gera uma fatura simples (1ª parcela) com vencimento no dia 10
    private void gerarPrimeiraFaturaDoPlano(int alunoId, PlanoPagamento plano) {
        LocalDate vencimento = calcularVencimentoPrimeiraFatura();
        Fatura f = new Fatura(0, alunoId, plano.getValor(), vencimento);
        f.setStatus(StatusFatura.PENDENTE);
        f.setPlanoPagamentoId(plano.getId());

        faturaRepository.salvar(f);
    }

    private LocalDate calcularVencimentoPrimeiraFatura() {
        LocalDate hoje = LocalDate.now();
        return (hoje.getDayOfMonth() <= 10)
                ? hoje.withDayOfMonth(10)
                : hoje.plusMonths(1).withDayOfMonth(10);
    }

    // ================= HU-09: Pagamento =================
    public boolean registrarPagamentoFatura(int faturaId, LocalDate dataPagamento) {
        Fatura fatura = faturaRepository.buscarPorId(faturaId)
                .orElseThrow(() -> new IllegalArgumentException("Fatura com ID " + faturaId + " não encontrada."));

        if (fatura.getStatus() == StatusFatura.PENDENTE
                || fatura.getStatus() == StatusFatura.VENCIDA) {

            fatura.setStatus(StatusFatura.PAGA);
            fatura.setDataPagamento(dataPagamento);
            faturaRepository.salvar(fatura);

            // 🔔 avisa os observers (PagamentoPanel, FinanceiroStatusPanel, etc.)
            notificarObservadores(fatura);
            return true;
        }
        return false; // já estava paga
    }

    public List<Fatura> listarFaturasAluno(int alunoId) {
        return faturaRepository.buscarPorAlunoId(alunoId);
    }

    /**
     * TDD 3 – Geração mensal de faturas para todos os alunos com plano vinculado.
     */
    public void gerarFaturasMensais() {

        LocalDate hoje = LocalDate.now();

        for (Aluno aluno : alunoRepository.listarTodos()) {

            // pega o plano vinculado
            Integer planoId = alunoParaPlano.get(aluno.getId());
            if (planoId == null) continue;

            PlanoPagamento plano = planoPagamentoRepository.buscarPorId(planoId).orElse(null);
            if (plano == null) continue;

            // todas as faturas do aluno
            List<Fatura> faturas = faturaRepository.buscarPorAlunoId(aluno.getId());

            // existe fatura deste mês?
            boolean jaTemFaturaDoMes = faturas.stream()
                    .anyMatch(f -> 
                        f.getDataVencimento().getMonth() == hoje.getMonth() &&
                        f.getDataVencimento().getYear() == hoje.getYear()
                    );

            if (jaTemFaturaDoMes) {
                continue; // não duplica
            }

            // calcula vencimento (dia 10 do mês atual, ou mês seguinte se já passou)
            LocalDate vencimento = hoje.getDayOfMonth() <= 10
                    ? hoje.withDayOfMonth(10)
                    : hoje.plusMonths(1).withDayOfMonth(10);

            // cria fatura nova
            Fatura nova = new Fatura(
                    0,
                    aluno.getId(),
                    plano.getValor(),
                    vencimento
            );
            nova.setStatus(StatusFatura.PENDENTE);
            nova.setPlanoPagamentoId(plano.getId());

            faturaRepository.salvar(nova);

            // notifica observers
            notificarObservadores(nova);
        }
    }
    

    private LocalDate proximoVencimento(LocalDate base) {
        return base.plusMonths(1).withDayOfMonth(10);
    }

    // ================= Nova funcionalidade para TDD =================

    /**
     * TDD 1 – Calcula o total já pago por um aluno considerando apenas faturas com status PAGA.
     */
    public BigDecimal calcularTotalPagoAluno(int alunoId) {
        return faturaRepository.buscarPorAlunoId(alunoId).stream()
                .filter(f -> f.getStatus() == StatusFatura.PAGA)
                .map(Fatura::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    

    // ================= Observer =================
    public void addObserver(PagamentoObserver observer) {
        if (observer != null) observers.add(observer);
    }

    public void removeObserver(PagamentoObserver observer) {
        observers.remove(observer);
    }

    private void notificarObservadores(Fatura fatura) {
        for (PagamentoObserver obs : observers) {
            try {
                obs.onPagamentoRegistrado(fatura);
            } catch (Exception ignored) { }
        }
    }
}
