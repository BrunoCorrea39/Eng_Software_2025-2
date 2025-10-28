package com.escolinha.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;

// Importações necessárias
import com.escolinha.domain.Aluno;
import com.escolinha.domain.Turma;
import com.escolinha.repository.AlunoRepository; // Para buscar alunos (poderia ser via TurmaService também)
import com.escolinha.repository.TurmaRepository; // Para listar turmas no ComboBox
import com.escolinha.service.TurmaService;

public class TurmaChamadaPanel extends JPanel {

    // Componentes da Interface
    private JComboBox<TurmaItem> comboTurmas; // ComboBox para selecionar a turma
    private JPanel painelAlunosChamada; // Painel para exibir a lista de alunos com checkboxes
    private JButton btnSalvarChamada;
    private JButton btnCancelarChamada;
    private JLabel lblStatusCarregamento; // Para feedback ao usuário

    // Serviços e Repositórios (Dependências)
    private final TurmaService turmaService;
    private final TurmaRepository turmaRepository; // Necessário para popular o ComboBox de turmas
    private final AlunoRepository alunoRepository; // Necessário para buscar alunos (ou usar TurmaService)

    // Mapa para guardar a relação Aluno -> JCheckBox
    private Map<Aluno, JCheckBox> checkboxesAlunos;

    // Construtor Corrigido
    public TurmaChamadaPanel(TurmaService tService, TurmaRepository tRepo, AlunoRepository aRepo) {
        this.turmaService = tService;
        this.turmaRepository = tRepo; // Atribuição correta
        this.alunoRepository = aRepo; // Atribuição correta
        this.checkboxesAlunos = new HashMap<>();

        // Configuração do Layout Principal
        setLayout(new BorderLayout(10, 10)); // Espaçamento entre componentes
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margem externa

        // --- Painel Superior: Seleção da Turma ---
        JPanel painelSelecao = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelSelecao.setBorder(BorderFactory.createTitledBorder("Seleção"));
        painelSelecao.add(new JLabel("Selecionar Turma:"));

        comboTurmas = new JComboBox<>();
        comboTurmas.setPreferredSize(new Dimension(200, 25)); // Tamanho preferencial
        carregarTurmasNoComboBox(); // Carrega as turmas no ComboBox
        painelSelecao.add(comboTurmas);

        // Listener para carregar alunos quando a turma mudar
        comboTurmas.addActionListener(e -> carregarAlunosDaTurmaSelecionada());

        add(painelSelecao, BorderLayout.NORTH);

        // --- Painel Central: Lista de Alunos para Chamada ---
        painelAlunosChamada = new JPanel();
        painelAlunosChamada.setLayout(new BoxLayout(painelAlunosChamada, BoxLayout.Y_AXIS)); // Layout vertical
        TitledBorder borderChamada = BorderFactory.createTitledBorder("Alunos - Marque os Presentes (" + LocalDate.now() + ")");
        painelAlunosChamada.setBorder(BorderFactory.createCompoundBorder(borderChamada,
                BorderFactory.createEmptyBorder(5, 5, 5, 5))); // Margem interna

        JScrollPane scrollPane = new JScrollPane(painelAlunosChamada); // Adiciona rolagem
        scrollPane.setPreferredSize(new Dimension(350, 250)); // Tamanho preferencial
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Label de status
        lblStatusCarregamento = new JLabel(" "); // Inicialmente vazio
        lblStatusCarregamento.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblStatusCarregamento, BorderLayout.CENTER); // Adiciona ao centro (será coberto pela lista se houver alunos)

        // --- Painel Inferior: Botões ---
        JPanel painelBotoesChamada = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSalvarChamada = new JButton("Salvar Presença");
        btnCancelarChamada = new JButton("Cancelar");
        painelBotoesChamada.add(btnSalvarChamada);
        painelBotoesChamada.add(btnCancelarChamada);
        add(painelBotoesChamada, BorderLayout.SOUTH);

        // Carrega alunos da primeira turma selecionada (se houver)
        // Faz isso depois que a interface estiver montada
        SwingUtilities.invokeLater(() -> {
            if (comboTurmas.getItemCount() > 0) {
                 comboTurmas.setSelectedIndex(0); // Garante que o listener seja acionado
                 carregarAlunosDaTurmaSelecionada();
            } else {
                 lblStatusCarregamento.setText("Nenhuma turma cadastrada.");
                 add(lblStatusCarregamento, BorderLayout.CENTER); // Garante que o label apareça
                 revalidate();
                 repaint();
            }
        });


        // --- Action Listeners ---
        btnSalvarChamada.addActionListener(e -> salvarPresenca());
        btnCancelarChamada.addActionListener(e -> fecharJanela());
    }

    private void carregarTurmasNoComboBox() {
        try {
            List<Turma> turmas = this.turmaRepository.listarTodas(); // Usa o repositório injetado
            comboTurmas.removeAllItems(); // Limpa itens antigos
            if (turmas.isEmpty()) {
                // Adicionar um item indicando que não há turmas? Ou deixar vazio?
                System.out.println("Nenhuma turma encontrada para carregar no ComboBox.");
            } else {
                for (Turma turma : turmas) {
                    comboTurmas.addItem(new TurmaItem(turma.getId(), turma.getNome()));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar turmas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace(); // Log do erro
        }
    }

    private void carregarAlunosDaTurmaSelecionada() {
        lblStatusCarregamento.setText("Carregando alunos..."); // Feedback visual
        remove(lblStatusCarregamento); // Remove o label se ele estava no centro

        painelAlunosChamada.removeAll(); // Limpa a lista anterior
        checkboxesAlunos.clear();
        TurmaItem itemSelecionado = (TurmaItem) comboTurmas.getSelectedItem();

        if (itemSelecionado == null) {
             lblStatusCarregamento.setText("Selecione uma turma.");
             add(lblStatusCarregamento, BorderLayout.CENTER); // Adiciona label de volta
             painelAlunosChamada.revalidate();
             painelAlunosChamada.repaint();
             return; // Nenhuma turma selecionada
        }

        int turmaId = itemSelecionado.getId();

        // Usar SwingWorker para carregar em background e não travar a interface
        SwingWorker<List<Aluno>, Void> worker = new SwingWorker<List<Aluno>, Void>() {
            @Override
            protected List<Aluno> doInBackground() throws Exception {
                // Busca os alunos usando o TurmaService (que pode usar TurmaRepo ou AlunoRepo)
                return turmaService.listarAlunosDaTurma(turmaId);
            }

            @Override
            protected void done() {
                try {
                    List<Aluno> alunos = get(); // Pega o resultado da busca
                    if (alunos.isEmpty()) {
                        painelAlunosChamada.add(new JLabel("Nenhum aluno cadastrado nesta turma."));
                        lblStatusCarregamento.setText(""); // Limpa status
                    } else {
                        for (Aluno aluno : alunos) {
                            JCheckBox checkBox = new JCheckBox(aluno.getNome() + " (ID: " + aluno.getId() + ")");
                            checkBox.setSelected(true); // Padrão: presente
                            painelAlunosChamada.add(checkBox);
                            checkboxesAlunos.put(aluno, checkBox); // Guarda referência
                        }
                        lblStatusCarregamento.setText(""); // Limpa status
                    }
                } catch (Exception e) {
                     JOptionPane.showMessageDialog(TurmaChamadaPanel.this, "Erro ao carregar alunos da turma: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                     e.printStackTrace();
                     painelAlunosChamada.add(new JLabel("Erro ao carregar alunos."));
                     lblStatusCarregamento.setText("Erro.");
                } finally {
                    // Atualiza a interface gráfica, independentemente do resultado
                    painelAlunosChamada.revalidate();
                    painelAlunosChamada.repaint();
                    // Ajusta o tamanho da janela container
                    Window window = SwingUtilities.getWindowAncestor(TurmaChamadaPanel.this);
                    if (window instanceof JDialog) {
                         // window.pack(); // pack pode não ser ideal se a lista for grande
                    }
                }
            }
        };
        worker.execute(); // Inicia a busca em background
    }

    private void salvarPresenca() {
        TurmaItem itemSelecionado = (TurmaItem) comboTurmas.getSelectedItem();
        if (itemSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Nenhuma turma selecionada.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (checkboxesAlunos.isEmpty()) {
             JOptionPane.showMessageDialog(this, "Nenhum aluno carregado para registrar presença.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int turmaId = itemSelecionado.getId();
        LocalDate hoje = LocalDate.now();
        Map<Integer, Boolean> presencaMap = new HashMap<>();

        // Monta o mapa de presença a partir dos checkboxes
        // Chave: ID do Aluno, Valor: true (presente) / false (ausente)
        for (Map.Entry<Aluno, JCheckBox> entry : checkboxesAlunos.entrySet()) {
            presencaMap.put(entry.getKey().getId(), entry.getValue().isSelected());
        }

        // Desabilitar botão para evitar cliques duplos enquanto salva
        btnSalvarChamada.setEnabled(false);
        btnCancelarChamada.setEnabled(false);

        // Usar SwingWorker para salvar em background
         SwingWorker<Void, Void> saveWorker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Chama o serviço para salvar
                 turmaService.registrarFrequencia(turmaId, hoje, presencaMap);
                 return null;
            }
             @Override
            protected void done() {
                 try {
                     get(); // Verifica se houve exceção durante o doInBackground
                     JOptionPane.showMessageDialog(TurmaChamadaPanel.this, "Presença para " + itemSelecionado.getNome() + " em " + hoje + " salva com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                     fecharJanela(); // Fecha após salvar
                 } catch (Exception ex) {
                     Throwable cause = ex.getCause() != null ? ex.getCause() : ex; // Pega a causa real
                      JOptionPane.showMessageDialog(TurmaChamadaPanel.this, "Ocorreu um erro ao salvar:\n" + cause.getMessage(), "Erro Crítico", JOptionPane.ERROR_MESSAGE);
                      cause.printStackTrace();
                      // Reabilitar botões em caso de erro
                        btnSalvarChamada.setEnabled(true);
                        btnCancelarChamada.setEnabled(true);
                 }
            }
         };
         saveWorker.execute();
    }

    private void fecharJanela() {
        Window janelaPai = SwingUtilities.getWindowAncestor(this);
        if (janelaPai != null) {
            janelaPai.dispose();
        }
    }

    // Classe auxiliar interna para guardar ID e Nome da Turma no ComboBox
    // É estática porque não precisa acessar membros da instância de TurmaChamadaPanel
    private static class TurmaItem {
        private final int id;
        private final String nome;

        public TurmaItem(int id, String nome) {
            this.id = id;
            this.nome = (nome != null ? nome : "Turma sem nome"); // Tratamento básico de nulo
        }

        public int getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        @Override
        public String toString() {
            // Texto que será exibido no ComboBox
            return nome; // Apenas o nome fica mais limpo no combo
        }

        // Implementar equals e hashCode é importante para o ComboBox funcionar corretamente
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TurmaItem turmaItem = (TurmaItem) o;
            return id == turmaItem.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }
}