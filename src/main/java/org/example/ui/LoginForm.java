package org.example.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginForm extends JFrame {

    private JTextField     tfEmail;
    private JPasswordField pfLozinka;
    private JLabel         lblStatus;
    private JButton        btnLogin;

    public LoginForm() {
        setTitle("Medicinska Istraživanja – Prijava");
        setSize(440, 420);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.PAGE_BG);

        add(buildHeader("Medicinska Istraživanja – Prijava"), BorderLayout.NORTH);
        add(buildBody(),                                      BorderLayout.CENTER);
        add(buildFooter(),                                    BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel buildHeader(String title) {
        JPanel h = new JPanel(new FlowLayout(FlowLayout.LEFT));
        h.setBackground(Color.HEADER_BG);
        h.setBorder(new EmptyBorder(18, 22, 18, 22));
        JLabel lbl = new JLabel(title);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(java.awt.Color.WHITE);
        h.add(lbl);
        return h;
    }

    private JPanel buildBody() {
        JPanel body = new JPanel();
        body.setBackground(Color.CARD_BG);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(24, 28, 20, 28));

        JLabel desc = new JLabel("Prijavite se na vaš - Medicinska Istraživanja - nalog.");
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        desc.setForeground(Color.TEXT_MUTED);
        desc.setAlignmentX(LEFT_ALIGNMENT);
        desc.setBorder(new EmptyBorder(0, 0, 18, 0));

        JLabel lblEmail = makeLabel("Korisničko ime");
        tfEmail = new RoundedField();
        tfEmail.setAlignmentX(LEFT_ALIGNMENT);
        tfEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JLabel lblPass = makeLabel("Lozinka");
        pfLozinka = new RoundedPass();
        pfLozinka.setAlignmentX(LEFT_ALIGNMENT);
        pfLozinka.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(Color.ERROR_CLR);
        lblStatus.setAlignmentX(LEFT_ALIGNMENT);
        lblStatus.setBorder(new EmptyBorder(8, 0, 4, 0));

        btnLogin = makeButton("PRIJAVI SE");
        btnLogin.setAlignmentX(LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogin.addActionListener(e -> login());
        tfEmail.addActionListener(e -> pfLozinka.requestFocus());
        pfLozinka.addActionListener(e -> login());

        JButton btnReg = makeLinkButton("Nemate nalog? Registrujte se →");
        btnReg.setAlignmentX(LEFT_ALIGNMENT);
        btnReg.addActionListener(e -> { dispose(); new RegistrationForm(); });

        body.add(desc);
        body.add(lblEmail);
        body.add(Box.createVerticalStrut(4));
        body.add(tfEmail);
        body.add(Box.createVerticalStrut(12));
        body.add(lblPass);
        body.add(Box.createVerticalStrut(4));
        body.add(pfLozinka);
        body.add(lblStatus);
        body.add(btnLogin);
        body.add(Box.createVerticalStrut(14));
        body.add(btnReg);

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

    private void login() {
        String korisnickoIme = tfEmail.getText().trim();
        String lozinka       = new String(pfLozinka.getPassword());

        if (korisnickoIme.isEmpty()) {
            lblStatus.setForeground(Color.ERROR_CLR);
            lblStatus.setText("⚠  Unesite korisničko ime."); return;
        }
        if (lozinka.isEmpty()) {
            lblStatus.setForeground(Color.ERROR_CLR);
            lblStatus.setText("⚠  Unesite lozinku."); return;
        }

        try {
            org.example.service.AuthService authService = new org.example.service.AuthService();
            java.util.Optional<org.example.model.User> user = authService.login(korisnickoIme, lozinka);
            if (user.isPresent()) {
                lblStatus.setForeground(Color.SUCCESS_CLR);
                lblStatus.setText("✔  Dobrodošli, " + user.get().getUsername() + "!");
                btnLogin.setEnabled(false);
            } else {
                lblStatus.setForeground(Color.ERROR_CLR);
                lblStatus.setText("⚠  Pogrešno korisničko ime ili lozinka.");
            }
        } catch (Exception ex) {
            lblStatus.setForeground(Color.ERROR_CLR);
            lblStatus.setText("⚠  Greška: " + ex.getMessage());
        }
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(Color.TEXT_MUTED);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    static JButton makeButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(Color.BTN_COLOR);
        b.setForeground(java.awt.Color.WHITE);
        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(Color.BTN_HOV); }
            public void mouseExited (MouseEvent e) { b.setBackground(Color.BTN_COLOR); }
        });
        return b;
    }

    static JButton makeLinkButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        b.setForeground(Color.BTN_COLOR);
        b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(0, 0, 0, 0));
        return b;
    }
}
