package com.escolinha.view;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;
import java.util.List;
import java.util.stream.Collectors; // importante para o join dos nomes dos alunos




import com.escolinha.domain.*;
import com.escolinha.repository.*;
import com.escolinha.service.*;

public class MainFrame extends JFrame {

    // --- Repositórios ---
	private final AlunoRepository alunoRepository = AlunoRepositoryMemoria.getInstance();
	private final FaturaRepository faturaRepository = FaturaRepositoryMemoria.getInstance();
	private final PlanoPagamentoRepository planoPagamentoRepository = PlanoPagamentoRepositoryMemoria.getInstance();
	private final TurmaRepository turmaRepository = TurmaRepositoryMemoria.getInstance();
	private final FrequenciaRepository frequenciaRepository = FrequenciaRepositoryMemoria.getInstance();
	private final AvaliacaoRepository avaliacaoRepository = AvaliacaoRepositoryMemoria.getInstance();
	private final ComunicadoRepository comunicadoRepository = ComunicadoRepositoryMemoria.getInstance();
	private final ResponsavelRepository responsavelRepository = ResponsavelRepositoryMemoria.getInstance();
	private final UsuarioRepository usuarioRepository = UsuarioRepositoryMemoria.getInstance();


    // --- Serviços ---
    private final AlunoService alunoService = new AlunoService(alunoRepository);
    private final FinanceiroService financeiroService =
            new FinanceiroService(faturaRepository, planoPagamentoRepository, alunoRepository);
    private final TurmaService turmaService =
            new TurmaService(turmaRepository, alunoRepository, frequenciaRepository, avaliacaoRepository, comunicadoRepository);

    // --- Usuário logado ---
    private final Usuario usuarioAtual;

    public MainFrame(Usuario usuarioAtual) {
        this.usuarioAtual = usuarioAtual;
        adicionarDadosIniciaisParaTeste();

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(root, BorderLayout.CENTER);
        add(root, BorderLayout.CENTER);

	     // Ajusta tamanho e aparência
	     pack(); // calcula automaticamente o melhor tamanho
	     setMinimumSize(new Dimension(900, 500)); // evita janelas pequenas
	     setLocationRelativeTo(null); // centraliza na tela
	     setExtendedState(JFrame.NORMAL); // abre em estado normal (não minimizado

        // título já existente
        JLabel lblTitulo = new JLabel("Bem-vindo, " + usuarioAtual.getNome() + " (" + usuarioAtual.getTipoUsuario() + ")");
        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTitulo.add(lblTitulo);
        add(painelTitulo, BorderLayout.NORTH);

        // --- Seções empilhadas no centro ---
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        root.add(centro, BorderLayout.CENTER);

        // Seções (bastante neutras: borda com título + FlowLayout à esquerda)
        JPanel secCadastros = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        secCadastros.setBorder(BorderFactory.createTitledBorder("Cadastros"));

        JPanel secListagens = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        secListagens.setBorder(BorderFactory.createTitledBorder("Listagens"));

        JPanel secOperacoes = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        secOperacoes.setBorder(BorderFactory.createTitledBorder("Operações"));

        // --- Botões (reutilizando seus listeners atuais) ---
        // Cadastros (ADMIN)
        JButton btnCadastrarAluno = new JButton("Cadastrar Aluno");
        btnCadastrarAluno.addActionListener(e -> abrirPainelCadastroAluno());

        JButton btnCadastrarResponsavel = new JButton("Cadastrar Responsável");
        btnCadastrarResponsavel.addActionListener(e -> abrirCadastroResponsavel());

        JButton btnCadastrarTreinador = new JButton("Cadastrar Treinador");
        btnCadastrarTreinador.addActionListener(e -> abrirCadastroTreinador());

        JButton btnGerenciarPlanos = new JButton("Gerenciar Planos");
        btnGerenciarPlanos.addActionListener(e -> abrirPainelGerenciarPlanos());

        // Listagens (ADMIN)
        JButton btnListarAlunos = new JButton("Listar Alunos");
        btnListarAlunos.addActionListener(e -> listarAlunos());

        JButton btnListarTreinadores = new JButton("Listar Treinadores");
        btnListarTreinadores.addActionListener(e -> listarTreinadores());

        JButton btnListarResponsaveis = new JButton("Listar Responsáveis");
        btnListarResponsaveis.addActionListener(e -> listarResponsaveis());

        JButton btnVerStatusFinanceiro = new JButton("Status Financeiro");
        btnVerStatusFinanceiro.addActionListener(e -> abrirPainelStatusFinanceiro());

        // Operações (variável por perfil)
        JButton btnRegistrarPresenca = new JButton("Registrar Presença");
        btnRegistrarPresenca.addActionListener(e -> abrirPainelChamada());

        JButton btnRegistrarAvaliacao = new JButton("Registrar Avaliação");
        btnRegistrarAvaliacao.addActionListener(e -> abrirPainelRegistroAvaliacao());

        JButton btnPublicarComunicado = new JButton("Publicar Comunicado");
        btnPublicarComunicado.addActionListener(e -> abrirPainelPublicarComunicado());

        JButton btnVerDesempenho = new JButton("Ver Desempenho Aluno");
        btnVerDesempenho.addActionListener(e -> abrirPainelDesempenhoAluno());

        JButton btnMuralAvisos = new JButton("Mural de Avisos");
        btnMuralAvisos.addActionListener(e -> abrirPainelMuralAvisos());

        JButton btnRealizarPagamento = new JButton("Realizar Pagamento");
        btnRealizarPagamento.addActionListener(e -> abrirPainelPagamento());

        // --- Regras de visibilidade por perfil ---
        TipoUsuario tipo = usuarioAtual.getTipoUsuario();
        boolean isAdmin = tipo == TipoUsuario.ADMINISTRADOR;
        boolean isTreinador = tipo == TipoUsuario.TREINADOR;
        boolean isResp = tipo == TipoUsuario.RESPONSAVEL;

        // Monta seções de ADMIN
        if (isAdmin) {
            // Cadastros
            secCadastros.add(btnCadastrarAluno);
            secCadastros.add(btnCadastrarResponsavel);
            secCadastros.add(btnCadastrarTreinador);
            
            centro.add(secCadastros);

            // Listagens
            secListagens.add(btnListarAlunos);
            secListagens.add(btnListarTreinadores);
            secListagens.add(btnListarResponsaveis);
    
            centro.add(secListagens);
        }

        // Operações (para todos, mas com botões específicos)
        if (isTreinador) {
            secOperacoes.add(btnListarAlunos);          // útil ao treinador também
            secOperacoes.add(btnRegistrarPresenca);
            secOperacoes.add(btnRegistrarAvaliacao);
            secOperacoes.add(btnPublicarComunicado);
            secOperacoes.add(btnVerDesempenho);
            secOperacoes.add(btnMuralAvisos);
        }
        if (isResp) {
            secOperacoes.add(btnVerDesempenho);
            secOperacoes.add(btnMuralAvisos);
            secOperacoes.add(btnRealizarPagamento);
        }
        // Admin também pode ver operações gerais, se quiser
        if (isAdmin) {
        	secOperacoes.add(btnVerStatusFinanceiro);
        	secOperacoes.add(btnGerenciarPlanos);
            secOperacoes.add(btnVerDesempenho);
            secOperacoes.add(btnMuralAvisos);
        }

        centro.add(secOperacoes);

        // --- Rodapé: Voltar para Login alinhado à direita ---
        JPanel rodape = new JPanel(new BorderLayout());
        JButton btnVoltarLogin = new JButton("Voltar para Login");
        btnVoltarLogin.addActionListener(e -> { dispose(); abrirComLogin(); });
        rodape.add(btnVoltarLogin, BorderLayout.EAST);
        root.add(rodape, BorderLayout.SOUTH);

    }

    // --- Menu Sessão ---
    private JMenuBar criarMenuSessao() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Sessão");

        JMenuItem mTrocar = new JMenuItem("Trocar usuário…");
        mTrocar.addActionListener(e -> trocarUsuario());

        JMenuItem mSair = new JMenuItem("Sair");
        mSair.addActionListener(e -> System.exit(0));

        menu.add(mTrocar);
        menu.addSeparator();
        menu.add(mSair);
        bar.add(menu);
        return bar;
    }

    private void trocarUsuario() {
        dispose();
        abrirComLogin();
    }

    // ---------- Cadastro de Responsável ----------
    private void abrirCadastroResponsavel() {
        ResponsavelCadastroPanel panel = new ResponsavelCadastroPanel(responsavelRepository, alunoRepository);
        JDialog dialog = new JDialog(this, "Cadastrar Responsável", true);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    



    // ---------- Cadastro de Treinador ----------
    private void abrirCadastroTreinador() {
        JTextField txtNome = new JTextField(22);
        JTextField txtLogin = new JTextField(22);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        form.add(new JLabel("Nome:"));
        form.add(txtNome);
        form.add(new JLabel("Login/E-mail:"));
        form.add(txtLogin);

        int opt = JOptionPane.showConfirmDialog(
                this, form, "Cadastrar Treinador", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opt == JOptionPane.OK_OPTION) {
            String nome = txtNome.getText().trim();
            String login = txtLogin.getText().trim();

            if (nome.isEmpty() || login.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha Nome e Login.", "Campos obrigatórios",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Treinador novo = new Treinador(0, nome, login);
                usuarioRepository.salvar(novo);
                JOptionPane.showMessageDialog(this, "Treinador cadastrado com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar treinador:\n" + ex.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // ---------- MÉTODOS QUE ABREM OS PAINÉIS (TODOS IMPLEMENTADOS) ----------
 // MainFrame.java
    private void abrirPainelCadastroAluno() {
        try {
            AlunoCadastroPanel cadastroPanel = new AlunoCadastroPanel(alunoService, turmaService);
            JDialog dialogCadastro = new JDialog(this, "Cadastrar Novo Aluno", true);
            dialogCadastro.setContentPane(cadastroPanel);
            dialogCadastro.pack();
            dialogCadastro.setMinimumSize(dialogCadastro.getSize());
            dialogCadastro.setLocationRelativeTo(this);
            dialogCadastro.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir painel de cadastro:\n" + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }



    private void listarAlunos() {
        // Modelo da tabela (colunas fixas; linhas dinâmicas)
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nome", "Nascimento", "Turmas", "Responsáveis", "Obs. Médicas"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            // Carrega todas as turmas uma vez para otimizar
            java.util.List<Turma> todasTurmas = turmaRepository.listarTodas();

            for (Aluno a : alunoService.listarAlunos()) {
                // Turmas do aluno (pelo contains do próprio objeto aluno na turma)
                String turmasDoAluno = todasTurmas.stream()
                        .filter(t -> t.getAlunos() != null &&
                                     t.getAlunos().stream().anyMatch(x -> x.getId() == a.getId()))
                        .map(Turma::getNome)
                        .sorted()
                        .collect(java.util.stream.Collectors.joining(", "));

                if (turmasDoAluno.isBlank()) turmasDoAluno = "-";

                // Responsáveis do aluno
                String responsaveis = (a.getResponsaveis() == null || a.getResponsaveis().isEmpty())
                        ? "-"
                        : a.getResponsaveis().stream()
                            .map(Responsavel::getNome)
                            .collect(java.util.stream.Collectors.joining(", "));

                // Observações médicas (ajuste o getter se seu nome for diferente)
                String obs = null;
                try {
                    // Tenta getObservacoesMedicas()
                    obs = (String) a.getClass().getMethod("getObservacoesMedicas").invoke(a);
                } catch (Exception ignore) {
                    try {
                        // fallback: getObsMedicas()
                        obs = (String) a.getClass().getMethod("getObsMedicas").invoke(a);
                    } catch (Exception ignore2) { /* deixa null */ }
                }
                if (obs == null || obs.isBlank()) obs = "-";

                model.addRow(new Object[]{
                    a.getId(),
                    a.getNome(),
                    a.getDataNascimento(),   // LocalDate será mostrado como yyyy-MM-dd
                    turmasDoAluno,
                    responsaveis,
                    obs
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                this, "Erro ao listar alunos: " + e.getMessage(),
                "Erro", JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
        }

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // Larguras agradáveis
        table.getColumnModel().getColumn(0).setPreferredWidth(40);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(180);  // Nome
        table.getColumnModel().getColumn(2).setPreferredWidth(100);  // Nascimento
        table.getColumnModel().getColumn(3).setPreferredWidth(180);  // Turmas
        table.getColumnModel().getColumn(4).setPreferredWidth(180);  // Responsáveis
        table.getColumnModel().getColumn(5).setPreferredWidth(200);  // Obs. Médicas

        JDialog dialog = new JDialog(this, "Lista de Alunos", true);
        dialog.setContentPane(new JScrollPane(table));
        dialog.setSize(900, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    

    private void listarTreinadores() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nome", "E-mail/Login"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            List<Usuario> usuarios = usuarioRepository.listarTodos(); // busca todos os usuários

            for (Usuario u : usuarios) {
                if (u instanceof Treinador) {
                    model.addRow(new Object[]{
                        u.getId(),
                        u.getNome(),
                        u.getLogin() // ou u.getEmail(), conforme seu modelo
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar treinadores: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(250);

        JDialog dialog = new JDialog(this, "Lista de Treinadores", true);
        dialog.setContentPane(new JScrollPane(table));
        dialog.setSize(550, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    private void listarResponsaveis() {
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"ID", "Nome", "E-mail/Login", "Alunos Vinculados"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        try {
            List<Responsavel> responsaveis = responsavelRepository.listarTodos();

            if (responsaveis.isEmpty()) {
                model.addRow(new Object[]{"-", "Nenhum responsável cadastrado", "-", "-"});
            } else {
                for (Responsavel r : responsaveis) {
                    String alunosVinculados = "-";

                    if (r.getAlunos() != null && !r.getAlunos().isEmpty()) {
                        alunosVinculados = r.getAlunos().stream()
                                .map(Aluno::getNome)
                                .collect(Collectors.joining(", "));
                    }

                    model.addRow(new Object[]{
                        r.getId(),
                        r.getNome(),
                        r.getLogin(), // ou r.getEmail(), dependendo do seu modelo
                        alunosVinculados
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar responsáveis: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        JTable table = new JTable(model);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(250);

        JDialog dialog = new JDialog(this, "Lista de Responsáveis", true);
        dialog.setContentPane(new JScrollPane(table));
        dialog.setSize(750, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }



    private void abrirPainelChamada() {
        try {
            TurmaChamadaPanel chamadaPanel = new TurmaChamadaPanel(turmaService, turmaRepository, alunoRepository);
            JDialog dialogChamada = new JDialog(this, "Registrar Presença", true);
            dialogChamada.setContentPane(chamadaPanel);
            dialogChamada.pack();
            dialogChamada.setMinimumSize(dialogChamada.getSize());
            dialogChamada.setLocationRelativeTo(this);
            dialogChamada.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir painel de chamada:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void abrirPainelStatusFinanceiro() {
        try {
            FinanceiroStatusPanel statusPanel = new FinanceiroStatusPanel(financeiroService);
            JDialog dialogStatus = new JDialog(this, "Status Financeiro dos Alunos", true);
            dialogStatus.setContentPane(statusPanel);
            dialogStatus.pack();
            dialogStatus.setMinimumSize(dialogStatus.getSize());
            dialogStatus.setLocationRelativeTo(this);
            dialogStatus.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir painel de status financeiro:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void abrirPainelGerenciarPlanos() {
        try {
            // agora o painel recebe FinanceiroService e AlunoService
            PlanoGerenciamentoPanel planoPanel = new PlanoGerenciamentoPanel(financeiroService, alunoService);
            JDialog dialogPlanos = new JDialog(this, "Gerenciar Planos de Pagamento", true);
            dialogPlanos.setContentPane(planoPanel);
            dialogPlanos.pack();
            dialogPlanos.setMinimumSize(dialogPlanos.getSize());
            dialogPlanos.setLocationRelativeTo(this);
            dialogPlanos.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir painel de gerenciamento de planos:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void abrirPainelRegistroAvaliacao() {
        try {
            AvaliacaoRegistroPanel avaliacaoPanel = new AvaliacaoRegistroPanel(turmaService, turmaRepository);
            JDialog dialogAvaliacao = new JDialog(this, "Registrar Nova Avaliação", true);
            dialogAvaliacao.setContentPane(avaliacaoPanel);
            dialogAvaliacao.pack();
            dialogAvaliacao.setMinimumSize(dialogAvaliacao.getSize());
            dialogAvaliacao.setLocationRelativeTo(this);
            dialogAvaliacao.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir painel de registro de avaliação:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void abrirPainelPublicarComunicado() {
        try {
            ComunicadoPanel comunicadoPanel = new ComunicadoPanel(turmaService, turmaRepository);
            JDialog dialogComunicado = new JDialog(this, "Publicar Novo Comunicado", true);
            dialogComunicado.setContentPane(comunicadoPanel);
            dialogComunicado.pack();
            dialogComunicado.setMinimumSize(dialogComunicado.getSize());
            dialogComunicado.setLocationRelativeTo(this);
            dialogComunicado.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir painel de publicação de comunicado:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void abrirPainelDesempenhoAluno() {
        try {
            AlunoDesempenhoPanel desempenhoPanel =
                    new AlunoDesempenhoPanel(alunoService, turmaService, frequenciaRepository);
            JDialog dialogDesempenho = new JDialog(this, "Consultar Desempenho do Aluno", true);
            dialogDesempenho.setContentPane(desempenhoPanel);
            dialogDesempenho.pack();
            dialogDesempenho.setMinimumSize(new Dimension(600, 450));
            dialogDesempenho.setLocationRelativeTo(this);
            dialogDesempenho.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir painel de desempenho:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void abrirPainelMuralAvisos() {
        try {
            MuralAvisosPanel muralPanel = new MuralAvisosPanel(comunicadoRepository);
            JDialog dialogMural = new JDialog(this, "Mural de Avisos e Calendário", true);
            dialogMural.setContentPane(muralPanel);
            dialogMural.pack();
            dialogMural.setMinimumSize(new Dimension(700, 400));
            dialogMural.setLocationRelativeTo(this);
            dialogMural.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir mural de avisos:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void abrirPainelPagamento() {
        try {
            PagamentoPanel pagamentoPanel = new PagamentoPanel(financeiroService, alunoService);
            JDialog dialogPagamento = new JDialog(this, "Realizar Pagamento de Mensalidade", true);
            dialogPagamento.setContentPane(pagamentoPanel);
            dialogPagamento.pack();
            dialogPagamento.setMinimumSize(dialogPagamento.getSize());
            dialogPagamento.setLocationRelativeTo(this);
            dialogPagamento.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao abrir painel de pagamento:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ---------- Dados iniciais ----------
    private void adicionarDadosIniciaisParaTeste() {
        try {
            Optional<Usuario> t = usuarioRepository.buscarPorLogin("carlos@escolinha.com");
            Treinador treinador;
            if (t.isPresent()) {
                treinador = (Treinador) t.get();
            } else {
                treinador = (Treinador) usuarioRepository.salvar(
                        new Treinador(0, "Prof. Carlos", "carlos@escolinha.com"));
            }
            if (turmaRepository.listarTodas().isEmpty()) {
                turmaService.criarTurma("Sub-10 Manhã", treinador.getId());
                turmaService.criarTurma("Sub-12 Tarde", treinador.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- Fluxo de Login ----------
    public static void abrirComLogin() {
        AppLoginDialog login = new AppLoginDialog(null);
        Usuario u = login.showAndReturn();
        if (u != null) {
            new MainFrame(u).setVisible(true);
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception ignored) {}
            abrirComLogin();
        });
    }
}
