package com.escolinha.repository;

import com.escolinha.domain.Avaliacao; // Importe a classe do domain
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface AvaliacaoRepository {

    /**
     * Salva uma avaliação (nova ou atualização).
     * @param avaliacao A avaliação a ser salva.
     * @return A avaliação salva.
     */
    Avaliacao salvar(Avaliacao avaliacao);

    /**
     * Busca uma avaliação pelo ID.
     * @param id O ID da avaliação.
     * @return Optional contendo a avaliação se encontrada.
     */
    Optional<Avaliacao> buscarPorId(int id);

    /**
     * Lista todas as avaliações de um aluno específico.
     * @param alunoId O ID do aluno.
     * @return Lista de avaliações do aluno.
     */
    List<Avaliacao> buscarPorAlunoId(int alunoId);

    /**
     * Lista todas as avaliações feitas por um treinador específico.
     * @param treinadorId O ID do treinador.
     * @return Lista de avaliações feitas pelo treinador.
     */
    List<Avaliacao> buscarPorTreinadorId(int treinadorId);

    /**
     * Lista todas as avaliações dentro de um período.
     * @param inicio Data de início do período.
     * @param fim Data de fim do período.
     * @return Lista de avaliações no período.
     */
    List<Avaliacao> buscarPorPeriodo(LocalDate inicio, LocalDate fim);

    /**
     * Deleta uma avaliação pelo ID.
     * @param id O ID da avaliação.
     * @return true se deletada com sucesso.
     */
    boolean deletarPorId(int id);
}