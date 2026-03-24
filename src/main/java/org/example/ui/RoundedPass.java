package org.example.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class RoundedPass extends JPasswordField {
    RoundedPass() {
        setFont(new Font("Segoe UI", Font.PLAIN, 13));
        setForeground(Color.TEXT_DARK);
        setCaretColor(Color.BTN_COLOR);
        setBackground(Color.FIELD_BG);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.FIELD_BOR, 1),
                new EmptyBorder(6, 10, 6, 10)));
        setOpaque(true);
        setEchoChar('●');
    }
}
