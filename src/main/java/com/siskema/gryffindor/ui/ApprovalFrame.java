package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.Activity;
import com.siskema.gryffindor.model.ActivityStatus;
import com.siskema.gryffindor.model.User;
import com.siskema.gryffindor.service.DataService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ApprovalFrame extends ListFrame {

    private DataService dataService;
    private JPanel listPanel;

    public ApprovalFrame(User user) {
        super("Persetujuan Kegiatan", user);
        this.dataService = new DataService();
        loadPendingActivities();
    }

    @Override
    protected JPanel createCenterColumn() {
        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setBackground(UIConstants.COLOR_BACKGROUND);

        JLabel title = new JLabel("Persetujuan Kegiatan");
        title.setFont(UIConstants.FONT_TITLE);
        center.add(title, BorderLayout.NORTH);

        // --- List Panel ---
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

    private void loadPendingActivities() {
        listPanel.removeAll();
        List<Activity> pendingActivities = dataService.getPendingActivities();

        if (pendingActivities.isEmpty()) {
            JLabel emptyLabel = new JLabel("Tidak ada kegiatan yang menunggu persetujuan.");
            emptyLabel.setFont(UIConstants.FONT_NORMAL);
            listPanel.add(emptyLabel);
        } else {
            for (Activity activity : pendingActivities) {
                listPanel.add(createApprovalCard(activity));
                listPanel.add(Box.createVerticalStrut(10));
            }
        }
        listPanel.add(Box.createVerticalGlue());
        listPanel.revalidate();
        listPanel.repaint();
    }
    
    protected JPanel createApprovalCard(Activity activity) {
        JPanel card = new JPanel(new BorderLayout(20, 0));
        card.setBackground(UIConstants.COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));

        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(activity.getName());
        titleLabel.setFont(UIConstants.FONT_SUBTITLE);
        infoPanel.add(titleLabel);

        JLabel organizerLabel = new JLabel("Oleh: " + activity.getOrganizerName());
        organizerLabel.setFont(UIConstants.FONT_SMALL);
        infoPanel.add(organizerLabel);
        
        JLabel dateLabel = new JLabel("Tanggal: " + activity.getDate());
        dateLabel.setFont(UIConstants.FONT_SMALL);
        infoPanel.add(dateLabel);

        card.add(infoPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton rejectButton = new JButton("Tolak");
        rejectButton.setBackground(UIConstants.COLOR_PRIMARY);
        rejectButton.setForeground(Color.WHITE);
        rejectButton.setOpaque(true); // <-- PERBAIKAN 1
        rejectButton.setFocusPainted(false); // <-- PERBAIKAN 2
        rejectButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20)); // <-- Styling
        rejectButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // <-- Styling
        rejectButton.addActionListener(e -> {
            activity.setStatus(ActivityStatus.REJECTED);
            dataService.updateActivity(activity);
            loadPendingActivities();
        });
        
        JButton approveButton = new JButton("Setujui");
        approveButton.setBackground(new Color(0, 150, 0)); // Hijau
        approveButton.setForeground(Color.WHITE);
        approveButton.setOpaque(true); // <-- PERBAIKAN 3
        approveButton.setFocusPainted(false); // <-- PERBAIKAN 4
        approveButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20)); // <-- Styling
        approveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // <-- Styling
        approveButton.addActionListener(e -> {
            activity.setStatus(ActivityStatus.APPROVED);
            dataService.updateActivity(activity);
            loadPendingActivities();
        });

        buttonPanel.add(rejectButton);
        buttonPanel.add(approveButton);

        card.add(buttonPanel, BorderLayout.EAST);

        return card;
    }

    @Override
    protected JPanel createCardContent(String title, String date, String participants, String status, boolean canRegister) {
        return new JPanel();
    }
}