package com.escolinha.repository;

import com.escolinha.domain.Aluno;

import java.time.LocalDate;                       // ✅ IMPORT NECESSÁRIO
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AlunoRepositoryMemoria implements AlunoRepository {

    // ---------- Singleton ----------
    private static final AlunoRepositoryMemoria INSTANCE = new AlunoRepositoryMemoria();
    public static AlunoRepositoryMemoria getInstance() { return INSTANCE; }
    public AlunoRepositoryMemoria() {}

    // ---------- Armazenamento compartilhado ----------
    private static final Map<Integer, Aluno> DB = new ConcurrentHashMap<>();
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    // ---------- Métodos da Interface ----------

    @Override
    public synchronized Aluno salvar(Aluno aluno) {
        if (aluno == null)
            throw new IllegalArgumentException("Aluno não pode ser nulo.");

        if (aluno.getId() <= 0) {
            aluno.setId(SEQ.incrementAndGet());
        }

        DB.put(aluno.getId(), aluno);
        return aluno;
    }

    @Override
    public Optional<Aluno> buscarPorId(int id) {
        return Optional.ofNullable(DB.get(id));
    }

    @Override
    public List<Aluno> listarTodos() {
        return new ArrayList<>(DB.values());
    }

    @Override
    public boolean deletarPorId(int id) {
        return DB.remove(id) != null;
    }

    @Override                                           // ✅ OVERRIDE ADICIONADO
    public boolean existeAlunoPorNomeEData(String nome, LocalDate dataNasc) {
        if (nome == null || dataNasc == null)
            return false;

        return DB.values().stream()
                .anyMatch(a ->
                        a.getNome().equalsIgnoreCase(nome.trim())
                        && a.getDataNascimento().equals(dataNasc)   // <-- CORRIGIDO AQUI
                );
    }
    // ---------- Métodos auxiliares (opcional) ----------

    /** Reseta o banco em memória (útil para testes). */
    public void limparTudo() {
        DB.clear();
        SEQ.set(0);
    }
}
