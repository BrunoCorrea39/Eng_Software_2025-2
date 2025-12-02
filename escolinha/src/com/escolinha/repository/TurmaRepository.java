package com.escolinha.repository;

import com.escolinha.domain.Turma;
import java.util.List;
import java.util.Optional;

public interface TurmaRepository {

    Turma salvar(Turma turma);

    Optional<Turma> buscarPorId(int id);

    List<Turma> listarTodas();

    boolean deletarPorId(int id);

    // Métodos específicos podem ser adicionados
    // Ex: List<Turma> buscarPorTreinadorId(int treinadorId);
}