package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.Activity; // BARU
import com.siskema.gryffindor.model.User; // BARU
import com.siskema.gryffindor.model.UserRole; // BARU
import com.siskema.gryffindor.service.DataService; // BARU

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ActivityDetailFrame extends JFrame {

    // --- BARU ---
    private User currentUser;
    private Activity currentActivity;
    private DataService dataService;
    private RoundedButton daftarButton;
    // --- END BARU ---

    public ActivityDetailFrame(User user, Activity activity) { // <-- DIUBAH
        this.currentUser = user;
        this.currentActivity = activity;
        this.dataService = new DataService();

        setTitle("Siskema Gryffindor - Detail Kegiatan");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        setResizable(true);

        getContentPane().setBackground(UIConstants.COLOR_BACKGROUND);
        setLayout(new BorderLayout());

        add(createTopBar(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    // Top Bar (Sama seperti ListFrame, tapi disesuaikan)
    private JComponent createTopBar() {
        // ... (Implementasi SAMA) ...
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(new EmptyBorder(10, 20, 10, 20));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        left.setBackground(Color.WHITE);
        JLabel logo = new JLabel("🛡️");
        logo.setFont(new Font("SansSerif", Font.PLAIN, 32));
        JLabel title = new JLabel("Siskema Gryffindor");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        left.add(logo);
        left.add(title);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        right.setBackground(Color.WHITE);
        JLabel profileName = new JLabel(currentUser.getFullName() + " 👤"); // <-- DIUBAH
        profileName.setFont(new Font("SansSerif", Font.PLAIN, 16));
        right.add(profileName);
        top.add(left, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);
        return top;
    }

    private JComponent createMainContent() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIConstants.COLOR_BACKGROUND);
        root.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- AMBIL DARI OBJEK ACTIVITY ---
        JLabel title = new JLabel(currentActivity.getName());
        title.setFont(UIConstants.FONT_TITLE);
        title.setForeground(UIConstants.COLOR_TEXT_DARK);
        root.add(title, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createDetailPanel(), createActionPanel()); // Panggil method yg diubah
        splitPane.setDividerLocation(850);
        splitPane.setResizeWeight(1.0); // <-- PENTING: Panel kiri (detail) yg meregang
        // ... (Style split pane SAMA) ...
        splitPane.setDividerSize(10);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setOpaque(false);
        
        root.add(splitPane, BorderLayout.CENTER);

        return root;
    }

    private JPanel createDetailPanel() {
        // ... (Struktur panel SAMA) ...
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 0, 0, 15));
        JPanel card = new JPanel();
        card.setBackground(UIConstants.COLOR_CARD);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- AMBIL DARI OBJEK ACTIVITY ---
        card.add(createInfoItem("Penyelenggara", currentActivity.getOrganizerName(), "👤"));
        card.add(Box.createVerticalStrut(15));
        card.add(createInfoItem("Waktu Pelaksanaan", currentActivity.getDate(), "⏱️"));
        card.add(Box.createVerticalStrut(15));
        card.add(createInfoItem("Lokasi", currentActivity.getLocation(), "📍"));
        card.add(Box.createVerticalStrut(25));
        
        JLabel descTitle = new JLabel("Deskripsi");
        descTitle.setFont(UIConstants.FONT_SUBTITLE);
        descTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(descTitle);
        card.add(Box.createVerticalStrut(10));

        JTextArea descArea = new JTextArea(currentActivity.getDescription());
        // ... (Styling JTextArea SAMA) ...
        descArea.setFont(UIConstants.FONT_NORMAL);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEditable(false);
        descArea.setOpaque(false);
        card.add(descArea);
        card.add(Box.createVerticalGlue());
        
        JScrollPane scrollPane = new JScrollPane(card);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    // ... (createInfoItem SAMA) ...
    private JPanel createInfoItem(String title, String value, String iconText) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        item.setOpaque(false);
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel icon = new JLabel(iconText);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        icon.setForeground(UIConstants.COLOR_PRIMARY);
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_NORMAL);
        titleLabel.setForeground(UIConstants.COLOR_TEXT_LIGHT);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(UIConstants.FONT_SUBTITLE);
        valueLabel.setForeground(UIConstants.COLOR_TEXT_DARK);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(valueLabel);
        item.add(icon);
        item.add(textPanel);
        return item;
    }


    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(15, 15, 0, 0));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UIConstants.COLOR_CARD);
        card.setBorder(new EmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        // --- LOGIKA TOMBOL (DIUBAH) ---

        // Hanya tampilkan tombol Daftar jika user adalah MAHASISWA
        if (currentUser.getRole() == UserRole.MAHASISWA) {
            daftarButton = new RoundedButton("Daftar Sekarang");
            daftarButton.setPreferredSize(new Dimension(200, 50));
            
            // Cek apakah sudah terdaftar
            if (currentActivity.getRegisteredParticipants().contains(currentUser.getUsername())) {
                daftarButton.setText("Anda Sudah Terdaftar");
                daftarButton.setEnabled(false);
                daftarButton.setBackground(Color.GRAY);
            } else {
                daftarButton.addActionListener(e -> registerToActivity());
            }
            card.add(daftarButton, gbc);
            gbc.gridy++;
        }

        // Tombol Kembali
        JButton kembaliButton = new JButton("Kembali");
        styleSecondaryButton(kembaliButton);
        kembaliButton.setPreferredSize(new Dimension(200, 50));

        // --- NAVIGASI KEMBALI (DIUBAH) ---
        kembaliButton.addActionListener(e -> {
            new ActivityListFrame(currentUser).setVisible(true); // Kirim user
            dispose();
        });

        card.add(kembaliButton, gbc);

        // ... (Container SAMA) ...
        JPanel buttonContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonContainer.setOpaque(false);
        buttonContainer.add(card);
        panel.add(buttonContainer, BorderLayout.NORTH);
        return panel;
    }

    // --- METHOD BARU ---
    private void registerToActivity() {
        try {
            currentActivity.addParticipant(currentUser.getUsername());
            dataService.updateActivity(currentActivity); // Simpan perubahan ke JSON
            
            JOptionPane.showMessageDialog(this, "Anda berhasil terdaftar!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            
            // Update UI
            daftarButton.setText("Anda Sudah Terdaftar");
            daftarButton.setEnabled(false);
            daftarButton.setBackground(Color.GRAY);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mendaftar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ... (styleSecondaryButton SAMA) ...
    private void styleSecondaryButton(JButton button) {
        button.setFont(UIConstants.FONT_SUBTITLE);
        button.setBackground(UIConstants.COLOR_BACKGROUND);
        button.setForeground(UIConstants.COLOR_TEXT_DARK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }
}