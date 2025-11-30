package sistemsiasat;

import Ui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== START APLIKASI SIASAT ===");
        
        try {
            SwingUtilities.invokeLater(() -> {
                System.out.println("Membuat LoginFrame...");
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                System.out.println("LoginFrame ditampilkan");
            });
        } catch (Exception e) {
            System.err.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}