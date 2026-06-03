package org.example.ui;

import org.bson.Document;
import org.example.repository.RezultatiRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RezultatiForm extends JPanel {

    private final RezultatiRepository repo = new RezultatiRepository();
    private final DefaultTableModel model;
    private List<Document> dokumenti;

    public RezultatiForm() throws Exception {
        setLayout(new BorderLayout());
        setBackground(Color.CARD_BG);
        setBorder(new EmptyBorder(16, 16, 16, 16));

        String[] kolone = {"ID", "Naziv", "Tip rezultata"};
        model = new DefaultTableModel(kolone, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        dokumenti = repo.findAll();
        for (Document d : dokumenti) {
            model.addRow(new Object[]{
                    d.getInteger("eksperiment_id"),
                    d.getString("naziv"),
                    d.getString("tip_rezultata")
            });
        }

        JTable tabela = new JTable(model);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.setRowHeight(26);
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabela.setFillsViewportHeight(true);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JLabel info = new JLabel("Klikni na eksperiment za detalje  (" + dokumenti.size() + ")");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info.setForeground(Color.TEXT_MUTED);
        info.setBorder(new EmptyBorder(0, 0, 10, 0));

        tabela.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = tabela.getSelectedRow();
                    if (row >= 0) prikaziDetalje(dokumenti.get(row));
                }
            }
        });

        add(info, BorderLayout.NORTH);
        add(new JScrollPane(tabela), BorderLayout.CENTER);
    }

    private void prikaziDetalje(Document d) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(d.getInteger("eksperiment_id")).append("\n");
        sb.append("Naziv: ").append(d.getString("naziv")).append("\n");
        sb.append("Cilj: ").append(d.getString("cilj")).append("\n");
        sb.append("Tip: ").append(d.getString("tip_rezultata")).append("\n");
        sb.append("Napomena: ").append(d.getString("napomena")).append("\n");
        sb.append("Datum unosa: ").append(d.getString("datum_unosa")).append("\n\n");
        sb.append("Merenja:\n");

        List<Document> merenja = d.getList("merenja", Document.class);
        if (merenja != null) {
            for (Document m : merenja) {
                sb.append("  • ").append(m.getString("parametar"))
                        .append(": ").append(m.get("vrednost"))
                        .append(" ").append(m.getString("jedinica")).append("\n");
            }
        }

        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        area.setEditable(false);
        area.setBackground(new java.awt.Color(245, 245, 245));

        JOptionPane.showMessageDialog(this, new JScrollPane(area),
                "Detalji eksperimenta", JOptionPane.INFORMATION_MESSAGE);
    }
}