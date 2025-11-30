package Ui;

import Model.Mahasiswa;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DashboardFrame extends JFrame {
    private Mahasiswa mahasiswa;
    
    public DashboardFrame(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
        System.out.println("DashboardFrame dibuat untuk: " + mahasiswa.getNama());
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("SIA.Sat - siasat.uksw.edu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Main layout
        setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = createHeader();
        
        // Menu Panel
        JPanel menuPanel = createMenuPanel();
        
        // Content Panel
        JPanel contentPanel = createContentPanel();
        
        // Add to frame
        add(headerPanel, BorderLayout.NORTH);
        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        System.out.println("DashboardFrame UI siap");
    }
    
    private JPanel createHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 51, 102));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        
        JLabel lblTitle = new JLabel("SIA.Sat - siasat.uksw.edu");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblInfo = new JLabel(mahasiswa.getNim() + " - " + mahasiswa.getNama());
        lblInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblInfo.setForeground(Color.WHITE);
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblInfo, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        menuPanel.setPreferredSize(new Dimension(250, getHeight()));
        
        String[] menuItems = {"📝 KRS", "📊 KHS", "📑 Transkrip", "🕐 Jadwal", "👤 Data Pribadi", "🚪 Logout"};
        
        for (int i = 0; i < menuItems.length; i++) {
            JButton button = new JButton(menuItems[i]);
            button.setFont(new Font("Arial", Font.PLAIN, 14));
            button.setBackground(new Color(240, 240, 240));
            button.setFocusPainted(false);
            
            final int index = i;
            button.addActionListener(e -> handleMenuClick(index));
            
            menuPanel.add(button);
        }
        
        return menuPanel;
    }
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel lblWelcome = new JLabel(
            "<html><center><h1>Selamat Datang di SIASAT UKSW!</h1>" +
            "<p style='font-size:16px; margin-top:20px;'>" +
            "<strong>NIM:</strong> " + mahasiswa.getNim() + "<br>" +
            "<strong>Nama:</strong> " + mahasiswa.getNama() + "<br>" +
            "<strong>Program Studi:</strong> " + mahasiswa.getProgramStudi() + "<br>" +
            "<strong>Fakultas:</strong> " + mahasiswa.getFakultas() + "<br>" +
            "<strong>Angkatan:</strong> " + mahasiswa.getAngkatan() + "<br><br>" +
            "Silakan pilih menu di sebelah kiri untuk mengakses fitur-fitur SIASAT." +
            "</p></center></html>", 
            SwingConstants.CENTER
        );
        
        contentPanel.add(lblWelcome, BorderLayout.CENTER);
        return contentPanel;
    }
    
    private void handleMenuClick(int menuIndex) {
        System.out.println("Menu diklik: " + menuIndex);
        
        switch (menuIndex) {
            case 0: // KRS
                System.out.println("Membuka KRS Frame...");
                new KRSFrame(mahasiswa).setVisible(true);
                break;
                
            case 1: // KHS
                System.out.println("Membuka KHS Frame...");
                new KhsFrame(mahasiswa).setVisible(true);
                break;
                
            case 2: // Transkrip
                System.out.println("Membuka Transkrip Frame...");
                new TranskripFrame(mahasiswa).setVisible(true);
                break;
                
            case 3: // Jadwal
                System.out.println("Membuka Jadwal Frame...");
                new JadwalFrame(mahasiswa).setVisible(true);
                break;
                
            case 4: // Data Pribadi
                System.out.println("Membuka Data Pribadi Frame...");
                new DataPribadiFrame(mahasiswa).setVisible(true);
                break;
                
            case 5: // Logout
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Apakah Anda yakin ingin logout?", "Konfirmasi Logout", 
                    JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.out.println("Logout...");
                    new LoginFrame().setVisible(true);
                    this.dispose();
                }
                break;
        }
    }
}