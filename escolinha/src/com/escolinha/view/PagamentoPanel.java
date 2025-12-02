package com.escolinha.view;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.Fatura;
import com.escolinha.domain.StatusFatura;
import com.escolinha.service.AlunoService;
import com.escolinha.service.FinanceiroService;
import com.escolinha.service.PagamentoObserver; // <-- IMPORTANTE!

public class PagamentoPanel extends JPanel implements PagamentoObserver { // <--- implementa aqui

    private final FinanceiroService financeiroService;
    private final AlunoService alunoService;

    private JTextField txtAlunoIdPag;
    private JButton btnBuscarFaturas;
    private JLabel lblNomeAlunoPag;
    private JTable tabelaFaturasPendentes;
    private DefaultTableModel modelFaturas;
    private JButton btnRegistrarPagamento;
    private JButton btnFecharPag;

    private static final DateTimeFormatter DATE_FORMATTER_PAG = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PagamentoPanel(FinanceiroService fService, AlunoService aService) {
        this.financeiroService = fService;
        this.alunoService = aService;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Pagamento de Mensalidades"));

        // --- Painel Superior ---
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

        // --- Tabela ---
        String[] colunasFat = {"ID Fatura", "Vencimento", "Valor (R$)", "Status"};
        modelFaturas = new DefaultTableModel(colunasFat, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tabelaFaturasPendentes = new JTable(modelFaturas);
        JScrollPane scrollFaturas = new JScrollPane(tabelaFaturasPendentes);
        scrollFaturas.setPreferredSize(new Dimension(500, 200));
        add(scrollFaturas, BorderLayout.CENTER);

        // --- Botões ---
        JPanel painelBotoesPag = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRegistrarPagamento = new JButton("Registrar Pagamento Selecionado");
        btnFecharPag = new JButton("Fechar");
        painelBotoesPag.add(btnRegistrarPagamento);
        painelBotoesPag.add(btnFecharPag);
        add(painelBotoesPag, BorderLayout.SOUTH);

        btnRegistrarPagamento.setEnabled(false);

        // --- Listeners ---
        btnBuscarFaturas.addActionListener(e -> carregarFaturasPendentes());
        btnRegistrarPagamento.addActionListener(e -> registrarPagamento());
        btnFecharPag.addActionListener(e -> fecharJanela());
        tabelaFaturasPendentes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnRegistrarPagamento.setEnabled(tabelaFaturasPendentes.getSelectedRow() != -1);
            }
        });

        // --- Registra como observador ---
        financeiroService.addObserver(this);
    }

    // --- Observer: quando um pagamento for registrado em outro lugar ---
    @Override
    public void onPagamentoRegistrado(Fatura fatura) {
        SwingUtilities.invokeLater(() -> {
            String idTxt = txtAlunoIdPag.getText().trim();
            if (!idTxt.isEmpty() && Integer.toString(fatura.getAlunoId()).equals(idTxt)) {
                carregarFaturasPendentes();
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

        Optional<Aluno> alunoOpt = alunoService.buscarAlunoPorId(alunoId);
        if (alunoOpt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Aluno não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        lblNomeAlunoPag.setText("Aluno: " + alunoOpt.get().getNome());

        List<Fatura> faturas = financeiroService.listarFaturasAluno(alunoId);
        faturas.stream()
                .filter(f -> f.getStatus() == StatusFatura.PENDENTE || f.getStatus() == StatusFatura.VENCIDA)
                .sorted(Comparator.comparing(Fatura::getDataVencimento))
                .forEach(f -> modelFaturas.addRow(new Object[]{
                        f.getId(),
                        f.getDataVencimento().format(DATE_FORMATTER_PAG),
                        f.getValor(),
                        f.getStatus().name()
                }));
    }

    private void registrarPagamento() {
        int row = tabelaFaturasPendentes.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma fatura.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int faturaId = (int) modelFaturas.getValueAt(row, 0);
        try {
            boolean ok = financeiroService.registrarPagamentoFatura(faturaId, LocalDate.now());
            if (ok) {
                JOptionPane.showMessageDialog(this, "Pagamento registrado!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                carregarFaturasPendentes();
            } else {
                JOptionPane.showMessageDialog(this, "Fatura já estava paga.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar pagamento: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fecharJanela() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) w.dispose();
        financeiroService.removeObserver(this); // <-- remove ao fechar
    }
}
