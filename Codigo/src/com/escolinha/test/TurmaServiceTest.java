package com.escolinha.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.Avaliacao;
import com.escolinha.domain.Frequencia;
import com.escolinha.domain.Turma;
import com.escolinha.repository.AlunoRepository;
import com.escolinha.repository.AlunoRepositoryMemoria;
import com.escolinha.repository.AvaliacaoRepository;
import com.escolinha.repository.AvaliacaoRepositoryMemoria;
import com.escolinha.repository.ComunicadoRepository;
import com.escolinha.repository.ComunicadoRepositoryMemoria;
import com.escolinha.repository.FrequenciaRepository;
import com.escolinha.repository.FrequenciaRepositoryMemoria;
import com.escolinha.repository.TurmaRepository;
import com.escolinha.repository.TurmaRepositoryMemoria;
import com.escolinha.service.TurmaService;

class TurmaServiceTest {

    private TurmaService turmaService;
    private TurmaRepository turmaRepo;
    private AlunoRepository alunoRepo;
    // Outros repositórios mockados/memória necessários para o construtor
    private FrequenciaRepository freqRepo;
    private AvaliacaoRepository avalRepo;
    private ComunicadoRepository comRepo;

    @BeforeEach
    void setUp() {
        turmaRepo = new TurmaRepositoryMemoria();
        alunoRepo = new AlunoRepositoryMemoria();
        freqRepo = new FrequenciaRepositoryMemoria();
        avalRepo = new AvaliacaoRepositoryMemoria();
        comRepo = new ComunicadoRepositoryMemoria();
        
        turmaService = new TurmaService(turmaRepo, alunoRepo, freqRepo, avalRepo, comRepo);
    }

    @Test
    @DisplayName("Deve criar uma turma com sucesso")
    void testCriarTurma() {
        Turma turma = turmaService.criarTurma("Sub-15 Vespertino", 10); // ID Treinador fictício

        assertNotNull(turma);
        assertEquals("Sub-15 Vespertino", turma.getNome());
        assertEquals(10, turma.getTreinadorId());
        assertTrue(turma.getId() > 0);
    }

    @Test
    @DisplayName("Deve adicionar aluno na turma corretamente")
    void testAdicionarAlunoNaTurma() {
        // Cenário: Criar turma e aluno
        Turma turma = turmaService.criarTurma("Turma Teste", 1);
        Aluno aluno = new Aluno(0, "Aluno Teste", LocalDate.now(), new ArrayList<>(), "");
        aluno = alunoRepo.salvar(aluno); // Salva para ter ID

        // Ação
        boolean adicionou = turmaService.adicionarAlunoNaTurma(turma.getId(), aluno.getId());

        // Verificação
        assertTrue(adicionou, "Deveria retornar true ao adicionar com sucesso");
        
        // Verifica se o aluno está na lista da turma recuperada do repositório
        Optional<Turma> turmaAtualizada = turmaRepo.buscarPorId(turma.getId());
        assertTrue(turmaAtualizada.isPresent());
        assertEquals(1, turmaAtualizada.get().getAlunos().size(), "Turma deve ter 1 aluno");
        assertEquals(aluno.getId(), turmaAtualizada.get().getAlunos().get(0).getId());
    }
    
    @Test
    @DisplayName("Cenário 4.2: Registro de Avaliação")
    void testRegistrarAvaliacao() {
        // Dado que: alunoId=5 e treinadorId=1 existem.
        Aluno aluno = alunoRepo.salvar(new Aluno(0, "Aluno Aval", LocalDate.now(), new ArrayList<>(), ""));
        int alunoId = aluno.getId();
        int treinadorId = 1;
        String comTec = "Ótimo chute";
        
        // Quando: O TurmaService.registrarAvaliacao() é chamado com comentários.
        Avaliacao avaliacao = turmaService.registrarAvaliacao(alunoId, treinadorId, comTec, "Bom tático", "Disciplinado");

        // Então: O método deve retornar um objeto Avaliacao válido.
        assertNotNull(avaliacao);
        assertTrue(avaliacao.getId() > 0);
        assertEquals(alunoId, avaliacao.getAlunoId());
        assertEquals(LocalDate.now(), avaliacao.getDataAvaliacao());
        
        // E: Deve ser encontrado no repositório.
        Optional<Avaliacao> busca = avalRepo.buscarPorId(avaliacao.getId());
        assertTrue(busca.isPresent());
        assertEquals(comTec, busca.get().getComentarioTecnico());
    }
    
    @Test
    void testAdicionarAlunoTurmaCheia() {
        // Crie uma turma e lote ela com 20 alunos fictícios
        Turma turma = turmaService.criarTurma("Turma Lotada", 1);
        for(int i=0; i<20; i++) {
            // Adiciona 20 alunos diretamente ou via mock
            turma.adicionarAluno(new Aluno(i, "Aluno"+i, null, null, null)); 
        }
        
        // Tenta adicionar o 21º
        boolean sucesso = turmaService.adicionarAlunoNaTurma(turma.getId(), 99);
        
        // Deve falhar (false) ou lançar exceção
        assertFalse(sucesso, "Não deveria permitir adicionar aluno em turma cheia");
    }
}