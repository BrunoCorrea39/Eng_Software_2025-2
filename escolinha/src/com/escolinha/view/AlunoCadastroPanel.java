package com.escolinha.view;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.DefaultComboBoxModel;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.Responsavel;
import com.escolinha.domain.Turma;
import com.escolinha.service.AlunoService;
import com.escolinha.service.TurmaService;

public class AlunoCadastroPanel extends JPanel {

    // Componentes
    private JTextField txtNome;
    private JTextField txtDataNasc;
    private JTextField txtObsMedicas;
    private JComboBox<Turma> cbTurma;
    private JButton btnSalvar;
    private JButton btnCancelar;

    // Serviços
    private final AlunoService alunoService;
    private final TurmaService turmaService;

    // Construtor agora recebe também o TurmaService
    public AlunoCadastroPanel(AlunoService alunoService, TurmaService turmaService) {
        this.alunoService = alunoService;
        this.turmaService = turmaService;

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Nome Completo:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; gbc.weightx = 1.0;
        txtNome = new JTextField(20);
        add(txtNome, gbc);
        gbc.weightx = 0.0;

        // Data Nasc
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Data Nasc. (AAAA-MM-DD):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        txtDataNasc = new JTextField(10);
        add(txtDataNasc, gbc);

        // Obs Médicas
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Obs. Médicas:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        txtObsMedicas = new JTextField(20);
        add(txtObsMedicas, gbc);

        // Turma (novo)
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Turma:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        cbTurma = new JComboBox<>();
        add(cbTurma, gbc);

        carregarTurmas(); // popula o combo

        // Botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalvar = new JButton("Salvar");
        btnCancelar = new JButton("Cancelar");
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; gbc.fill = GridBagConstraints.NONE;
        add(painelBotoes, gbc);

        // Listeners
        btnSalvar.addActionListener(e -> salvarAluno());
        btnCancelar.addActionListener(e -> fecharJanela());
    }

    private void carregarTurmas() {
        try {
            var turmas = turmaService.listarTurmas(); // se não existir, crie no service delegando ao repository
            if (turmas == null || turmas.isEmpty()) {
                cbTurma.setModel(new DefaultComboBoxModel<>());
                cbTurma.addItem(null);
                cbTurma.setSelectedIndex(0);
                cbTurma.setEnabled(false);
                JOptionPane.showMessageDialog(this,
                        "Não há turmas cadastradas. Crie uma turma antes de matricular o aluno.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
            } else {
                DefaultComboBoxModel<Turma> model = new DefaultComboBoxModel<>();
                for (Turma t : turmas) model.addElement(t);
                cbTurma.setModel(model);
                cbTurma.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            cbTurma.setEnabled(false);
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar turmas: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void salvarAluno() {
        String nome = txtNome.getText().trim();
        String dataNascStr = txtDataNasc.getText().trim();
        String obsMedicas = txtObsMedicas.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome do aluno é obrigatório.",
                    "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            txtNome.requestFocus();
            return;
        }

        LocalDate dataNasc;
        try {
            dataNasc = LocalDate.parse(dataNascStr);
            if (dataNasc.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "Data de nascimento não pode ser no futuro.",
                        "Erro de Validação", JOptionPane.WARNING_MESSAGE);
                txtDataNasc.requestFocus();
                return;
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato inválido para Data de Nascimento. Use AAAA-MM-DD.",
                    "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            txtDataNasc.requestFocus();
            return;
        }

        Turma turmaSelecionada = (Turma) cbTurma.getSelectedItem();
        if (turmaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma turma (ou crie turmas antes).",
                    "Turma obrigatória", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // cadastra o aluno
            Aluno alunoSalvo = alunoService.cadastrarAluno(
                    nome, dataNasc, new ArrayList<Responsavel>(), obsMedicas);

            // matricula na turma escolhida
            turmaService.adicionarAlunoNaTurma(turmaSelecionada.getId(), alunoSalvo.getId());

            JOptionPane.showMessageDialog(this,
                    "Aluno '" + alunoSalvo.getNome() + "' cadastrado e matriculado na turma "
                            + turmaSelecionada.getNome() + " (ID " + turmaSelecionada.getId() + ").",
                    "Cadastro Realizado", JOptionPane.INFORMATION_MESSAGE);

            limparCampos();

        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar: " + iae.getMessage(),
                    "Erro de Negócio", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro inesperado: " + ex.getMessage(),
                    "Erro Crítico", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtDataNasc.setText("");
        txtObsMedicas.setText("");
        if (cbTurma.isEnabled() && cbTurma.getItemCount() > 0) cbTurma.setSelectedIndex(0);
    }

    private void fecharJanela() {
        Window janelaPai = SwingUtilities.getWindowAncestor(this);
        if (janelaPai != null) janelaPai.dispose();
    }
}
