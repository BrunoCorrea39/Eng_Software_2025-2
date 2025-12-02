package com.escolinha.view;

import java.awt.*;
import javax.swing.*;

import com.escolinha.domain.Treinador;
import com.escolinha.repository.UsuarioRepository;

public class TreinadorCadastroPanel extends JPanel {
    private final UsuarioRepository usuarioRepository;
    private final JTextField txtNome = new JTextField(22);
    private final JTextField txtEmail = new JTextField(22);

    public TreinadorCadastroPanel(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx=0; c.gridy=0; add(new JLabel("Nome:"), c);
        c.gridx=1; add(txtNome, c);
        c.gridx=0; c.gridy=1; add(new JLabel("E-mail/Login:"), c);
        c.gridx=1; add(txtEmail, c);

        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> salvar());
        c.gridx=1; c.gridy=2; c.anchor = GridBagConstraints.EAST;
        add(btnSalvar, c);
    }

    private void salvar() {
        String nome = txtNome.getText().trim();
        String email = txtEmail.getText().trim();
        if (nome.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha nome e e-mail.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Treinador t = new Treinador(0, nome, email);
        usuarioRepository.salvar(t);
        JOptionPane.showMessageDialog(this, "Treinador cadastrado!");
        txtNome.setText(""); txtEmail.setText("");
    }
}
