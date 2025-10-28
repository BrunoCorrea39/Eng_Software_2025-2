package com.escolinha.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel; // Para a tabela
import java.awt.*;
import java.util.List;
import java.util.Map;

// Importações necessárias
import com.escolinha.domain.Aluno;
import com.escolinha.domain.StatusFatura; // Importar o Enum
import com.escolinha.service.FinanceiroService;

public class FinanceiroStatusPanel extends JPanel {

    private final FinanceiroService financeiroService;

    // Componentes
    private JTable tabelaStatus;
    private DefaultTableModel tableModel;
    private JButton btnAtualizar;
    private JButton btnFechar;

    public FinanceiroStatusPanel(FinanceiroService fService) {
        this.financeiroService = fService;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Status Financeiro dos Alunos"));

        // --- Tabela ---
        String[] colunas = {"ID Aluno", "Nome Aluno", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            // Torna as células não editáveis
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaStatus = new JTable(tableModel);
        tabelaStatus.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Permite selecionar apenas uma linha
        tabelaStatus.getTableHeader().setReorderingAllowed(false); // Impede reordenar colunas

        JScrollPane scrollPane = new JScrollPane(tabelaStatus);
        scrollPane.setPreferredSize(new Dimension(500, 300));
        add(scrollPane, BorderLayout.CENTER);

        // --- Painel de Botões ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAtualizar = new JButton("Atualizar Lista");
        btnFechar = new JButton("Fechar");
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnFechar);
        add(painelBotoes, BorderLayout.SOUTH);

        // --- Action Listeners ---
        btnAtualizar.addActionListener(e -> carregarStatusFinanceiro());
        btnFechar.addActionListener(e -> fecharJanela());

        // Carrega os dados iniciais
        carregarStatusFinanceiro();
    }

    private void carregarStatusFinanceiro() {
        // Limpa a tabela
        tableModel.setRowCount(0);

        try {
            // Chama o serviço para buscar o status (Map<Aluno, StatusFatura>)
            Map<Aluno, StatusFatura> statusMap = financeiroService.getStatusFinanceiroAlunos();

            if (statusMap.isEmpty()) {
                tableModel.addRow(new Object[]{"", "Nenhum aluno encontrado.", ""});
            } else {
                // Adiciona cada aluno e seu status à tabela
                for (Map.Entry<Aluno, StatusFatura> entry : statusMap.entrySet()) {
                    Aluno aluno = entry.getKey();
                    StatusFatura status = entry.getValue();
                    // Formata o status para exibição amigável
                    String statusStr = formatarStatus(status);
                    tableModel.addRow(new Object[]{aluno.getId(), aluno.getNome(), statusStr});
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar status financeiro: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            tableModel.addRow(new Object[]{"", "Erro ao carregar dados.", ""});
        }
    }

    private String formatarStatus(StatusFatura status) {
        if (status == null) return "Desconhecido";
        switch (status) {
            case PAGA: return "Em dia";
            case PENDENTE: return "Pendente";
            case VENCIDA: return "Em Atraso";
            default: return status.name(); // Retorna o nome do enum se não mapeado
        }
    }

    private void fecharJanela() {
        Window janelaPai = SwingUtilities.getWindowAncestor(this);
        if (janelaPai != null) {
            janelaPai.dispose();
        }
    }
}