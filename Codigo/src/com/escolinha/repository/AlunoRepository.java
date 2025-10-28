package com.escolinha.repository;

import com.escolinha.domain.Aluno; // Importe a classe do domain
import java.util.List;
import java.util.Optional; // Usar Optional é uma boa prática para buscas que podem não retornar nada

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
    boolean deletarPorId(int id); // Opcional, mas útil

    // Você pode adicionar outros métodos de busca conforme necessário
    // Ex: List<Aluno> buscarPorNome(String nome);
}