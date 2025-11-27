package siasat;

import siasat.model.User;
import siasat.model.Mahasiswa;
import siasat.model.Dosen;
import siasat.database.MahasiswaDAO;
import siasat.database.DosenDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DashboardUI extends JFrame {
    private User currentUser;
    private Mahasiswa mahasiswaData;
    private Dosen dosenData;
    
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JLabel welcomeLabel;
    private JTextArea infoArea;
    
    private MahasiswaDAO mahasiswaDAO;
    private DosenDAO dosenDAO;
    
    public DashboardUI(User user, Mahasiswa mahasiswa, Dosen dosen) {
        this.currentUser = user;
        this.mahasiswaData = mahasiswa;
        this.dosenData = dosen;
        
        // Initialize DAOs
        this.mahasiswaDAO = new MahasiswaDAO();
        this.dosenDAO = new DosenDAO();
        
        initializeDashboard();
    }
    
    private void initializeDashboard() {
        setTitle("Dashboard SIASAT - " + getDisplayName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 700));
        
        setupDashboardUI();
        setVisible(true);
    }
    
    private void setupDashboardUI() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        setupHeader();
        setupNavigation();
        setupContentArea();
        
        add(mainPanel);
        
        // Show home content by default
        showContent("Beranda");
    }
    
    private void setupHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 51, 102));
        headerPanel.setPreferredSize(new Dimension(1200, 70));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        // Left side - Title
        String roleText = currentUser.isMahasiswa() ? "Mahasiswa" : 
                         currentUser.isDosen() ? "Dosen" : "Administrator";
        JLabel titleLabel = new JLabel("SIASAT UKSW - Dashboard " + roleText);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        // Right side - User info and logout
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setBackground(new Color(0, 51, 102));
        
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        
        JLabel userInfo = new JLabel("<html><b>" + getDisplayName() + "</b><br>" + 
                                     currentUser.getUsername() + "</html>");
        userInfo.setForeground(Color.WHITE);
        userInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutButton.setPreferredSize(new Dimension(90, 35));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        logoutButton.addActionListener(e -> logout());
        
        // Hover effect
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(200, 35, 51));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutButton.setBackground(new Color(220, 53, 69));
            }
        });
        
        rightPanel.add(userIcon);
        rightPanel.add(userInfo);
        rightPanel.add(Box.createHorizontalStrut(10));
        rightPanel.add(logoutButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }
    
    private void setupNavigation() {
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.Y_AXIS));
        navPanel.setBackground(new Color(248, 249, 250));
        navPanel.setPreferredSize(new Dimension(250, 0));
        navPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        
        // Add menu items based on role
        addMenuItem(navPanel, "🏠 Beranda", true);
        addMenuItem(navPanel, "📊 Profil Akademik", false);
        
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        addSeparator(navPanel);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        if (currentUser.isMahasiswa()) {
            addMahasiswaMenus(navPanel);
        } else if (currentUser.isDosen()) {
            addDosenMenus(navPanel);
        } else {
            addAdminMenus(navPanel);
        }
        
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        addSeparator(navPanel);
        navPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        addMenuItem(navPanel, "📢 Pengumuman", false);
        addMenuItem(navPanel, "⚙️ Pengaturan", false);
        
        navPanel.add(Box.createVerticalGlue());
        
        mainPanel.add(navPanel, BorderLayout.WEST);
    }
    
    private void addMahasiswaMenus(JPanel panel) {
        addMenuItem(panel, "📝 KRS Online", false);
        addMenuItem(panel, "📋 Kartu Rencana Studi", false);
        addMenuItem(panel, "📈 Nilai & Transkrip", false);
        addMenuItem(panel, "💳 Keuangan", false);
        addMenuItem(panel, "📅 Jadwal Kuliah", false);
    }
    
    private void addDosenMenus(JPanel panel) {
        addMenuItem(panel, "📚 Mata Kuliah Diampu", false);
        addMenuItem(panel, "📝 Input Nilai", false);
        addMenuItem(panel, "👥 Daftar Mahasiswa", false);
        addMenuItem(panel, "📅 Jadwal Mengajar", false);
    }
    
    private void addAdminMenus(JPanel panel) {
        addMenuItem(panel, "👥 Manajemen User", false);
        addMenuItem(panel, "📚 Manajemen Matkul", false);
        addMenuItem(panel, "🏫 Manajemen Prodi", false);
        addMenuItem(panel, "📊 Laporan Akademik", false);
    }
    
    private void addMenuItem(JPanel panel, String text, boolean isActive) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 45));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        if (isActive) {
            button.setBackground(new Color(0, 123, 255));
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(52, 58, 64));
        }
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    button.setBackground(new Color(233, 236, 239));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isActive) {
                    button.setBackground(Color.WHITE);
                }
            }
        });
        
        button.addActionListener(e -> {
            String menuName = text.replaceAll("[^a-zA-Z\\s]", "").trim();
            showContent(menuName);
            updateActiveMenu(panel, button);
        });
        
        panel.add(button);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    private void updateActiveMenu(JPanel panel, JButton activeButton) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                if (btn == activeButton) {
                    btn.setBackground(new Color(0, 123, 255));
                    btn.setForeground(Color.WHITE);
                } else {
                    btn.setBackground(Color.WHITE);
                    btn.setForeground(new Color(52, 58, 64));
                }
            }
        }
    }
    
    private void addSeparator(JPanel panel) {
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(220, 1));
        separator.setForeground(new Color(222, 226, 230));
        panel.add(separator);
    }
    
    private void setupContentArea() {
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        
        welcomeLabel = new JLabel("Selamat Datang", JLabel.LEFT);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(33, 37, 41));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        contentPanel.add(welcomeLabel, BorderLayout.NORTH);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    private void showContent(String menu) {
        // Clear current content (except welcome label)
        Component[] components = contentPanel.getComponents();
        for (Component comp : components) {
            if (comp != welcomeLabel) {
                contentPanel.remove(comp);
            }
        }
        
        welcomeLabel.setText(menu);
        
        JPanel content = null;
        
        switch (menu) {
            case "Beranda":
                content = createBerandaContent();
                break;
            case "Profil Akademik":
                content = createProfilContent();
                break;
            case "Nilai Transkrip":
                content = createNilaiContent();
                break;
            case "Mata Kuliah Diampu":
                content = createMataKuliahDosenContent();
                break;
            case "Daftar Mahasiswa":
                content = createDaftarMahasiswaContent();
                break;
            default:
                content = createDefaultContent(menu);
        }
        
        if (content != null) {
            contentPanel.add(content, BorderLayout.CENTER);
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createBerandaContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        // Welcome card
        JPanel welcomeCard = createCard("Informasi Pengguna", getWelcomeInfo());
        panel.add(welcomeCard);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Announcements card
        JPanel announcementCard = createCard("Pengumuman Terbaru", getAnnouncements());
        panel.add(announcementCard);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createProfilContent() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        String profileInfo = getProfileInfo();
        JPanel profileCard = createCard("Profil Lengkap", profileInfo);
        panel.add(profileCard);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createNilaiContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        if (mahasiswaData != null) {
            // Create table for grades
            String[] columns = {"Kode", "Mata Kuliah", "SKS", "Nilai", "Grade", "Semester"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            
            // Sample data (you would fetch this from database)
            model.addRow(new Object[]{"TIF101", "Algoritma dan Pemrograman", "4", "85.5", "A", "1"});
            model.addRow(new Object[]{"TIF102", "Matematika Diskrit", "3", "78.0", "B+", "1"});
            model.addRow(new Object[]{"TIF103", "Pengantar TI", "3", "90.0", "A", "1"});
            model.addRow(new Object[]{"TIF104", "Bahasa Inggris", "2", "82.0", "A-", "1"});
            
            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            table.getTableHeader().setBackground(new Color(0, 123, 255));
            table.getTableHeader().setForeground(Color.WHITE);
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230)));
            
            // IPK Panel
            JPanel ipkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            ipkPanel.setBackground(new Color(232, 244, 253));
            ipkPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
            
            JLabel ipkLabel = new JLabel("📊 IPK Kumulatif: 3.65 | Total SKS: 12");
            ipkLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            ipkLabel.setForeground(new Color(0, 51, 102));
            ipkPanel.add(ipkLabel);
            
            panel.add(ipkPanel, BorderLayout.NORTH);
            panel.add(scrollPane, BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    private JPanel createMataKuliahDosenContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        if (dosenData != null) {
            String[] columns = {"Kode", "Nama Mata Kuliah", "SKS", "Semester", "Prodi"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            
            // Fetch from database
            List<siasat.model.MataKuliah> matkulList = dosenDAO.getMataKuliahByDosen(dosenData.getId());
            
            if (matkulList.isEmpty()) {
                model.addRow(new Object[]{"TIF101", "Algoritma dan Pemrograman", "4", "1", "Teknik Informatika"});
                model.addRow(new Object[]{"TIF201", "Struktur Data", "4", "2", "Teknik Informatika"});
            } else {
                for (siasat.model.MataKuliah mk : matkulList) {
                    model.addRow(new Object[]{
                        mk.getKode(),
                        mk.getNama(),
                        mk.getSks(),
                        mk.getSemester(),
                        mk.getProgramStudi()
                    });
                }
            }
            
            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            table.getTableHeader().setBackground(new Color(0, 123, 255));
            table.getTableHeader().setForeground(Color.WHITE);
            
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230)));
            
            panel.add(scrollPane, BorderLayout.CENTER);
        }
        
        return panel;
    }
    
    private JPanel createDaftarMahasiswaContent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        String[] columns = {"NIM", "Nama", "Program Studi", "Angkatan", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        // Fetch from database
        List<Mahasiswa> mahasiswaList = mahasiswaDAO.getAllMahasiswa();
        
        for (Mahasiswa mhs : mahasiswaList) {
            model.addRow(new Object[]{
                mhs.getNim(),
                mhs.getNama(),
                mhs.getProgramStudi(),
                mhs.getAngkatan(),
                "Aktif"
            });
        }
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(0, 123, 255));
        table.getTableHeader().setForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 226, 230)));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createDefaultContent(String menu) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        
        String message = "Fitur \"" + menu + "\" sedang dalam pengembangan.\n\n" +
                        "Akan segera tersedia dalam update mendatang.";
        
        JPanel card = createCard("Informasi", message);
        panel.add(card);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createCard(String title, String content) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 51, 102));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(Color.WHITE);
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(textArea, BorderLayout.CENTER);
        
        return card;
    }
    
    private String getWelcomeInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Selamat datang, ").append(getDisplayName()).append("!\n\n");
        info.append("Username: ").append(currentUser.getUsername()).append("\n");
        info.append("Role: ").append(currentUser.getRole()).append("\n\n");
        
        if (mahasiswaData != null) {
            info.append("NIM: ").append(mahasiswaData.getNim()).append("\n");
            info.append("Program Studi: ").append(mahasiswaData.getProgramStudi()).append("\n");
            info.append("Angkatan: ").append(mahasiswaData.getAngkatan()).append("\n");
        } else if (dosenData != null) {
            info.append("NIDN: ").append(dosenData.getNidn()).append("\n");
            info.append("Program Studi: ").append(dosenData.getProgramStudi()).append("\n");
            info.append("Jabatan: ").append(dosenData.getJabatan()).append("\n");
        }
        
        return info.toString();
    }
    
    private String getAnnouncements() {
        return "• Pendaftaran wisuda periode Juni 2024 dibuka\n" +
               "• Libur semester genap: 15 Juni - 15 Juli 2024\n" +
               "• Perkuliahan semester ganjil dimulai 1 Agustus 2024\n" +
               "• Batas pembayaran UKT tanggal 10 setiap bulan";
    }
    
    private String getProfileInfo() {
        StringBuilder info = new StringBuilder();
        
        if (mahasiswaData != null) {
            info.append("PROFIL AKADEMIK MAHASISWA\n\n");
            info.append("Nama: ").append(mahasiswaData.getNama()).append("\n");
            info.append("NIM: ").append(mahasiswaData.getNim()).append("\n");
            info.append("Program Studi: ").append(mahasiswaData.getProgramStudi()).append("\n");
            info.append("Fakultas: ").append(mahasiswaData.getFakultas()).append("\n");
            info.append("Angkatan: ").append(mahasiswaData.getAngkatan()).append("\n");
            info.append("Status: Aktif\n");
            info.append("Email: ").append(mahasiswaData.getEmail()).append("\n");
        } else if (dosenData != null) {
            info.append("PROFIL DOSEN\n\n");
            info.append("Nama: ").append(dosenData.getNama()).append("\n");
            info.append("NIDN: ").append(dosenData.getNidn()).append("\n");
            info.append("Program Studi: ").append(dosenData.getProgramStudi()).append("\n");
            info.append("Fakultas: ").append(dosenData.getFakultas()).append("\n");
            info.append("Jabatan: ").append(dosenData.getJabatan()).append("\n");
            info.append("Status: Aktif\n");
        } else {
            info.append("PROFIL ADMINISTRATOR\n\n");
            info.append("Username: ").append(currentUser.getUsername()).append("\n");
            info.append("Role: Administrator\n");
            info.append("Hak Akses: Full Access\n");
        }
        
        return info.toString();
    }
    
    private String getDisplayName() {
        if (mahasiswaData != null) {
            return mahasiswaData.getNama();
        } else if (dosenData != null) {
            return dosenData.getNama();
        } else {
            return currentUser.getUsername();
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            System.out.println("🚪 User logout: " + currentUser.getUsername());
            SiasatLoginUI loginUI = new SiasatLoginUI();
            loginUI.setVisible(true);
            this.dispose();
        }
    }
}