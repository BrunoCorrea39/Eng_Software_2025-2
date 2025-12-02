package com.escolinha.service;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.Avaliacao;
import com.escolinha.domain.Comunicado;
import com.escolinha.domain.Frequencia;
import com.escolinha.domain.Turma;
import com.escolinha.repository.AlunoRepository;
import com.escolinha.repository.AvaliacaoRepository;
import com.escolinha.repository.ComunicadoRepository;
import com.escolinha.repository.FrequenciaRepository;
import com.escolinha.repository.TurmaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TurmaService {

    private final TurmaRepository turmaRepository;
    private final AlunoRepository alunoRepository;
    private final FrequenciaRepository frequenciaRepository;
    private final AvaliacaoRepository avaliacaoRepository;
    private final ComunicadoRepository comunicadoRepository;

    public TurmaService(TurmaRepository turmaRepository,
                        AlunoRepository alunoRepository,
                        FrequenciaRepository frequenciaRepository,
                        AvaliacaoRepository avaliacaoRepository,
                        ComunicadoRepository comunicadoRepository) {
        this.turmaRepository = turmaRepository;
        this.alunoRepository = alunoRepository;
        this.frequenciaRepository = frequenciaRepository;
        this.avaliacaoRepository = avaliacaoRepository;
        this.comunicadoRepository = comunicadoRepository;
    }

    // --- HU-04: Chamada Digital ---
    public List<Aluno> listarAlunosDaTurma(int turmaId) {
        Optional<Turma> turmaOpt = turmaRepository.buscarPorId(turmaId);
        if (turmaOpt.isPresent()) {
            return turmaOpt.get().getAlunos();
        }
        return List.of();
    }

    public void registrarFrequencia(int turmaId, LocalDate data, Map<Integer, Boolean> presencaAlunos) {
        if (presencaAlunos == null || presencaAlunos.isEmpty()) {
            System.out.println("Nenhum dado de frequência para registrar.");
            return;
        }

        Optional<Turma> turmaOpt = turmaRepository.buscarPorId(turmaId);
        if (turmaOpt.isEmpty()) {
            throw new IllegalArgumentException("Turma com ID " + turmaId + " não encontrada.");
        }

        for (Map.Entry<Integer, Boolean> entry : presencaAlunos.entrySet()) {
            int alunoId = entry.getKey();
            boolean presente = entry.getValue();
            Frequencia freq = new Frequencia(0, alunoId, turmaId, data, presente);
            frequenciaRepository.salvar(freq);
        }
        System.out.println("[Service] Frequência registrada para turma " + turmaId + " em " + data);
    }

    // --- HU-05: Avaliações ---
    public Avaliacao registrarAvaliacao(int alunoId, int treinadorId, String comTecnico, String comTatico, String comComp) {
        Optional<Aluno> alunoOpt = alunoRepository.buscarPorId(alunoId);
        if (alunoOpt.isEmpty()) {
            throw new IllegalArgumentException("Aluno com ID " + alunoId + " não encontrado.");
        }
        Avaliacao novaAvaliacao = new Avaliacao(0, alunoId, treinadorId, LocalDate.now(), comTecnico, comTatico, comComp);
        return avaliacaoRepository.salvar(novaAvaliacao);
    }

    public List<Avaliacao> listarAvaliacoesAluno(int alunoId) {
        return avaliacaoRepository.buscarPorAlunoId(alunoId);
    }

    // --- HU-06: Comunicados ---
    public Comunicado publicarComunicadoTurma(int turmaId, int autorId, String titulo, String mensagem) {
        Optional<Turma> turmaOpt = turmaRepository.buscarPorId(turmaId);
        if (turmaOpt.isEmpty()) {
            throw new IllegalArgumentException("Turma com ID " + turmaId + " não encontrada.");
        }
        Comunicado novoComunicado = new Comunicado(0, titulo, mensagem, LocalDateTime.now(), autorId, turmaId);
        return comunicadoRepository.salvar(novoComunicado);
    }

    public List<Comunicado> listarComunicadosDaTurma(int turmaId) {
        return comunicadoRepository.buscarPorTurmaId(turmaId);
    }

    // --- Gerenciar Turmas ---
    public Turma criarTurma(String nome, int treinadorId) {
        Turma novaTurma = new Turma(0, nome, treinadorId);
        return turmaRepository.salvar(novaTurma);
    }

    public boolean adicionarAlunoNaTurma(int turmaId, int alunoId) {
        Optional<Turma> turmaOpt = turmaRepository.buscarPorId(turmaId);
        Optional<Aluno> alunoOpt = alunoRepository.buscarPorId(alunoId);

        if (turmaOpt.isPresent() && alunoOpt.isPresent()) {
            Turma turma = turmaOpt.get();
            Aluno aluno = alunoOpt.get();
            
            if (turma.getAlunos().size() >= 20) {
                System.out.println("Turma cheia!");
                return false;
            }
            
            turma.adicionarAluno(aluno);
            turmaRepository.salvar(turma);
            return true;
        }
        return false;
        
        
    }

    // === NOVOS MÉTODOS ===

 // TurmaService.java
    public List<Turma> listarTurmas() {
        return turmaRepository.listarTodas(); // delega pro repositório
    }


    /** Busca turma por id. */
    public Optional<Turma> buscarTurmaPorId(int turmaId) {
        return turmaRepository.buscarPorId(turmaId);
    }

    /** Remove um aluno da turma; funciona mesmo sem método removerAluno() em Turma. */
    public boolean removerAlunoDaTurma(int turmaId, int alunoId) {
        Optional<Turma> turmaOpt = turmaRepository.buscarPorId(turmaId);
        if (turmaOpt.isEmpty()) return false;

        Turma turma = turmaOpt.get();
        boolean removed = turma.getAlunos().removeIf(a -> a.getId() == alunoId);
        if (removed) {
            turmaRepository.salvar(turma);
        }
        return removed;
    }
}
