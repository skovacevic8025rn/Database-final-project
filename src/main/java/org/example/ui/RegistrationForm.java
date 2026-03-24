package org.example.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.regex.Pattern;

public class RegistrationForm extends JFrame {

    private RoundedField      tfIme, tfPrezime, tfEmail, tfKorisnickoIme;
    private RoundedPass       pfLozinka, pfPotvrda;
    private JComboBox<String> cbUloga;
    private JCheckBox         chkUslovi;
    private JLabel            lblStatus;
    private JButton           btnRegistracija;
    private JProgressBar      pbStrength;
    private JLabel            lblStrengthText;

    public RegistrationForm() {
        setTitle("Medicinska Istraživanja – Registracija");
        setSize(520, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.PAGE_BG);

        add(buildHeader(), BorderLayout.NORTH);
        add(buildBody(),   BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel(new FlowLayout(FlowLayout.LEFT));
        h.setBackground(Color.HEADER_BG);
        h.setBorder(new EmptyBorder(18, 22, 18, 22));
        JLabel lbl = new JLabel("Medicinska Istraživanja – Registracija");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(java.awt.Color.WHITE);
        h.add(lbl);
        return h;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel();
        body.setBackground(Color.CARD_BG);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 28, 16, 28));

        JLabel desc = new JLabel("Popunite podatke za kreiranje novog naloga.");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setForeground(Color.TEXT_MUTED);
        desc.setAlignmentX(LEFT_ALIGNMENT);
        desc.setBorder(new EmptyBorder(0, 0, 16, 0));

        tfIme     = new RoundedField();
        tfPrezime = new RoundedField();
        JPanel row1 = hRow("Ime", tfIme, "Prezime", tfPrezime);

        tfEmail = new RoundedField();
        JPanel colEmail = vField("Email adresa", tfEmail);
        colEmail.setAlignmentX(LEFT_ALIGNMENT);
        colEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

        tfKorisnickoIme = new RoundedField();
        String[] uloge = {"— Odaberite ulogu —", "Korisnik", "Moderator", "Administrator"};
        cbUloga = new JComboBox<>(uloge);
        cbUloga.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbUloga.setBackground(Color.FIELD_BG);
        cbUloga.setForeground(Color.TEXT_DARK);
        cbUloga.setBorder(BorderFactory.createLineBorder(Color.FIELD_BOR, 1));
        cbUloga.setFocusable(false);
        JPanel row3 = hRow("Korisničko ime", tfKorisnickoIme, "Uloga", cbUloga);

        pfLozinka = new RoundedPass();
        pfPotvrda = new RoundedPass();
        JPanel row4 = hRow("Lozinka", pfLozinka, "Potvrdi lozinku", pfPotvrda);

        JPanel strength = buildStrengthPanel();

        chkUslovi = new JCheckBox("Prihvatam uslove korišćenja i politiku privatnosti");
        chkUslovi.setBackground(Color.CARD_BG);
        chkUslovi.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        chkUslovi.setForeground(Color.TEXT_MUTED);
        chkUslovi.setAlignmentX(LEFT_ALIGNMENT);
        chkUslovi.setFocusPainted(false);

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(Color.ERROR_CLR);
        lblStatus.setAlignmentX(LEFT_ALIGNMENT);
        lblStatus.setBorder(new EmptyBorder(6, 0, 2, 0));

        btnRegistracija = LoginForm.makeButton("REGISTRUJ SE");
        btnRegistracija.setAlignmentX(LEFT_ALIGNMENT);
        btnRegistracija.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnRegistracija.addActionListener(e -> validiraj());

        JButton btnOcisti = new JButton("Obriši");
        btnOcisti.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnOcisti.setBackground(new java.awt.Color(235, 235, 242));
        btnOcisti.setForeground(Color.TEXT_MUTED);
        btnOcisti.setOpaque(true);
        btnOcisti.setBorderPainted(false);
        btnOcisti.setFocusPainted(false);
        btnOcisti.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOcisti.addActionListener(e -> ocisti());

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 10, 0));
        btnRow.setBackground(Color.CARD_BG);
        btnRow.setAlignmentX(LEFT_ALIGNMENT);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnRow.add(btnRegistracija);
        btnRow.add(btnOcisti);

        JButton btnLogin = LoginForm.makeLinkButton("Već imate nalog? Prijavite se →");
        btnLogin.setAlignmentX(LEFT_ALIGNMENT);
        btnLogin.addActionListener(e -> { dispose(); new LoginForm(); });

        body.add(desc);
        body.add(row1);
        body.add(Box.createVerticalStrut(10));
        body.add(colEmail);
        body.add(Box.createVerticalStrut(10));
        body.add(row3);
        body.add(Box.createVerticalStrut(10));
        body.add(row4);
        body.add(Box.createVerticalStrut(8));
        body.add(strength);
        body.add(Box.createVerticalStrut(10));
        body.add(chkUslovi);
        body.add(lblStatus);
        body.add(btnRow);
        body.add(Box.createVerticalStrut(12));
        body.add(btnLogin);

        return body;
    }

    private JPanel buildFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.CENTER));
        f.setBackground(Color.FOOTER_BG);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.FOOTER_LINE),
                new EmptyBorder(10, 25, 12, 25)));
        JLabel lbl = new JLabel("Ovo je automatska poruka sistema - Medicinska Istraživanja - .");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(Color.TEXT_MUTED);
        f.add(lbl);
        return f;
    }

    private JPanel buildStrengthPanel() {
        JPanel p = new JPanel(new BorderLayout(8, 0));
        p.setBackground(Color.CARD_BG);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        p.setAlignmentX(LEFT_ALIGNMENT);

        pbStrength = new JProgressBar(0, 100);
        pbStrength.setStringPainted(false);
        pbStrength.setBorderPainted(false);
        pbStrength.setBackground(new java.awt.Color(220, 220, 230));
        pbStrength.setForeground(Color.ERROR_CLR);
        pbStrength.setPreferredSize(new Dimension(0, 5));

        lblStrengthText = new JLabel("Unesite lozinku");
        lblStrengthText.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblStrengthText.setForeground(Color.TEXT_MUTED);
        lblStrengthText.setPreferredSize(new Dimension(110, 14));

        p.add(pbStrength,      BorderLayout.CENTER);
        p.add(lblStrengthText, BorderLayout.EAST);

        pfLozinka.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e)  { updateStrength(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e)  { updateStrength(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateStrength(); }
        });
        return p;
    }

    private void updateStrength() {
        String pass = new String(pfLozinka.getPassword());
        int s = 0;
        if (pass.length() >= 8)                s += 25;
        if (pass.matches(".*[A-Z].*"))         s += 25;
        if (pass.matches(".*[0-9].*"))         s += 25;
        if (pass.matches(".*[^A-Za-z0-9].*")) s += 25;
        pbStrength.setValue(s);
        if      (s <= 25) { pbStrength.setForeground(Color.ERROR_CLR);                  lblStrengthText.setText("Slaba"); }
        else if (s <= 50) { pbStrength.setForeground(new java.awt.Color(200, 100, 30)); lblStrengthText.setText("Srednja"); }
        else if (s <= 75) { pbStrength.setForeground(new java.awt.Color(170, 140, 20)); lblStrengthText.setText("Dobra"); }
        else              { pbStrength.setForeground(Color.SUCCESS_CLR);                lblStrengthText.setText("Jaka ✓"); }
    }

    private void validiraj() {
        lblStatus.setForeground(Color.ERROR_CLR);
        if (tfIme.getText().trim().isEmpty())                                                { lblStatus.setText("⚠  Ime je obavezno."); return; }
        if (tfPrezime.getText().trim().isEmpty())                                            { lblStatus.setText("⚠  Prezime je obavezno."); return; }
        if (!java.util.regex.Pattern.matches("^[\\w.+-]+@[\\w-]+\\.[a-z]{2,}$", tfEmail.getText().trim())) { lblStatus.setText("⚠  Unesite ispravnu email adresu."); return; }
        if (tfKorisnickoIme.getText().trim().length() < 3)                                   { lblStatus.setText("⚠  Korisničko ime mora imati najmanje 3 karaktera."); return; }
        if (cbUloga.getSelectedIndex() == 0)                                                 { lblStatus.setText("⚠  Odaberite ulogu."); return; }
        String pass = new String(pfLozinka.getPassword());
        if (pass.length() < 8)                                                               { lblStatus.setText("⚠  Lozinka mora imati najmanje 8 karaktera."); return; }
        if (!pass.equals(new String(pfPotvrda.getPassword())))                               { lblStatus.setText("⚠  Lozinke se ne poklapaju."); return; }
        if (!chkUslovi.isSelected())                                                         { lblStatus.setText("⚠  Prihvatite uslove korišćenja."); return; }

        try {
            org.example.model.User user = new org.example.model.User(
                    tfIme.getText().trim(),
                    tfPrezime.getText().trim(),
                    tfEmail.getText().trim(),
                    tfKorisnickoIme.getText().trim(),
                    pass,
                    (String) cbUloga.getSelectedItem()
            );
            org.example.service.AuthService authService = new org.example.service.AuthService();
            authService.register(user);
            lblStatus.setForeground(Color.SUCCESS_CLR);
            lblStatus.setText("✔  Registracija uspešna! Dobrodošli, " + user.getUsername() + ".");
            btnRegistracija.setEnabled(false);
        } catch (IllegalArgumentException ex) {
            lblStatus.setForeground(Color.ERROR_CLR);
            lblStatus.setText("⚠  " + ex.getMessage());
        } catch (Exception ex) {
            lblStatus.setForeground(Color.ERROR_CLR);
            lblStatus.setText("⚠  Greška: " + ex.getMessage());
        }
    }

    private void ocisti() {
        tfIme.setText(""); tfPrezime.setText(""); tfEmail.setText("");
        tfKorisnickoIme.setText(""); pfLozinka.setText(""); pfPotvrda.setText("");
        cbUloga.setSelectedIndex(0); chkUslovi.setSelected(false);
        lblStatus.setText(" "); pbStrength.setValue(0);
        lblStrengthText.setText("Unesite lozinku");
        pbStrength.setForeground(Color.ERROR_CLR);
        btnRegistracija.setEnabled(true);
    }

    private JPanel hRow(String lbl1, JComponent f1, String lbl2, JComponent f2) {
        JPanel p = new JPanel(new java.awt.GridLayout(1, 2, 12, 0));
        p.setBackground(Color.CARD_BG);
        p.setAlignmentX(LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));
        p.add(vField(lbl1, f1));
        p.add(vField(lbl2, f2));
        return p;
    }

    private JPanel vField(String labelText, JComponent input) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.CARD_BG);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(Color.TEXT_MUTED);
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        input.setAlignmentX(LEFT_ALIGNMENT);
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        p.add(lbl);
        p.add(Box.createVerticalStrut(4));
        p.add(input);
        return p;
    }
}
