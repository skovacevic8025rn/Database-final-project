package org.example.ui;

import org.example.model.Istrazivac;
import org.example.model.User;
import org.example.repository.IstrazivacRepository;
import org.example.repository.SesijaRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public class ResearcherDashboard extends JFrame {
    private DefaultTableModel modelEksperimenti;

    private Istrazivac istrazivac;
    private IstrazivacRepository repo = new IstrazivacRepository();
    private SesijaRepository sesijaRepo = new SesijaRepository();

    public ResearcherDashboard(User user) throws Exception {
        setTitle("Dashboard – " + user.getName() + " " + user.getSurname());
        setSize(950, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.PAGE_BG);

        Optional<Istrazivac> opt = repo.findByIme(user.getName(), user.getSurname());
        if (opt.isEmpty()) {
            add(buildHeader(user), BorderLayout.NORTH);
            JLabel lbl = new JLabel("⚠  Profil istraživača nije pronađen u bazi.");
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lbl.setForeground(Color.ERROR_CLR);
            lbl.setBorder(new EmptyBorder(30, 30, 0, 0));
            add(lbl, BorderLayout.CENTER);
            setVisible(true);
            return;
        }
        istrazivac = opt.get();

        add(buildHeader(user), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.addTab("Eksperimenti",   buildTabEksperimenti());
        tabs.addTab("Promeni status", buildTabStatus());
        tabs.addTab("Obriši sesiju",  buildTabSesija());
        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel buildHeader(User user) {
        JPanel h = new JPanel(new FlowLayout(FlowLayout.LEFT));
        h.setBackground(Color.HEADER_BG);
        h.setBorder(new EmptyBorder(18, 22, 18, 22));
        JLabel lbl = new JLabel("Dobrodošli, " + user.getName() + " " + user.getSurname());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(java.awt.Color.WHITE);
        h.add(lbl);
        return h;
    }

    private JPanel buildTabEksperimenti() throws Exception {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        p.setBackground(Color.CARD_BG);

        String[] kolone = {"ID", "Naziv", "Cilj", "Datum početka", "Datum završetka", "Status"};
        this.modelEksperimenti = new DefaultTableModel(kolone, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        List<Object[]> eksperimenti = repo.findPlaniraniZavrseniEksperimenti(istrazivac.getIstrazivacId());
        for (Object[] red : eksperimenti) modelEksperimenti.addRow(red);

        JTable tabela = makeTable(modelEksperimenti);
        JLabel lbl = makeInfo("Eksperimenti  (" + eksperimenti.size() + ")");

        p.add(lbl, BorderLayout.NORTH);
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return p;
    }
    // ── Tab 2: Promena statusa ────────────────────────────────────────────────
    private JPanel buildTabStatus() throws Exception {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(24, 24, 24, 24));
        p.setBackground(Color.CARD_BG);

        List<Object[]> eksperimenti = repo.findPlaniraniZavrseniEksperimenti(istrazivac.getIstrazivacId());

        JComboBox<String> cbEks = new JComboBox<>();
        for (Object[] e : eksperimenti) {
            cbEks.addItem(e[0] + " – " + e[1] + " [" + e[5] + "]");
        }
        cbEks.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbEks.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cbEks.setAlignmentX(LEFT_ALIGNMENT);

        String[] statusi = {"Planiran", "U toku", "Zavrsen", "Otkazan"};
        JComboBox<String> cbStatus = new JComboBox<>(statusi);
        cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbStatus.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cbStatus.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblRez = new JLabel(" ");
        lblRez.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRez.setAlignmentX(LEFT_ALIGNMENT);

        JButton btn = LoginForm.makeButton("SAČUVAJ");
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.addActionListener(e -> {
            int idx = cbEks.getSelectedIndex();
            if (idx < 0 || eksperimenti.isEmpty()) return;
            int eksId = (int) eksperimenti.get(idx)[0];
            String noviStatus = (String) cbStatus.getSelectedItem();
            try {
                boolean ok = repo.updateStatusEksperimenta(eksId, noviStatus);
                lblRez.setForeground(ok ? Color.SUCCESS_CLR : Color.ERROR_CLR);
                lblRez.setText(ok ? " Status uspešno promenjen." : " Promena nije uspela.");
                if (ok) {
                    lblRez.setForeground(Color.SUCCESS_CLR);
                    lblRez.setText("✔  Status uspešno promenjen.");
                    // osvezi tabelu u Tab 1
                    modelEksperimenti.setRowCount(0);
                    try {
                        for (Object[] red : repo.findPlaniraniZavrseniEksperimenti(istrazivac.getIstrazivacId())) {
                            modelEksperimenti.addRow(red);
                        }
                    } catch (Exception ex2) {
                        // tiho ignorisi gresku osvezavanja
                    }
                } else {
                    lblRez.setForeground(Color.ERROR_CLR);
                    lblRez.setText("⚠  Promena nije uspela.");
                }
            } catch (Exception ex) {
                lblRez.setForeground(Color.ERROR_CLR);
                lblRez.setText(" Greška: " + ex.getMessage());
            }
        });

        p.add(makeLabel("Izaberi eksperiment:"));
        p.add(Box.createVerticalStrut(6));
        p.add(cbEks);
        p.add(Box.createVerticalStrut(16));
        p.add(makeLabel("Novi status:"));
        p.add(Box.createVerticalStrut(6));
        p.add(cbStatus);
        p.add(Box.createVerticalStrut(16));
        p.add(btn);
        p.add(Box.createVerticalStrut(10));
        p.add(lblRez);
        return p;
    }
    // ── Tab 3: Brisanje sesije ────────────────────────────────────────────────
    private JPanel buildTabSesija() throws Exception {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        p.setBackground(Color.CARD_BG);

        String[] kolone = {"ID", "Datum", "Vreme početka", "Vreme završetka", "Tip", "Eksperiment"};
        DefaultTableModel model = new DefaultTableModel(kolone, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        List<Object[]> sesije = sesijaRepo.findSesijeByIstrazivac(istrazivac.getIstrazivacId());
        for (Object[] red : sesije) model.addRow(red);

        JTable tabela = makeTable(model);

        JLabel lblRez = makeInfo("Izaberi sesiju i klikni Obriši");

        JButton btn = LoginForm.makeButton("OBRIŠI IZABRANU SESIJU");
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.addActionListener(e -> {
            int row = tabela.getSelectedRow();
            if (row < 0) {
                lblRez.setForeground(Color.ERROR_CLR);
                lblRez.setText("⚠  Nije izabrana sesija.");
                return;
            }
            int sesijId = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Obrisati sesiju ID " + sesijId + "?", "Potvrda", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            try {
                boolean ok = sesijaRepo.deleteSesija(sesijId, istrazivac.getIstrazivacId());
                if (ok) {
                    model.removeRow(row);
                    lblRez.setForeground(Color.SUCCESS_CLR);
                    lblRez.setText("✔  Sesija je obrisana.");
                } else {
                    lblRez.setForeground(Color.ERROR_CLR);
                    lblRez.setText("⚠  Brisanje nije uspelo.");
                }
            } catch (Exception ex) {
                lblRez.setForeground(Color.ERROR_CLR);
                lblRez.setText("⚠  Greška: " + ex.getMessage());
            }
        });

        JPanel south = new JPanel(new BorderLayout());
        south.setBackground(Color.CARD_BG);
        south.setBorder(new EmptyBorder(10, 0, 0, 0));
        south.add(btn,    BorderLayout.CENTER);
        south.add(lblRez, BorderLayout.SOUTH);

        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        p.add(south, BorderLayout.SOUTH);
        return p;
    }

    // ── Pomoćne metode ────────────────────────────────────────────────────────
    private JTable makeTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setRowHeight(26);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setFillsViewportHeight(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return t;
    }

    private JLabel makeInfo(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(Color.TEXT_MUTED);
        l.setBorder(new EmptyBorder(0, 0, 10, 0));
        return l;
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(Color.TEXT_MUTED);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }
}