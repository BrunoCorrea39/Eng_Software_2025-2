package com.escolinha.repository;

import com.escolinha.domain.Fatura;
import com.escolinha.domain.StatusFatura;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface FaturaRepository {

    Fatura salvar(Fatura fatura);

    Optional<Fatura> buscarPorId(int id);

    List<Fatura> listarTodas();

    List<Fatura> buscarPorAlunoId(int alunoId);

    List<Fatura> buscarPorStatus(StatusFatura status);

    List<Fatura> buscarVencidas(LocalDate dataReferencia);

    // Adicione esta linha 👇
    boolean deletarPorId(int id);
}
