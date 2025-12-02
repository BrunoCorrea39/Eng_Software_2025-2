package com.escolinha.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.Fatura;
import com.escolinha.domain.PlanoPagamento;
import com.escolinha.domain.StatusFatura;
import com.escolinha.domain.Turma;
import com.escolinha.repository.*;
import com.escolinha.service.*;

class SistemaIntegracaoTest {

    private AlunoService alunoService;
    private TurmaService turmaService;
    private FinanceiroService financeiroService;
    
    // Repositórios
    private AlunoRepository alunoRepo;
    private TurmaRepository turmaRepo;
    private FaturaRepository faturaRepo;
    private PlanoPagamentoRepository planoRepo;
    private FrequenciaRepository freqRepo;
    private AvaliacaoRepository avalRepo;
    private ComunicadoRepository comRepo;

    @BeforeEach
    void setUp() {
        // Inicializa todo o "banco de dados" em memória limpo
        alunoRepo = new AlunoRepositoryMemoria();
        turmaRepo = new TurmaRepositoryMemoria();
        faturaRepo = new FaturaRepositoryMemoria();
        planoRepo = new PlanoPagamentoRepositoryMemoria();
        freqRepo = new FrequenciaRepositoryMemoria();
        avalRepo = new AvaliacaoRepositoryMemoria();
        comRepo = new ComunicadoRepositoryMemoria();

        // Inicializa serviços conectados
        alunoService = new AlunoService(alunoRepo);
        financeiroService = new FinanceiroService(faturaRepo, planoRepo, alunoRepo);
        turmaService = new TurmaService(turmaRepo, alunoRepo, freqRepo, avalRepo, comRepo);
    }

    @Test
    @DisplayName("Teste de Sistema: Fluxo Completo do Aluno (Matrícula -> Turma -> Pagamento com Desconto)")
    void testFluxoCompletoSistema() {
        // 1. Cadastrar Aluno (Validando Idade Mínima - TDD)
        System.out.println("Passo 1: Cadastrando Aluno...");
        LocalDate dataNasc = LocalDate.now().minusYears(10); // 10 anos (Válido)
        Aluno aluno = alunoService.cadastrarAluno("Aluno Integração", dataNasc, new ArrayList<>(), "Nenhuma");
        
        assertNotNull(aluno);
        assertTrue(aluno.getId() > 0);

        // 2. Criar Turma e Matricular (Validando Capacidade - TDD)
        System.out.println("Passo 2: Criando Turma e Matriculando...");
        Turma turma = turmaService.criarTurma("Sub-10 Manhã", 1);
        boolean matriculaSucesso = turmaService.adicionarAlunoNaTurma(turma.getId(), aluno.getId());
        
        assertTrue(matriculaSucesso);
        assertEquals(1, turmaRepo.buscarPorId(turma.getId()).get().getAlunos().size());

        // 3. Criar Plano e Gerar Fatura
        System.out.println("Passo 3: Gerando Cobrança...");
        PlanoPagamento plano = financeiroService.criarPlano("Mensal", new BigDecimal("100.00"), 1);
        
        // Simula geração de fatura (como no método do MainFrame)
        Fatura fatura = new Fatura(0, aluno.getId(), plano.getValor(), LocalDate.now().plusDays(5)); // Vence em 5 dias
        fatura = faturaRepo.salvar(fatura);

        // 4. Realizar Pagamento Antecipado (Validando Desconto - TDD)
        System.out.println("Passo 4: Pagando Antecipado...");
        LocalDate dataPagamento = LocalDate.now(); // Hoje (antes do vencimento)
        
        // Verifica cálculo do desconto antes de pagar
        BigDecimal valorComDesconto = financeiroService.calcularValorPagamento(fatura.getValor(), fatura.getDataVencimento(), dataPagamento);
        assertEquals(0, new BigDecimal("95.00").compareTo(valorComDesconto), "Deve ter 5% de desconto");

        // Efetiva o pagamento
        boolean pagamentoSucesso = financeiroService.registrarPagamentoFatura(fatura.getId(), dataPagamento);
        assertTrue(pagamentoSucesso);

        // 5. Verificação Final do Estado
        Fatura faturaFinal = faturaRepo.buscarPorId(fatura.getId()).get();
        assertEquals(StatusFatura.PAGA, faturaFinal.getStatus());
        
        System.out.println("Teste de Sistema Completo: SUCESSO!");
    }
}