package com.escolinha.service;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.Responsavel; // Importe se necessário
import com.escolinha.repository.AlunoRepository; // Importe a INTERFACE
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AlunoService {
	
    private final AlunoRepository alunoRepository; // Dependência da INTERFACE

    // Injeção de dependência via construtor
    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    /**
     * Cadastra um novo aluno (Implementa lógica da HU-03).
     */
    public Aluno cadastrarAluno(String nome, LocalDate dataNasc, List<Responsavel> responsaveis, String obsMedicas) {
        // --- Regras de Negócio ---
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do aluno não pode ser vazio.");
        }
        if (dataNasc == null || dataNasc.isAfter(LocalDate.now())) {
             throw new IllegalArgumentException("Data de nascimento inválida.");
        }
        // Outras validações podem ser adicionadas (ex: idade mínima?)

        // Cria o objeto Aluno (ID 0 para indicar que é novo)
        Aluno novoAluno = new Aluno(0, nome.trim(), dataNasc, responsaveis, obsMedicas);

        // Chama o repositório para salvar
        return alunoRepository.salvar(novoAluno);
    }

    /**
     * Busca um aluno pelo ID.
     */
    public Optional<Aluno> buscarAlunoPorId(int id) {
        return alunoRepository.buscarPorId(id);
    }

    /**
     * Lista todos os alunos.
     */
    public List<Aluno> listarAlunos() {
        return alunoRepository.listarTodos();
    }

    /**
     * Deleta um aluno.
     */
    public boolean deletarAluno(int id) {
        // Regra de negócio: talvez verificar se o aluno tem faturas pendentes antes de deletar?
        // Por enquanto, apenas deleta.
        return alunoRepository.deletarPorId(id);
    }

     // --- Métodos para HU-07 (Visualizar Desempenho) ---
     // Esses métodos podem precisar de outros repositórios (Avaliacao, Frequencia)
     // Por isso, talvez um AlunoCompletoDTO fosse útil, ou o serviço retorna
     // o Aluno e a View busca os outros dados em seus respectivos serviços.
     // Vamos manter simples por agora: a View chama AlunoService.buscarAlunoPorId()
     // e depois chama AvaliacaoService.buscarPorAlunoId() e FrequenciaService.buscarPorAlunoId().

}