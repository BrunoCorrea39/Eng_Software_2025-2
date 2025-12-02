package com.escolinha.repository;

import com.escolinha.domain.Frequencia; // Importe a classe do domain
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

public interface FrequenciaRepository {

    /**
     * Salva um registro de frequência.
     * @param frequencia O registro a ser salvo.
     * @return O registro salvo.
     */
    Frequencia salvar(Frequencia frequencia);

    /**
     * Busca um registro de frequência pelo seu ID único (se aplicável).
     * @param id O ID do registro.
     * @return Optional contendo o registro se encontrado.
     */
    Optional<Frequencia> buscarPorId(int id); // ID pode não ser necessário, chave natural é Aluno+Turma+Data

     /**
     * Busca o registro de frequência de um aluno específico em uma data específica.
     * @param alunoId O ID do aluno.
     * @param dataAula A data da aula.
     * @return Optional contendo o registro se encontrado.
     */
    Optional<Frequencia> buscarPorAlunoEData(int alunoId, LocalDate dataAula);


    /**
     * Lista todos os registros de frequência de um aluno.
     * @param alunoId O ID do aluno.
     * @return Lista de registros de frequência do aluno.
     */
    List<Frequencia> buscarPorAlunoId(int alunoId);

    /**
     * Lista todos os registros de frequência de uma turma em uma data específica.
     * @param turmaId O ID da turma.
     * @param dataAula A data da aula.
     * @return Lista de registros de frequência da turma na data.
     */
    List<Frequencia> buscarPorTurmaEData(int turmaId, LocalDate dataAula);

    /**
     * Deleta um registro de frequência (pode ser por ID ou pela chave Aluno+Turma+Data).
     * @param id O ID do registro.
     * @return true se deletado com sucesso.
     */
    boolean deletarPorId(int id); // Ou boolean deletar(int alunoId, int turmaId, LocalDate dataAula);
}