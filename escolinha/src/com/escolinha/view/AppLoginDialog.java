package com.escolinha.view;

import java.awt.*;

import javax.swing.*;

import com.escolinha.domain.*;
import com.escolinha.repository.UsuarioRepository;
import com.escolinha.repository.UsuarioRepositoryMemoria;
import com.escolinha.domain.Usuario;
import com.escolinha.domain.Treinador;
import com.escolinha.domain.Responsavel;
import com.escolinha.domain.Administrador;
import com.escolinha.domain.TipoUsuario;

import com.escolinha.service.AuthService;

public class AppLoginDialog extends JDialog {
    private final JComboBox<TipoUsuario> cbTipo = new JComboBox<>(TipoUsuario.values());
    private final JTextField txtNome  = new JTextField("Administrador", 22);
    private final JTextField txtEmail = new JTextField("admin@escolinha.com", 22);
    private final JPasswordField txtSenha = new JPasswordField("", 22);

    private Usuario usuarioAutenticado;
    private final UsuarioRepository usuarioRepo = UsuarioRepositoryMemoria.getInstance();

    public AppLoginDialog(Frame owner) {
        super(owner, "Login", true);
        setLayout(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        // Tipo
        c.gridx=0; c.gridy=0; c.anchor = GridBagConstraints.WEST;
        form.add(new JLabel("Entrar como:"), c);
        c.gridx=1; form.add(cbTipo, c);

        // Nome
        c.gridx=0; c.gridy=1; form.add(new JLabel("Nome:"), c);
        c.gridx=1; form.add(txtNome, c);

        // Email
        c.gridx=0; c.gridy=2; form.add(new JLabel("E-mail/Login:"), c);
        c.gridx=1; form.add(txtEmail, c);

        // Senha (para todos os perfis)
        c.gridx=0; c.gridy=3; form.add(new JLabel("Senha:"), c);
        c.gridx=1; form.add(txtSenha, c);

        // Ações
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnForgot = new JButton("Esqueceu a senha?");
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnEntrar   = new JButton("Entrar");
        actions.add(btnForgot);
        actions.add(btnCancelar);
        actions.add(btnEntrar);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        // botão Esqueceu a senha
        btnForgot.addActionListener(e -> {
            new ResetSenhaDialog((Frame) getOwner(), txtEmail.getText().trim()).setVisible(true);
        });

        btnCancelar.addActionListener(e -> { usuarioAutenticado = null; dispose(); });
        btnEntrar.addActionListener(e -> autenticar());

        // garantir senha default para admin
        AuthService.getInstance().setPasswordIfAbsent("admin@escolinha.com", "admin123");

        cbTipo.setSelectedItem(TipoUsuario.ADMINISTRADOR);
        getRootPane().setDefaultButton(btnEntrar);
        pack();
        setLocationRelativeTo(owner);
    }

    private void autenticar() {
        try {
            String nome  = txtNome.getText().trim();
            String email = txtEmail.getText().trim();
            String senha = new String(txtSenha.getPassword());
            TipoUsuario tipoSelecionado = (TipoUsuario) cbTipo.getSelectedItem();

            if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Preencha nome, e-mail e senha.",
                        "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // valida senha pelo AuthService
            if (!AuthService.getInstance().validate(email, senha)) {
                JOptionPane.showMessageDialog(this, "E-mail ou senha inválidos.",
                        "Falha no login", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // tenta buscar usuário real (treinador/responsável cadastrados pelo admin)
            Usuario u = usuarioRepo.buscarPorLogin(email).orElse(null);
            if (u != null) {
                // usa o perfil real do repositório
                usuarioAutenticado = u;
            } else {
                // fallback: cria conforme seleção (útil p/ admin)
                switch (tipoSelecionado) {
                    case ADMINISTRADOR -> usuarioAutenticado = new Administrador(1, nome, email);
                    case TREINADOR     -> usuarioAutenticado = new Treinador(0, nome, email);
                    case RESPONSAVEL   -> usuarioAutenticado = new Responsavel(0, nome, email, "", email);
                }
            }
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro inesperado no login:\n" + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public Usuario showAndReturn() {
        setVisible(true);
        return usuarioAutenticado;
    }
}
