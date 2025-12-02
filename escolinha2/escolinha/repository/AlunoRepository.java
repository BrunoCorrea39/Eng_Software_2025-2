package com.escolinha.repository;

import com.escolinha.domain.Aluno;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AlunoRepository {

    /**
     * Salva um aluno. Se o aluno tiver ID 0, insere um novo; caso contrário, atualiza o existente.
     * @param aluno O aluno a ser salvo.
     * @return O aluno salvo (possivelmente com o ID atualizado).
     */
    Aluno salvar(Aluno aluno);

    /**
     * Busca um aluno pelo seu ID.
     * @param id O ID do aluno.
     * @return Um Optional contendo o aluno se encontrado, ou Optional vazio caso contrário.
     */
    Optional<Aluno> buscarPorId(int id);

    /**
     * Lista todos os alunos cadastrados.
     * @return Uma lista com todos os alunos.
     */
    List<Aluno> listarTodos();

    /**
     * Deleta um aluno pelo seu ID.
     * @param id O ID do aluno a ser deletado.
     * @return true se o aluno foi deletado, false caso contrário.
     */
    boolean deletarPorId(int id);

    /**
     * Verifica se já existe um aluno cadastrado com o mesmo nome e data de nascimento.
     * Usado para evitar cadastros duplicados.
     */
    boolean existeAlunoPorNomeEData(String nome, LocalDate dataNasc);
}
