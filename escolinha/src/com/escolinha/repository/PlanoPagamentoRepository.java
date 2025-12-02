package com.escolinha.repository;

import com.escolinha.domain.PlanoPagamento; // Importe a classe do domain
import java.util.List;
import java.util.Optional;

public interface PlanoPagamentoRepository {

    /**
     * Salva um plano de pagamento (novo ou atualização).
     * @param plano O plano a ser salvo.
     * @return O plano salvo.
     */
    PlanoPagamento salvar(PlanoPagamento plano);

    /**
     * Busca um plano de pagamento pelo ID.
     * @param id O ID do plano.
     * @return Optional contendo o plano se encontrado.
     */
    Optional<PlanoPagamento> buscarPorId(int id);

    /**
     * Lista todos os planos de pagamento disponíveis.
     * @return Lista de todos os planos.
     */
    List<PlanoPagamento> listarTodos();
    Optional<PlanoPagamento> buscarPorNome(String nome);

    /**
     * Deleta um plano de pagamento pelo ID.
     * @param id O ID do plano.
     * @return true se deletado com sucesso.
     */
    boolean deletarPorId(int id);
}