package com.escolinha.repository;

import com.escolinha.domain.Responsavel; // Importe a classe do domain
import java.util.List;
import java.util.Optional;

public interface ResponsavelRepository {

    /**
     * Salva um responsável (novo ou atualização).
     * @param responsavel O responsável a ser salvo.
     * @return O responsável salvo.
     */
    Responsavel salvar(Responsavel responsavel);

    /**
     * Busca um responsável pelo ID.
     * @param id O ID do responsável.
     * @return Optional contendo o responsável se encontrado.
     */
    Optional<Responsavel> buscarPorId(int id);

    /**
     * Lista todos os responsáveis.
     * @return Lista de todos os responsáveis.
     */
    List<Responsavel> listarTodos();

    /**
     * Deleta um responsável pelo ID.
     * @param id O ID do responsável.
     * @return true se deletado com sucesso.
     */
    boolean deletarPorId(int id);

    /**
     * Busca responsáveis pelo email (ou login).
     * @param email O email a ser buscado.
     * @return Optional contendo o responsável se encontrado.
     */
    Optional<Responsavel> buscarPorEmail(String email); // Pode ser útil para login
}