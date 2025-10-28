package com.escolinha.repository;

import com.escolinha.domain.Comunicado; // Importe a classe do domain
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

public interface ComunicadoRepository {

    /**
     * Salva um comunicado (novo ou atualização).
     * @param comunicado O comunicado a ser salvo.
     * @return O comunicado salvo.
     */
    Comunicado salvar(Comunicado comunicado);

    /**
     * Busca um comunicado pelo ID.
     * @param id O ID do comunicado.
     * @return Optional contendo o comunicado se encontrado.
     */
    Optional<Comunicado> buscarPorId(int id);

    /**
     * Lista todos os comunicados gerais (sem turma específica).
     * @return Lista de comunicados gerais.
     */
    List<Comunicado> listarComunicadosGerais();

    /**
     * Lista todos os comunicados de uma turma específica.
     * @param turmaId O ID da turma.
     * @return Lista de comunicados da turma.
     */
    List<Comunicado> buscarPorTurmaId(int turmaId);

     /**
     * Lista todos os comunicados (gerais e de turmas específicas) publicados após uma data.
     * Útil para exibir no mural.
     * @param dataReferencia A data a partir da qual buscar.
     * @return Lista de comunicados recentes.
     */
    List<Comunicado> buscarRecentes(LocalDateTime dataReferencia);


    /**
     * Deleta um comunicado pelo ID.
     * @param id O ID do comunicado.
     * @return true se deletado com sucesso.
     */
    boolean deletarPorId(int id);
}