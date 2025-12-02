package com.escolinha.domain;

import java.util.Objects;

import java.util.List;
import java.util.ArrayList;

// Herda de Usuario
public class Responsavel extends Usuario {

    // Atributos específicos do Responsavel (id e nome são herdados)
    private String telefone;
    private String email; // Mantendo o email caso seja diferente do login
    private List<Aluno> alunos = new ArrayList<>();
    // Construtor chamando o super() primeiro
    public Responsavel(int id, String nome, String login /*ou email*/, String telefone, String email) {
        super(id, nome, login); // Passa id, nome, login para o construtor de Usuario
        this.telefone = telefone;
        this.email = email;
    }

    // Implementação obrigatória do método abstrato herdado
    @Override
    public TipoUsuario getTipoUsuario() {
        return TipoUsuario.RESPONSAVEL;
    }

    // --- Getters e Setters para os atributos específicos ---
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }


    // --- toString, equals, hashCode ---
    // toString pode incluir atributos da superclasse se desejar
    @Override
    public String toString() {
        // super.toString() pode ser útil se Usuario tiver um toString implementado
        return "Responsavel [id=" + getId() + ", nome=" + getNome() + ", login=" + getLogin() + ", telefone=" + telefone + ", email=" + email + "]";
    }

    // equals e hashCode podem usar a implementação da superclasse (baseada no ID)
    // Se Usuario já implementa equals/hashCode baseado no ID, não precisa redefinir aqui,
    // a menos que a lógica de igualdade para Responsavel seja diferente.
    // Exemplo usando o da superclasse (se ela já o tiver):
    @Override
    public int hashCode() {
        return super.hashCode(); // Delega para Usuario
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj); // Delega para Usuario
    }
    public List<Aluno> getAlunos() {
        return alunos;
    }

    public void setAlunos(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public void adicionarAluno(Aluno aluno) {
        this.alunos.add(aluno);
    }
    

    /* Ou, se Usuario não tiver equals/hashCode, gere novamente baseado no ID:
    @Override
    public int hashCode() {
        return Objects.hash(getId()); // Usa getId() herdado
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!super.equals(obj)) return false; // Chama o equals de Usuario (se houver) ou verifica classe
        if (getClass() != obj.getClass()) return false;
        // Se chegou aqui e super.equals() passou (ou não existe), compara atributos específicos se necessário
        // Mas geralmente comparar pelo ID herdado é suficiente.
        return true; // Se super.equals() já compara ID, está ok.
    }
    */
}