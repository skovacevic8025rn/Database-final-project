package org.example.ui;

import org.example.model.User;
import org.example.repository.AdminRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {

    private final AdminRepository repo = new AdminRepository();
    private DefaultTableModel modelSesije;

    public AdminDashboard(User user) throws Exception {
        setTitle("Admin Dashboard – " + user.getName() + " " + user.getSurname());
        setSize(1050, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.PAGE_BG);

        add(buildHeader(user), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.addTab("Zakazane sesije",    buildTabSesije());
        tabs.addTab("Izmeni sesiju",      buildTabIzmeniSesiju());
        tabs.addTab("Obriši laboratoriju", buildTabLaboratorija());
        add(tabs, BorderLayout.CENTER);

        setVisible(true);
    }

    private JPanel buildHeader(User user) {
        JPanel h = new JPanel(new FlowLayout(FlowLayout.LEFT));
        h.setBackground(Color.HEADER_BG);
        h.setBorder(new EmptyBorder(18, 22, 18, 22));
        JLabel lbl = new JLabel("Administrator: " + user.getName() + " " + user.getSurname());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(java.awt.Color.WHITE);
        h.add(lbl);
        return h;
    }

    // ── Tab 1: Pregled zakazanih sesija ───────────────────────────────────────
    private JPanel buildTabSesije() throws Exception {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        p.setBackground(Color.CARD_BG);

        String[] kolone = {"ID", "Datum", "Poč.", "Kraj", "Tip", "Eksperiment", "Laboratorija", "Status izv."};
        this.modelSesije = new DefaultTableModel(kolone, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        List<Object[]> sesije = repo.findZakazaneSesije();
        for (Object[] red : sesije) modelSesije.addRow(red);

        JTable tabela = makeTable(modelSesije);
        JLabel lbl = makeInfo("Sve zakazane sesije  (" + sesije.size() + ")");

        p.add(lbl, BorderLayout.NORTH);
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return p;
    }

    // ── Tab 2: Izmena podataka sesije ─────────────────────────────────────────
    private JPanel buildTabIzmeniSesiju() throws Exception {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(24, 24, 24, 24));
        p.setBackground(Color.CARD_BG);

        List<Object[]> sesije = repo.findZakazaneSesije();

        JComboBox<String> cbSesija = new JComboBox<>();
        for (Object[] s : sesije) {
            cbSesija.addItem(s[0] + " – " + s[1] + " [" + s[5] + "]");
        }
        cbSesija.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbSesija.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cbSesija.setAlignmentX(LEFT_ALIGNMENT);

        JTextField tfDatum     = makeField();
        JTextField tfVremePoc  = makeField();
        JTextField tfVremeKraj = makeField();
        JTextField tfTip       = makeField();

        cbSesija.addActionListener(e -> {
            int idx = cbSesija.getSelectedIndex();
            if (idx < 0 || sesije.isEmpty()) return;
            Object[] s = sesije.get(idx);
            tfDatum.setText(s[1] != null ? s[1].toString() : "");
            tfVremePoc.setText(s[2] != null ? s[2].toString() : "");
            tfVremeKraj.setText(s[3] != null ? s[3].toString() : "");
            tfTip.setText(s[4] != null ? s[4].toString() : "");
        });
        if (!sesije.isEmpty()) cbSesija.setSelectedIndex(0);

        JLabel lblRez = new JLabel(" ");
        lblRez.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRez.setAlignmentX(LEFT_ALIGNMENT);

        JButton btn = LoginForm.makeButton("SAČUVAJ IZMENE");
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.addActionListener(e -> {
            int idx = cbSesija.getSelectedIndex();
            if (idx < 0 || sesije.isEmpty()) return;
            int sesijaId = (int) sesije.get(idx)[0];
            try {
                boolean ok = repo.updateSesija(sesijaId,
                        tfDatum.getText().trim(),
                        tfVremePoc.getText().trim(),
                        tfVremeKraj.getText().trim(),
                        tfTip.getText().trim());
                lblRez.setForeground(ok ? Color.SUCCESS_CLR : Color.ERROR_CLR);
                lblRez.setText(ok ? "✔  Sesija uspešno izmenjena." : "⚠  Izmena nije uspela.");
                if (ok) {
                    modelSesije.setRowCount(0);
                    try {
                        for (Object[] red : repo.findZakazaneSesije()) modelSesije.addRow(red);
                    } catch (Exception ex2) { /* tiho ignoriši */ }
                }
            } catch (Exception ex) {
                lblRez.setForeground(Color.ERROR_CLR);
                lblRez.setText("⚠  Greška: " + ex.getMessage());
            }
        });

        p.add(makeLabel("Izaberi sesiju:"));
        p.add(Box.createVerticalStrut(6));
        p.add(cbSesija);
        p.add(Box.createVerticalStrut(16));
        p.add(makeLabel("Datum (YYYY-MM-DD):"));
        p.add(Box.createVerticalStrut(4));
        p.add(tfDatum);
        p.add(Box.createVerticalStrut(12));
        p.add(makeLabel("Vreme početka (HH:MM:SS):"));
        p.add(Box.createVerticalStrut(4));
        p.add(tfVremePoc);
        p.add(Box.createVerticalStrut(12));
        p.add(makeLabel("Vreme završetka (HH:MM:SS):"));
        p.add(Box.createVerticalStrut(4));
        p.add(tfVremeKraj);
        p.add(Box.createVerticalStrut(12));
        p.add(makeLabel("Tip:"));
        p.add(Box.createVerticalStrut(4));
        p.add(tfTip);
        p.add(Box.createVerticalStrut(16));
        p.add(btn);
        p.add(Box.createVerticalStrut(10));
        p.add(lblRez);
        return p;
    }
    // ── Tab 3: Brisanje laboratorije ──────────────────────────────────────────
    private JPanel buildTabLaboratorija() throws Exception {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(24, 24, 24, 24));
        p.setBackground(Color.CARD_BG);

        List<Object[]> laboratorije = repo.findSveLaboratorije();

        JComboBox<String> cbLab = new JComboBox<>();
        for (Object[] l : laboratorije) {
            cbLab.addItem(l[0] + " – " + l[1]);
        }
        cbLab.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbLab.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        cbLab.setAlignmentX(LEFT_ALIGNMENT);

        JLabel lblRez = new JLabel(" ");
        lblRez.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRez.setAlignmentX(LEFT_ALIGNMENT);

        JButton btn = LoginForm.makeButton("OBRIŠI LABORATORIJU");
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.addActionListener(e -> {
            int idx = cbLab.getSelectedIndex();
            if (idx < 0 || laboratorije.isEmpty()) return;
            int labId    = (int) laboratorije.get(idx)[0];
            String naziv = laboratorije.get(idx)[1].toString();

            if (!repo_mozeBrisati(labId, lblRez)) return;

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Obrisati laboratoriju \"" + naziv + "\" (ID " + labId + ")?",
                    "Potvrda", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                boolean ok = repo.deleteLaboratorija(labId);
                if (ok) {
                    laboratorije.remove(idx);
                    cbLab.removeItemAt(idx);
                    lblRez.setForeground(Color.SUCCESS_CLR);
                    lblRez.setText("✔  Laboratorija je obrisana.");
                } else {
                    lblRez.setForeground(Color.ERROR_CLR);
                    lblRez.setText("⚠  Brisanje nije uspelo.");
                }
            } catch (Exception ex) {
                lblRez.setForeground(Color.ERROR_CLR);
                lblRez.setText("⚠  Greška: " + ex.getMessage());
            }
        });

        p.add(makeLabel("Izaberi laboratoriju:"));
        p.add(Box.createVerticalStrut(6));
        p.add(cbLab);
        p.add(Box.createVerticalStrut(16));
        p.add(btn);
        p.add(Box.createVerticalStrut(10));
        p.add(lblRez);
        return p;
    }

    private boolean repo_mozeBrisati(int labId, JLabel lblRez) {
        try {
            if (!repo.mozeBrisatiLaboratoriju(labId)) {
                lblRez.setForeground(Color.ERROR_CLR);
                lblRez.setText("⚠  Nije moguće: u laboratoriji rade istraživači.");
                return false;
            }
            return true;
        } catch (Exception ex) {
            lblRez.setForeground(Color.ERROR_CLR);
            lblRez.setText("⚠  Greška: " + ex.getMessage());
            return false;
        }
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

    private JTextField makeField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tf.setAlignmentX(LEFT_ALIGNMENT);
        return tf;
    }
}