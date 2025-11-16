package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.User; // BARU
import com.siskema.gryffindor.model.UserRole; // BARU
import com.siskema.gryffindor.service.SessionManager; // BARU

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public abstract class ListFrame extends JFrame {

    protected User currentUser; // <-- BARU: Menyimpan user yang login

    public ListFrame(String title, User user) { // <-- DIUBAH
        this.currentUser = user; // <-- BARU

        setTitle("Siskema Gryffindor - " + title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));
        setResizable(true);

        getContentPane().setBackground(UIConstants.COLOR_BACKGROUND);
        setLayout(new BorderLayout());

        add(createTopBar(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
    }

    // Top Bar (Header)
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
        // Tampilkan nama user di Top Bar
        JLabel profileName = new JLabel(currentUser.getFullName() + " 👤"); // <-- DIUBAH
        profileName.setFont(new Font("SansSerif", Font.PLAIN, 16));
        right.add(profileName);
        top.add(left, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);
        return top;
    }

    private JComponent createMainContent() {
        // ... (Implementasi SAMA) ...
        JPanel root = new JPanel(new BorderLayout(20, 0));
        root.setBackground(UIConstants.COLOR_BACKGROUND);
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        JPanel rightColumn = createRightMenu(); // <-- Panggil method yg diubah
        root.add(rightColumn, BorderLayout.EAST);
        JPanel centerColumn = createCenterColumn();
        root.add(centerColumn, BorderLayout.CENTER);
        return root;
    }

    // Menu Sidebar Kanan (DIUBAH TOTAL)
    private JPanel createRightMenu() {
        JPanel menu = new JPanel();
        menu.setBackground(UIConstants.COLOR_PRIMARY);
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBorder(new EmptyBorder(20, 20, 20, 20));
        menu.setMinimumSize(new Dimension(180, 0));
        menu.setPreferredSize(new Dimension(200, 0));

        // --- ROLE BASED MENU ---
        menu.add(createMenuItem("🏠", "Beranda", this instanceof DashboardFrame, "Dashboard"));
        menu.add(Box.createVerticalStrut(10));

        if (currentUser.getRole() == UserRole.MAHASISWA) {
            menu.add(createMenuItem("🏫", "UKM", this instanceof UKMListFrame, "UKMList"));
            menu.add(Box.createVerticalStrut(10));
            menu.add(createMenuItem("📅", "Kegiatan", this instanceof ActivityListFrame, "ActivityList"));
            menu.add(Box.createVerticalStrut(10));
        }

        if (currentUser.getRole() == UserRole.PENYELENGGARA) {
            menu.add(createMenuItem("🛠️", "Kelola Kegiatan", this instanceof ManageActivityFrame, "ManageActivity"));
            menu.add(Box.createVerticalStrut(10));
        }

        if (currentUser.getRole() == UserRole.PKM) {
            menu.add(createMenuItem("✅", "Persetujuan", this instanceof ApprovalFrame, "Approval"));
            menu.add(Box.createVerticalStrut(10));
            menu.add(createMenuItem("📊", "Cetak Laporan", this instanceof ReportFrame, "Report"));
            menu.add(Box.createVerticalStrut(10));
        }

        menu.add(createMenuItem("🚪", "Keluar", false, "Logout"));
        menu.add(Box.createVerticalGlue());
        return menu;
    }

    // Item Menu dengan logika Navigasi (DIUBAH)
    private JComponent createMenuItem(String iconText, String label, boolean selected, String target) {
        // ... (Styling SAMA) ...
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        item.setOpaque(false);
        item.setBorder(new EmptyBorder(8, 5, 8, 5));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel icon = new JLabel(iconText);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 20));
        icon.setForeground(Color.WHITE);
        JLabel text = new JLabel(label);
        text.setFont(UIConstants.FONT_NORMAL);
        text.setForeground(Color.WHITE);
        item.add(icon);
        item.add(text);
        if (selected) {
            item.setBackground(UIConstants.COLOR_PRIMARY_DARK);
            item.setOpaque(true);
            item.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 4, 0, 0, Color.WHITE),
                    new EmptyBorder(8, 5, 8, 5)
            ));
        }

        // --- LOGIKA NAVIGASI (DIUBAH) ---
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Jangan lakukan navigasi jika sudah di halaman yang sama
                if (selected) return; 
                
                switch (target) {
                    case "Dashboard":
                        new DashboardFrame(currentUser).setVisible(true);
                        dispose();
                        break;
                    case "UKMList":
                        new UKMListFrame(currentUser).setVisible(true);
                        dispose();
                        break;
                    case "ActivityList":
                        new ActivityListFrame(currentUser).setVisible(true);
                        dispose();
                        break;
                    // --- TARGET BARU ---
                    case "ManageActivity":
                        new ManageActivityFrame(currentUser).setVisible(true);
                        dispose();
                        break;
                    case "Approval":
                        new ApprovalFrame(currentUser).setVisible(true);
                        dispose();
                        break;
                    case "Report":
                        new ReportFrame(currentUser).setVisible(true);
                        dispose();
                        break;
                    case "Logout":
                        SessionManager.getInstance().logout(); // Hapus sesi
                        new LoginFrame().setVisible(true);
                        dispose();
                        break;
                }
            }
        });

        return item;
    }

    // Abstract methods (SAMA)
    protected abstract JPanel createCenterColumn();
    protected abstract JPanel createCardContent(String title, String date, String participants, String status, boolean canRegister);
}