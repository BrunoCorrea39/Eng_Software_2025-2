package com.escolinha.repository;

import com.escolinha.domain.Fatura;
import com.escolinha.domain.StatusFatura;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FaturaRepositoryMemoria implements FaturaRepository {

    // ---------- Singleton ----------
    private static final FaturaRepositoryMemoria INSTANCE = new FaturaRepositoryMemoria();
    public static FaturaRepositoryMemoria getInstance() { return INSTANCE; }
    private FaturaRepositoryMemoria() {} // impede new

    // ---------- Armazenamento compartilhado ----------
    private static final Map<Integer, Fatura> DB = new ConcurrentHashMap<>();
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    @Override
    public synchronized Fatura salvar(Fatura fatura) {
        if (fatura == null) {
            throw new IllegalArgumentException("Fatura não pode ser nula.");
        }

        if (fatura.getId() == 0) {
            fatura.setId(SEQ.incrementAndGet());
        }
        DB.put(fatura.getId(), fatura);
        System.out.println("[RepoMemoria] Fatura salva: ID=" + fatura.getId() + " | Aluno ID=" + fatura.getAlunoId());
        return fatura;
    }

    @Override
    public Optional<Fatura> buscarPorId(int id) {
        return Optional.ofNullable(DB.get(id));
    }

    @Override
    public List<Fatura> listarTodas() {
        return new ArrayList<>(DB.values());
    }

    @Override
    public List<Fatura> buscarPorAlunoId(int alunoId) {
        return DB.values().stream()
                .filter(f -> f.getAlunoId() == alunoId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Fatura> buscarPorStatus(StatusFatura status) {
        if (status == null) return Collections.emptyList();
        return DB.values().stream()
                .filter(f -> f.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<Fatura> buscarVencidas(LocalDate dataReferencia) {
        if (dataReferencia == null) return Collections.emptyList();
        return DB.values().stream()
                .filter(f -> f.getStatus() == StatusFatura.PENDENTE && f.getDataVencimento().isBefore(dataReferencia))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deletarPorId(int id) {
        Fatura removida = DB.remove(id);
        if (removida != null) {
            System.out.println("[RepoMemoria] Fatura deletada: ID=" + id);
            return true;
        }
        return false;
    }
}
