package com.siskema.gryffindor.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ParticipantListDialog extends JDialog {

    // --- CONSTRUCTOR DIUBAH ---
    public ParticipantListDialog(Frame parent, String activityName, List<String> participantDetails) {
        super(parent, "Pendaftar: " + activityName, true);

        setSize(400, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIConstants.COLOR_BACKGROUND);

        // 'participantDetails' adalah list yang sudah diformat (NIM - Nama)
        if (participantDetails == null || participantDetails.isEmpty()) {
            JLabel emptyLabel = new JLabel("Belum ada pendaftar.", SwingConstants.CENTER);
            emptyLabel.setFont(UIConstants.FONT_SUBTITLE);
            add(emptyLabel, BorderLayout.CENTER);
        } else {
            // Gunakan JList untuk menampilkan daftar
            DefaultListModel<String> listModel = new DefaultListModel<>();
            
            // 'detail' sekarang adalah string "NIM - NAMA"
            for (String detail : participantDetails) {
                listModel.addElement(detail);
            }
            
            JList<String> participantList = new JList<>(listModel);
            participantList.setFont(UIConstants.FONT_NORMAL);
            participantList.setBorder(new EmptyBorder(10, 10, 10, 10));
            
            JScrollPane scrollPane = new JScrollPane(participantList);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            add(scrollPane, BorderLayout.CENTER);
        }

        // --- Tombol Tutup (Tetap SAMA) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton closeButton = new JButton("Tutup");
        closeButton.addActionListener(e -> dispose()); // 'dispose()' menutup dialog
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }
}