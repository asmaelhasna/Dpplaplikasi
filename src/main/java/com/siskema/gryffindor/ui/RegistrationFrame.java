package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.User; // BARU
import com.siskema.gryffindor.model.UserRole; // BARU
import com.siskema.gryffindor.service.DataService; // BARU

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegistrationFrame extends JFrame {

    // --- BARU ---
    private DataService dataService;
    private RoundedTextField nimField = new RoundedTextField();
    private RoundedTextField nameField = new RoundedTextField();
    private RoundedTextField emailField = new RoundedTextField();
    private RoundedPasswordField passwordField = new RoundedPasswordField();
    // ... tambahkan field lain jika perlu ...

    public RegistrationFrame() {
        this.dataService = new DataService(); // <-- BARU

        setTitle("Siskema Gryffindor - Daftar Akun Baru");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(UIConstants.COLOR_BACKGROUND);

        JPanel root = new JPanel(new GridBagLayout());
        root.setOpaque(false);
        setContentPane(root);
        JPanel card = createCardPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(0, 0, 0, 40);
        gbc.gridx = 0;
        gbc.weightx = 0.35;
        card.add(createLeftPanel(), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.65;
        gbc.insets = new Insets(0, 0, 0, 0);
        JScrollPane scrollPane = new JScrollPane(createRightPanel());
        scrollPane.setPreferredSize(new Dimension(450, 600));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        card.add(scrollPane, gbc);
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
        JPanel formPanel = new JPanel();
        formPanel.setBackground(UIConstants.COLOR_CARD);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        // ... (Judul Halaman & Link Login SAMA) ...
        JLabel titleLabel = new JLabel("Daftar Akun Baru");
        titleLabel.setFont(UIConstants.FONT_TITLE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(titleLabel);
        JLabel loginLink = new JLabel("<html>Sudah punya akun? <a href='#'>Silahkan login</a></html>");
        loginLink.setFont(UIConstants.FONT_SMALL);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        formPanel.add(loginLink);
        formPanel.add(Box.createVerticalStrut(25));


        // --- Informasi Pribadi (DIUBAH) ---
        formPanel.add(createSubtitle("Informasi Pribadi"));
        formPanel.add(createInputField("NIM (sebagai username)", nimField)); // Diubah
        formPanel.add(createInputField("Nama Lengkap", nameField)); // Diubah
        formPanel.add(createInputField("Email", emailField)); // Diubah
        // (Hapus field yg tidak perlu atau tambahkan field baru di sini)
        formPanel.add(Box.createVerticalStrut(25));

        // --- Hapus Informasi Akademik (jika tidak disimpan) ---
        // formPanel.add(createSubtitle("Informasi Akademik")); ...

        // --- Password (DIUBAH) ---
        formPanel.add(createSubtitle("Password"));
        formPanel.add(createPasswordField("Password", passwordField)); // Diubah
        formPanel.add(Box.createVerticalStrut(40));

        // Tombol Daftar
        RoundedButton registerButton = new RoundedButton("Daftar");
        registerButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // --- NAVIGASI (DIUBAH) ---
        registerButton.addActionListener(e -> {
            registerUser(); // Panggil method registrasi
        });

        formPanel.add(registerButton);
        formPanel.add(Box.createVerticalStrut(20));

        return formPanel;
    }

    // --- METHOD BARU ---
    private void registerUser() {
        String nim = nimField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String pass = new String(passwordField.getPassword());

        if (nim.isEmpty() || name.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "NIM, Nama, dan Password wajib diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Buat user baru (default sebagai MAHASISWA)
        User newUser = new User(nim, pass, name, UserRole.MAHASISWA, null);
        
        try {
            dataService.addUser(newUser);
            JOptionPane.showMessageDialog(this, "Pendaftaran Berhasil! Silahkan Login.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            new LoginFrame().setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal mendaftar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ... (Helper methods createSubtitle, createInputField, createPasswordField SAMA) ...
    private JLabel createSubtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIConstants.FONT_SUBTITLE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createInputField(String labelText, RoundedTextField field) { // Diubah
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel label = new JLabel(labelText);
        label.setFont(UIConstants.FONT_NORMAL);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(Box.createVerticalStrut(15));
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        return panel;
    }

    private JPanel createPasswordField(String labelText, RoundedPasswordField field) { // Diubah
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel label = new JLabel(labelText);
        label.setFont(UIConstants.FONT_NORMAL);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, field.getPreferredSize().height));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(Box.createVerticalStrut(15));
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        return panel;
    }
}