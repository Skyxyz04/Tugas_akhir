package Ui;

import Model.Admin;
import Model.MataKuliah;
import Service.AdminService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManajemenMataKuliahFrame extends JFrame {
    private Admin admin;
    private AdminService adminService;
    private JTable tblMataKuliah;
    private DefaultTableModel tableModel;
    
    public ManajemenMataKuliahFrame(Admin admin) {
        this.admin = admin;
        this.adminService = new AdminService();
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        setTitle("Manajemen Mata Kuliah - SIASAT UKSW");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"No", "Kode", "Nama", "SKS", "Semester", "Jurusan", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Non-editable table
            }
        };
        
        tblMataKuliah = new JTable(tableModel);
        tblMataKuliah.setRowHeight(30);
        tblMataKuliah.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tblMataKuliah);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("MANAJEMEN MATA KULIAH");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(102, 0, 0));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTambah = new JButton("Tambah MK");
        JButton btnEdit = new JButton("Edit");
        JButton btnHapus = new JButton("Hapus");
        JButton btnRefresh = new JButton("Refresh");
        
        btnTambah.setBackground(new Color(0, 102, 204));
        btnEdit.setBackground(new Color(255, 153, 0));
        btnHapus.setBackground(new Color(204, 0, 0));
        btnRefresh.setBackground(new Color(102, 102, 102));
        
        btnTambah.setForeground(Color.WHITE);
        btnEdit.setForeground(Color.WHITE);
        btnHapus.setForeground(Color.WHITE);
        btnRefresh.setForeground(Color.WHITE);
        
        btnTambah.addActionListener(e -> showTambahMKDialog());
        btnEdit.addActionListener(e -> editSelectedMK());
        btnHapus.addActionListener(e -> hapusSelectedMK());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnRefresh);
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private void loadData() {
        try {
            tableModel.setRowCount(0);
            List<MataKuliah> list = adminService.getAllMataKuliah();
            
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Tidak ada data mata kuliah yang ditemukan!", 
                    "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            int no = 1;
            for (MataKuliah mk : list) {
                tableModel.addRow(new Object[]{
                    no++,
                    mk.getKode(),
                    mk.getNama(),
                    mk.getSks(),
                    mk.getSemester(),
                    mk.getJurusan(), // Diubah dari getProgramStudi() ke getJurusan()
                    mk.getStatus()
                });
            }
            
            System.out.println("✅ Data mata kuliah berhasil dimuat: " + list.size() + " records");
            
        } catch (Exception e) {
            System.err.println("❌ Error loading mata kuliah data: " + e.getMessage());
            JOptionPane.showMessageDialog(this, 
                "Error memuat data mata kuliah: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void showTambahMKDialog() {
        JDialog dialog = new JDialog(this, "Tambah Mata Kuliah", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextField txtKode = new JTextField();
        JTextField txtNama = new JTextField();
        JSpinner spnSks = new JSpinner(new SpinnerNumberModel(2, 1, 4, 1));
        JTextField txtSemester = new JTextField(); // Diubah dari JSpinner ke JTextField
        JTextField txtJurusan = new JTextField(); // Diubah dari txtProdi ke txtJurusan
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Aktif", "Non-Aktif"});
        
        // Set default values
        txtSemester.setText("Semester 1");
        txtJurusan.setText("Teknik Informatika");
        
        panel.add(new JLabel("Kode MK:*"));
        panel.add(txtKode);
        panel.add(new JLabel("Nama MK:*"));
        panel.add(txtNama);
        panel.add(new JLabel("SKS:*"));
        panel.add(spnSks);
        panel.add(new JLabel("Semester:*"));
        panel.add(txtSemester);
        panel.add(new JLabel("Jurusan:*")); // Diubah dari Program Studi ke Jurusan
        panel.add(txtJurusan);
        panel.add(new JLabel("Status:*"));
        panel.add(cmbStatus);
        
        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");
        
        btnSimpan.setBackground(new Color(40, 167, 69));
        btnBatal.setBackground(new Color(108, 117, 125));
        btnSimpan.setForeground(Color.WHITE);
        btnBatal.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        btnSimpan.addActionListener(e -> {
            if (validateInput(txtKode, txtNama, txtSemester, txtJurusan)) {
                try {
                    MataKuliah mk = new MataKuliah();
                    mk.setKode(txtKode.getText().trim().toUpperCase());
                    mk.setNama(txtNama.getText().trim());
                    mk.setSks((Integer) spnSks.getValue());
                    mk.setSemester(txtSemester.getText().trim()); // Diubah ke String
                    mk.setJurusan(txtJurusan.getText().trim()); // Diubah dari setProgramStudi ke setJurusan
                    mk.setStatus((String) cmbStatus.getSelectedItem());
                    
                    if (adminService.addMataKuliah(mk)) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Mata kuliah berhasil ditambahkan!", 
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                            "Gagal menambahkan mata kuliah!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Error: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        
        btnBatal.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void editSelectedMK() {
        int selectedRow = tblMataKuliah.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih mata kuliah yang akan diedit!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String kode = (String) tableModel.getValueAt(selectedRow, 1);
        
        try {
            List<MataKuliah> list = adminService.getAllMataKuliah();
            MataKuliah mkToEdit = null;
            
            for (MataKuliah mk : list) {
                if (mk.getKode().equals(kode)) {
                    mkToEdit = mk;
                    break;
                }
            }
            
            if (mkToEdit != null) {
                showEditMKDialog(mkToEdit);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Data mata kuliah tidak ditemukan!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void showEditMKDialog(MataKuliah mk) {
        JDialog dialog = new JDialog(this, "Edit Mata Kuliah", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextField txtKode = new JTextField(mk.getKode());
        JTextField txtNama = new JTextField(mk.getNama());
        JSpinner spnSks = new JSpinner(new SpinnerNumberModel(mk.getSks(), 1, 4, 1));
        JTextField txtSemester = new JTextField(mk.getSemester()); // Diubah ke JTextField
        JTextField txtJurusan = new JTextField(mk.getJurusan()); // Diubah dari getProgramStudi ke getJurusan
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Aktif", "Non-Aktif"});
        cmbStatus.setSelectedItem(mk.getStatus());
        
        txtKode.setEditable(false);
        
        panel.add(new JLabel("Kode MK:"));
        panel.add(txtKode);
        panel.add(new JLabel("Nama MK:*"));
        panel.add(txtNama);
        panel.add(new JLabel("SKS:*"));
        panel.add(spnSks);
        panel.add(new JLabel("Semester:*"));
        panel.add(txtSemester);
        panel.add(new JLabel("Jurusan:*")); // Diubah dari Program Studi ke Jurusan
        panel.add(txtJurusan);
        panel.add(new JLabel("Status:*"));
        panel.add(cmbStatus);
        
        JButton btnUpdate = new JButton("Update");
        JButton btnBatal = new JButton("Batal");
        
        btnUpdate.setBackground(new Color(40, 167, 69));
        btnBatal.setBackground(new Color(108, 117, 125));
        btnUpdate.setForeground(Color.WHITE);
        btnBatal.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnBatal);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        btnUpdate.addActionListener(e -> {
            if (validateInput(txtNama, txtSemester, txtJurusan)) {
                try {
                    mk.setNama(txtNama.getText().trim());
                    mk.setSks((Integer) spnSks.getValue());
                    mk.setSemester(txtSemester.getText().trim()); // Diubah ke String
                    mk.setJurusan(txtJurusan.getText().trim()); // Diubah dari setProgramStudi ke setJurusan
                    mk.setStatus((String) cmbStatus.getSelectedItem());
                    
                    if (adminService.updateMataKuliah(mk)) {
                        JOptionPane.showMessageDialog(dialog, 
                            "Mata kuliah berhasil diupdate!", 
                            "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                        loadData();
                    } else {
                        JOptionPane.showMessageDialog(dialog, 
                            "Gagal mengupdate mata kuliah!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Error: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });
        
        btnBatal.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void hapusSelectedMK() {
        int selectedRow = tblMataKuliah.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih mata kuliah yang akan dihapus!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String kode = (String) tableModel.getValueAt(selectedRow, 1);
        String nama = (String) tableModel.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus mata kuliah:\n" + 
            kode + " - " + nama + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (adminService.deleteMataKuliah(kode)) {
                    JOptionPane.showMessageDialog(this, 
                        "Mata kuliah berhasil dihapus!", 
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Gagal menghapus mata kuliah!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
    
    private boolean validateInput(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Semua field bertanda (*) harus diisi!", 
                    "Peringatan", JOptionPane.WARNING_MESSAGE);
                field.requestFocus();
                return false;
            }
        }
        return true;
    }
}