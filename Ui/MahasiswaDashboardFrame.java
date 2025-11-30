package Ui;

import Model.Mahasiswa;
import Service.KRSService;
import Model.KRS;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import Ui.PembayaranMahasiswaFrame;
import Ui.LoginFrame;
import java.util.List;

public class MahasiswaDashboardFrame extends JFrame {
    private Mahasiswa mahasiswa;
    private KRSService krsService;
    
    private JLabel lblWelcome, lblNim, lblProdi, lblTotalSKS;
    private JTable tblKRS;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbSemester;
    
    public MahasiswaDashboardFrame(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
        this.krsService = new KRSService();
        initializeUI();
        loadKRSData();
    }
    
    private void initializeUI() {
        setTitle("Dashboard Mahasiswa - SIASAT UKSW");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Menu Panel - BARU DITAMBAHKAN
        JPanel menuPanel = createMenuPanel();
        
        // KRS Panel
        JPanel krsPanel = createKRSPanel();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.WEST); // BARU DITAMBAHKAN
        mainPanel.add(krsPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(102, 0, 0));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(new Color(102, 0, 0));
        
        lblWelcome = new JLabel("Selamat Datang, " + mahasiswa.getNama());
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        
        JPanel detailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        detailPanel.setBackground(new Color(102, 0, 0));
        
        lblNim = new JLabel("NIM: " + mahasiswa.getNim());
        lblProdi = new JLabel("Program Studi: " + mahasiswa.getProgramStudi());
        lblTotalSKS = new JLabel("Total SKS: 0");
        
        lblNim.setForeground(Color.WHITE);
        lblProdi.setForeground(Color.WHITE);
        lblTotalSKS.setForeground(Color.YELLOW);
        
        detailPanel.add(lblNim);
        detailPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        detailPanel.add(lblProdi);
        detailPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        detailPanel.add(lblTotalSKS);
        
        infoPanel.add(lblWelcome);
        infoPanel.add(detailPanel);
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(e -> logout());
        
        headerPanel.add(infoPanel, BorderLayout.WEST);
        headerPanel.add(btnLogout, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    // BARU DITAMBAHKAN: Menu Panel untuk navigasi
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createTitledBorder("Menu"));
        menuPanel.setPreferredSize(new Dimension(200, 0));
        menuPanel.setBackground(Color.WHITE);
        
        // Button Dashboard
        JButton btnDashboard = createMenuButton("📊 Dashboard", new Color(41, 128, 185));
        btnDashboard.addActionListener(e -> {
            // Already in dashboard, just refresh
            loadKRSData();
        });
        
        // Button KRS
        JButton btnKRS = createMenuButton("📚 KRS", new Color(52, 152, 219));
        btnKRS.addActionListener(e -> {
            // Focus ke bagian KRS yang sudah ada
            JOptionPane.showMessageDialog(this, 
                "Anda sedang berada di menu KRS", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Button Pembayaran - INI YANG DITAMBAHKAN
        JButton btnPembayaran = createMenuButton("💰 Pembayaran", new Color(39, 174, 96));
        btnPembayaran.addActionListener(e -> {
            new PembayaranMahasiswaFrame(mahasiswa).setVisible(true);
        });
        
        // Button Nilai
        JButton btnNilai = createMenuButton("📈 Nilai", new Color(230, 126, 34));
        btnNilai.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Fitur nilai akan segera tersedia", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Button Jadwal
        JButton btnJadwal = createMenuButton("📅 Jadwal", new Color(155, 89, 182));
        btnJadwal.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Fitur jadwal akan segera tersedia", "Info", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Button Profil
        JButton btnProfil = createMenuButton("👤 Profil", new Color(52, 73, 94));
        btnProfil.addActionListener(e -> {
            showProfilDialog();
        });
        
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnDashboard);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnKRS);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnPembayaran); // TAMBAH INI
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnNilai);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnJadwal);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnProfil);
        menuPanel.add(Box.createVerticalGlue());
        
        return menuPanel;
    }
    
    // BARU DITAMBAHKAN: Method untuk membuat button menu
    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setMaximumSize(new Dimension(180, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private JPanel createKRSPanel() {
        JPanel krsPanel = new JPanel(new BorderLayout());
        krsPanel.setBorder(BorderFactory.createTitledBorder("Kartu Rencana Studi (KRS)"));
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Semester:"));
        
        cmbSemester = new JComboBox<>(new String[]{
            "2023/2024 Ganjil", "2023/2024 Genap",
            "2024/2025 Ganjil", "2024/2025 Genap"
        });
        filterPanel.add(cmbSemester);
        
        JButton btnLoad = new JButton("Load KRS");
        JButton btnTambah = new JButton("Tambah Mata Kuliah");
        JButton btnHapus = new JButton("Hapus Selected");
        JButton btnSubmit = new JButton("Submit KRS");
        
        btnLoad.setBackground(new Color(0, 123, 255));
        btnTambah.setBackground(new Color(40, 167, 69));
        btnHapus.setBackground(new Color(220, 53, 69));
        btnSubmit.setBackground(new Color(255, 193, 7));
        
        btnLoad.setForeground(Color.WHITE);
        btnTambah.setForeground(Color.WHITE);
        btnHapus.setForeground(Color.WHITE);
        btnSubmit.setForeground(Color.BLACK);
        
        btnLoad.addActionListener(e -> loadKRSData());
        btnTambah.addActionListener(e -> showTambahKRSDialog());
        btnHapus.addActionListener(e -> hapusKRSSelected());
        btnSubmit.addActionListener(e -> submitKRS());
        
        filterPanel.add(btnLoad);
        filterPanel.add(btnTambah);
        filterPanel.add(btnHapus);
        filterPanel.add(btnSubmit);
        
        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Kode MK", "Mata Kuliah", "SKS", "Status"}, 0
        );
        
        tblKRS = new JTable(tableModel);
        tblKRS.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(tblKRS);
        
        krsPanel.add(filterPanel, BorderLayout.NORTH);
        krsPanel.add(scrollPane, BorderLayout.CENTER);
        
        return krsPanel;
    }
    
    private void loadKRSData() {
        String semester = (String) cmbSemester.getSelectedItem();
        if (semester == null) return;
        
        tableModel.setRowCount(0);
        List<KRS> list = krsService.getKRSByNim(mahasiswa.getNim(), semester);
        
        for (KRS krs : list) {
            tableModel.addRow(new Object[]{
                krs.getId(),
                krs.getKodeMk(),
                krs.getNamaMk(),
                krs.getSks(),
                krs.getStatus()
            });
        }
        
        int totalSKS = krsService.getTotalSKS(mahasiswa.getNim(), semester);
        lblTotalSKS.setText("Total SKS: " + totalSKS);
    }
    
    private void showTambahKRSDialog() {
        JDialog dialog = new JDialog(this, "Tambah Mata Kuliah ke KRS", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // TODO: Implement tambah mata kuliah dialog
        JLabel label = new JLabel("<html><center><h2>Tambah Mata Kuliah</h2>" +
            "<p>Fitur untuk menambah mata kuliah ke KRS</p>" +
            "<p style='color: orange;'>Fitur dalam pengembangan...</p></center></html>", 
            SwingConstants.CENTER);
        
        panel.add(label, BorderLayout.CENTER);
        
        JButton btnClose = new JButton("Tutup");
        btnClose.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnClose);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void hapusKRSSelected() {
        int selectedRow = tblKRS.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mata kuliah yang akan dihapus!");
            return;
        }
        
        int krsId = (int) tableModel.getValueAt(selectedRow, 0);
        String mataKuliah = (String) tableModel.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Hapus mata kuliah " + mataKuliah + " dari KRS?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (krsService.deleteKRS(krsId)) {
                JOptionPane.showMessageDialog(this, "Mata kuliah berhasil dihapus!");
                loadKRSData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus mata kuliah!");
            }
        }
    }
    
    private void submitKRS() {
        String semester = (String) cmbSemester.getSelectedItem();
        int totalSKS = krsService.getTotalSKS(mahasiswa.getNim(), semester);
        
        if (totalSKS < 12) {
            JOptionPane.showMessageDialog(this, 
                "Minimal SKS yang harus diambil adalah 12!\n" +
                "SKS saat ini: " + totalSKS, 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Submit KRS dengan total " + totalSKS + " SKS?\n" +
            "Setelah disubmit, KRS tidak dapat diubah!",
            "Konfirmasi Submit KRS", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (krsService.submitKRS(mahasiswa.getNim(), semester)) {
                JOptionPane.showMessageDialog(this, "KRS berhasil disubmit!");
                loadKRSData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal submit KRS!");
            }
        }
    }
    
    // BARU DITAMBAHKAN: Method untuk menampilkan dialog profil
    private void showProfilDialog() {
        JDialog dialog = new JDialog(this, "Profil Mahasiswa", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(new JLabel("NIM:"));
        panel.add(new JLabel(mahasiswa.getNim()));
        panel.add(new JLabel("Nama:"));
        panel.add(new JLabel(mahasiswa.getNama()));
        panel.add(new JLabel("Program Studi:"));
        panel.add(new JLabel(mahasiswa.getProgramStudi()));
        panel.add(new JLabel("Fakultas:"));
        panel.add(new JLabel(mahasiswa.getFakultas()));
        panel.add(new JLabel("Angkatan:"));
        panel.add(new JLabel(String.valueOf(mahasiswa.getAngkatan())));
        panel.add(new JLabel("Status:"));
        panel.add(new JLabel(mahasiswa.getStatus()));
        
        JButton btnClose = new JButton("Tutup");
        btnClose.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnClose);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin logout?",
            "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            new LoginFrame().setVisible(true);
            dispose();
        }
    }
}
