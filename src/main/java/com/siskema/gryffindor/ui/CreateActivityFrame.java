package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.Activity;
import com.siskema.gryffindor.model.User;
import com.siskema.gryffindor.service.DataService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CreateActivityFrame extends JFrame {

    private User currentUser;
    private DataService dataService;
    private Activity activityToEdit; // <-- BARU: Untuk menyimpan data activity yg diedit

    private RoundedTextField nameField = new RoundedTextField();
    private RoundedTextField dateField = new RoundedTextField();
    private RoundedTextField locationField = new RoundedTextField();
    private JTextArea descriptionArea = new JTextArea();
    private RoundedButton submitButton; // <-- BARU: Pindahkan ke scope class

    // Constructor 1: Untuk membuat kegiatan BARU
    public CreateActivityFrame(User user) {
        this(user, null); // Panggil constructor utama dengan activityToEdit = null
    }

    // Constructor 2: Untuk MENGEDIT kegiatan
    public CreateActivityFrame(User user, Activity activityToEdit) {
        this.currentUser = user;
        this.activityToEdit = activityToEdit; // <-- Simpan activity
        this.dataService = new DataService();

        // --- Logika UI ---
        boolean isEditing = (activityToEdit != null); // Cek apakah ini mode edit

        setTitle(isEditing ? "Siskema - Edit Kegiatan" : "Siskema - Buat Kegiatan Baru");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        getContentPane().setBackground(UIConstants.COLOR_BACKGROUND);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        JLabel title = new JLabel(isEditing ? "Edit Kegiatan" : "Buat Kegiatan Baru");
        title.setFont(UIConstants.FONT_TITLE);
        formPanel.add(title);
        formPanel.add(Box.createVerticalStrut(20));

        formPanel.add(createInputField("Nama Kegiatan", nameField));
        formPanel.add(createInputField("Tanggal (cth: 25 Des 2025)", dateField));
        formPanel.add(createInputField("Lokasi", locationField));

        formPanel.add(new JLabel("Deskripsi Kegiatan:") {{ setFont(UIConstants.FONT_NORMAL); }});
        formPanel.add(Box.createVerticalStrut(5));
        descriptionArea.setFont(UIConstants.FONT_NORMAL);
        descriptionArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setPreferredSize(new Dimension(400, 150));
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        formPanel.add(scrollPane);
        
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(Box.createVerticalGlue());
        
        // --- Tombol ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        JButton cancelButton = new JButton("Batal");
        cancelButton.addActionListener(e -> {
            new ManageActivityFrame(currentUser).setVisible(true);
            dispose();
        });
        
        // --- Ubah Teks Tombol ---
        submitButton = new RoundedButton(isEditing ? "Simpan Perubahan" : "Ajukan Kegiatan");
        submitButton.addActionListener(e -> submitActivity());
        
        buttonPanel.add(cancelButton);
        buttonPanel.add(submitButton);
        
        formPanel.add(buttonPanel);
        add(formPanel, BorderLayout.CENTER);

        // --- Jika Mode Edit, Isi Datanya ---
        if (isEditing) {
            nameField.setText(activityToEdit.getName());
            dateField.setText(activityToEdit.getDate());
            locationField.setText(activityToEdit.getLocation());
            descriptionArea.setText(activityToEdit.getDescription());
        }
    }

    private JPanel createInputField(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(UIConstants.FONT_NORMAL);
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        panel.add(field);
        panel.add(Box.createVerticalStrut(15));
        return panel;
    }

    // --- Logika Submit Diperbarui ---
    private void submitActivity() {
        String name = nameField.getText();
        String date = dateField.getText();
        String location = locationField.getText();
        String description = descriptionArea.getText();
        String organizer = currentUser.getOrganizationName();

        if (name.isEmpty() || date.isEmpty() || location.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field wajib diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (activityToEdit == null) {
            // --- LOGIKA CREATE BARU ---
            Activity newActivity = new Activity(name, organizer, date, location, description);
            dataService.addActivity(newActivity);
            JOptionPane.showMessageDialog(this, "Kegiatan berhasil diajukan dan menunggu persetujuan PKM.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            // --- LOGIKA UPDATE (EDIT) ---
            activityToEdit.setName(name);
            activityToEdit.setDate(date);
            activityToEdit.setLocation(location);
            activityToEdit.setDescription(description);
            // (Opsional) Jika diedit, mungkin harus menunggu persetujuan lagi
            // activityToEdit.setStatus(ActivityStatus.PENDING_APPROVAL); 
            
            dataService.updateActivity(activityToEdit);
            JOptionPane.showMessageDialog(this, "Kegiatan berhasil diperbarui.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }

        new ManageActivityFrame(currentUser).setVisible(true);
        dispose();
    }
}