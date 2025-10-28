package com.escolinha.view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter; // Para formatar datas
import java.util.Collections; // Para lista vazia
import java.util.Comparator; // Para ordenar
import java.util.List;
import java.util.Optional;

// Importações
import com.escolinha.domain.Aluno;
import com.escolinha.domain.Avaliacao;
import com.escolinha.domain.Frequencia;
import com.escolinha.service.AlunoService; // Para buscar o nome do aluno
import com.escolinha.service.TurmaService; // Para buscar avaliações
import com.escolinha.repository.FrequenciaRepository; // Para buscar frequência

public class AlunoDesempenhoPanel extends JPanel {

    // Serviços e Repositórios
    private final AlunoService alunoService;
    private final TurmaService turmaService;
    private final FrequenciaRepository frequenciaRepository;

    // Componentes
    private JTextField txtAlunoIdBusca; // Campo para digitar o ID do aluno
    private JButton btnBuscarDesempenho;
    private JLabel lblNomeAluno;
    private JTable tabelaFrequencia;
    private DefaultTableModel modelFrequencia;
    private JTable tabelaAvaliacoes;
    private DefaultTableModel modelAvaliacoes;
    private JButton btnFecharDesempenho;

    // Formatter para datas
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Construtor
    public AlunoDesempenhoPanel(AlunoService aService, TurmaService tService, FrequenciaRepository fRepo) {
        this.alunoService = aService;
        this.turmaService = tService;
        this.frequenciaRepository = fRepo;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Desempenho do Aluno"));

        // --- Painel Superior: Busca do Aluno ---
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBusca.add(new JLabel("ID do Aluno:"));
        txtAlunoIdBusca = new JTextField(5);
        painelBusca.add(txtAlunoIdBusca);
        btnBuscarDesempenho = new JButton("Buscar Desempenho");
        painelBusca.add(btnBuscarDesempenho);
        lblNomeAluno = new JLabel(" "); // Espaço para nome do aluno
        lblNomeAluno.setFont(lblNomeAluno.getFont().deriveFont(Font.BOLD));
        painelBusca.add(lblNomeAluno);
        add(painelBusca, BorderLayout.NORTH);

        // --- Painel Central: Frequência e Avaliações (dividido) ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.4); // 40% do espaço para frequência, 60% para avaliações

        // Tabela de Frequência
        String[] colunasFreq = {"Data Aula", "Status"};
        modelFrequencia = new DefaultTableModel(colunasFreq, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaFrequencia = new JTable(modelFrequencia);
        JScrollPane scrollFreq = new JScrollPane(tabelaFrequencia);
        scrollFreq.setBorder(BorderFactory.createTitledBorder("Histórico de Frequência"));
        splitPane.setTopComponent(scrollFreq);

        // Tabela de Avaliações
        String[] colunasAval = {"Data", "Técnico", "Tático", "Comportamental"};
        modelAvaliacoes = new DefaultTableModel(colunasAval, 0){
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaAvaliacoes = new JTable(modelAvaliacoes);
        // Ajustar largura das colunas de comentários (exemplo)
        tabelaAvaliacoes.getColumnModel().getColumn(1).setPreferredWidth(150);
        tabelaAvaliacoes.getColumnModel().getColumn(2).setPreferredWidth(150);
        tabelaAvaliacoes.getColumnModel().getColumn(3).setPreferredWidth(150);

        JScrollPane scrollAval = new JScrollPane(tabelaAvaliacoes);
        scrollAval.setBorder(BorderFactory.createTitledBorder("Avaliações do Treinador"));
        splitPane.setBottomComponent(scrollAval);

        add(splitPane, BorderLayout.CENTER);


        // --- Painel Inferior: Botões ---
        JPanel painelBotoesDes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnFecharDesempenho = new JButton("Fechar");
        painelBotoesDes.add(btnFecharDesempenho);
        add(painelBotoesDes, BorderLayout.SOUTH);

        // --- Action Listeners ---
        btnBuscarDesempenho.addActionListener(e -> carregarDesempenhoAluno());
        btnFecharDesempenho.addActionListener(e -> fecharJanela());

        // Carregar dados de teste se ID for informado inicialmente (ex: passar ID pelo construtor)
        // carregarDesempenhoAluno(); // Ou deixar vazio até clicar em buscar
    }

    private void carregarDesempenhoAluno() {
        // Limpa tabelas e nome
        modelFrequencia.setRowCount(0);
        modelAvaliacoes.setRowCount(0);
        lblNomeAluno.setText(" ");

        String idStr = txtAlunoIdBusca.getText().trim();
        int alunoId;
        try {
            alunoId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do Aluno inválido. Digite um número.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Busca o nome do Aluno
        Optional<Aluno> alunoOpt = alunoService.buscarAlunoPorId(alunoId);
        if (alunoOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aluno com ID " + alunoId + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        lblNomeAluno.setText("Aluno: " + alunoOpt.get().getNome());

        // Usa SwingWorker para carregar dados em background
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            List<Frequencia> frequencias = Collections.emptyList();
            List<Avaliacao> avaliacoes = Collections.emptyList();
            String errorMsg = null;

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    // Busca Frequência (usando FrequenciaRepository)
                    frequencias = frequenciaRepository.buscarPorAlunoId(alunoId);
                    // Ordena por data (mais recente primeiro)
                    frequencias.sort(Comparator.comparing(Frequencia::getDataAula).reversed());

                    // Busca Avaliações (usando TurmaService)
                    avaliacoes = turmaService.listarAvaliacoesAluno(alunoId);
                    // Ordena por data (mais recente primeiro)
                    avaliacoes.sort(Comparator.comparing(Avaliacao::getDataAvaliacao).reversed());
                } catch (Exception e) {
                    errorMsg = e.getMessage();
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                if (errorMsg != null) {
                     JOptionPane.showMessageDialog(AlunoDesempenhoPanel.this, "Erro ao carregar dados: " + errorMsg, "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Preenche Tabela de Frequência
                    if (frequencias.isEmpty()) {
                         modelFrequencia.addRow(new Object[]{"", "Nenhum registro"});
                    } else {
                        for (Frequencia f : frequencias) {
                            modelFrequencia.addRow(new Object[]{
                                f.getDataAula().format(DATE_FORMATTER),
                                f.isPresente() ? "Presente" : "Ausente"
                            });
                        }
                    }
                    // Preenche Tabela de Avaliações
                    if (avaliacoes.isEmpty()){
                         modelAvaliacoes.addRow(new Object[]{"", "Nenhuma avaliação", "", ""});
                    } else {
                        for (Avaliacao a : avaliacoes) {
                            modelAvaliacoes.addRow(new Object[]{
                                a.getDataAvaliacao().format(DATE_FORMATTER),
                                a.getComentarioTecnico(),
                                a.getComentarioTatico(),
                                a.getComentarioComportamental()
                            });
                        }
                    }
                }
                // Ajustar tamanho da janela container após carregar
                Window window = SwingUtilities.getWindowAncestor(AlunoDesempenhoPanel.this);
                if (window instanceof JDialog) {
                    // window.pack(); // Pack pode ser melhor aqui
                }
            }
        };
        worker.execute(); // Inicia a busca
    }


    private void fecharJanela() {
        Window janelaPai = SwingUtilities.getWindowAncestor(this);
        if (janelaPai != null) {
            janelaPai.dispose();
        }
    }
}