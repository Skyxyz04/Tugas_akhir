package Ui;

import Model.Admin;
import Service.AdminService;
import Service.MahasiswaService;
import Service.DosenService;
import Service.MataKuliahService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboardFrame extends JFrame {
    private Admin admin;
    private AdminService adminService;
    private MahasiswaService mahasiswaService;
    private DosenService dosenService;
    private MataKuliahService mataKuliahService;
    
    private JTabbedPane tabbedPane;
    private JLabel lblWelcome;
    private JLabel lblStats;
    
    public AdminDashboardFrame(Admin admin) {
        this.admin = admin;
        this.adminService = new AdminService();
        this.mahasiswaService = new MahasiswaService();
        this.dosenService = new DosenService();
        this.mataKuliahService = new MataKuliahService();
        
        initializeUI();
        loadStatistics();
        
        System.out.println("✅ AdminDashboardFrame created for: " + admin.getNama());
    }
    
    private void initializeUI() {
        setTitle("Admin Dashboard - SIASAT UKSW");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Content area dengan tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Tambahkan tab-tab
        tabbedPane.addTab("🏠 Dashboard", createDashboardTab());
        tabbedPane.addTab("👨‍🎓 Manajemen Mahasiswa", createMahasiswaTab());
        tabbedPane.addTab("👨‍🏫 Manajemen Dosen", createDosenTab());
        tabbedPane.addTab("📚 Manajemen Mata Kuliah", createMataKuliahTab());
        tabbedPane.addTab("📊 Input Nilai", createInputNilaiTab());
        tabbedPane.addTab("⚙️ Pengaturan", createSettingsTab());
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(102, 0, 0)); // Maroon UKSW
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 80));
        
        // Info admin
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(new Color(102, 0, 0));
        
        lblWelcome = new JLabel("Selamat Datang, " + admin.getNama());
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        
        JLabel lblRole = new JLabel("Role: " + admin.getRole() + " | SIASAT UKSW");
        lblRole.setForeground(Color.WHITE);
        lblRole.setFont(new Font("Arial", Font.PLAIN, 12));
        
        infoPanel.add(lblWelcome);
        infoPanel.add(lblRole);
        
        // Stats panel
        lblStats = new JLabel("Memuat statistik...");
        lblStats.setForeground(Color.YELLOW);
        lblStats.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(new Color(102, 0, 0));
        
        JButton btnRefresh = new JButton("🔄 Refresh");
        JButton btnInputNilai = new JButton("📊 Input Nilai");
        JButton btnLogout = new JButton("🚪 Logout");
        
        // Style buttons
        btnRefresh.setBackground(new Color(255, 204, 0));
        btnInputNilai.setBackground(new Color(40, 167, 69));
        btnLogout.setBackground(new Color(220, 53, 69));
        
        btnRefresh.setForeground(Color.BLACK);
        btnInputNilai.setForeground(Color.WHITE);
        btnLogout.setForeground(Color.WHITE);
        
        btnRefresh.setFocusPainted(false);
        btnInputNilai.setFocusPainted(false);
        btnLogout.setFocusPainted(false);
        
        // Add action listeners
        btnRefresh.addActionListener(e -> {
            loadStatistics();
            JOptionPane.showMessageDialog(this, "Data diperbarui!");
        });
        
        btnInputNilai.addActionListener(e -> {
            new InputNilaiFrame(admin).setVisible(true);
        });
        
        btnLogout.addActionListener(e -> {
            logout();
        });
        
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnInputNilai);
        buttonPanel.add(btnLogout);
        
        headerPanel.add(infoPanel, BorderLayout.WEST);
        headerPanel.add(lblStats, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createDashboardTab() {
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dashboardPanel.setBackground(Color.WHITE);
        
        // Statistics cards
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Create stat cards (akan diisi oleh loadStatistics)
        JPanel cardMahasiswa = createStatCard("👨‍🎓 Total Mahasiswa", "0", new Color(41, 128, 185));
        JPanel cardDosen = createStatCard("👨‍🏫 Total Dosen", "0", new Color(39, 174, 96));
        JPanel cardMataKuliah = createStatCard("📚 Mata Kuliah", "0", new Color(142, 68, 173));
        JPanel cardMahasiswaAktif = createStatCard("✅ Mahasiswa Aktif", "0", new Color(230, 126, 34));
        JPanel cardDosenAktif = createStatCard("✅ Dosen Aktif", "0", new Color(231, 76, 60));
        JPanel cardAdmin = createStatCard("⚙️ Admin", "1", new Color(52, 73, 94));
        
        statsPanel.add(cardMahasiswa);
        statsPanel.add(cardDosen);
        statsPanel.add(cardMataKuliah);
        statsPanel.add(cardMahasiswaAktif);
        statsPanel.add(cardDosenAktif);
        statsPanel.add(cardAdmin);
        
        // Quick actions
        JPanel actionsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        actionsPanel.setBorder(BorderFactory.createTitledBorder("Aksi Cepat"));
        
        JButton btnManageMhs = createActionButton("👨‍🎓 Kelola Mahasiswa", new Color(41, 128, 185));
        JButton btnManageDosen = createActionButton("👨‍🏫 Kelola Dosen", new Color(39, 174, 96));
        JButton btnInputNilai = createActionButton("📊 Input Nilai", new Color(142, 68, 173));
        JButton btnManageMK = createActionButton("📚 Kelola MK", new Color(230, 126, 34));
        
        btnManageMhs.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        btnManageDosen.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        btnInputNilai.addActionListener(e -> new InputNilaiFrame(admin).setVisible(true));
        btnManageMK.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        
        actionsPanel.add(btnManageMhs);
        actionsPanel.add(btnManageDosen);
        actionsPanel.add(btnInputNilai);
        actionsPanel.add(btnManageMK);
        
        // Recent activity
        JPanel activityPanel = new JPanel(new BorderLayout());
        activityPanel.setBorder(BorderFactory.createTitledBorder("Aktivitas Terbaru"));
        
        JTextArea activityArea = new JTextArea();
        activityArea.setEditable(false);
        activityArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        activityArea.setText(
            "• " + java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + " - Login admin\n" +
            "• Sistem SIASAT UKSW siap digunakan\n" +
            "• Selamat datang di dashboard administrator\n" +
            "• Gunakan menu di atas untuk mengelola data"
        );
        
        JScrollPane scrollPane = new JScrollPane(activityArea);
        activityPanel.add(scrollPane, BorderLayout.CENTER);
        
        dashboardPanel.add(statsPanel, BorderLayout.NORTH);
        dashboardPanel.add(actionsPanel, BorderLayout.CENTER);
        dashboardPanel.add(activityPanel, BorderLayout.SOUTH);
        
        return dashboardPanel;
    }
    
    private JPanel createMahasiswaTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("<html><center><h1>Manajemen Mahasiswa</h1>" +
            "<p>Fitur untuk mengelola data mahasiswa</p>" +
            "<p>Fitur: Tambah, Edit, Hapus, Lihat data mahasiswa</p>" +
            "<br><p style='color: blue;'>Klik tombol di bawah untuk membuka manajemen mahasiswa</p></center></html>", 
            SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton btnOpen = new JButton("Buka Manajemen Mahasiswa");
        btnOpen.setBackground(new Color(41, 128, 185));
        btnOpen.setForeground(Color.WHITE);
        btnOpen.setFont(new Font("Arial", Font.BOLD, 14));
        btnOpen.setPreferredSize(new Dimension(300, 50));
        
        btnOpen.addActionListener(e -> {
            new ManajemenMahasiswaFrame(admin).setVisible(true);
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnOpen);
        
        panel.add(label, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createDosenTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("<html><center><h1>Manajemen Dosen</h1>" +
            "<p>Fitur untuk mengelola data dosen</p>" +
            "<p>Fitur: Tambah, Edit, Hapus, Lihat data dosen</p>" +
            "<br><p style='color: blue;'>Klik tombol di bawah untuk membuka manajemen dosen</p></center></html>", 
            SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton btnOpen = new JButton("Buka Manajemen Dosen");
        btnOpen.setBackground(new Color(39, 174, 96));
        btnOpen.setForeground(Color.WHITE);
        btnOpen.setFont(new Font("Arial", Font.BOLD, 14));
        btnOpen.setPreferredSize(new Dimension(300, 50));
        
        btnOpen.addActionListener(e -> {
            new ManajemenDosenFrame(admin).setVisible(true);
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnOpen);
        
        panel.add(label, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createMataKuliahTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("<html><center><h1>Manajemen Mata Kuliah</h1>" +
            "<p>Fitur untuk mengelola data mata kuliah</p>" +
            "<p>Fitur: Tambah, Edit, Hapus mata kuliah</p>" +
            "<br><p style='color: blue;'>Klik tombol di bawah untuk membuka manajemen mata kuliah</p></center></html>", 
            SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton btnOpen = new JButton("Buka Manajemen Mata Kuliah");
        btnOpen.setBackground(new Color(142, 68, 173));
        btnOpen.setForeground(Color.WHITE);
        btnOpen.setFont(new Font("Arial", Font.BOLD, 14));
        btnOpen.setPreferredSize(new Dimension(300, 50));
        
        btnOpen.addActionListener(e -> {
            new ManajemenMataKuliahFrame(admin).setVisible(true);
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnOpen);
        
        panel.add(label, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createInputNilaiTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("<html><center><h1>Manajemen Nilai</h1>" +
            "<p>Fitur untuk mengelola nilai mahasiswa</p>" +
            "<p>Fitur: Input nilai, lihat daftar nilai</p></center></html>", 
            SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        
        JButton btnInputNilai = new JButton("Input Nilai Baru");
        JButton btnLihatNilai = new JButton("Lihat Daftar Nilai");
        
        btnInputNilai.setBackground(new Color(40, 167, 69));
        btnLihatNilai.setBackground(new Color(0, 123, 255));
        btnInputNilai.setForeground(Color.WHITE);
        btnLihatNilai.setForeground(Color.WHITE);
        btnInputNilai.setFont(new Font("Arial", Font.BOLD, 14));
        btnLihatNilai.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnInputNilai.addActionListener(e -> new InputNilaiFrame(admin).setVisible(true));
        btnLihatNilai.addActionListener(e -> new ManajemenNilaiFrame(admin).setVisible(true));
        
        buttonPanel.add(btnInputNilai);
        buttonPanel.add(btnLihatNilai);
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSettingsTab() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("<html><center><h1>Pengaturan Sistem</h1>" +
            "<p>Fitur pengaturan dan konfigurasi sistem</p>" +
            "<br><p style='color: orange;'>Fitur dalam pengembangan...</p></center></html>", 
            SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 24));
        lblValue.setForeground(color);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);
        
        return card;
    }
    
    private JButton createActionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }
    
    private void loadStatistics() {
        // Untuk sementara, kita gunakan data dummy
        // Nanti bisa diintegrasikan dengan service yang sesungguhnya
        
        int totalMahasiswa = 125;
        int totalDosen = 25;
        int totalMataKuliah = 45;
        int mahasiswaAktif = 118;
        int dosenAktif = 22;
        
        lblStats.setText("Statistik: " + totalMahasiswa + " Mahasiswa • " + totalDosen + " Dosen • " + totalMataKuliah + " Mata Kuliah");
        
        // Update stat cards
        Component[] statsComponents = ((JPanel)tabbedPane.getComponentAt(0)).getComponents();
        if (statsComponents.length > 0 && statsComponents[0] instanceof JPanel) {
            JPanel statsPanel = (JPanel) statsComponents[0];
            Component[] cards = statsPanel.getComponents();
            
            if (cards.length >= 6) {
                updateCardValue((JPanel)cards[0], String.valueOf(totalMahasiswa));
                updateCardValue((JPanel)cards[1], String.valueOf(totalDosen));
                updateCardValue((JPanel)cards[2], String.valueOf(totalMataKuliah));
                updateCardValue((JPanel)cards[3], String.valueOf(mahasiswaAktif));
                updateCardValue((JPanel)cards[4], String.valueOf(dosenAktif));
                updateCardValue((JPanel)cards[5], "1");
            }
        }
        
        System.out.println("📊 Statistics loaded");
    }
    
    private void updateCardValue(JPanel card, String value) {
        for (Component comp : card.getComponents()) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getFont().getSize() == 24) { // Ini label value
                    label.setText(value);
                    break;
                }
            }
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Apakah Anda yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("👋 Admin logging out: " + admin.getNama());
            new AdminLoginFrame().setVisible(true);
            dispose();
        }
    }
    
    // Method untuk menampilkan frame
    public void display() {
        setVisible(true);
        System.out.println("🎯 AdminDashboardFrame displayed");
    }
}