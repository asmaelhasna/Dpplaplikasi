package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.Activity;
import com.siskema.gryffindor.model.User;
import com.siskema.gryffindor.model.ActivityStatus;
import com.siskema.gryffindor.service.DataService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.ArrayList; // <-- Import BARU

public class ManageActivityFrame extends ListFrame {

    private DataService dataService;
    private JPanel listPanel;

    public ManageActivityFrame(User user) {
        super("Kelola Kegiatan", user);
        this.dataService = new DataService();
        loadActivities();
    }

    @Override
    protected JPanel createCenterColumn() {
        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setBackground(UIConstants.COLOR_BACKGROUND);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel title = new JLabel("Kelola Kegiatan Saya");
        title.setFont(UIConstants.FONT_TITLE);
        topPanel.add(title, BorderLayout.WEST);

        RoundedButton createButton = new RoundedButton("Buat Kegiatan Baru");
        createButton.addActionListener(e -> {
            new CreateActivityFrame(currentUser).setVisible(true);
            dispose();
        });
        topPanel.add(createButton, BorderLayout.EAST);
        
        center.add(topPanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(UIConstants.COLOR_BACKGROUND);
        listPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        center.add(scrollPane, BorderLayout.CENTER);

        return center;
    }

    private void loadActivities() {
        listPanel.removeAll();
        List<Activity> myActivities = dataService.getActivitiesByOrganizer(currentUser.getOrganizationName());

        if (myActivities.isEmpty()) {
            JLabel emptyLabel = new JLabel("Anda belum membuat kegiatan apapun.");
            emptyLabel.setFont(UIConstants.FONT_NORMAL);
            listPanel.add(emptyLabel);
        } else {
            for (Activity activity : myActivities) {
                listPanel.add(createManageCard(activity));
                listPanel.add(Box.createVerticalStrut(10));
            }
        }
        listPanel.add(Box.createVerticalGlue());
        listPanel.revalidate();
        listPanel.repaint();
    }

    @Override
    protected JPanel createCardContent(String title, String date, String participants, String status, boolean canRegister) {
        // Tidak terpakai
        return new JPanel();
    }

    protected JPanel createManageCard(Activity activity) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(UIConstants.COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));

        // --- Info Panel ---
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(activity.getName());
        titleLabel.setFont(UIConstants.FONT_SUBTITLE);
        infoPanel.add(titleLabel);

        JLabel dateLabel = new JLabel(activity.getDate() + " | Pendaftar: " + activity.getRegisteredParticipants().size() + " orang");
        dateLabel.setFont(UIConstants.FONT_SMALL);
        infoPanel.add(dateLabel);
        
        JLabel statusLabel = new JLabel("Status: " + activity.getStatus().toString());
        statusLabel.setFont(UIConstants.FONT_SMALL);
        
        if (activity.getStatus() == ActivityStatus.PENDING_APPROVAL) {
            statusLabel.setForeground(Color.ORANGE);
        } else if (activity.getStatus() == ActivityStatus.APPROVED) {
            statusLabel.setForeground(new Color(0, 150, 0));
        } else if (activity.getStatus() == ActivityStatus.REJECTED) {
            statusLabel.setForeground(Color.RED);
        } else {
            statusLabel.setForeground(UIConstants.COLOR_TEXT_LIGHT);
        }
        infoPanel.add(statusLabel);
        card.add(infoPanel, BorderLayout.CENTER);

        // --- Button Panel (ActionListener DIUBAH) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton editButton = new JButton("Edit");
        editButton.addActionListener(e -> {
            new CreateActivityFrame(currentUser, activity).setVisible(true);
            dispose();
        });
        
        JButton viewParticipantsButton = new JButton("Lihat Pendaftar");
        
        // --- INI ADALAH LOGIKA BARU ---
        viewParticipantsButton.addActionListener(e -> {
            // 1. Dapatkan daftar NIM pendaftar
            List<String> participantNims = activity.getRegisteredParticipants();
            
            // 2. Siapkan list baru untuk detail (NIM - Nama)
            List<String> participantDetails = new ArrayList<>();
            
            for (String nim : participantNims) {
                // 3. Cari user di database berdasarkan NIM
                User participantUser = dataService.getUserByUsername(nim);
                
                if (participantUser != null) {
                    // 4. Jika ketemu, format stringnya
                    participantDetails.add(participantUser.getUsername() + " - " + participantUser.getFullName());
                } else {
                    // 4b. Jika tidak ketemu (data anomali)
                    participantDetails.add(nim + " - (Nama Tidak Ditemukan)");
                }
            }

            // 5. Kirim list yang sudah diformat ke Dialog
            ParticipantListDialog dialog = new ParticipantListDialog(this, activity.getName(), participantDetails);
            dialog.setVisible(true);
        });
        // --- AKHIR LOGIKA BARU ---

        buttonPanel.add(editButton);
        buttonPanel.add(viewParticipantsButton);
        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }
}