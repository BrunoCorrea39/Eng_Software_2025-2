package com.escolinha.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Armazena senhas em memória por e-mail/login.
 * É só para DEMO; depois substitua por persistência/hasheamento real.
 */
public class AuthService {
    private static final AuthService INSTANCE = new AuthService();
    private final Map<String, String> senhas = new ConcurrentHashMap<>();

    private AuthService() {
        // seed do admin
        senhas.put("admin@escolinha.com", "admin123");
    }

    public static AuthService getInstance() { return INSTANCE; }

    /** define/atualiza senha para um login (e-mail) */
    public void setPassword(String email, String senha) {
        if (email == null || email.isBlank()) return;
        senhas.put(email.toLowerCase(), senha);
    }

    /** define senha só se não existir ainda */
    public void setPasswordIfAbsent(String email, String senhaDefault) {
        senhas.putIfAbsent(email.toLowerCase(), senhaDefault);
    }

    /** valida login + senha */
    public boolean validate(String email, String senha) {
        if (email == null) return false;
        String s = senhas.get(email.toLowerCase());
        return s != null && s.equals(senha);
    }

    /** verifica se existe uma senha cadastrada para esse e-mail */
    public boolean hasPassword(String email) {
        return email != null && senhas.containsKey(email.toLowerCase());
    }
}
