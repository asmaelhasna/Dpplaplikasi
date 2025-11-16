package com.siskema.gryffindor.ui;

import com.siskema.gryffindor.model.User;
import javax.swing.*;
import java.awt.*;

public class ReportFrame extends ListFrame {

    public ReportFrame(User user) {
        super("Cetak Laporan", user);
    }

    @Override
    protected JPanel createCenterColumn() {
        JPanel center = new JPanel(new BorderLayout(0, 15));
        center.setBackground(UIConstants.COLOR_BACKGROUND);

        JLabel title = new JLabel("Cetak Laporan");
        title.setFont(UIConstants.FONT_TITLE);
        center.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        JLabel soon = new JLabel("Fitur cetak laporan akan diimplementasikan di sini.");
        soon.setFont(UIConstants.FONT_SUBTITLE);
        content.add(soon);
        
        center.add(content, BorderLayout.CENTER);

        return center;
    }

    // Overriding method abstract dari ListFrame, tapi tidak digunakan
    @Override
    protected JPanel createCardContent(String title, String date, String participants, String status, boolean canRegister) {
        return new JPanel(); 
    }
}