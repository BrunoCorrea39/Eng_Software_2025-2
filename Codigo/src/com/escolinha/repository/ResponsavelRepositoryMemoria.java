package com.escolinha.repository;

import com.escolinha.domain.Responsavel;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ResponsavelRepositoryMemoria implements ResponsavelRepository {

    // ---------- Singleton ----------
    private static final ResponsavelRepositoryMemoria INSTANCE = new ResponsavelRepositoryMemoria();
    public static ResponsavelRepositoryMemoria getInstance() { return INSTANCE; }
    private ResponsavelRepositoryMemoria() {} // impede instanciar fora

    // ---------- Armazenamento compartilhado ----------
    private static final Map<Integer, Responsavel> DB = new ConcurrentHashMap<>();
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    @Override
    public synchronized Responsavel salvar(Responsavel responsavel) {
        if (responsavel == null) {
            throw new IllegalArgumentException("Responsável não pode ser nulo.");
        }
        if (responsavel.getId() == 0) {
            responsavel.setId(SEQ.incrementAndGet());
        }
        DB.put(responsavel.getId(), responsavel);
        System.out.println("[RepoMemoria] Responsável salvo: ID=" + responsavel.getId() + ", Nome=" + responsavel.getNome());
        return responsavel;
    }

    @Override
    public Optional<Responsavel> buscarPorId(int id) {
        return Optional.ofNullable(DB.get(id));
    }

    @Override
    public List<Responsavel> listarTodos() {
        return new ArrayList<>(DB.values());
    }

    @Override
    public boolean deletarPorId(int id) {
        return DB.remove(id) != null;
    }

    @Override
    public Optional<Responsavel> buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) return Optional.empty();
        return DB.values().stream()
                .filter(r ->
                        (r.getLogin() != null && r.getLogin().equalsIgnoreCase(email)) ||
                        (r.getEmail() != null && r.getEmail().equalsIgnoreCase(email)))
                .findFirst();
    }
}
