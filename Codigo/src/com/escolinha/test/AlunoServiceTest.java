package com.escolinha.test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.Responsavel;
import com.escolinha.repository.AlunoRepository;
import com.escolinha.repository.AlunoRepositoryMemoria;
import com.escolinha.service.AlunoService;

class AlunoServiceTest {

    private AlunoService alunoService;
    private AlunoRepository alunoRepo;

    @BeforeEach
    void setUp() {
        // Inicializa um repositório limpo antes de cada teste para garantir isolamento
        alunoRepo = new AlunoRepositoryMemoria();
        alunoService = new AlunoService(alunoRepo);
    }

    @Test
    @DisplayName("Deve cadastrar um aluno com sucesso quando os dados são válidos")
    void testCadastrarAlunoSucesso() {
        // Cenário (Arrange)
        String nome = "Joãozinho Silva";
        LocalDate dataNasc = LocalDate.of(2015, 5, 20);
        List<Responsavel> responsaveis = new ArrayList<>();
        String obs = "Nenhuma";

        // Ação (Act)
        Aluno alunoCadastrado = alunoService.cadastrarAluno(nome, dataNasc, responsaveis, obs);

        // Verificação (Assert)
        assertNotNull(alunoCadastrado, "O aluno retornado não deveria ser nulo");
        assertTrue(alunoCadastrado.getId() > 0, "O aluno deveria ter um ID gerado maior que 0");
        assertEquals(nome, alunoCadastrado.getNome(), "O nome do aluno cadastrado está incorreto");
        
        // Verifica se realmente foi salvo no repositório
        Optional<Aluno> busca = alunoRepo.buscarPorId(alunoCadastrado.getId());
        assertTrue(busca.isPresent(), "O aluno deveria ser encontrado no repositório");
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar aluno com nome vazio")
    void testCadastrarAlunoNomeVazioErro() {
        // Cenário & Ação & Verificação
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            alunoService.cadastrarAluno("", LocalDate.of(2015, 1, 1), new ArrayList<>(), "");
        });

        assertEquals("Nome do aluno não pode ser vazio.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar cadastrar aluno com data de nascimento futura")
    void testCadastrarAlunoDataFuturaErro() {
        // Cenário & Ação & Verificação
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            alunoService.cadastrarAluno("Viajante do Tempo", LocalDate.now().plusDays(1), new ArrayList<>(), "");
        });

        assertEquals("Data de nascimento inválida.", exception.getMessage());
    }
    
    @Test
    void testValidarIdadeMinima() {
        // Tenta cadastrar bebê de 1 ano
        LocalDate dataNascBebe = LocalDate.now().minusYears(1);
        
        assertThrows(IllegalArgumentException.class, () -> {
            alunoService.cadastrarAluno("Bebê", dataNascBebe, new ArrayList<>(), "");
        });
    }
}