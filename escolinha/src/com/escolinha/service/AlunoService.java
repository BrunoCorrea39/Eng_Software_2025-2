package com.escolinha.service;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.Responsavel;
import com.escolinha.repository.AlunoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AlunoService {

    private final AlunoRepository alunoRepository;

    // Injeção de dependência via construtor
    public AlunoService(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    /**
     * Cadastra um novo aluno (Implementa lógica da HU-03).
     */
    public Aluno cadastrarAluno(String nome,
                                LocalDate dataNasc,
                                List<Responsavel> responsaveis,
                                String obsMedicas) {

        // --- Regras de Negócio / Validações ---
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do aluno não pode ser vazio.");
        }

        if (dataNasc == null || dataNasc.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento inválida.");
        }

        // ✅ NOVA REGRA: evitar cadastro duplicado
        if (alunoRepository.existeAlunoPorNomeEData(nome.trim(), dataNasc)) {
            throw new IllegalArgumentException("Já existe um aluno cadastrado com este nome e data de nascimento.");
        }

        // Cria o objeto Aluno (ID 0 para indicar que é novo)
        Aluno novoAluno = new Aluno(0, nome.trim(), dataNasc, responsaveis, obsMedicas);

        // Persiste no repositório
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
        // Aqui poderia ter regra: não deletar se tiver pendência financeira, etc.
        return alunoRepository.deletarPorId(id);
    }

    // Comentários da HU-07 podem ficar aqui se quiser, mas não afetam a lógica de negócio.
}
