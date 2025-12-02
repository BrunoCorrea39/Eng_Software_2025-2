package com.escolinha.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Window;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

// Importações
import com.escolinha.domain.Comunicado;
//import com.escolinha.domain.EventoCalendario; // Classe hipotética para calendário
import com.escolinha.repository.ComunicadoRepository; // Buscar comunicados
//import com.escolinha.service.CalendarioService; // Serviço hipotético para calendário

public class MuralAvisosPanel extends JPanel {

    // Repositórios/Serviços
    private final ComunicadoRepository comunicadoRepository;
    // private final CalendarioService calendarioService; // Se houver

    // Componentes
    private JTextArea areaComunicados;
    private JTextArea areaCalendario; // Simplesmente lista próximos eventos
    private JButton btnAtualizarMural;
    private JButton btnFecharMural;

    // Formatters
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER_CAL = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Construtor
    public MuralAvisosPanel(ComunicadoRepository cRepo /*, CalendarioService calService */) {
        this.comunicadoRepository = cRepo;
        // this.calendarioService = calService;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Mural de Avisos e Calendário"));

        // --- Painel Central: Comunicados e Calendário (dividido) ---
        JSplitPane splitPaneMural = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneMural.setResizeWeight(0.6); // 60% para comunicados, 40% para calendário

        // Área de Comunicados
        areaComunicados = new JTextArea(15, 40);
        areaComunicados.setEditable(false);
        areaComunicados.setLineWrap(true);
        areaComunicados.setWrapStyleWord(true);
        JScrollPane scrollComunicados = new JScrollPane(areaComunicados);
        scrollComunicados.setBorder(BorderFactory.createTitledBorder("Últimos Comunicados Gerais"));
        splitPaneMural.setLeftComponent(scrollComunicados);

        // Área de Calendário (Lista de Eventos)
        areaCalendario = new JTextArea(15, 25);
        areaCalendario.setEditable(false);
        JScrollPane scrollCalendario = new JScrollPane(areaCalendario);
        scrollCalendario.setBorder(BorderFactory.createTitledBorder("Próximos Eventos"));
        splitPaneMural.setRightComponent(scrollCalendario);

        add(splitPaneMural, BorderLayout.CENTER);

        // --- Painel Inferior: Botões ---
        JPanel painelBotoesMural = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAtualizarMural = new JButton("Atualizar");
        btnFecharMural = new JButton("Fechar");
        painelBotoesMural.add(btnAtualizarMural);
        painelBotoesMural.add(btnFecharMural);
        add(painelBotoesMural, BorderLayout.SOUTH);

        // --- Action Listeners ---
        btnAtualizarMural.addActionListener(e -> carregarMuralECalendario());
        btnFecharMural.addActionListener(e -> fecharJanela());

        // Carrega dados iniciais
        carregarMuralECalendario();
    }

    private void carregarMuralECalendario() {
        // Usar SwingWorker para carregar em background
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            StringBuilder comunicadosTexto = new StringBuilder();
            StringBuilder calendarioTexto = new StringBuilder("Próximos eventos (Exemplo):\n\n"); // Placeholder
            String errorMsg = null;

            @Override
            protected Void doInBackground() throws Exception {
                try {
                    // Carrega Comunicados Recentes (ex: últimos 7 dias)
                    LocalDateTime dataLimite = LocalDateTime.now().minusDays(7);
                    List<Comunicado> comunicados = comunicadoRepository.buscarRecentes(dataLimite);
                    // Ou usar listarComunicadosGerais() se preferir
                    // List<Comunicado> comunicados = comunicadoRepository.listarComunicadosGerais();

                    if (comunicados.isEmpty()) {
                        comunicadosTexto.append("Nenhum comunicado recente.");
                    } else {
                        for (Comunicado c : comunicados) {
                            comunicadosTexto.append("--- ").append(c.getTitulo()).append(" ---\n");
                            comunicadosTexto.append("(").append(c.getDataPublicacao().format(DATETIME_FORMATTER)).append(")\n");
                            comunicadosTexto.append(c.getMensagem()).append("\n\n");
                        }
                    }

                    // Carrega Calendário (SIMULADO - precisaria de um serviço/repo)
                    // Exemplo: List<EventoCalendario> eventos = calendarioService.buscarProximosEventos(LocalDate.now(), 15); // Próximos 15 dias
                    // for (EventoCalendario ev : eventos) {
                    //     calendarioTexto.append(ev.getData().format(DATE_FORMATTER_CAL))
                    //                    .append(" - ").append(ev.getDescricao()).append("\n");
                    // }
                    calendarioTexto.append(LocalDate.now().plusDays(3).format(DATE_FORMATTER_CAL)).append(" - Jogo Amistoso Sub-10\n");
                    calendarioTexto.append(LocalDate.now().plusDays(7).format(DATE_FORMATTER_CAL)).append(" - Reunião de Pais\n");


                } catch (Exception e) {
                    errorMsg = e.getMessage();
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                if (errorMsg != null) {
                    JOptionPane.showMessageDialog(MuralAvisosPanel.this, "Erro ao carregar dados: " + errorMsg, "Erro", JOptionPane.ERROR_MESSAGE);
                    areaComunicados.setText("Erro ao carregar comunicados.");
                    areaCalendario.setText("Erro ao carregar calendário.");
                } else {
                    areaComunicados.setText(comunicadosTexto.toString());
                    areaComunicados.setCaretPosition(0); // Rola para o topo
                    areaCalendario.setText(calendarioTexto.toString());
                    areaCalendario.setCaretPosition(0); // Rola para o topo
                }
                // Ajustar janela se necessário
                // Window window = SwingUtilities.getWindowAncestor(MuralAvisosPanel.this);
                // if (window instanceof JDialog) { window.pack(); }
            }
        };
        worker.execute();
    }

    private void fecharJanela() {
        Window janelaPai = SwingUtilities.getWindowAncestor(this);
        if (janelaPai != null) {
            janelaPai.dispose();
        }
    }
}