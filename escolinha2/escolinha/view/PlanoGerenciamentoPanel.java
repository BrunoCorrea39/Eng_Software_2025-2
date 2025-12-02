package com.escolinha.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.PlanoPagamento;
import com.escolinha.service.AlunoService;
import com.escolinha.service.FinanceiroService;

public class PlanoGerenciamentoPanel extends JPanel {

    private final FinanceiroService financeiroService;
    private final AlunoService alunoService;

    // ---- Tabela de planos
    private JTable tabelaPlanos;
    private DefaultTableModel tableModelPlanos;
    private JButton btnAtualizarLista;

    // ---- Criação de plano
    private JTextField txtNomePlano;
    private JTextField txtValorPlano;
    private JTextField txtDuracaoMeses;
    private JButton btnCriarPlano;
    private JButton btnFecharGerPlanos;

    // ---- Atribuição de plano ↔ aluno
    private JComboBox<Aluno> cbAluno;
    private JComboBox<PlanoPagamento> cbPlanoExistente;
    private JButton btnAtribuirPlano;

    public PlanoGerenciamentoPanel(FinanceiroService fService, AlunoService aService) {
        this.financeiroService = fService;
        this.alunoService = aService;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------- Painel superior: criação de plano ----------
        JPanel painelCriacao = new JPanel(new GridBagLayout());
        painelCriacao.setBorder(BorderFactory.createTitledBorder("Criar Novo Plano"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Nome do plano
        gbc.gridx=0; gbc.gridy=0; painelCriacao.add(new JLabel("Nome do Plano:"), gbc);
        gbc.gridx=1; gbc.gridy=0; gbc.fill=GridBagConstraints.HORIZONTAL; gbc.weightx=1.0;
        txtNomePlano = new JTextField(15); painelCriacao.add(txtNomePlano, gbc);
        gbc.fill=GridBagConstraints.NONE; gbc.weightx=0.0;

        // Valor
        gbc.gridx=0; gbc.gridy=1; painelCriacao.add(new JLabel("Valor (R$):"), gbc);
        gbc.gridx=1; gbc.gridy=1; txtValorPlano = new JTextField(8); painelCriacao.add(txtValorPlano, gbc);

        // Duração
        gbc.gridx=0; gbc.gridy=2; painelCriacao.add(new JLabel("Duração (meses):"), gbc);
        gbc.gridx=1; gbc.gridy=2; txtDuracaoMeses = new JTextField(3); painelCriacao.add(txtDuracaoMeses, gbc);

        // Botão criar
        gbc.gridx=1; gbc.gridy=3; gbc.anchor=GridBagConstraints.EAST;
        btnCriarPlano = new JButton("Criar Plano"); painelCriacao.add(btnCriarPlano, gbc);

        add(painelCriacao, BorderLayout.NORTH);

        // ---------- Centro: lista de planos ----------
        String[] colunasPlanos = {"ID", "Nome", "Valor (R$)", "Duração (Meses)"};
        tableModelPlanos = new DefaultTableModel(colunasPlanos, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaPlanos = new JTable(tableModelPlanos);
        JScrollPane scrollPlanos = new JScrollPane(tabelaPlanos);
        scrollPlanos.setBorder(BorderFactory.createTitledBorder("Planos Existentes"));
        scrollPlanos.setPreferredSize(new Dimension(450, 150));

        // ---------- Painel de atribuição: aluno x plano ----------
        JPanel painelAtribuicao = new JPanel(new GridBagLayout());
        painelAtribuicao.setBorder(BorderFactory.createTitledBorder("Atribuir Plano a Aluno"));
        GridBagConstraints g2 = new GridBagConstraints();
        g2.insets = new Insets(4, 6, 4, 6);
        g2.anchor = GridBagConstraints.WEST;
        g2.fill = GridBagConstraints.HORIZONTAL;

        // Aluno
        g2.gridx=0; g2.gridy=0; painelAtribuicao.add(new JLabel("Aluno:"), g2);
        g2.gridx=1; g2.gridy=0; g2.weightx=1.0;
        cbAluno = new JComboBox<>(); painelAtribuicao.add(cbAluno, g2);
        g2.weightx=0.0;

        // Plano existente
        g2.gridx=0; g2.gridy=1; painelAtribuicao.add(new JLabel("Plano:"), g2);
        g2.gridx=1; g2.gridy=1; g2.weightx=1.0;
        cbPlanoExistente = new JComboBox<>(); painelAtribuicao.add(cbPlanoExistente, g2);
        g2.weightx=0.0;

        // Botão atribuir
        g2.gridx=1; g2.gridy=2; g2.anchor = GridBagConstraints.EAST;
        btnAtribuirPlano = new JButton("Atribuir Plano");
        painelAtribuicao.add(btnAtribuirPlano, g2);

        // Empacota centro (tabela + atribuição) em um painel vertical
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.add(scrollPlanos);
        centro.add(Box.createVerticalStrut(10));
        centro.add(painelAtribuicao);

        add(centro, BorderLayout.CENTER);

        // ---------- Rodapé ----------
        JPanel painelBotoesPlanos = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAtualizarLista = new JButton("Atualizar Lista");
        btnFecharGerPlanos = new JButton("Fechar");
        painelBotoesPlanos.add(btnAtualizarLista);
        painelBotoesPlanos.add(btnFecharGerPlanos);
        add(painelBotoesPlanos, BorderLayout.SOUTH);

        // Listeners
        btnCriarPlano.addActionListener(e -> criarNovoPlano());
        btnAtualizarLista.addActionListener(e -> {
            carregarPlanos();
            carregarAlunos();
        });
        btnFecharGerPlanos.addActionListener(e -> fecharJanela());
        btnAtribuirPlano.addActionListener(e -> atribuirPlano());
        
        // Cargas iniciais
        carregarPlanos();
        carregarAlunos();
    }
    

    // ----------------- criação/listagem de planos -----------------

    private void carregarPlanos() {
        tableModelPlanos.setRowCount(0);
        try {
            List<PlanoPagamento> planos = financeiroService.listarPlanos();
            if (planos.isEmpty()){
                tableModelPlanos.addRow(new Object[]{"", "Nenhum plano cadastrado.", "", ""});
            } else {
                for (PlanoPagamento p : planos) {
                    tableModelPlanos.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        p.getValor().toString(),
                        p.getDuracaoMeses()
                    });
                }
            }
            // povoar combo de planos
            cbPlanoExistente.removeAllItems();
            for (PlanoPagamento p : planos) cbPlanoExistente.addItem(p);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar planos: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            tableModelPlanos.addRow(new Object[]{"", "Erro ao carregar.", "", ""});
        }
    }

    private void criarNovoPlano() {
        String nome = txtNomePlano.getText().trim();
        String valorStr = txtValorPlano.getText().trim().replace(",", ".");
        String duracaoStr = txtDuracaoMeses.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome do plano é obrigatório.",
                    "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal valor;
        int duracao;
        try {
            valor = new BigDecimal(valorStr);
            if (valor.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Valor inválido. Use números (ex: 150.00).",
                    "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            duracao = Integer.parseInt(duracaoStr);
            if (duracao <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Duração inválida. Use um inteiro em meses.",
                    "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            PlanoPagamento novo = financeiroService.criarPlano(nome, valor, duracao);
            JOptionPane.showMessageDialog(this,
                    "Plano '" + novo.getNome() + "' criado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCamposCriacao();
            carregarPlanos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao criar plano: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void limparCamposCriacao() {
        txtNomePlano.setText("");
        txtValorPlano.setText("");
        txtDuracaoMeses.setText("");
    }

    // ----------------- atribuição de plano ao aluno -----------------

    private void carregarAlunos() {
        try {
            cbAluno.removeAllItems();
            List<Aluno> alunos = alunoService.listarAlunos();
            for (Aluno a : alunos) cbAluno.addItem(a);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar alunos: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void atribuirPlano() {
        Aluno aluno = (Aluno) cbAluno.getSelectedItem();
        PlanoPagamento plano = (PlanoPagamento) cbPlanoExistente.getSelectedItem();

        if (aluno == null || plano == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno e um plano.",
                    "Dados obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // método a implementar no FinanceiroService
            financeiroService.atribuirPlanoAoAluno(aluno.getId(), plano.getId());

            JOptionPane.showMessageDialog(this,
                    "Plano '" + plano.getNome() + "' atribuído ao aluno '" + aluno.getNome() + "'.",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atribuir plano: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // ----------------- util -----------------

    private void fecharJanela() {
        Window janelaPai = SwingUtilities.getWindowAncestor(this);
        if (janelaPai != null) janelaPai.dispose();
    }
}
