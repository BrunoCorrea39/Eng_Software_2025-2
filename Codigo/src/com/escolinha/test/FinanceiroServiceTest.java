package com.escolinha.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.Fatura;
import com.escolinha.domain.PlanoPagamento;
import com.escolinha.domain.StatusFatura;
import com.escolinha.repository.AlunoRepository;
import com.escolinha.repository.AlunoRepositoryMemoria;
import com.escolinha.repository.FaturaRepository;
import com.escolinha.repository.FaturaRepositoryMemoria;
import com.escolinha.repository.PlanoPagamentoRepository;
import com.escolinha.repository.PlanoPagamentoRepositoryMemoria;
import com.escolinha.service.FinanceiroService;

class FinanceiroServiceTest {

    private FinanceiroService financeiroService;
    private FaturaRepository faturaRepo;
    private PlanoPagamentoRepository planoRepo;
    private AlunoRepository alunoRepo; // Necessário para alguns métodos do FinanceiroService

    @BeforeEach
    void setUp() {
        faturaRepo = new FaturaRepositoryMemoria();
        planoRepo = new PlanoPagamentoRepositoryMemoria();
        alunoRepo = new AlunoRepositoryMemoria();
        financeiroService = new FinanceiroService(faturaRepo, planoRepo, alunoRepo);
    }
    
    @Test
    @DisplayName("Deve criar um plano de pagamento corretamente")
    void testCriarPlanoSucesso() {
        PlanoPagamento plano = financeiroService.criarPlano("Trimestral", new BigDecimal("400.00"), 3);

        assertNotNull(plano);
        assertEquals("Trimestral", plano.getNome());
        assertEquals(new BigDecimal("400.00"), plano.getValor());
        assertEquals(3, plano.getDuracaoMeses());
        assertEquals(1, planoRepo.listarTodos().size());
    }

    @Test
    @DisplayName("Deve registrar o pagamento de uma fatura pendente com sucesso")
    void testRegistrarPagamentoSucesso() {
        // Cenário: Criar um aluno e uma fatura pendente
        Aluno aluno = new Aluno(1, "Teste", LocalDate.now(), new ArrayList<>(), "");
        // Simula salvar aluno (opcional para este teste específico de fatura, mas boa prática)
        
        Fatura fatura = new Fatura(0, 1, new BigDecimal("100.00"), LocalDate.now().plusDays(5));
        fatura = faturaRepo.salvar(fatura); // Salva com status PENDENTE
        
        int faturaId = fatura.getId();

        // Ação: Pagar a fatura
        boolean sucesso = financeiroService.registrarPagamentoFatura(faturaId, LocalDate.now());

        // Verificação
        assertTrue(sucesso, "O pagamento deveria ser registrado com sucesso");
        
        Fatura faturaAtualizada = faturaRepo.buscarPorId(faturaId).get();
        assertEquals(StatusFatura.PAGA, faturaAtualizada.getStatus(), "Status da fatura deve ser PAGA");
        assertNotNull(faturaAtualizada.getDataPagamento(), "Data de pagamento não deve ser nula");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar pagar uma fatura inexistente")
    void testRegistrarPagamentoFaturaInexistente() {
        assertThrows(IllegalArgumentException.class, () -> {
            financeiroService.registrarPagamentoFatura(999, LocalDate.now());
        });
    }
    
    @Test
    void testCalcularValorComDescontoAntecipado() {
        // Cenário: Fatura de 100 reais, vencimento dia 20, pagamento dia 10
        BigDecimal valorOriginal = new BigDecimal("100.00");
        LocalDate vencimento = LocalDate.of(2025, 12, 20);
        LocalDate pagamento = LocalDate.of(2025, 12, 10);
        
        // Ação (Método ainda não existe, vai dar erro de compilação/falha)
        BigDecimal valorFinal = financeiroService.calcularValorPagamento(valorOriginal, vencimento, pagamento);
        
        // Verificação: 5% de desconto = 95.00
        assertEquals(0, new BigDecimal("95.00").compareTo(valorFinal));
    }
}