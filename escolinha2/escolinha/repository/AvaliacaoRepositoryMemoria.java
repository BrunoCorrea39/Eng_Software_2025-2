package com.escolinha.repository;

import com.escolinha.domain.Avaliacao;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class AvaliacaoRepositoryMemoria implements AvaliacaoRepository {

    // ----------- Singleton -----------
    private static final AvaliacaoRepositoryMemoria INSTANCE = new AvaliacaoRepositoryMemoria();
    public static AvaliacaoRepositoryMemoria getInstance() { return INSTANCE; }
    private AvaliacaoRepositoryMemoria() {} // construtor privado

    // ----------- Armazenamento compartilhado -----------
    private static final Map<Integer, Avaliacao> DB = new ConcurrentHashMap<>();
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    @Override
    public synchronized Avaliacao salvar(Avaliacao avaliacao) {
        if (avaliacao == null) {
            throw new IllegalArgumentException("Avaliação não pode ser nula.");
        }

        if (avaliacao.getId() == 0) {
            int novoId = SEQ.incrementAndGet();
            avaliacao.setId(novoId);
            DB.put(novoId, avaliacao);
            System.out.println("[RepoMemoria] Nova avaliação inserida: ID=" + novoId + " para Aluno ID=" + avaliacao.getAlunoId());
        } else {
            if (!DB.containsKey(avaliacao.getId())) {
                throw new IllegalArgumentException("Tentativa de atualizar avaliação inexistente com ID: " + avaliacao.getId());
            }
            DB.put(avaliacao.getId(), avaliacao);
            System.out.println("[RepoMemoria] Avaliação atualizada: ID=" + avaliacao.getId());
        }
        return avaliacao;
    }

    @Override
    public Optional<Avaliacao> buscarPorId(int id) {
        return Optional.ofNullable(DB.get(id));
    }

    @Override
    public List<Avaliacao> buscarPorAlunoId(int alunoId) {
        return DB.values().stream()
                .filter(a -> a.getAlunoId() == alunoId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Avaliacao> buscarPorTreinadorId(int treinadorId) {
        return DB.values().stream()
                .filter(a -> a.getTreinadorId() == treinadorId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Avaliacao> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null || inicio.isAfter(fim)) return new ArrayList<>();
        return DB.values().stream()
                .filter(a -> !a.getDataAvaliacao().isBefore(inicio) && !a.getDataAvaliacao().isAfter(fim))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deletarPorId(int id) {
        Avaliacao removida = DB.remove(id);
        if (removida != null) {
            System.out.println("[RepoMemoria] Avaliação deletada: ID=" + id);
            return true;
        } else {
            System.out.println("[RepoMemoria] Avaliação com ID=" + id + " não encontrada para deleção.");
            return false;
        }
    }
}
