package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.User;
import com.siskema.gryffindor.service.DataService;
import com.siskema.gryffindor.service.SessionManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {

    private RoundedTextField emailField;
    private RoundedPasswordField passwordField;
    private DataService dataService; // <-- BARU

    public LoginFrame() {
        this.dataService = new DataService(); // <-- BARU
        
        setTitle("Siskema Gryffindor - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        getContentPane().setBackground(UIConstants.COLOR_BACKGROUND);

        // ... (Kode UI createCardPanel, createLeftPanel tetap SAMA) ...
        JPanel root = new JPanel(new GridBagLayout());
        root.setOpaque(false);
        setContentPane(root);
        JPanel card = createCardPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        root.add(card, gbc);
    }

    private JPanel createCardPanel() {
        // ... (Implementasi SAMA) ...
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UIConstants.COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                new EmptyBorder(32, 40, 32, 40)
        ));
        JPanel contentContainer = new JPanel(new GridBagLayout());
        contentContainer.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 40);
        gbc.gridx = 0;
        gbc.weightx = 0.4;
        contentContainer.add(createLeftPanel(), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 0, 0, 0);
        contentContainer.add(createRightPanel(), gbc); // Right panel memanggil createRightPanel
        GridBagConstraints cardGBC = new GridBagConstraints();
        cardGBC.gridx = 0;
        cardGBC.gridy = 0;
        cardGBC.fill = GridBagConstraints.NONE;
        contentContainer.setPreferredSize(new Dimension(820, 420));
        contentContainer.setMaximumSize(new Dimension(900, 600));
        card.add(contentContainer, cardGBC);
        cardGBC.weightx = 1.0;
        cardGBC.weighty = 1.0;
        cardGBC.fill = GridBagConstraints.BOTH;
        card.add(new JPanel() {{ setOpaque(false); }}, cardGBC);
        return card;
    }

    private JPanel createLeftPanel() {
        // ... (Implementasi SAMA) ...
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(UIConstants.COLOR_CARD);
        leftPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        JLabel shield = new JLabel("🛡️", SwingConstants.CENTER);
        shield.setFont(new Font("SansSerif", Font.PLAIN, 64));
        gbc.gridy = 0;
        leftPanel.add(shield, gbc);
        JLabel title = new JLabel("SISKEMA", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(UIConstants.COLOR_PRIMARY);
        gbc.gridy = 1;
        leftPanel.add(title, gbc);
        JLabel subtitle = new JLabel("GRYFFINDOR", SwingConstants.CENTER);
        subtitle.setFont(new Font("Serif", Font.BOLD, 22));
        subtitle.setForeground(UIConstants.COLOR_TEXT_DARK);
        gbc.gridy = 2;
        leftPanel.add(subtitle, gbc);
        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(UIConstants.COLOR_CARD);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1.0;

        // Judul
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(UIConstants.FONT_TITLE);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        rightPanel.add(titleLabel, gbc);

        // Input Email/NIM
        JLabel emailLabel = new JLabel("Username (NIM/ID)"); // Diubah
        emailLabel.setFont(UIConstants.FONT_NORMAL);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 0, 0);
        rightPanel.add(emailLabel, gbc);

        emailField = new RoundedTextField();
        emailField.setText("mhs"); // Default untuk tes
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 15, 0);
        rightPanel.add(emailField, gbc);

        // Input Password
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(UIConstants.FONT_NORMAL);
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 0, 0);
        rightPanel.add(passwordLabel, gbc);

        passwordField = new RoundedPasswordField();
        passwordField.setText("123"); // Default untuk tes
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 0, 15, 0);
        rightPanel.add(passwordField, gbc);

        // ... (Link Daftar & Lupa Kata Sandi SAMA) ...
        gbc.gridy = 5;
        gbc.insets = new Insets(5, 0, 5, 0);
        JPanel helperPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        helperPanel.setOpaque(false);
        JLabel belum = new JLabel("Belum punya akun? ");
        belum.setFont(UIConstants.FONT_SMALL);
        JLabel daftar = new JLabel("Silahkan daftar");
        daftar.setFont(UIConstants.FONT_SMALL);
        daftar.setForeground(UIConstants.COLOR_PRIMARY);
        daftar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        daftar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new RegistrationFrame().setVisible(true);
                dispose();
            }
        });
        helperPanel.add(belum);
        helperPanel.add(daftar);
        rightPanel.add(helperPanel, gbc);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 20, 0);
        JLabel lupa = new JLabel("Lupa kata sandi?");
        lupa.setFont(UIConstants.FONT_SMALL);
        lupa.setForeground(UIConstants.COLOR_PRIMARY);
        lupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightPanel.add(lupa, gbc);

        // Tombol "Masuk"
        gbc.gridy = 7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.SOUTHEAST;

        RoundedButton loginButton = new RoundedButton("Masuk");

        // --- NAVIGASI (DIUBAH) ---
        loginButton.addActionListener(e -> {
            attemptLogin(); // Panggil method login
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        rightPanel.add(buttonPanel, gbc);

        return rightPanel;
    }

    // --- METHOD BARU ---
    private void attemptLogin() {
        String username = emailField.getText();
        String password = new String(passwordField.getPassword());

        User user = dataService.authenticateUser(username, password);

        if (user != null) {
            // Sukses Login
            SessionManager.getInstance().setCurrentUser(user);
            new DashboardFrame(user).setVisible(true);
            dispose();
        } else {
            // Gagal Login
            JOptionPane.showMessageDialog(this,
                    "Username atau password salah.",
                    "Login Gagal",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}