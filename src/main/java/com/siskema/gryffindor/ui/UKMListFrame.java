package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.User; // BARU
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class UKMListFrame extends ListFrame {

    public UKMListFrame(User user) { // <-- DIUBAH
        super("Daftar UKM", user); // <-- DIUBAH
    }

    // ... (Semua method lain SAMA, masih pakai data dummy) ...
    // ... (createCenterColumn, createCardContent, styleButton) ...
    
    @Override
    protected JPanel createCenterColumn() {
        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setBackground(UIConstants.COLOR_BACKGROUND);

        JLabel title = new JLabel("Daftar UKM");
        title.setFont(UIConstants.FONT_TITLE);
        center.add(title, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        searchPanel.setBackground(UIConstants.COLOR_BACKGROUND);
        RoundedTextField searchField = new RoundedTextField();
        searchField.setPreferredSize(new Dimension(350, 38));
        searchField.setText("  Cari UKM...");
        searchField.setForeground(UIConstants.COLOR_TEXT_LIGHT);
        searchPanel.add(searchField);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(UIConstants.COLOR_BACKGROUND);
        listPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        listPanel.add(createCardContent("DPM", "2010", "50 orang", "Aktif", true));
        listPanel.add(Box.createVerticalStrut(10));
        listPanel.add(createCardContent("Mapala", "1998", "100 orang", "Aktif", true));
        listPanel.add(Box.createVerticalStrut(10));
        listPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        center.add(contentPanel, BorderLayout.CENTER);

        return center;
    }
    
    @Override
    protected JPanel createCardContent(String title, String estDate, String participants, String status, boolean canRegister) {
        JPanel ukm = new JPanel(new GridBagLayout());
        ukm.setBackground(UIConstants.COLOR_CARD);
        ukm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 20, 15, 20)
        ));
        ukm.setMaximumSize(new Dimension(Integer.MAX_VALUE, ukm.getPreferredSize().height));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(UIConstants.COLOR_CARD);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIConstants.FONT_SUBTITLE);
        JLabel dateLabel = new JLabel("Est. " + estDate + " | Anggota: " + participants);
        dateLabel.setFont(UIConstants.FONT_SMALL);
        dateLabel.setForeground(UIConstants.COLOR_TEXT_LIGHT);
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createVerticalStrut(3));
        infoPanel.add(dateLabel);
        ukm.add(infoPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UIConstants.COLOR_CARD);
        JButton detailButton = new JButton("Lihat Detail");
        styleButton(detailButton, UIConstants.COLOR_BUTTON_GRAY, UIConstants.COLOR_TEXT_DARK);
        JButton daftarButton = new JButton("Gabung");
        styleButton(daftarButton, UIConstants.COLOR_PRIMARY, Color.WHITE);
        daftarButton.setEnabled(canRegister);
        buttonPanel.add(detailButton);
        buttonPanel.add(daftarButton);
        ukm.add(buttonPanel, gbc);

        return ukm;
    }
    
    private void styleButton(JButton button, Color background, Color foreground) {
        button.setFont(UIConstants.FONT_SMALL);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
    }
}