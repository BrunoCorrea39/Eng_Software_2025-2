package com.escolinha.repository;

import com.escolinha.domain.PlanoPagamento;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PlanoPagamentoRepositoryMemoria implements PlanoPagamentoRepository {

    // ---------- Singleton ----------
    private static final PlanoPagamentoRepositoryMemoria INSTANCE = new PlanoPagamentoRepositoryMemoria();
    public static PlanoPagamentoRepositoryMemoria getInstance() { return INSTANCE; }
    private PlanoPagamentoRepositoryMemoria() {} // Impede instanciamento externo

    // ---------- Armazenamento compartilhado (permanece vivo enquanto o programa roda) ----------
    private static final Map<Integer, PlanoPagamento> DB = new ConcurrentHashMap<>();
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    @Override
    public synchronized PlanoPagamento salvar(PlanoPagamento plano) {
        if (plano == null) throw new IllegalArgumentException("Plano de Pagamento não pode ser nulo.");

        if (plano.getId() == 0) {
            plano.setId(SEQ.incrementAndGet());
            System.out.println("[RepoMemoria] Novo plano de pagamento inserido: ID=" + plano.getId());
        } else {
            System.out.println("[RepoMemoria] Plano de pagamento atualizado: ID=" + plano.getId());
        }

        DB.put(plano.getId(), plano);
        return plano;
    }

    @Override
    public Optional<PlanoPagamento> buscarPorId(int id) {
        return Optional.ofNullable(DB.get(id));
    }

    @Override
    public List<PlanoPagamento> listarTodos() {
        return new ArrayList<>(DB.values());
    }
    @Override
    public Optional<PlanoPagamento> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) return Optional.empty();
        return DB.values().stream()
            .filter(p -> p.getNome() != null && p.getNome().equalsIgnoreCase(nome.trim()))
            .findFirst();
    }

    @Override
    public boolean deletarPorId(int id) {
        PlanoPagamento removido = DB.remove(id);
        if (removido != null) {
            System.out.println("[RepoMemoria] Plano de pagamento deletado: ID=" + id);
            return true;
        } else {
            System.out.println("[RepoMemoria] Plano de pagamento com ID=" + id + " não encontrado para deleção.");
            return false;
        }
    }
}
