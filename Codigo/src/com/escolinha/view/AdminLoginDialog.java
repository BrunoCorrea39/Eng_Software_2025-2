package com.escolinha.view;

import java.awt.*;
import javax.swing.*;

import com.escolinha.domain.Administrador;
import com.escolinha.domain.Usuario;

public class AdminLoginDialog extends JDialog {
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private Usuario adminAutenticado;

    // credenciais mínimas (troque por algo melhor depois)
    private static final String LOGIN_PADRAO = "admin@escolinha.com";
    private static final String SENHA_PADRAO = "123";

    public AdminLoginDialog(Frame owner) {
        super(owner, "Login do Administrador", true);
        setLayout(new BorderLayout(10,10));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        txtLogin = new JTextField(LOGIN_PADRAO, 22);
        txtSenha = new JPasswordField("", 22);

        // Login
        c.gridx=0; c.gridy=0; c.anchor = GridBagConstraints.WEST;
        form.add(new JLabel("Login (e-mail):"), c);
        c.gridx=1; form.add(txtLogin, c);

        // Senha
        c.gridx=0; c.gridy=1; form.add(new JLabel("Senha:"), c);
        c.gridx=1; form.add(txtSenha, c);

        // Botões
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnEntrar   = new JButton("Entrar");
        actions.add(btnCancelar);
        actions.add(btnEntrar);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> { adminAutenticado = null; dispose(); });

        btnEntrar.addActionListener(e -> autenticar());

        getRootPane().setDefaultButton(btnEntrar); // Enter ativa "Entrar"
        pack();
        setLocationRelativeTo(owner);
    }

    private void autenticar() {
        String login = txtLogin.getText().trim();
        String senha = new String(txtSenha.getPassword());

        if (login.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe login e senha.", "Campos obrigatórios",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (LOGIN_PADRAO.equalsIgnoreCase(login) && SENHA_PADRAO.equals(senha)) {
            // autenticação ok → cria o usuário admin
            adminAutenticado = new Administrador(1, "Administrador", login);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciais inválidas.", "Falha na autenticação",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Retorna o admin autenticado ou null se cancelado/erro */
    public Usuario showAndReturn() {
        setVisible(true);
        return adminAutenticado;
    }
}
