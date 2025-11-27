package siasat.komponen;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FooterPanel extends JPanel {
    private JLabel copyrightLabel;
    private JLabel versionLabel;
    private JLabel statusLabel;
    private JLabel timeLabel;
    private Timer timer;
    
    public FooterPanel() {
        initComponents();
        setupUI();
        setupTimer();
    }
    
    private void initComponents() {
        copyrightLabel = new JLabel();
        versionLabel = new JLabel();
        statusLabel = new JLabel();
        timeLabel = new JLabel();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240));
        setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        setPreferredSize(new Dimension(800, 50));
        
        // Left panel untuk copyright
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(new Color(240, 240, 240));
        
        copyrightLabel.setText("Â© 2024 Universitas Kristen Satya Wacana - Hak Cipta Dilindungi Undang-Undang");
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        copyrightLabel.setForeground(Color.DARK_GRAY);
        
        leftPanel.add(copyrightLabel);
        
        // Center panel untuk status
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setBackground(new Color(240, 240, 240));
        
        statusLabel.setText("Sistem SIASAT - Status: Online");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        statusLabel.setForeground(new Color(0, 128, 0)); // Green for online
        
        centerPanel.add(statusLabel);
        
        // Right panel untuk version dan time
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(new Color(240, 240, 240));
        
        versionLabel.setText("v1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        versionLabel.setForeground(Color.GRAY);
        
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        timeLabel.setForeground(Color.GRAY);
        
        rightPanel.add(versionLabel);
        rightPanel.add(new JLabel(" | "));
        rightPanel.add(timeLabel);
        
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);
    }
    
    private void setupTimer() {
        timer = new Timer(1000, e -> updateTime());
        timer.start();
        updateTime(); // Initial call
    }
    
    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        timeLabel.setText(sdf.format(new Date()));
    }
    
    // Public methods untuk kustomisasi
    public void setCopyrightText(String text) {
        copyrightLabel.setText(text);
    }
    
    public void setVersion(String version) {
        versionLabel.setText(version);
    }
    
    public void setStatus(String status, boolean isOnline) {
        statusLabel.setText("Sistem SIASAT - Status: " + status);
        if (isOnline) {
            statusLabel.setForeground(new Color(0, 128, 0)); // Green
        } else {
            statusLabel.setForeground(new Color(255, 0, 0)); // Red
        }
    }
    
    public void setBackgroundColor(Color color) {
        setBackground(color);
        // Update semua child panels
        for (Component comp : getComponents()) {
            if (comp instanceof JPanel) {
                ((JPanel) comp).setBackground(color);
            }
        }
    }
    
    public void setTextColor(Color color) {
        copyrightLabel.setForeground(color);
        versionLabel.setForeground(color);
        statusLabel.setForeground(color);
        timeLabel.setForeground(color);
    }
    
    public void setFontSize(int copyrightSize, int statusSize, int versionSize) {
        copyrightLabel.setFont(new Font("Segoe UI", Font.PLAIN, copyrightSize));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, statusSize));
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, versionSize));
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, versionSize));
    }
    
    public void setPadding(int top, int left, int bottom, int right) {
        setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
    
    public void showTime(boolean show) {
        timeLabel.setVisible(show);
    }
    
    public void showVersion(boolean show) {
        versionLabel.setVisible(show);
    }
    
    public void showStatus(boolean show) {
        statusLabel.setVisible(show);
    }
    
    // Method untuk update status koneksi database
    public void updateDatabaseStatus(boolean connected) {
        if (connected) {
            setStatus("Database Terhubung", true);
        } else {
            setStatus("Database Terputus", false);
        }
    }
    
    // Method untuk menampilkan pesan sementara
    public void showTemporaryMessage(String message, int duration) {
        String originalStatus = statusLabel.getText();
        Color originalColor = statusLabel.getForeground();
        
        statusLabel.setText(message);
        statusLabel.setForeground(new Color(255, 140, 0)); // Orange for temporary messages
        
        Timer messageTimer = new Timer(duration, e -> {
            statusLabel.setText(originalStatus);
            statusLabel.setForeground(originalColor);
        });
        messageTimer.setRepeats(false);
        messageTimer.start();
    }
    
    // Method untuk menampilkan progress (jika diperlukan)
    public void showProgress(boolean show) {
        if (show) {
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setPreferredSize(new Dimension(100, 10));
            
            // Replace center panel dengan progress bar
            removeAll();
            
            JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            leftPanel.setBackground(getBackground());
            leftPanel.add(copyrightLabel);
            
            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            centerPanel.setBackground(getBackground());
            centerPanel.add(progressBar);
            
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            rightPanel.setBackground(getBackground());
            rightPanel.add(versionLabel);
            rightPanel.add(new JLabel(" | "));
            rightPanel.add(timeLabel);
            
            add(leftPanel, BorderLayout.WEST);
            add(centerPanel, BorderLayout.CENTER);
            add(rightPanel, BorderLayout.EAST);
            
            revalidate();
            repaint();
        } else {
            // Kembalikan ke tampilan normal
            removeAll();
            setupUI();
            revalidate();
            repaint();
        }
    }
    
    // Cleanup method
    public void cleanup() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    }
}