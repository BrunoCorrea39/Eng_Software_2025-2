package com.escolinha.repository;

import com.escolinha.domain.Frequencia;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FrequenciaRepositoryMemoria implements FrequenciaRepository {

    // ---------- Singleton ----------
    private static final FrequenciaRepositoryMemoria INSTANCE = new FrequenciaRepositoryMemoria();
    public static FrequenciaRepositoryMemoria getInstance() { return INSTANCE; }
    private FrequenciaRepositoryMemoria() {} // impede new

    // ---------- Armazenamento compartilhado ----------
    private static final Map<Integer, Frequencia> DB = new ConcurrentHashMap<>();
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    @Override
    public synchronized Frequencia salvar(Frequencia frequencia) {
        if (frequencia == null) throw new IllegalArgumentException("Frequência não pode ser nula.");
        // estratégia atual: sempre inserindo um novo registro
        frequencia.setId(SEQ.incrementAndGet());
        DB.put(frequencia.getId(), frequencia);
        System.out.println("[RepoMemoria] Frequência inserida: ID=" + frequencia.getId()
                + " | Aluno ID=" + frequencia.getAlunoId()
                + " | Turma ID=" + frequencia.getTurmaId()
                + " | Data=" + frequencia.getDataAula());
        return frequencia;
    }

    @Override
    public Optional<Frequencia> buscarPorId(int id) {
        return Optional.ofNullable(DB.get(id));
    }

    @Override
    public Optional<Frequencia> buscarPorAlunoEData(int alunoId, LocalDate dataAula) {
        if (dataAula == null) return Optional.empty();
        return DB.values().stream()
                .filter(f -> f.getAlunoId() == alunoId && dataAula.equals(f.getDataAula()))
                .findFirst();
    }

    @Override
    public List<Frequencia> buscarPorAlunoId(int alunoId) {
        return DB.values().stream()
                .filter(f -> f.getAlunoId() == alunoId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Frequencia> buscarPorTurmaEData(int turmaId, LocalDate dataAula) {
        if (dataAula == null) return Collections.emptyList();
        return DB.values().stream()
                .filter(f -> f.getTurmaId() == turmaId && dataAula.equals(f.getDataAula()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean deletarPorId(int id) {
        return DB.remove(id) != null;
    }
}
