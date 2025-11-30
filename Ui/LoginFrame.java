package Ui;

import Model.Mahasiswa;
import Model.Admin;
import Service.AuthService;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private AuthService authService;
    
    public LoginFrame() {
        System.out.println("=== LOGIN FRAME DIBUAT ===");
        authService = new AuthService();
        initializeUI();
    }
    
    // Di LoginFrame, tambahkan debug:
    private void loginUser() {
    String username = txtUsername.getText().trim();
    String password = new String(txtPassword.getPassword());
    
    System.out.println("=== USER LOGIN ATTEMPT ===");
    System.out.println("Input - Username: " + username + ", Password: " + password);
    
    // ... kode login lainnya
}
    
    private void initializeUI() {
        setTitle("SIASAT UKSW - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel lblTitle = new JLabel("SIASAT UKSW LOGIN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 51, 102));
        
        // Form
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 5, 10));
        formPanel.add(new JLabel("Username/NIM:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);
        
        formPanel.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);
        
        // Buttons
        JButton btnLoginMhs = new JButton("Login Mahasiswa");
        JButton btnLoginAdmin = new JButton("Login Admin");
        
        btnLoginMhs.setBackground(new Color(0, 102, 204));
        btnLoginMhs.setForeground(Color.WHITE);
        
        btnLoginAdmin.setBackground(new Color(102, 0, 0));
        btnLoginAdmin.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.add(btnLoginMhs);
        buttonPanel.add(btnLoginAdmin);
        
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(panel);
        
        // Event Listeners
        btnLoginMhs.addActionListener(e -> loginMahasiswa());
        btnLoginAdmin.addActionListener(e -> loginAdmin());
        txtPassword.addActionListener(e -> loginMahasiswa());
        
        System.out.println("LoginFrame UI siap");
    }
    
    private void loginMahasiswa() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        System.out.println("Login mahasiswa attempt: " + username);
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap isi username dan password!");
            return;
        }
        
        try {
            Mahasiswa mhs = authService.loginMahasiswa(username, password);
            if (mhs != null) {
                System.out.println("Login BERHASIL - Mahasiswa: " + mhs.getNama());
                JOptionPane.showMessageDialog(this, 
                    "Login berhasil! Selamat datang " + mhs.getNama(), 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                // BUKA DASHBOARD MAHASISWA
                openMahasiswaDashboard(mhs);
            } else {
                System.out.println("Login GAGAL - username/password salah");
                JOptionPane.showMessageDialog(this, 
                    "Login gagal! Periksa NIM dan password.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("ERROR login mahasiswa: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    
    private void loginAdmin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        System.out.println("Login admin attempt: " + username);
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap isi username dan password!");
            return;
        }
        
        try {
            Admin admin = authService.loginAdmin(username, password);
            if (admin != null) {
                System.out.println("Login BERHASIL - Admin: " + admin.getNama());
                JOptionPane.showMessageDialog(this, 
                    "Login berhasil! Selamat datang " + admin.getNama(), 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                
                // BUKA DASHBOARD ADMIN
                openAdminDashboard(admin);
            } else {
                System.out.println("Login GAGAL - username/password salah");
                JOptionPane.showMessageDialog(this, 
                    "Login gagal! Periksa username dan password.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("ERROR login admin: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    
    private void openMahasiswaDashboard(Mahasiswa mhs) {
        System.out.println("Membuka Dashboard Mahasiswa...");
        try {
            // TUTUP LOGIN FRAME DULU
            this.dispose();
            System.out.println("LoginFrame ditutup");
            
            // BUKA DASHBOARD
            DashboardFrame dashboard = new DashboardFrame(mhs);
            dashboard.setVisible(true);
            System.out.println("DashboardFrame DIBUKA untuk: " + mhs.getNama());
            
        } catch (Exception e) {
            System.err.println("ERROR buka DashboardFrame: " + e.getMessage());
            e.printStackTrace();
            // Jika error, buka kembali login
            new LoginFrame().setVisible(true);
        }
    }
    
    private void openAdminDashboard(Admin admin) {
        System.out.println("Membuka Admin Dashboard...");
        try {
            // TUTUP LOGIN FRAME DULU
            this.dispose();
            System.out.println("LoginFrame ditutup");
            
            // BUKA ADMIN DASHBOARD
            AdminDashboardFrame adminDashboard = new AdminDashboardFrame(admin);
            adminDashboard.setVisible(true);
            System.out.println("AdminDashboardFrame DIBUKA untuk: " + admin.getNama());
            
        } catch (Exception e) {
            System.err.println("ERROR buka AdminDashboardFrame: " + e.getMessage());
            e.printStackTrace();
            // Jika error, buka kembali login
            new LoginFrame().setVisible(true);
        }
    }
}