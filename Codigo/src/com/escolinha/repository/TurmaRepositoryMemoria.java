package com.escolinha.repository;

import com.escolinha.domain.Turma;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class TurmaRepositoryMemoria implements TurmaRepository {

    // ---------- Singleton ----------
    private static final TurmaRepositoryMemoria INSTANCE = new TurmaRepositoryMemoria();
    public static TurmaRepositoryMemoria getInstance() { return INSTANCE; }
    private TurmaRepositoryMemoria() {} // impede new externo

    // ---------- Armazenamento compartilhado ----------
    private static final Map<Integer, Turma> DB = new ConcurrentHashMap<>();
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    @Override
    public synchronized Turma salvar(Turma turma) {
        if (turma == null) {
            throw new IllegalArgumentException("Turma não pode ser nula.");
        }

        if (turma.getId() == 0) {
            int novoId = SEQ.incrementAndGet();
            turma.setId(novoId);
            DB.put(novoId, turma);
            System.out.println("[RepoMemoria] Nova turma criada: ID=" + novoId + ", Nome=" + turma.getNome());
        } else {
            if (!DB.containsKey(turma.getId())) {
                throw new IllegalArgumentException("Tentativa de atualizar turma inexistente com ID: " + turma.getId());
            }
            DB.put(turma.getId(), turma);
            System.out.println("[RepoMemoria] Turma atualizada: ID=" + turma.getId() + ", Nome=" + turma.getNome());
        }
        return turma;
    }

    @Override
    public Optional<Turma> buscarPorId(int id) {
        return Optional.ofNullable(DB.get(id));
    }

    @Override
    public List<Turma> listarTodas() {
        return new ArrayList<>(DB.values());
    }

    @Override
    public boolean deletarPorId(int id) {
        Turma removida = DB.remove(id);
        if (removida != null) {
            System.out.println("[RepoMemoria] Turma deletada: ID=" + id);
            return true;
        } else {
            System.out.println("[RepoMemoria] Turma com ID=" + id + " não encontrada para deleção.");
            return false;
        }
    }

    // Exemplo de método adicional útil:
    public List<Turma> buscarPorTreinadorId(int treinadorId) {
        return DB.values().stream()
                .filter(t -> t.getTreinadorId() == treinadorId)
                .toList();
    }
}
