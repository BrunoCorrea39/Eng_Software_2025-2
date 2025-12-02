package com.escolinha.view;

import java.awt.*;
import javax.swing.*;
import com.escolinha.service.AuthService;

public class ResetSenhaDialog extends JDialog {
    private final JTextField txtEmail = new JTextField(24);
    private final JPasswordField txtNova = new JPasswordField(24);
    private final JPasswordField txtConfirma = new JPasswordField(24);

    public ResetSenhaDialog(Frame owner, String emailPrefill) {
        super(owner, "Redefinir senha", true);
        setLayout(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        txtEmail.setText(emailPrefill == null ? "" : emailPrefill);

        c.gridx=0; c.gridy=0; c.anchor = GridBagConstraints.WEST;
        form.add(new JLabel("E-mail/Login:"), c);
        c.gridx=1; form.add(txtEmail, c);

        c.gridx=0; c.gridy=1; form.add(new JLabel("Nova senha:"), c);
        c.gridx=1; form.add(txtNova, c);

        c.gridx=0; c.gridy=2; form.add(new JLabel("Confirmar senha:"), c);
        c.gridx=1; form.add(txtConfirma, c);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnSalvar   = new JButton("Salvar nova senha");
        actions.add(btnCancelar);
        actions.add(btnSalvar);

        add(form, BorderLayout.CENTER);
        add(actions, BorderLayout.SOUTH);

        btnCancelar.addActionListener(e -> dispose());
        btnSalvar.addActionListener(e -> salvar());

        getRootPane().setDefaultButton(btnSalvar);
        pack();
        setLocationRelativeTo(owner);
    }

    private void salvar() {
        String email = txtEmail.getText().trim();
        String nova  = new String(txtNova.getPassword());
        String conf  = new String(txtConfirma.getPassword());

        if (email.isEmpty() || nova.isEmpty() || conf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.",
                    "Campos obrigatórios", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!nova.equals(conf)) {
            JOptionPane.showMessageDialog(this, "As senhas não coincidem.",
                    "Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        AuthService.getInstance().setPassword(email, nova);
        JOptionPane.showMessageDialog(this, "Senha redefinida com sucesso!",
                "OK", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
