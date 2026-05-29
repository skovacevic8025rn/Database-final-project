package org.example.ui;

import org.example.model.Istrazivac;
import org.example.service.AuthService;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class RegistrationForm extends JFrame {

    private RoundedField      tfKontakt, tfKorisnickoIme;
    private RoundedPass       pfLozinka, pfPotvrda;
    private JCheckBox         chkUslovi;
    private JLabel            lblStatus;
    private JButton           btnRegistracija;
    private JProgressBar      pbStrength;
    private JLabel            lblStrengthText;

    public RegistrationForm() {
        setTitle("Medicinska Istraživanja – Registracija");
        setSize(520, 580);
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

        tfKontakt = new RoundedField();
        JPanel colKontakt = vField("Kontakt email", tfKontakt);
        colKontakt.setAlignmentX(LEFT_ALIGNMENT);
        colKontakt.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

        tfKorisnickoIme = new RoundedField();
        JPanel colKorisnickoIme = vField("Korisničko ime", tfKorisnickoIme);
        colKorisnickoIme.setAlignmentX(LEFT_ALIGNMENT);
        colKorisnickoIme.setMaximumSize(new Dimension(Integer.MAX_VALUE, 58));

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
        body.add(colKontakt);
        body.add(Box.createVerticalStrut(10));
        body.add(colKorisnickoIme);
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
        String kontakt       = tfKontakt.getText().trim();
        String korisnickoIme = tfKorisnickoIme.getText().trim();
        String lozinka       = new String(pfLozinka.getPassword());
        String potvrda       = new String(pfPotvrda.getPassword());

        if (kontakt.isEmpty())          { show("Kontakt email je obavezan."); return; }
        if (korisnickoIme.length() < 3) { show("Korisničko ime mora imati najmanje 3 karaktera."); return; }
        if (lozinka.length() < 8)       { show("Lozinka mora imati najmanje 8 karaktera."); return; }
        if (!lozinka.equals(potvrda))   { show("Lozinke se ne poklapaju."); return; }
        if (!chkUslovi.isSelected())    { show("Prihvatite uslove korišćenja."); return; }

        try {
            AuthService auth = new AuthService();
            Istrazivac ist = auth.register(kontakt, korisnickoIme, lozinka);
            lblStatus.setForeground(Color.SUCCESS_CLR);
            lblStatus.setText("✔  Dobrodošli, " + ist.getName() + " " + ist.getSurname() + "!");
            btnRegistracija.setEnabled(false);
        } catch (IllegalArgumentException ex) {
            show(ex.getMessage());
        } catch (Exception ex) {
            show("Greška: " + ex.getMessage());
        }
    }

    private void show(String msg) {
        lblStatus.setForeground(Color.ERROR_CLR);
        lblStatus.setText("⚠  " + msg);
    }

    private void ocisti() {
        tfKontakt.setText("");
        tfKorisnickoIme.setText("");
        pfLozinka.setText("");
        pfPotvrda.setText("");
        chkUslovi.setSelected(false);
        lblStatus.setText(" ");
        pbStrength.setValue(0);
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
