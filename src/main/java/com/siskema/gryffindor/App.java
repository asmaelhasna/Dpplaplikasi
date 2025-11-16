package com.siskema.gryffindor;

import javax.swing.SwingUtilities;
import javax.swing.UIManager; 
import com.siskema.gryffindor.ui.LoginFrame;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            new LoginFrame().setVisible(true);
        });
    }
}
