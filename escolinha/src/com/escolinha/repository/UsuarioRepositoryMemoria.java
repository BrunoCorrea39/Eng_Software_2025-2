package com.escolinha.repository;

import com.escolinha.domain.TipoUsuario;
import com.escolinha.domain.Usuario;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class UsuarioRepositoryMemoria implements UsuarioRepository {

    // ---------- Singleton ----------
    private static final UsuarioRepositoryMemoria INSTANCE = new UsuarioRepositoryMemoria();
    public static UsuarioRepositoryMemoria getInstance() { return INSTANCE; }
    private UsuarioRepositoryMemoria() {} // impede new

    // ---------- Armazenamento compartilhado (permanência em memória) ----------
    private static final Map<Integer, Usuario> DB = new ConcurrentHashMap<>();
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    @Override
    public synchronized Usuario salvar(Usuario usuario) {
        if (usuario == null) throw new IllegalArgumentException("Usuário não pode ser nulo.");
        if (usuario.getId() == 0) {
            usuario.setId(SEQ.incrementAndGet());
        }
        DB.put(usuario.getId(), usuario);
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) {
        return Optional.ofNullable(DB.get(id));
    }

    @Override
    public Optional<Usuario> buscarPorLogin(String login) {
        if (login == null || login.trim().isEmpty()) return Optional.empty();
        return DB.values().stream()
                .filter(u -> u.getLogin() != null && u.getLogin().equalsIgnoreCase(login))
                .findFirst();
    }

    @Override
    public List<Usuario> listarTodos() {
        return new ArrayList<>(DB.values());
    }

    @Override
    public List<Usuario> buscarPorTipo(TipoUsuario tipo) {
        if (tipo == null) return Collections.emptyList();
        return DB.values().stream()
                .filter(u -> u.getTipoUsuario() == tipo)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deletarPorId(int id) {
        return DB.remove(id) != null;
    }
}
