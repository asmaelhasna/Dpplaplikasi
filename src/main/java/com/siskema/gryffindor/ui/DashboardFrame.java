package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.User; // BARU
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DashboardFrame extends ListFrame {

    public DashboardFrame(User user) { // <-- DIUBAH
        super("Beranda", user); // <-- DIUBAH: Kirim user ke parent
    }

    // ... (createCardContent dummy SAMA) ...
    @Override
    protected JPanel createCardContent(String title, String date, String participants, String status, boolean canRegister) {
        return new JPanel();
    }

    @Override
    protected JPanel createCenterColumn() {
        // ... (Implementasi SAMA, tapi kita akan ubah createLeftColumn) ...
        JPanel root = new JPanel(new BorderLayout(20, 0));
        root.setBackground(UIConstants.COLOR_BACKGROUND);
        JPanel leftColumn = createLeftColumn(); // Panggil method yg diubah
        leftColumn.setMinimumSize(new Dimension(280, 0));
        leftColumn.setPreferredSize(new Dimension(300, 0));
        root.add(leftColumn, BorderLayout.WEST);
        JPanel centerColumn = createCenterDashboardContent();
        root.add(centerColumn, BorderLayout.CENTER);
        return root;
    }

    // Bagian Kiri Dashboard (DIUBAH)
    private JPanel createLeftColumn() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(UIConstants.COLOR_BACKGROUND);

        JPanel profileCard = new JPanel();
        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
        profileCard.setBackground(UIConstants.COLOR_CARD);
        profileCard.setBorder(new EmptyBorder(20, 20, 20, 20));
        profileCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel avatar = new JLabel("🧑‍💻"); // Bisa diganti icon berdasarkan role
        avatar.setFont(new Font("SansSerif", Font.PLAIN, 64));
        avatar.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileCard.add(avatar);
        profileCard.add(Box.createVerticalStrut(10));

        // --- AMBIL DARI CURRENTUSER ---
        JLabel name = new JLabel(currentUser.getFullName(), SwingConstants.CENTER);
        name.setFont(UIConstants.FONT_SUBTITLE);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileCard.add(name);

        JLabel nim = new JLabel(currentUser.getUsername(), SwingConstants.CENTER);
        nim.setFont(UIConstants.FONT_NORMAL);
        nim.setForeground(UIConstants.COLOR_TEXT_LIGHT);
        nim.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileCard.add(nim);
        
        JLabel role = new JLabel(currentUser.getRole().toString(), SwingConstants.CENTER);
        role.setFont(UIConstants.FONT_NORMAL);
        role.setForeground(UIConstants.COLOR_PRIMARY);
        role.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileCard.add(role);
        // --- END ---

        profileCard.add(Box.createVerticalStrut(20));

        // Statistik (Contoh)
        profileCard.add(createStatItem("Kegiatan Diikuti", "12", UIConstants.COLOR_PRIMARY));
        profileCard.add(Box.createVerticalStrut(5));
        profileCard.add(createStatItem("Total Poin", "450", new Color(0, 150, 0)));

        leftPanel.add(profileCard);
        leftPanel.add(Box.createVerticalGlue());
        return leftPanel;
    }

    // ... (createStatItem SAMA) ...
    private JPanel createStatItem(String label, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(240, 240, 240)));
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(UIConstants.FONT_NORMAL);
        panel.add(labelComp, BorderLayout.WEST);
        JLabel valueComp = new JLabel(value);
        valueComp.setFont(UIConstants.FONT_SUBTITLE);
        valueComp.setForeground(color);
        panel.add(valueComp, BorderLayout.EAST);
        return panel;
    }

    // ... (createCenterDashboardContent SAMA, masih pakai data dummy) ...
    private JPanel createCenterDashboardContent() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(UIConstants.COLOR_BACKGROUND);
        JLabel title = new JLabel("Kegiatan Terbaru");
        title.setFont(UIConstants.FONT_TITLE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(title);
        centerPanel.add(Box.createVerticalStrut(15));
        JPanel list = new JPanel(new GridLayout(0, 1, 0, 10));
        list.setOpaque(false);
        list.add(createSimpleActivityCard("Webinar UI/UX Design", "12 Nov 2025", "Fakultas Teknik"));
        list.add(createSimpleActivityCard("Lomba Karya Tulis Ilmiah", "20 Nov 2025", "BEM"));
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        centerPanel.add(scrollPane);
        centerPanel.add(Box.createVerticalGlue());
        return centerPanel;
    }
    
    // ... (createSimpleActivityCard SAMA) ...
    private JPanel createSimpleActivityCard(String title, String date, String organization) {
        JPanel card = new JPanel(new BorderLayout(15, 0));
        card.setBackground(UIConstants.COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));
        JLabel icon = new JLabel("📅");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 32));
        card.add(icon, BorderLayout.WEST);
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setOpaque(false);
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_SUBTITLE);
        JLabel subtitleLabel = new JLabel(date + " | Oleh: " + organization);
        subtitleLabel.setFont(UIConstants.FONT_NORMAL);
        subtitleLabel.setForeground(UIConstants.COLOR_TEXT_LIGHT);
        info.add(titleLabel);
        info.add(subtitleLabel);
        card.add(info, BorderLayout.CENTER);
        JButton detailButton = new JButton("Detail");
        detailButton.setFont(UIConstants.FONT_NORMAL);
        detailButton.setBackground(UIConstants.COLOR_BUTTON_GRAY);
        detailButton.setFocusPainted(false);
        detailButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        detailButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.add(detailButton, BorderLayout.EAST);
        return card;
    }
}