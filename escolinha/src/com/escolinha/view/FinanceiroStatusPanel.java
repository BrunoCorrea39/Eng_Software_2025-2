package com.escolinha.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

import com.escolinha.domain.Aluno;
import com.escolinha.domain.Fatura;
import com.escolinha.domain.StatusFatura;
import com.escolinha.service.FinanceiroService;
import com.escolinha.service.PagamentoObserver;

public class FinanceiroStatusPanel extends JPanel implements PagamentoObserver {

    private final FinanceiroService financeiroService;

    private JTable tabelaStatus;
    private DefaultTableModel tableModel;

    public FinanceiroStatusPanel(FinanceiroService fService) {
        this.financeiroService = fService;

        // Layout base
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Status Financeiro dos Alunos"));

        // --- Tabela ---
        tableModel = new DefaultTableModel(
            new Object[]{"ID Aluno", "Nome Aluno", "Status"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        tabelaStatus = new JTable(tableModel);
        tabelaStatus.getTableHeader().setReorderingAllowed(false);
        tabelaStatus.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabelaStatus);
        scroll.setPreferredSize(new Dimension(600, 300));
        add(scroll, BorderLayout.CENTER);

        // Registrar painel como observador
        financeiroService.addObserver(this);

        // Carregar dados iniciais
        carregarStatusFinanceiro();
    }

    private void carregarStatusFinanceiro() {
        tableModel.setRowCount(0);

        try {
            Map<Aluno, StatusFatura> statusMap = financeiroService.getStatusFinanceiroAlunos();

            if (statusMap.isEmpty()) {
                tableModel.addRow(new Object[]{"", "Nenhum aluno encontrado.", ""});
                return;
            }

            for (Map.Entry<Aluno, StatusFatura> entry : statusMap.entrySet()) {
                Aluno aluno = entry.getKey();
                StatusFatura status = entry.getValue();
                tableModel.addRow(new Object[]{
                        aluno.getId(),
                        aluno.getNome(),
                        formatarStatus(status)
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar status financeiro:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private String formatarStatus(StatusFatura status) {
        if (status == null) return "Desconhecido";
        return switch (status) {
            case PAGA -> "Em dia";
            case PENDENTE -> "Pendente";
            case VENCIDA -> "Em atraso";
        };
    }

    // --- Chamado automaticamente quando ocorre um pagamento ---
    @Override
    public void onPagamentoRegistrado(Fatura fatura) {
        SwingUtilities.invokeLater(this::carregarStatusFinanceiro);
    }

    // Remove o painel da lista de observadores ao fechar
    @Override
    public void removeNotify() {
        super.removeNotify();
        financeiroService.removeObserver(this);
    }
}
