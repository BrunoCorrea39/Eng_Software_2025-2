package com.escolinha.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

// Importações
import com.escolinha.domain.Aluno;
import com.escolinha.domain.Avaliacao;
import com.escolinha.domain.Turma;
import com.escolinha.repository.TurmaRepository; // Para listar turmas
import com.escolinha.service.TurmaService; // Para buscar alunos e registrar avaliação

public class AvaliacaoRegistroPanel extends JPanel {

    // Serviços e Repositórios
    private final TurmaService turmaService;
    private final TurmaRepository turmaRepository; // Para o ComboBox de Turmas

    // Componentes
    private JComboBox<TurmaItem> comboTurmasAval;
    private JComboBox<AlunoItem> comboAlunosAval;
    private JTextArea txtComentarioTecnico;
    private JTextArea txtComentarioTatico;
    private JTextArea txtComentarioComportamental;
    private JButton btnSalvarAvaliacao;
    private JButton btnCancelarAvaliacao;
    private JLabel lblStatusAval;

    // Construtor
    public AvaliacaoRegistroPanel(TurmaService tService, TurmaRepository tRepo) {
        this.turmaService = tService;
        this.turmaRepository = tRepo;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Registrar Nova Avaliação"));
        setBorder(BorderFactory.createCompoundBorder(getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // --- Painel de Seleção ---
        JPanel painelSelecao = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Selecionar Turma
        gbc.gridx = 0; gbc.gridy = 0; painelSelecao.add(new JLabel("Turma:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
        comboTurmasAval = new JComboBox<>();
        comboTurmasAval.setPreferredSize(new Dimension(200, 25));
        painelSelecao.add(comboTurmasAval, gbc);

        // Selecionar Aluno
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        painelSelecao.add(new JLabel("Aluno:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        comboAlunosAval = new JComboBox<>();
        comboAlunosAval.setPreferredSize(new Dimension(200, 25));
        painelSelecao.add(comboAlunosAval, gbc);
        gbc.fill = GridBagConstraints.NONE;

        // Status Label
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        lblStatusAval = new JLabel(" ");
        painelSelecao.add(lblStatusAval, gbc);


        add(painelSelecao, BorderLayout.NORTH);

        // --- Painel de Comentários ---
        JPanel painelComentarios = new JPanel();
        painelComentarios.setLayout(new BoxLayout(painelComentarios, BoxLayout.Y_AXIS)); // Vertical

        painelComentarios.add(new JLabel("Comentário Técnico:"));
        txtComentarioTecnico = new JTextArea(3, 30); // 3 linhas, 30 colunas
        painelComentarios.add(new JScrollPane(txtComentarioTecnico));
        painelComentarios.add(Box.createVerticalStrut(10)); // Espaçamento

        painelComentarios.add(new JLabel("Comentário Tático:"));
        txtComentarioTatico = new JTextArea(3, 30);
        painelComentarios.add(new JScrollPane(txtComentarioTatico));
        painelComentarios.add(Box.createVerticalStrut(10));

        painelComentarios.add(new JLabel("Comentário Comportamental:"));
        txtComentarioComportamental = new JTextArea(3, 30);
        painelComentarios.add(new JScrollPane(txtComentarioComportamental));

        add(painelComentarios, BorderLayout.CENTER);

        // --- Painel de Botões ---
        JPanel painelBotoesAval = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalvarAvaliacao = new JButton("Salvar Avaliação");
        btnCancelarAvaliacao = new JButton("Cancelar");
        painelBotoesAval.add(btnSalvarAvaliacao);
        painelBotoesAval.add(btnCancelarAvaliacao);
        add(painelBotoesAval, BorderLayout.SOUTH);

        // --- Carregar Dados Iniciais ---
        carregarTurmasAvalComboBox();
        // Listener para carregar alunos quando turma mudar
        comboTurmasAval.addActionListener(e -> carregarAlunosAvalComboBox());
        // Carrega alunos da primeira turma (se houver)
         SwingUtilities.invokeLater(() -> {
            if (comboTurmasAval.getItemCount() > 0) {
                 comboTurmasAval.setSelectedIndex(0);
                 carregarAlunosAvalComboBox();
            } else {
                 lblStatusAval.setText("Nenhuma turma disponível.");
            }
        });


        // --- Action Listeners ---
        btnSalvarAvaliacao.addActionListener(e -> salvarAvaliacao());
        btnCancelarAvaliacao.addActionListener(e -> fecharJanela());

    }

    private void carregarTurmasAvalComboBox() {
        lblStatusAval.setText("Carregando turmas...");
        comboTurmasAval.removeAllItems();
        try {
            List<Turma> turmas = turmaRepository.listarTodas();
            if(turmas.isEmpty()){
                lblStatusAval.setText("Nenhuma turma cadastrada.");
            } else {
                for (Turma turma : turmas) {
                    comboTurmasAval.addItem(new TurmaItem(turma.getId(), turma.getNome()));
                }
                lblStatusAval.setText(" "); // Limpa status
            }
        } catch (Exception e) {
            lblStatusAval.setText("Erro ao carregar turmas.");
            JOptionPane.showMessageDialog(this, "Erro ao carregar turmas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void carregarAlunosAvalComboBox() {
        lblStatusAval.setText("Carregando alunos...");
        comboAlunosAval.removeAllItems();
        TurmaItem turmaSelecionada = (TurmaItem) comboTurmasAval.getSelectedItem();

        if (turmaSelecionada == null) {
            lblStatusAval.setText("Selecione uma turma.");
            return;
        }

        int turmaId = turmaSelecionada.getId();
        try {
            List<Aluno> alunos = turmaService.listarAlunosDaTurma(turmaId);
             if(alunos.isEmpty()){
                lblStatusAval.setText("Nenhum aluno nesta turma.");
            } else {
                for (Aluno aluno : alunos) {
                    comboAlunosAval.addItem(new AlunoItem(aluno.getId(), aluno.getNome()));
                }
                 lblStatusAval.setText(" "); // Limpa status
            }
        } catch (Exception e) {
             lblStatusAval.setText("Erro ao carregar alunos.");
             JOptionPane.showMessageDialog(this, "Erro ao carregar alunos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
             e.printStackTrace();
        }
    }


    private void salvarAvaliacao() {
        AlunoItem alunoSelecionado = (AlunoItem) comboAlunosAval.getSelectedItem();
        if (alunoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um aluno para avaliar.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Obter ID do treinador (simplificado - pegar de um usuário logado no futuro)
        int treinadorId = 1; // Exemplo fixo

        String comTecnico = txtComentarioTecnico.getText().trim();
        String comTatico = txtComentarioTatico.getText().trim();
        String comComp = txtComentarioComportamental.getText().trim();

        if (comTecnico.isEmpty() && comTatico.isEmpty() && comComp.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Preencha ao menos um campo de comentário.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Avaliacao avaliacaoSalva = turmaService.registrarAvaliacao(
                    alunoSelecionado.getId(),
                    treinadorId,
                    comTecnico,
                    comTatico,
                    comComp);
            JOptionPane.showMessageDialog(this, "Avaliação para " + alunoSelecionado.getNome() + " salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            limparCamposAvaliacao();
            // Poderia fechar a janela ou permitir registrar outra
             // fecharJanela();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar avaliação: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

     private void limparCamposAvaliacao() {
        txtComentarioTecnico.setText("");
        txtComentarioTatico.setText("");
        txtComentarioComportamental.setText("");
        // Resetar ComboBox de alunos se desejar
        // comboAlunosAval.setSelectedIndex(-1); // Ou recarregar
    }

     private void fecharJanela() {
        Window janelaPai = SwingUtilities.getWindowAncestor(this);
        if (janelaPai != null) {
            janelaPai.dispose();
        }
    }

    // --- Classes internas para ComboBox ---
    private static class TurmaItem { /* ... Mesmo código de TurmaChamadaPanel ... */
         private final int id;
         private final String nome;
         public TurmaItem(int id, String nome) { this.id = id; this.nome = nome; }
         public int getId() { return id; }
         public String getNome() { return nome; }
         @Override public String toString() { return nome; }
         @Override public boolean equals(Object o) { /*...*/ return id == ((TurmaItem)o).id;} // Simplificado
         @Override public int hashCode() { return Objects.hash(id); }
    }

    private static class AlunoItem {
        private final int id;
        private final String nome;
        public AlunoItem(int id, String nome) { this.id = id; this.nome = nome; }
        public int getId() { return id; }
        public String getNome() { return nome; }
        @Override public String toString() { return nome; }
         @Override public boolean equals(Object o) { /*...*/ return id == ((AlunoItem)o).id;} // Simplificado
         @Override public int hashCode() { return Objects.hash(id); }
    }
}