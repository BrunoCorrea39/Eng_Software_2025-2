package com.escolinha.view;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;

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

        setTitle("Sistema de Gestão - Escolinha de Futebol");
        setSize(920, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setJMenuBar(criarMenuSessao());

        JPanel painelPrincipal = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Bem-vindo, " + usuarioAtual.getNome() + " (" + usuarioAtual.getTipoUsuario() + ")");
        JPanel painelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelTitulo.add(lblTitulo);
        add(painelTitulo, BorderLayout.NORTH);

        // --- Botões do sistema ---
        JButton btnCadastrarAluno = new JButton("Cadastrar Aluno");
        btnCadastrarAluno.addActionListener(e -> abrirPainelCadastroAluno());
        painelPrincipal.add(btnCadastrarAluno);

        JButton btnListarAlunos = new JButton("Listar Alunos");
        btnListarAlunos.addActionListener(e -> listarAlunos());
        painelPrincipal.add(btnListarAlunos);

        JButton btnRegistrarPresenca = new JButton("Registrar Presença");
        btnRegistrarPresenca.addActionListener(e -> abrirPainelChamada());
        painelPrincipal.add(btnRegistrarPresenca);

        JButton btnVerStatusFinanceiro = new JButton("Status Financeiro");
        btnVerStatusFinanceiro.addActionListener(e -> abrirPainelStatusFinanceiro());
        painelPrincipal.add(btnVerStatusFinanceiro);

        JButton btnGerenciarPlanos = new JButton("Gerenciar Planos");
        btnGerenciarPlanos.addActionListener(e -> abrirPainelGerenciarPlanos());
        painelPrincipal.add(btnGerenciarPlanos);

        JButton btnRegistrarAvaliacao = new JButton("Registrar Avaliação");
        btnRegistrarAvaliacao.addActionListener(e -> abrirPainelRegistroAvaliacao());
        painelPrincipal.add(btnRegistrarAvaliacao);

        JButton btnPublicarComunicado = new JButton("Publicar Comunicado");
        btnPublicarComunicado.addActionListener(e -> abrirPainelPublicarComunicado());
        painelPrincipal.add(btnPublicarComunicado);

        JButton btnVerDesempenho = new JButton("Ver Desempenho Aluno");
        btnVerDesempenho.addActionListener(e -> abrirPainelDesempenhoAluno());
        painelPrincipal.add(btnVerDesempenho);

        JButton btnMuralAvisos = new JButton("Mural de Avisos");
        btnMuralAvisos.addActionListener(e -> abrirPainelMuralAvisos());
        painelPrincipal.add(btnMuralAvisos);

        JButton btnRealizarPagamento = new JButton("Realizar Pagamento");
        btnRealizarPagamento.addActionListener(e -> abrirPainelPagamento());
        painelPrincipal.add(btnRealizarPagamento);

        JButton btnCadastrarResponsavel = new JButton("Cadastrar Responsável");
        btnCadastrarResponsavel.addActionListener(e -> abrirCadastroResponsavel());
        painelPrincipal.add(btnCadastrarResponsavel);

        JButton btnCadastrarTreinador = new JButton("Cadastrar Treinador");
        btnCadastrarTreinador.addActionListener(e -> abrirCadastroTreinador());
        painelPrincipal.add(btnCadastrarTreinador);
        
        JButton btnVoltarLogin = new JButton("Voltar para Login");
        btnVoltarLogin.addActionListener(e -> {
            dispose(); // Fecha a janela atual
            abrirComLogin(); // Reabre a tela de login
        });
        painelPrincipal.add(btnVoltarLogin);

        // --- ACL por perfil ---
        TipoUsuario tipo = usuarioAtual.getTipoUsuario();
        boolean isAdmin = tipo == TipoUsuario.ADMINISTRADOR;
        boolean isTreinador = tipo == TipoUsuario.TREINADOR;
        boolean isResp = tipo == TipoUsuario.RESPONSAVEL;

        btnCadastrarAluno.setVisible(isAdmin);
        btnListarAlunos.setVisible(isAdmin || isTreinador);
        btnRegistrarPresenca.setVisible(isTreinador);
        btnVerStatusFinanceiro.setVisible(isAdmin);
        btnGerenciarPlanos.setVisible(isAdmin);
        btnRegistrarAvaliacao.setVisible(isTreinador);
        btnPublicarComunicado.setVisible(isTreinador);
        btnVerDesempenho.setVisible(isTreinador || isResp);
        btnMuralAvisos.setVisible(true);
        btnRealizarPagamento.setVisible(isResp);
        btnCadastrarResponsavel.setVisible(isAdmin);
        btnCadastrarTreinador.setVisible(isAdmin);
        
        
        	
        add(painelPrincipal, BorderLayout.CENTER);
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
        JTextArea displayArea = new JTextArea(15, 50);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        StringBuilder sb = new StringBuilder("--- Alunos Cadastrados ---\n\n");
        sb.append(String.format("%-5s %-30s %-15s\n", "ID", "Nome", "Nascimento"));
        sb.append("------------------------------------------------------\n");
        try {
            alunoService.listarAlunos().forEach(a ->
                    sb.append(String.format("%-5d %-30s %-15s\n",
                            a.getId(), a.getNome(), a.getDataNascimento().toString()))
            );
        } catch (Exception e) {
            sb.append("\nErro ao listar alunos: ").append(e.getMessage());
            e.printStackTrace();
        }
        displayArea.setText(sb.toString());
        displayArea.setCaretPosition(0);

        JDialog dialogLista = new JDialog(this, "Lista de Alunos", true);
        dialogLista.setContentPane(new JScrollPane(displayArea));
        dialogLista.setSize(500, 400);
        dialogLista.setLocationRelativeTo(this);
        dialogLista.setVisible(true);
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
