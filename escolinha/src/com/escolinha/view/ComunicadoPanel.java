package com.escolinha.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

// Importações
import com.escolinha.domain.Turma;
import com.escolinha.repository.TurmaRepository; // Para listar turmas
import com.escolinha.service.TurmaService; // Para publicar e buscar turmas

public class ComunicadoPanel extends JPanel {

    // Serviços e Repositórios
    private final TurmaService turmaService;
    private final TurmaRepository turmaRepository;

    // Componentes
    private JTextField txtTituloComunicado;
    private JTextArea txtMensagemComunicado;
    private JComboBox<TurmaItem> comboTurmasCom; // Inclui opção "Geral"
    private JButton btnPublicarComunicado;
    private JButton btnCancelarComunicado;

    // Construtor
    public ComunicadoPanel(TurmaService tService, TurmaRepository tRepo) {
        this.turmaService = tService;
        this.turmaRepository = tRepo;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Publicar Novo Comunicado"));
         setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // --- Painel de Campos ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Título
        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        txtTituloComunicado = new JTextField(30); painelCampos.add(txtTituloComunicado, gbc);
        gbc.weightx = 0.0; gbc.fill = GridBagConstraints.NONE;

        // Destinatário (Turma ou Geral)
        gbc.gridx = 0; gbc.gridy = 1; painelCampos.add(new JLabel("Para:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        comboTurmasCom = new JComboBox<>();
        carregarTurmasComComboBox(); // Carrega turmas + opção Geral
        painelCampos.add(comboTurmasCom, gbc);
        gbc.fill = GridBagConstraints.NONE;

        // Mensagem
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.NORTHWEST; // Alinha label ao topo
        painelCampos.add(new JLabel("Mensagem:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.fill = GridBagConstraints.BOTH; // Expande vertical e horizontal
        gbc.weightx = 1.0; gbc.weighty = 1.0; // Faz a área de texto expandir
        txtMensagemComunicado = new JTextArea(5, 30);
        txtMensagemComunicado.setLineWrap(true); // Quebra de linha automática
        txtMensagemComunicado.setWrapStyleWord(true); // Quebra por palavra
        JScrollPane scrollMsg = new JScrollPane(txtMensagemComunicado);
        painelCampos.add(scrollMsg, gbc);

        add(painelCampos, BorderLayout.CENTER);

         // --- Painel de Botões ---
        JPanel painelBotoesCom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPublicarComunicado = new JButton("Publicar");
        btnCancelarComunicado = new JButton("Cancelar");
        painelBotoesCom.add(btnPublicarComunicado);
        painelBotoesCom.add(btnCancelarComunicado);
        add(painelBotoesCom, BorderLayout.SOUTH);

        // --- Action Listeners ---
        btnPublicarComunicado.addActionListener(e -> publicarComunicado());
        btnCancelarComunicado.addActionListener(e -> fecharJanela());
    }


    private void carregarTurmasComComboBox() {
        comboTurmasCom.removeAllItems();
        // Adiciona a opção "Geral" (representada por ID 0 ou null)
        comboTurmasCom.addItem(new TurmaItem(0, "== Geral (Todos os Pais) =="));
        try {
            List<Turma> turmas = turmaRepository.listarTodas();
            for (Turma turma : turmas) {
                comboTurmasCom.addItem(new TurmaItem(turma.getId(), turma.getNome()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar turmas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void publicarComunicado() {
        String titulo = txtTituloComunicado.getText().trim();
        String mensagem = txtMensagemComunicado.getText().trim();
        TurmaItem destinatario = (TurmaItem) comboTurmasCom.getSelectedItem();

        // Validação
        if (titulo.isEmpty() || mensagem.isEmpty() || destinatario == null) {
            JOptionPane.showMessageDialog(this, "Título, Mensagem e Destinatário são obrigatórios.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obter ID do autor (simplificado - pegar usuário logado no futuro)
        int autorId = 1; // Exemplo: Admin ID 1

        Integer turmaId = (destinatario.getId() == 0) ? null : destinatario.getId(); // null para Geral

        try {
            // Chamar o serviço apropriado (TurmaService ou um ComunicadoService dedicado)
            if (turmaId == null) {
                // Lógica para comunicado geral (precisaria de um ComunicadoService talvez)
                // comunicadoService.publicarComunicadoGeral(autorId, titulo, mensagem);
                 JOptionPane.showMessageDialog(this, "Comunicado Geral publicado (simulado)!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                turmaService.publicarComunicadoTurma(turmaId, autorId, titulo, mensagem);
                JOptionPane.showMessageDialog(this, "Comunicado para Turma '" + destinatario.getNome() + "' publicado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            }
            limparCamposComunicado();
            // fecharJanela();

        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Erro ao publicar comunicado: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
             e.printStackTrace();
        }
    }

    private void limparCamposComunicado() {
        txtTituloComunicado.setText("");
        txtMensagemComunicado.setText("");
        comboTurmasCom.setSelectedIndex(0); // Volta para Geral
    }

     private void fecharJanela() {
        Window janelaPai = SwingUtilities.getWindowAncestor(this);
        if (janelaPai != null) {
            janelaPai.dispose();
        }
    }

    // --- Classe interna TurmaItem ---
     private static class TurmaItem { /* ... Mesmo código de antes ... */
         private final int id;
         private final String nome;
         public TurmaItem(int id, String nome) { this.id = id; this.nome = nome; }
         public int getId() { return id; }
         public String getNome() { return nome; }
         @Override public String toString() { return nome; }
         @Override public boolean equals(Object o) { /*...*/ return id == ((TurmaItem)o).id;} // Simplificado
         @Override public int hashCode() { return Objects.hash(id); }
     }
}