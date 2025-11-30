package Ui;

import Model.Mahasiswa;
import Model.MataKuliah;
import Model.KRS;
import Service.KRSService;
import Service.AkademikService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KRSFrame extends JFrame {
    private Mahasiswa mahasiswa;
    private KRSService krsService;
    private AkademikService akademikService;
    private JTable tblMataKuliah, tblKRS;
    private DefaultTableModel modelMataKuliah, modelKRS;
    private JLabel lblTotalSKS;
    private JComboBox<String> cmbSemester;
    
    public KRSFrame(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
        this.krsService = new KRSService();
        this.akademikService = new AkademikService();
        initializeUI();
        loadMataKuliah();
        loadKRS();
        
        System.out.println("✅ KRSFrame opened for: " + mahasiswa.getNama());
    }
    
    private void initializeUI() {
        setTitle("Kartu Rencana Studi - " + mahasiswa.getNama());
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Content - Split pane untuk mata kuliah dan KRS
        JSplitPane splitPane = createSplitPane();
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("KARTU RENCANA STUDI (KRS)");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 51, 102));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Data Mahasiswa"));
        
        infoPanel.add(new JLabel("NIM:"));
        infoPanel.add(new JLabel(mahasiswa.getNim()));
        infoPanel.add(new JLabel("Nama:"));
        infoPanel.add(new JLabel(mahasiswa.getNama()));
        infoPanel.add(new JLabel("Program Studi:"));
        infoPanel.add(new JLabel(mahasiswa.getProgramStudi()));
        infoPanel.add(new JLabel("Semester:"));
        cmbSemester = new JComboBox<>(new String[]{
            "2023/2024 Ganjil", "2023/2024 Genap", 
            "2024/2025 Ganjil", "2024/2025 Genap"
        });
        infoPanel.add(cmbSemester);
        
        cmbSemester.addActionListener(e -> {
            loadMataKuliah();
            loadKRS();
        });
        
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(infoPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JSplitPane createSplitPane() {
        // Panel Mata Kuliah Tersedia
        JPanel mkPanel = new JPanel(new BorderLayout());
        mkPanel.setBorder(BorderFactory.createTitledBorder("Mata Kuliah Tersedia"));
        
        modelMataKuliah = new DefaultTableModel(
            new Object[]{"Kode", "Mata Kuliah", "SKS", "Semester", "Aksi"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Hanya kolom aksi yang editable
            }
        };
        
        tblMataKuliah = new JTable(modelMataKuliah);
        tblMataKuliah.setRowHeight(25);
        JScrollPane scrollMk = new JScrollPane(tblMataKuliah);
        
        JButton btnAdd = new JButton("➕ Tambah ke KRS");
        btnAdd.setBackground(new Color(40, 167, 69));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.addActionListener(e -> tambahKeKRS());
        
        mkPanel.add(scrollMk, BorderLayout.CENTER);
        mkPanel.add(btnAdd, BorderLayout.SOUTH);
        
        // Panel KRS
        JPanel krsPanel = new JPanel(new BorderLayout());
        krsPanel.setBorder(BorderFactory.createTitledBorder("KRS Anda"));
        
        modelKRS = new DefaultTableModel(
            new Object[]{"ID", "Kode", "Mata Kuliah", "SKS", "Status", "Aksi"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Hanya kolom aksi yang editable
            }
        };
        
        tblKRS = new JTable(modelKRS);
        tblKRS.setRowHeight(25);
        JScrollPane scrollKRS = new JScrollPane(tblKRS);
        
        // Panel Total SKS dan Tombol
        JPanel actionPanel = new JPanel(new BorderLayout());
        
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        totalPanel.add(new JLabel("Total SKS:"));
        lblTotalSKS = new JLabel("0");
        lblTotalSKS.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalSKS.setForeground(Color.BLUE);
        totalPanel.add(lblTotalSKS);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDelete = new JButton("🗑️ Hapus Selected");
        JButton btnSubmit = new JButton("📤 Submit KRS");
        
        btnDelete.setBackground(new Color(220, 53, 69));
        btnSubmit.setBackground(new Color(255, 193, 7));
        btnDelete.setForeground(Color.WHITE);
        btnSubmit.setForeground(Color.BLACK);
        
        btnDelete.addActionListener(e -> hapusKRS());
        btnSubmit.addActionListener(e -> submitKRS());
        
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnSubmit);
        
        actionPanel.add(totalPanel, BorderLayout.WEST);
        actionPanel.add(buttonPanel, BorderLayout.EAST);
        
        krsPanel.add(scrollKRS, BorderLayout.CENTER);
        krsPanel.add(actionPanel, BorderLayout.SOUTH);
        
        // Split Pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mkPanel, krsPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(300);
        
        return splitPane;
    }
    
    private void loadMataKuliah() {
        modelMataKuliah.setRowCount(0);
        
        try {
            List<MataKuliah> list = akademikService.getMataKuliahByProdi(mahasiswa.getProgramStudi());
            
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Tidak ada mata kuliah tersedia untuk program studi " + mahasiswa.getProgramStudi(),
                    "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            for (MataKuliah mk : list) {
                // Filter berdasarkan semester yang dipilih
                String selectedSemester = (String) cmbSemester.getSelectedItem();
                String semesterPattern = selectedSemester.contains("Ganjil") ? "Ganjil" : "Genap";
                
                // Cek jika semester mata kuliah sesuai dengan yang dipilih
                if (mk.getSemester() != null && mk.getSemester().contains(semesterPattern)) {
                    modelMataKuliah.addRow(new Object[]{
                        mk.getKode(), 
                        mk.getNama(), 
                        mk.getSks(), 
                        mk.getSemester(),
                        "➕ Tambah"
                    });
                }
            }
            
            System.out.println("✅ Loaded " + modelMataKuliah.getRowCount() + " mata kuliah");
            
        } catch (Exception e) {
            System.err.println("❌ Error loading mata kuliah: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error memuat mata kuliah: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void loadKRS() {
        modelKRS.setRowCount(0);
        
        try {
            String semester = (String) cmbSemester.getSelectedItem();
            List<KRS> list = krsService.getKRSByNim(mahasiswa.getNim(), semester);
            
            for (KRS krs : list) {
                modelKRS.addRow(new Object[]{
                    krs.getId(),
                    krs.getKodeMk(), 
                    krs.getNamaMk(), 
                    krs.getSks(), 
                    krs.getStatus(),
                    "🗑️ Hapus"
                });
            }
            
            updateTotalSKS();
            System.out.println("✅ Loaded " + list.size() + " KRS items");
            
        } catch (Exception e) {
            System.err.println("❌ Error loading KRS: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error memuat KRS: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void tambahKeKRS() {
        int selectedRow = tblMataKuliah.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih mata kuliah terlebih dahulu!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String kodeMk = (String) modelMataKuliah.getValueAt(selectedRow, 0);
            String namaMk = (String) modelMataKuliah.getValueAt(selectedRow, 1);
            int sks = (Integer) modelMataKuliah.getValueAt(selectedRow, 2);
            String semester = (String) cmbSemester.getSelectedItem();
            
            // Cek apakah sudah ada di KRS
            if (isMataKuliahSudahDiKRS(kodeMk)) {
                JOptionPane.showMessageDialog(this, 
                    "Mata kuliah " + kodeMk + " sudah ada di KRS!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Cek total SKS
            int currentSKS = krsService.getTotalSKS(mahasiswa.getNim(), semester);
            if (currentSKS + sks > 24) {
                JOptionPane.showMessageDialog(this, 
                    "Total SKS tidak boleh lebih dari 24!\n" +
                    "SKS saat ini: " + currentSKS + "\n" +
                    "SKS yang akan ditambah: " + sks + "\n" +
                    "Total akan menjadi: " + (currentSKS + sks), 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            KRS krs = new KRS();
            krs.setNim(mahasiswa.getNim());
            krs.setKodeMk(kodeMk);
            krs.setNamaMk(namaMk);
            krs.setSks(sks);
            krs.setSemester(semester);
            krs.setTahunAkademik(getTahunAkademikFromSemester(semester));
            krs.setStatus("Draft");
            
            if (krsService.addKRS(krs)) {
                JOptionPane.showMessageDialog(this, 
                    "Mata kuliah " + kodeMk + " berhasil ditambahkan ke KRS!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadKRS();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Gagal menambahkan mata kuliah!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error adding to KRS: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void hapusKRS() {
        int selectedRow = tblKRS.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih mata kuliah yang akan dihapus!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int krsId = (Integer) modelKRS.getValueAt(selectedRow, 0);
            String kodeMk = (String) modelKRS.getValueAt(selectedRow, 1);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus mata kuliah " + kodeMk + " dari KRS?",
                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                if (krsService.deleteKRS(krsId)) {
                    JOptionPane.showMessageDialog(this, 
                        "Mata kuliah berhasil dihapus dari KRS!", 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    loadKRS();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal menghapus mata kuliah!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error deleting KRS: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void submitKRS() {
        try {
            String semester = (String) cmbSemester.getSelectedItem();
            int totalSKS = krsService.getTotalSKS(mahasiswa.getNim(), semester);
            
            if (totalSKS == 0) {
                JOptionPane.showMessageDialog(this, 
                    "KRS masih kosong! Tambahkan mata kuliah terlebih dahulu.", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (totalSKS < 12) {
                JOptionPane.showMessageDialog(this, 
                    "Minimal SKS yang harus diambil adalah 12!\nSKS saat ini: " + totalSKS, 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (totalSKS > 24) {
                JOptionPane.showMessageDialog(this, 
                    "Maksimal SKS yang dapat diambil adalah 24!\nSKS saat ini: " + totalSKS, 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Submit KRS dengan total " + totalSKS + " SKS?\n\n" +
                "Setelah disubmit, KRS akan dikirim untuk persetujuan " +
                "dan tidak dapat diubah lagi.",
                "Konfirmasi Submit KRS", JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                if (krsService.submitKRS(mahasiswa.getNim(), semester)) {
                    JOptionPane.showMessageDialog(this, 
                        "KRS berhasil disubmit!\n\n" +
                        "Total SKS: " + totalSKS + "\n" +
                        "Status: Menunggu persetujuan", 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    loadKRS(); // Reload untuk update status
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal submit KRS!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error submitting KRS: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private boolean isMataKuliahSudahDiKRS(String kodeMk) {
        for (int i = 0; i < modelKRS.getRowCount(); i++) {
            if (modelKRS.getValueAt(i, 1).equals(kodeMk)) {
                return true;
            }
        }
        return false;
    }
    
    private String getTahunAkademikFromSemester(String semester) {
        if (semester.contains("2023/2024")) return "2023/2024";
        if (semester.contains("2024/2025")) return "2024/2025";
        return "2023/2024"; // default
    }
    
    private void updateTotalSKS() {
        try {
            String semester = (String) cmbSemester.getSelectedItem();
            int totalSKS = krsService.getTotalSKS(mahasiswa.getNim(), semester);
            lblTotalSKS.setText(String.valueOf(totalSKS));
            
            // Warna berdasarkan batas SKS
            if (totalSKS < 12) {
                lblTotalSKS.setForeground(Color.RED);
            } else if (totalSKS > 24) {
                lblTotalSKS.setForeground(Color.RED);
            } else {
                lblTotalSKS.setForeground(Color.BLUE);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error updating total SKS: " + e.getMessage());
            lblTotalSKS.setText("0");
            lblTotalSKS.setForeground(Color.RED);
        }
    }
}