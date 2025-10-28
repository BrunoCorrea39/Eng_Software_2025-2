package com.escolinha.view;

import java.awt.*;
import javax.swing.*;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.Responsavel;
import com.escolinha.repository.AlunoRepository;
import com.escolinha.repository.ResponsavelRepository;

import java.util.ArrayList;
import java.util.List;

public class ResponsavelCadastroPanel extends JPanel {

    private final ResponsavelRepository responsavelRepository;
    private final AlunoRepository alunoRepository;

    private final JTextField txtNome     = new JTextField(22);
    private final JTextField txtLogin    = new JTextField(22);
    private final JTextField txtTelefone = new JTextField(22);
    private final JTextField txtEmail    = new JTextField(22);

    // seleção do filho
    private final JComboBox<Aluno> cbAluno = new JComboBox<>();

    public ResponsavelCadastroPanel(ResponsavelRepository responsavelRepository,
                                    AlunoRepository alunoRepository) {
        this.responsavelRepository = responsavelRepository;
        this.alunoRepository = alunoRepository;

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        // Nome
        c.gridx=0; c.gridy=0; c.anchor = GridBagConstraints.WEST; add(new JLabel("Nome:"), c);
        c.gridx=1; add(txtNome, c);

        // Login
        c.gridx=0; c.gridy=1; add(new JLabel("Login (usuário):"), c);
        c.gridx=1; add(txtLogin, c);

        // Telefone
        c.gridx=0; c.gridy=2; add(new JLabel("Telefone:"), c);
        c.gridx=1; add(txtTelefone, c);

        // E-mail
        c.gridx=0; c.gridy=3; add(new JLabel("E-mail:"), c);
        c.gridx=1; add(txtEmail, c);

        // Aluno (filho)
        c.gridx=0; c.gridy=4; add(new JLabel("Vincular ao Aluno:"), c);
        c.gridx=1; add(cbAluno, c);

        // Renderiza só o nome no combo
        cbAluno.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Aluno a) setText(a.getNome() + " (ID " + a.getId() + ")");
                return this;
            }
        });

        carregarAlunosNoCombo();

        // Botões
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        actions.add(btnCancelar);
        actions.add(btnSalvar);

        c.gridx=0; c.gridy=5; c.gridwidth=2; c.anchor = GridBagConstraints.EAST; c.fill = GridBagConstraints.NONE;
        add(actions, c);

        btnCancelar.addActionListener(e -> SwingUtilities.getWindowAncestor(this).dispose());
        btnSalvar.addActionListener(e -> salvar());
    }

    private void carregarAlunosNoCombo() {
        cbAluno.removeAllItems();
        try {
            List<Aluno> alunos = new ArrayList<>(alunoRepository.listarTodos());
            if (alunos.isEmpty()) {
                cbAluno.addItem(null);
                cbAluno.setEnabled(false);
                JOptionPane.showMessageDialog(this,
                        "Não há alunos cadastrados. Cadastre um aluno antes de vincular o responsável.",
                        "Atenção", JOptionPane.WARNING_MESSAGE);
            } else {
                for (Aluno a : alunos) cbAluno.addItem(a);
                cbAluno.setSelectedIndex(0);
            }
        } catch (Exception ex) {
            cbAluno.setEnabled(false);
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar alunos: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void salvar() {
        String nome  = txtNome.getText().trim();
        String login = txtLogin.getText().trim();
        String tel   = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();
        Aluno alunoSelecionado = (Aluno) cbAluno.getSelectedItem();

        if (nome.isEmpty() || login.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha Nome, Login e E-mail.",
                    "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para vincular.",
                    "Aluno obrigatório", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 1) cria e salva o responsável
            Responsavel novoTemp = new Responsavel(0, nome, login, tel, email);
            Responsavel novo = responsavelRepository.salvar(novoTemp); // ID gerado

            // 2) vincula ao aluno (adiciona na lista do aluno) e salva aluno
            var alunoOpt = alunoRepository.buscarPorId(alunoSelecionado.getId());
            if (alunoOpt.isPresent()) {
                Aluno a = alunoOpt.get();

                if (a.getResponsaveis() == null) {
                    a.setResponsaveis(new ArrayList<>());
                }

                final String loginNovo = novo.getLogin(); // variável final usada no lambda

                boolean jaExiste = a.getResponsaveis().stream()
                        .anyMatch(r -> r.getLogin().equalsIgnoreCase(loginNovo));

                if (!jaExiste) {
                    a.getResponsaveis().add(novo);
                    alunoRepository.salvar(a);
                }

                JOptionPane.showMessageDialog(this,
                        "Responsável cadastrado e vinculado a " + a.getNome() + " com sucesso!",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                txtNome.setText("");
                txtLogin.setText("");
                txtTelefone.setText("");
                txtEmail.setText("");
                if (cbAluno.isEnabled() && cbAluno.getItemCount() > 0) cbAluno.setSelectedIndex(0);

            } else {
                JOptionPane.showMessageDialog(this,
                        "Aluno selecionado não encontrado para vincular.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar responsável:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}

