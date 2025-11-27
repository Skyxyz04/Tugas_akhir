package siasat.komponen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class HeaderPanel extends JPanel {
    private JLabel titleLabel;
    private JLabel logoLabel;
    private JButton backButton;
    private JPanel rightPanel;
    
    public HeaderPanel() {
        initComponents();
        setupUI();
    }
    
    private void initComponents() {
        titleLabel = new JLabel();
        logoLabel = new JLabel();
        backButton = new JButton("â† Kembali");
        rightPanel = new JPanel();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(0, 51, 102)); // Biru UKSW
        setPreferredSize(new Dimension(800, 80));
        setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        // Setup title
        titleLabel.setText("SISTEM INFORMASI AKADEMIK TERPADU (SIASAT)");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        // Setup logo
        logoLabel.setText("UNIVERSITAS KRISTEN SATYA WACANA");
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        // Setup back button (awalnya hidden)
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0, 102, 204));
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backButton.setVisible(false);
        
        // Setup right panel
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(0, 51, 102));
        rightPanel.add(logoLabel);
        
        add(titleLabel, BorderLayout.WEST);
        add(backButton, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
    
    // Public methods untuk kustomisasi
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
    
    public void setLogoText(String logoText) {
        logoLabel.setText(logoText);
    }
    
    public void setBackgroundColor(Color color) {
        setBackground(color);
        if (rightPanel != null) {
            rightPanel.setBackground(color);
        }
    }
    
    public void setTitleColor(Color color) {
        titleLabel.setForeground(color);
    }
    
    public void setLogoColor(Color color) {
        logoLabel.setForeground(color);
    }
    
    public void showBackButton(boolean show) {
        backButton.setVisible(show);
    }
    
    public void setBackButtonListener(ActionListener listener) {
        for (ActionListener al : backButton.getActionListeners()) {
            backButton.removeActionListener(al);
        }
        backButton.addActionListener(listener);
    }
    
    public void setBackButtonText(String text) {
        backButton.setText(text);
    }
    
    public void addRightComponent(Component component) {
        rightPanel.add(component);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    
    public void clearRightComponents() {
        rightPanel.removeAll();
        rightPanel.add(logoLabel);
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    
    public void setFontSize(int titleSize, int logoSize) {
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, titleSize));
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, logoSize));
    }
    
    public void setPadding(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
    
    // Method untuk update header berdasarkan user role
    public void updateForUserRole(String role, String userName) {
        switch (role) {
            case "MAHASISWA":
                setTitle("SIASAT - Dashboard Mahasiswa");
                setLogoText(userName != null ? userName : "Mahasiswa");
                break;
            case "DOSEN":
                setTitle("SIASAT - Dashboard Dosen");
                setLogoText(userName != null ? userName : "Dosen");
                break;
            case "ADMIN":
                setTitle("SIASAT - Dashboard Administrator");
                setLogoText(userName != null ? userName : "Administrator");
                break;
            default:
                setTitle("SISTEM INFORMASI AKADEMIK TERPADU (SIASAT)");
                setLogoText("UNIVERSITAS KRISTEN SATYA WACANA");
        }
    }
    
    // Method untuk menampilkan informasi waktu
    public void showDateTimeInfo() {
        JLabel timeLabel = new JLabel();
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        // Update waktu secara real-time
        Timer timer = new Timer(1000, e -> {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            timeLabel.setText(sdf.format(new java.util.Date()));
        });
        timer.start();
        
        addRightComponent(timeLabel);
    }
    
    // Method untuk menampilkan status koneksi
    public void showConnectionStatus(boolean connected) {
        JLabel statusLabel = new JLabel();
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        if (connected) {
            statusLabel.setText("ðŸŸ¢ Terhubung");
            statusLabel.setForeground(new Color(144, 238, 144)); // Light green
        } else {
            statusLabel.setText("ðŸ”´ Offline");
            statusLabel.setForeground(new Color(255, 182, 193)); // Light red
        }
        
        addRightComponent(statusLabel);
    }
}