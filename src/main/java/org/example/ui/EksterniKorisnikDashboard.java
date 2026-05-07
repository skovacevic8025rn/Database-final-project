package org.example.ui;

import org.example.model.User;
import org.example.repository.EksterniKorisnikRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EksterniKorisnikDashboard extends JFrame {

    private final EksterniKorisnikRepository repo = new EksterniKorisnikRepository();
    private final User user;

    public EksterniKorisnikDashboard(User user) throws Exception {
        this.user = user;
        setTitle("Dashboard – " + user.getName() + " " + user.getSurname());
        setSize(950, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.PAGE_BG);

        add(buildHeader(user), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.addTab("Laboratorije i istraživači", buildTabLaboratorije());
        tabs.addTab("Izmeni profil",              buildTabIzmeniProfil());
        tabs.addTab("Obriši nalog",               buildTabObrisiNalog());
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

    // ── Tab 1: Pregled laboratorija i istraživača ─────────────────────────────
    private JPanel buildTabLaboratorije() throws Exception {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(16, 16, 16, 16));
        p.setBackground(Color.CARD_BG);

        String[] kolone = {"Laboratorija", "Lokacija", "Ime", "Prezime", "Kontakt"};
        DefaultTableModel model = new DefaultTableModel(kolone, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        List<Object[]> lista = repo.findLaboratorijeIIstrazivaci();
        for (Object[] red : lista) model.addRow(red);

        JTable tabela = makeTable(model);
        JLabel lbl = makeInfo("Laboratorije i istraživači  (" + lista.size() + " stavki)");

        p.add(lbl, BorderLayout.NORTH);
        p.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return p;
    }

    // ── Tab 2: Izmena korisničkog imena i lozinke ─────────────────────────────
    private JPanel buildTabIzmeniProfil() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(24, 24, 24, 24));
        p.setBackground(Color.CARD_BG);

        JTextField tfKorisnickoIme = makeField();
        tfKorisnickoIme.setText(user.getUsername());

        JPasswordField pfNovaLozinka    = makePassField();
        JPasswordField pfPotvrdaLozinke = makePassField();

        JLabel lblRez = new JLabel(" ");
        lblRez.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRez.setAlignmentX(LEFT_ALIGNMENT);

        JButton btn = LoginForm.makeButton("SAČUVAJ IZMENE");
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.addActionListener(e -> {
            String novoIme   = tfKorisnickoIme.getText().trim();
            String novaLoz   = new String(pfNovaLozinka.getPassword());
            String potvrda   = new String(pfPotvrdaLozinke.getPassword());

            if (novoIme.isEmpty()) {
                lblRez.setForeground(Color.ERROR_CLR);
                lblRez.setText("⚠  Korisničko ime ne sme biti prazno."); return;
            }
            if (novaLoz.isEmpty()) {
                lblRez.setForeground(Color.ERROR_CLR);
                lblRez.setText("⚠  Lozinka ne sme biti prazna."); return;
            }
            if (!novaLoz.equals(potvrda)) {
                lblRez.setForeground(Color.ERROR_CLR);
                lblRez.setText("⚠  Lozinke se ne poklapaju."); return;
            }
            try {
                boolean ok = repo.updateKorisnickoImeILozinka(user.getId(), novoIme, novaLoz);
                if (ok) {
                    user.setUsername(novoIme);
                    user.setPassword(novaLoz);
                    lblRez.setForeground(Color.SUCCESS_CLR);
                    lblRez.setText("✔  Podaci su uspešno sačuvani.");
                } else {
                    lblRez.setForeground(Color.ERROR_CLR);
                    lblRez.setText("⚠  Korisničko ime je već zauzeto.");
                }
            } catch (Exception ex) {
                lblRez.setForeground(Color.ERROR_CLR);
                lblRez.setText("⚠  Greška: " + ex.getMessage());
            }
        });

        p.add(makeLabel("Novo korisničko ime:"));
        p.add(Box.createVerticalStrut(4));
        p.add(tfKorisnickoIme);
        p.add(Box.createVerticalStrut(12));
        p.add(makeLabel("Nova lozinka:"));
        p.add(Box.createVerticalStrut(4));
        p.add(pfNovaLozinka);
        p.add(Box.createVerticalStrut(12));
        p.add(makeLabel("Potvrda lozinke:"));
        p.add(Box.createVerticalStrut(4));
        p.add(pfPotvrdaLozinke);
        p.add(Box.createVerticalStrut(16));
        p.add(btn);
        p.add(Box.createVerticalStrut(10));
        p.add(lblRez);
        return p;
    }

    // ── Tab 3: Brisanje naloga ────────────────────────────────────────────────
    private JPanel buildTabObrisiNalog() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(24, 24, 24, 24));
        p.setBackground(Color.CARD_BG);

        JPasswordField pfLozinka = makePassField();

        JLabel lblRez = new JLabel(" ");
        lblRez.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRez.setAlignmentX(LEFT_ALIGNMENT);

        JButton btn = LoginForm.makeButton("OBRIŠI MОЈ NALOG");
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.addActionListener(e -> {
            String lozinka = new String(pfLozinka.getPassword());
            if (lozinka.isEmpty()) {
                lblRez.setForeground(Color.ERROR_CLR);
                lblRez.setText("⚠  Unesite lozinku."); return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Da li ste sigurni da želite da obrišete nalog? Ova akcija je nepovratna.",
                    "Potvrda brisanja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
            try {
                boolean ok = repo.deleteNalog(user.getId(), lozinka);
                if (ok) {
                    JOptionPane.showMessageDialog(this,
                            "Nalog je obrisan. Aplikacija će se zatvoriti.",
                            "Nalog obrisan", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(0);
                } else {
                    lblRez.setForeground(Color.ERROR_CLR);
                    lblRez.setText("⚠  Pogrešna lozinka.");
                }
            } catch (Exception ex) {
                lblRez.setForeground(Color.ERROR_CLR);
                lblRez.setText("⚠  Greška: " + ex.getMessage());
            }
        });

        p.add(makeLabel("Unesite lozinku za potvrdu:"));
        p.add(Box.createVerticalStrut(4));
        p.add(pfLozinka);
        p.add(Box.createVerticalStrut(16));
        p.add(btn);
        p.add(Box.createVerticalStrut(10));
        p.add(lblRez);
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

    private JTextField makeField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tf.setAlignmentX(LEFT_ALIGNMENT);
        return tf;
    }

    private JPasswordField makePassField() {
        JPasswordField pf = new JPasswordField();
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        pf.setAlignmentX(LEFT_ALIGNMENT);
        return pf;
    }
}