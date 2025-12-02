package com.escolinha.repository;

import com.escolinha.domain.Comunicado;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.Comparator;

public class ComunicadoRepositoryMemoria implements ComunicadoRepository {

    // -------- Singleton --------
    private static final ComunicadoRepositoryMemoria INSTANCE = new ComunicadoRepositoryMemoria();
    public static ComunicadoRepositoryMemoria getInstance() { return INSTANCE; }
    private ComunicadoRepositoryMemoria() {} // evita new externo

    // -------- Armazenamento compartilhado --------
    private static final Map<Integer, Comunicado> DB = new ConcurrentHashMap<>();
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    @Override
    public synchronized Comunicado salvar(Comunicado comunicado) {
        if (comunicado == null) {
            throw new IllegalArgumentException("Comunicado não pode ser nulo.");
        }

        if (comunicado.getId() == 0) {
            int novoId = SEQ.incrementAndGet();
            comunicado.setId(novoId);
            DB.put(novoId, comunicado);
            System.out.println("[RepoMemoria] Novo comunicado inserido: ID=" + novoId);
        } else {
            if (!DB.containsKey(comunicado.getId())) {
                throw new IllegalArgumentException("Tentativa de atualizar comunicado inexistente com ID: " + comunicado.getId());
            }
            DB.put(comunicado.getId(), comunicado);
            System.out.println("[RepoMemoria] Comunicado atualizado: ID=" + comunicado.getId());
        }
        return comunicado;
    }

    @Override
    public Optional<Comunicado> buscarPorId(int id) {
        return Optional.ofNullable(DB.get(id));
    }

    @Override
    public List<Comunicado> listarComunicadosGerais() {
        return DB.values().stream()
                .filter(c -> c.getTurmaId() == null)
                .sorted(Comparator.comparing(Comunicado::getDataPublicacao).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Comunicado> buscarPorTurmaId(int turmaId) {
        return DB.values().stream()
                .filter(c -> c.getTurmaId() != null && c.getTurmaId() == turmaId)
                .sorted(Comparator.comparing(Comunicado::getDataPublicacao).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Comunicado> buscarRecentes(LocalDateTime dataReferencia) {
        if (dataReferencia == null) return listarTodosOrdenados();
        return DB.values().stream()
                .filter(c -> c.getDataPublicacao().isAfter(dataReferencia))
                .sorted(Comparator.comparing(Comunicado::getDataPublicacao).reversed())
                .collect(Collectors.toList());
    }

    private List<Comunicado> listarTodosOrdenados() {
        return DB.values().stream()
                .sorted(Comparator.comparing(Comunicado::getDataPublicacao).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public boolean deletarPorId(int id) {
        Comunicado removido = DB.remove(id);
        if (removido != null) {
            System.out.println("[RepoMemoria] Comunicado deletado: ID=" + id);
            return true;
        } else {
            System.out.println("[RepoMemoria] Comunicado com ID=" + id + " não encontrado para deleção.");
            return false;
        }
    }
}
