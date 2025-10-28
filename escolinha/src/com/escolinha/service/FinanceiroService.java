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
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap; // <-- adicionado
import java.util.stream.Collectors;

public class FinanceiroService {

    private final FaturaRepository faturaRepository;
    private final PlanoPagamentoRepository planoPagamentoRepository;
    private final AlunoRepository alunoRepository;

    // Mantém o vínculo em memória durante a execução (alunoId -> planoId)
    private final Map<Integer, Integer> alunoParaPlano = new ConcurrentHashMap<>();

    public FinanceiroService(FaturaRepository faturaRepository,
                             PlanoPagamentoRepository planoPagamentoRepository,
                             AlunoRepository alunoRepository) {
        this.faturaRepository = faturaRepository;
        this.planoPagamentoRepository = planoPagamentoRepository;
        this.alunoRepository = alunoRepository;
    }

    // ================= HU-01: Status Financeiro =================
    public Map<Aluno, StatusFatura> getStatusFinanceiroAlunos() {
        List<Aluno> todosAlunos = alunoRepository.listarTodos();
        return todosAlunos.stream()
                .collect(Collectors.toMap(
                        a -> a,
                        a -> verificarStatusAluno(a.getId())
                ));
    }

    private StatusFatura verificarStatusAluno(int alunoId) {
        LocalDate hoje = LocalDate.now();
        List<Fatura> faturas = faturaRepository.buscarPorAlunoId(alunoId);

        boolean temVencida = faturas.stream()
                .anyMatch(f -> f.getStatus() != StatusFatura.PAGA && f.getDataVencimento().isBefore(hoje));
        if (temVencida) return StatusFatura.VENCIDA;

        boolean temPendente = faturas.stream()
                .anyMatch(f -> f.getStatus() == StatusFatura.PENDENTE);
        if (temPendente) return StatusFatura.PENDENTE;

        return StatusFatura.PAGA;
    }

    // ================= HU-02: Gerenciar Planos =================
    public PlanoPagamento criarPlano(String nome, BigDecimal valor, int duracaoMeses) {
        PlanoPagamento novoPlano = new PlanoPagamento(0, nome, valor, duracaoMeses);
        return planoPagamentoRepository.salvar(novoPlano);
    }

    public List<PlanoPagamento> listarPlanos() {
        return planoPagamentoRepository.listarTodos();
    }

    // Vincular um plano a um aluno (chamado pelo painel Gerenciar Planos)
    public synchronized void atribuirPlanoAoAluno(int alunoId, int planoId) {
        var aluno = alunoRepository.buscarPorId(alunoId)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado (ID " + alunoId + ")."));

        var plano = planoPagamentoRepository.buscarPorId(planoId)
                .orElseThrow(() -> new IllegalArgumentException("Plano não encontrado (ID " + planoId + ")."));

        alunoParaPlano.put(aluno.getId(), plano.getId());          // salva vínculo em memória
        gerarPrimeiraFaturaDoPlano(aluno.getId(), plano);           // opcional: já cria 1ª fatura
    }

    // Permite consultar o plano do aluno em outras telas (opcional)
    public PlanoPagamento obterPlanoDoAluno(int alunoId) {
        Integer planoId = alunoParaPlano.get(alunoId);
        if (planoId == null) return null;
        return planoPagamentoRepository.buscarPorId(planoId).orElse(null);
    }

    // Gera uma fatura simples (1ª parcela) com vencimento no dia 10
    private void gerarPrimeiraFaturaDoPlano(int alunoId, PlanoPagamento plano) {
        LocalDate hoje = LocalDate.now();
        LocalDate vencimento = (hoje.getDayOfMonth() <= 10)
                ? hoje.withDayOfMonth(10)
                : hoje.plusMonths(1).withDayOfMonth(10);

        // Usa o construtor disponível (sem status nem descrição)
        Fatura f = new Fatura(0, alunoId, plano.getValor(), vencimento);

        // Define o status explicitamente (embora o construtor já defina PENDENTE)
        f.setStatus(StatusFatura.PENDENTE);

        // (Opcional) define o plano se quiser usar o campo planoPagamentoId
        f.setPlanoPagamentoId(plano.getId());

        faturaRepository.salvar(f);
    }


    // ================= HU-09: Pagamento =================
    public boolean registrarPagamentoFatura(int faturaId, LocalDate dataPagamento) {
        Optional<Fatura> faturaOpt = faturaRepository.buscarPorId(faturaId);
        if (faturaOpt.isEmpty())
            throw new IllegalArgumentException("Fatura com ID " + faturaId + " não encontrada.");

        Fatura fatura = faturaOpt.get();
        if (fatura.getStatus() == StatusFatura.PENDENTE || fatura.getStatus() == StatusFatura.VENCIDA) {
            fatura.setStatus(StatusFatura.PAGA);
            fatura.setDataPagamento(dataPagamento);
            faturaRepository.salvar(fatura);
            return true;
        }
        return false; // já estava paga
    }

    public List<Fatura> listarFaturasAluno(int alunoId) {
        return faturaRepository.buscarPorAlunoId(alunoId);
    }

    // Geração recorrente (pode evoluir depois)
    public void gerarFaturasMensais() {
        System.out.println("[Service] Geração mensal de faturas: implementar conforme regra do plano.");
    }
}
