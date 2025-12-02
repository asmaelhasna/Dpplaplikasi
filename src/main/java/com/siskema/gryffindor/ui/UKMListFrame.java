package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.Activity;
import com.siskema.gryffindor.model.User; 
import com.siskema.gryffindor.service.DataService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class UKMListFrame extends ListFrame {

    private DataService dataService;
    private JPanel listPanel;

    public UKMListFrame(User user) { 
        super("Kegiatan Saya", user);
        this.dataService = new DataService();
    }

    @Override
    protected JPanel createCenterColumn() {
        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setBackground(UIConstants.COLOR_BACKGROUND);

        JLabel title = new JLabel("Kegiatan Saya");
        title.setFont(UIConstants.FONT_TITLE);
        center.add(title, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        searchPanel.setBackground(UIConstants.COLOR_BACKGROUND);
        RoundedTextField searchField = new RoundedTextField();
        searchField.setPreferredSize(new Dimension(350, 38));
        searchField.setText("  Cari Kegiatan...");
        searchField.setForeground(UIConstants.COLOR_TEXT_LIGHT);
        searchPanel.add(searchField);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(UIConstants.COLOR_BACKGROUND);
        listPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        
        center.add(contentPanel, BorderLayout.CENTER);
        
        // Load activities after UI initialization
        loadUserActivities();

        return center;
    }

    private void loadUserActivities() {
        listPanel.removeAll();
        List<Activity> allActivities = dataService.getAllActivities();
        
        String organizationName = currentUser.getOrganizationName();
        System.out.println("UKM Organization: " + organizationName);
        System.out.println("Total activities: " + allActivities.size());
        
        // Filter aktivitas yang dibuat oleh UKM ini
        List<Activity> userActivities = allActivities.stream()
                .filter(a -> {
                    boolean matches = a.getOrganizerName() != null && 
                                    a.getOrganizerName().equals(organizationName);
                    System.out.println("Activity: " + a.getName() + " | Organizer: " + a.getOrganizerName() + " | Matches: " + matches);
                    return matches;
                })
                .toList();

        System.out.println("Filtered activities: " + userActivities.size());

        if (userActivities.isEmpty()) {
            JLabel emptyLabel = new JLabel("Anda belum membuat kegiatan.");
            emptyLabel.setFont(UIConstants.FONT_NORMAL);
            listPanel.add(emptyLabel);
        } else {
            for (Activity activity : userActivities) {
                listPanel.add(createActivityCard(activity));
                listPanel.add(Box.createVerticalStrut(10));
            }
        }
        listPanel.add(Box.createVerticalGlue());
        listPanel.revalidate();
        listPanel.repaint();
    }

    protected JPanel createActivityCard(Activity activity) {
        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(UIConstants.COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, card.getPreferredSize().height));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(UIConstants.COLOR_CARD);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(activity.getName());
        titleLabel.setFont(UIConstants.FONT_SUBTITLE);
        infoPanel.add(titleLabel);

        JLabel dateLabel = new JLabel(activity.getDate() + " | " + activity.getLocation());
        dateLabel.setFont(UIConstants.FONT_SMALL);
        dateLabel.setForeground(UIConstants.COLOR_TEXT_LIGHT);
        infoPanel.add(dateLabel);

        card.add(infoPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(UIConstants.COLOR_CARD);

        JButton detailButton = new JButton("Lihat Detail");
        styleButton(detailButton, UIConstants.COLOR_BUTTON_GRAY, UIConstants.COLOR_TEXT_DARK);

        detailButton.addActionListener(e -> {
            new ActivityDetailFrame(currentUser, activity).setVisible(true);
            dispose();
        });

        buttonPanel.add(detailButton);
        card.add(buttonPanel, gbc);

        return card;
    }
    
    @Override
    protected JPanel createCardContent(String title, String date, String participants, String status, boolean canRegister) {
        return new JPanel();
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