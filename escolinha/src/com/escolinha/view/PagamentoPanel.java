package com.escolinha.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Window;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

// Importações
import com.escolinha.domain.Aluno; // Para selecionar aluno
import com.escolinha.domain.Fatura;
import com.escolinha.domain.StatusFatura;
import com.escolinha.service.AlunoService; // Para buscar aluno
import com.escolinha.service.FinanceiroService;

public class PagamentoPanel extends JPanel {

    // Serviços
    private final FinanceiroService financeiroService;
    private final AlunoService alunoService; // Para buscar o aluno

    // Componentes
    private JTextField txtAlunoIdPag; // Ou ComboBox se preferir listar
    private JButton btnBuscarFaturas;
    private JLabel lblNomeAlunoPag;
    private JTable tabelaFaturasPendentes;
    private DefaultTableModel modelFaturas;
    private JButton btnRegistrarPagamento;
    private JButton btnFecharPag;

    // Formatter
    private static final DateTimeFormatter DATE_FORMATTER_PAG = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Construtor
    public PagamentoPanel(FinanceiroService fService, AlunoService aService) {
        this.financeiroService = fService;
        this.alunoService = aService;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Pagamento de Mensalidades"));

        // --- Painel Superior: Busca Aluno ---
        JPanel painelBuscaPag = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelBuscaPag.add(new JLabel("ID do Aluno:"));
        txtAlunoIdPag = new JTextField(5);
        painelBuscaPag.add(txtAlunoIdPag);
        btnBuscarFaturas = new JButton("Buscar Faturas Pendentes");
        painelBuscaPag.add(btnBuscarFaturas);
        lblNomeAlunoPag = new JLabel(" ");
        lblNomeAlunoPag.setFont(lblNomeAlunoPag.getFont().deriveFont(Font.BOLD));
        painelBuscaPag.add(lblNomeAlunoPag);
        add(painelBuscaPag, BorderLayout.NORTH);

        // --- Tabela de Faturas Pendentes ---
        String[] colunasFat = {"ID Fatura", "Vencimento", "Valor (R$)", "Status"};
        modelFaturas = new DefaultTableModel(colunasFat, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaFaturasPendentes = new JTable(modelFaturas);
        tabelaFaturasPendentes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Selecionar fatura a pagar
        JScrollPane scrollFaturas = new JScrollPane(tabelaFaturasPendentes);
        scrollFaturas.setPreferredSize(new Dimension(500, 200));
        add(scrollFaturas, BorderLayout.CENTER);

        // --- Painel Inferior: Botões ---
        JPanel painelBotoesPag = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRegistrarPagamento = new JButton("Registrar Pagamento Selecionado");
        btnFecharPag = new JButton("Fechar");
        painelBotoesPag.add(btnRegistrarPagamento);
        painelBotoesPag.add(btnFecharPag);
        add(painelBotoesPag, BorderLayout.SOUTH);

        // Desabilitar botão de pagamento inicialmente
        btnRegistrarPagamento.setEnabled(false);

        // --- Action Listeners ---
        btnBuscarFaturas.addActionListener(e -> carregarFaturasPendentes());
        btnRegistrarPagamento.addActionListener(e -> registrarPagamento());
        btnFecharPag.addActionListener(e -> fecharJanela());

        // Listener para habilitar botão de pagamento quando uma linha é selecionada
        tabelaFaturasPendentes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Evento disparado quando a seleção termina
                btnRegistrarPagamento.setEnabled(tabelaFaturasPendentes.getSelectedRow() != -1);
            }
        });
    }

    private void carregarFaturasPendentes() {
        modelFaturas.setRowCount(0);
        lblNomeAlunoPag.setText(" ");
        btnRegistrarPagamento.setEnabled(false);

        String idStr = txtAlunoIdPag.getText().trim();
        int alunoId;
        try {
            alunoId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID do Aluno inválido.", "Erro", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Busca Aluno
        Optional<Aluno> alunoOpt = alunoService.buscarAlunoPorId(alunoId);
        if (alunoOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aluno com ID " + alunoId + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        lblNomeAlunoPag.setText("Aluno: " + alunoOpt.get().getNome());

        // Busca Faturas do Aluno (Pendentes ou Vencidas)
        SwingWorker<List<Fatura>, Void> worker = new SwingWorker<List<Fatura>, Void>() {
            @Override
            protected List<Fatura> doInBackground() throws Exception {
                List<Fatura> faturas = financeiroService.listarFaturasAluno(alunoId);
                // Filtrar apenas pendentes ou vencidas
                return faturas.stream()
                        .filter(f -> f.getStatus() == StatusFatura.PENDENTE || f.getStatus() == StatusFatura.VENCIDA)
                        .sorted(Comparator.comparing(Fatura::getDataVencimento)) // Ordena por vencimento
                        .toList(); // Java 16+
                        // .collect(Collectors.toList()); // Versões anteriores
            }

            @Override
            protected void done() {
                try {
                    List<Fatura> faturasPendentes = get();
                    if (faturasPendentes.isEmpty()) {
                        modelFaturas.addRow(new Object[]{"", "Nenhuma fatura pendente.", "", ""});
                    } else {
                        for (Fatura f : faturasPendentes) {
                            modelFaturas.addRow(new Object[]{
                                f.getId(),
                                f.getDataVencimento().format(DATE_FORMATTER_PAG),
                                f.getValor().toString(),
                                f.getStatus().name() // Exibe PENDENTE ou VENCIDA
                            });
                        }
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(PagamentoPanel.this, "Erro ao carregar faturas: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    modelFaturas.addRow(new Object[]{"", "Erro ao carregar.", "", ""});
                }
            }
        };
        worker.execute();
    }

    private void registrarPagamento() {
        int linhaSelecionada = tabelaFaturasPendentes.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma fatura na tabela para registrar o pagamento.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Obtém o ID da fatura da tabela (assumindo que está na coluna 0)
        int faturaId = (int) modelFaturas.getValueAt(linhaSelecionada, 0);
        LocalDate dataPagamento = LocalDate.now(); // Assume pagamento na data atual

        try {
            boolean sucesso = financeiroService.registrarPagamentoFatura(faturaId, dataPagamento);
            if (sucesso) {
                JOptionPane.showMessageDialog(this, "Pagamento da fatura ID " + faturaId + " registrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                // Atualiza a lista após o pagamento
                carregarFaturasPendentes();
            } else {
                 JOptionPane.showMessageDialog(this, "Não foi possível registrar o pagamento (fatura pode já estar paga).", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
             JOptionPane.showMessageDialog(this, "Erro ao registrar pagamento: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
             e.printStackTrace();
        }
    }


    private void fecharJanela() {
        Window janelaPai = SwingUtilities.getWindowAncestor(this);
        if (janelaPai != null) {
            janelaPai.dispose();
        }
    }
}