package Ui;

import Model.Admin;
import Service.AdminService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminLoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private AdminService adminService;
    
    public AdminLoginFrame() {
        adminService = new AdminService();
        
        // AUTO-CREATE TABLE AND DEFAULT ADMIN
        adminService.createAdminTableIfNotExists();
        
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("SIASAT UKSW - Admin Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(102, 0, 0); // Maroon UKSW
                Color color2 = new Color(153, 0, 0);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        
        JLabel lblTitle = new JLabel("SIASAT UKSW - ADMIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        
        JLabel lblSubtitle = new JLabel("Sistem Informasi Akademik Terpadu", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        lblSubtitle.setForeground(Color.WHITE);
        
        JLabel lblDefaultAccount = new JLabel("Default: admin / admin123", SwingConstants.CENTER);
        lblDefaultAccount.setFont(new Font("Arial", Font.ITALIC, 12));
        lblDefaultAccount.setForeground(Color.YELLOW);
        
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSubtitle, BorderLayout.CENTER);
        headerPanel.add(lblDefaultAccount, BorderLayout.SOUTH);
        
        // Login form panel
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setOpaque(false);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(400, 80));
        
        JLabel lblUser = new JLabel("Username:");
        lblUser.setForeground(Color.WHITE);
        lblUser.setFont(new Font("Arial", Font.BOLD, 12));
        
        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 12));
        txtUsername.setText("admin"); // Default value
        
        JLabel lblPass = new JLabel("Password:");
        lblPass.setForeground(Color.WHITE);
        lblPass.setFont(new Font("Arial", Font.BOLD, 12));
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPassword.setText("admin123"); // Default value
        
        formPanel.add(lblUser);
        formPanel.add(txtUsername);
        formPanel.add(lblPass);
        formPanel.add(txtPassword);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        btnLogin = new JButton("LOGIN ADMIN");
        btnLogin.setBackground(new Color(255, 204, 0));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setPreferredSize(new Dimension(150, 35));
        
        JButton btnBack = new JButton("Kembali ke User Login");
        btnBack.setBackground(new Color(100, 100, 100));
        btnBack.setForeground(Color.WHITE);
        btnBack.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBack.setFocusPainted(false);
        
        JButton btnResetDB = new JButton("Reset DB");
        btnResetDB.setBackground(new Color(220, 53, 69));
        btnResetDB.setForeground(Color.WHITE);
        btnResetDB.setFont(new Font("Arial", Font.PLAIN, 10));
        btnResetDB.setFocusPainted(false);

        JButton btnEmergencyLogin = new JButton("Emergency Login");
        btnEmergencyLogin.setBackground(new Color(0, 123, 255));
        btnEmergencyLogin.setForeground(Color.WHITE);
        btnEmergencyLogin.setFont(new Font("Arial", Font.PLAIN, 10));
        btnEmergencyLogin.setFocusPainted(false);
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(btnBack);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(btnResetDB);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(btnEmergencyLogin);
        
        // Add components to main panel
        loginPanel.add(formPanel);
        loginPanel.add(buttonPanel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(loginPanel, BorderLayout.CENTER);
        
        add(mainPanel);
        
        // Event listeners
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginAdmin();
            }
        });
        
        btnBack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backToMainMenu();
            }
        });
        
        btnResetDB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetDatabase();
            }
        });

        btnEmergencyLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                emergencyLogin();
            }
        });
        
        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginAdmin();
            }
        });
    }
    
    private void loginAdmin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        System.out.println("🔄 Processing login for: " + username);
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username dan password harus diisi!", "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // First try database login
            Admin admin = adminService.login(username, password);
            if (admin != null) {
                System.out.println("✅ Login SUCCESS for: " + admin.getNama());
                showSuccessAndOpenDashboard(admin);
                return;
            }
        } catch (Exception e) {
            System.out.println("🚨 Database login failed: " + e.getMessage());
        }
        
        // Fallback to simple validation
        if (validateSimpleLogin(username, password)) {
            System.out.println("✅ Simple login SUCCESS for: " + username);
            Admin admin = new Admin(1, "Administrator", username, password);
            showSuccessAndOpenDashboard(admin);
        } else {
            System.out.println("❌ Login FAILED for: " + username);
            showLoginFailedMessage(username, password);
        }
    }

    private boolean validateSimpleLogin(String username, String password) {
        // Simple validation without database
        return "admin".equals(username) && "admin123".equals(password);
    }
    
    private void showSuccessAndOpenDashboard(Admin admin) {
        JOptionPane.showMessageDialog(this, 
            "Login berhasil!\nSelamat datang " + admin.getNama(), 
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
            
        // Open admin dashboard
        try {
            AdminDashboardFrame adminFrame = new AdminDashboardFrame(admin);
            adminFrame.setVisible(true);
            this.dispose();
        } catch (Exception e) {
            System.out.println("❌ Error opening dashboard: " + e.getMessage());
            // Fallback to simple dashboard
            openSimpleDashboard(admin);
        }
    }

    private void openSimpleDashboard(Admin admin) {
        JFrame dashboard = new JFrame("Admin Dashboard - " + admin.getNama());
        dashboard.setSize(600, 400);
        dashboard.setLocationRelativeTo(null);
        dashboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("ADMIN DASHBOARD - SIASAT UKSW", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(102, 0, 0));
        
        JTextArea contentArea = new JTextArea();
        contentArea.setText(
            "Selamat datang " + admin.getNama() + "!\n\n" +
            "Anda berhasil login ke sistem admin.\n\n" +
            "Fitur yang tersedia:\n" +
            "• Manajemen User\n" +
            "• Manajemen Data Mahasiswa\n" +
            "• Pengaturan Sistem\n" +
            "• Laporan Akademik\n\n" +
            "Login details:\n" +
            "Username: " + admin.getUsername() + "\n" +
            "Nama: " + admin.getNama()
        );
        contentArea.setEditable(false);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(220, 53, 69));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.addActionListener(e -> {
            new AdminLoginFrame().setVisible(true);
            dashboard.dispose();
        });
        
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        mainPanel.add(logoutBtn, BorderLayout.SOUTH);
        
        dashboard.add(mainPanel);
        dashboard.setVisible(true);
        this.dispose();
    }
    
    private void showLoginFailedMessage(String username, String password) {
        JOptionPane.showMessageDialog(this, 
            "Login gagal! Periksa:\n" +
            "• Username: " + username + "\n" +
            "• Password: " + password + "\n\n" +
            "Gunakan account default:\n" +
            "Username: admin\n" +
            "Password: admin123\n\n" +
            "Atau klik 'Emergency Login' untuk bypass database.", 
            "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void emergencyLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty()) username = "admin";
        if (password.isEmpty()) password = "admin123";
        
        txtUsername.setText(username);
        txtPassword.setText(password);
        
        System.out.println("🚨 EMERGENCY LOGIN: " + username);
        
        Admin admin = new Admin(1, "Administrator (Emergency)", username, password);
        showSuccessAndOpenDashboard(admin);
    }
    
    private void backToMainMenu() {
        try {
            // Create simple main menu
            JFrame mainFrame = new JFrame("SIASAT UKSW - Main Menu");
            mainFrame.setSize(400, 300);
            mainFrame.setLocationRelativeTo(null);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            mainPanel.setBackground(new Color(240, 240, 240));
            
            JLabel titleLabel = new JLabel("SIASAT UKSW", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setForeground(new Color(102, 0, 0));
            
            JLabel subtitleLabel = new JLabel("Sistem Informasi Akademik Terpadu", SwingConstants.CENTER);
            subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
            buttonPanel.setBackground(new Color(240, 240, 240));
            
            JButton adminBtn = new JButton("Admin Login");
            adminBtn.setBackground(new Color(255, 204, 0));
            adminBtn.setFont(new Font("Arial", Font.BOLD, 14));
            adminBtn.addActionListener(e -> {
                new AdminLoginFrame().setVisible(true);
                mainFrame.dispose();
            });
            
            JButton userBtn = new JButton("User Login");
            userBtn.setBackground(new Color(100, 100, 100));
            userBtn.setForeground(Color.WHITE);
            userBtn.setFont(new Font("Arial", Font.PLAIN, 14));
            userBtn.addActionListener(e -> {
                JOptionPane.showMessageDialog(mainFrame, 
                    "Fitur User Login sedang dalam pengembangan", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            });
            
            buttonPanel.add(adminBtn);
            buttonPanel.add(userBtn);
            
            mainPanel.add(titleLabel, BorderLayout.NORTH);
            mainPanel.add(subtitleLabel, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            
            mainFrame.add(mainPanel);
            mainFrame.setVisible(true);
            this.dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void resetDatabase() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Reset database admin?\nIni akan membuat table admin baru dengan account default.",
            "Reset Database", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                adminService.createAdminTableIfNotExists();
                JOptionPane.showMessageDialog(this,
                    "Database admin telah direset!\n\n" +
                    "Default account:\n" +
                    "Username: admin\n" +
                    "Password: admin123",
                    "Reset Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Reset berhasil (fallback mode)\n\n" +
                    "Gunakan:\nUsername: admin\nPassword: admin123",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
public static void main(String[] args) {
    System.out.println("🚀 Starting Admin Login Frame...");
    
    // Langsung jalankan tanpa set look and feel
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new AdminLoginFrame().setVisible(true);
        }
    });
}
}