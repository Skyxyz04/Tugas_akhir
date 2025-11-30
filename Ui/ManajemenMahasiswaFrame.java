package Ui;

import Model.Admin;
import Model.Mahasiswa;
import Service.AdminService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManajemenMahasiswaFrame extends JFrame {
    private Admin admin;
    private AdminService adminService;
    private JTable tblMahasiswa;
    private DefaultTableModel tableModel;
    
    public ManajemenMahasiswaFrame(Admin admin) {
        this.admin = admin;
        this.adminService = new AdminService();
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        setTitle("Manajemen Mahasiswa - SIASAT UKSW");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"No", "NIM", "Nama", "Program Studi", "Fakultas", "Angkatan", "Status", "Email", "Aksi"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Hanya kolom aksi yang editable
            }
        };
        
        tblMahasiswa = new JTable(tableModel);
        tblMahasiswa.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(tblMahasiswa);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("MANAJEMEN DATA MAHASISWA");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(102, 0, 0));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTambah = new JButton("Tambah Mahasiswa");
        JButton btnEdit = new JButton("Edit Selected");
        JButton btnHapus = new JButton("Hapus Selected");
        JButton btnRefresh = new JButton("Refresh");
        
        btnTambah.setBackground(new Color(0, 102, 204));
        btnEdit.setBackground(new Color(255, 153, 0));
        btnHapus.setBackground(new Color(204, 0, 0));
        btnRefresh.setBackground(new Color(102, 102, 102));
        
        btnTambah.setForeground(Color.WHITE);
        btnEdit.setForeground(Color.WHITE);
        btnHapus.setForeground(Color.WHITE);
        btnRefresh.setForeground(Color.WHITE);
        
        btnTambah.addActionListener(e -> showTambahMahasiswaDialog());
        btnEdit.addActionListener(e -> editSelectedMahasiswa());
        btnHapus.addActionListener(e -> hapusSelectedMahasiswa());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnRefresh);
        
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(buttonPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Mahasiswa> list = adminService.getAllMahasiswa();
        
        int no = 1;
        for (Mahasiswa mhs : list) {
            tableModel.addRow(new Object[]{
                no++,
                mhs.getNim(),
                mhs.getNama(),
                mhs.getProgramStudi(),
                mhs.getFakultas(),
                mhs.getAngkatan(),
                mhs.getStatus(),
                mhs.getEmail(),
                "✏️ Hapus"
            });
        }
        
        JOptionPane.showMessageDialog(this, 
            "Data mahasiswa berhasil dimuat: " + list.size() + " mahasiswa", 
            "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showTambahMahasiswaDialog() {
        JDialog dialog = new JDialog(this, "Tambah Mahasiswa", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(10, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextField txtNim = new JTextField();
        JTextField txtNama = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtProdi = new JTextField();
        JTextField txtFakultas = new JTextField();
        JTextField txtAngkatan = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JTextField txtTelepon = new JTextField();
        JTextArea txtAlamat = new JTextArea(3, 20);
        
        panel.add(new JLabel("NIM:*"));
        panel.add(txtNim);
        panel.add(new JLabel("Nama:*"));
        panel.add(txtNama);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Program Studi:*"));
        panel.add(txtProdi);
        panel.add(new JLabel("Fakultas:*"));
        panel.add(txtFakultas);
        panel.add(new JLabel("Angkatan:*"));
        panel.add(txtAngkatan);
        panel.add(new JLabel("Password:*"));
        panel.add(txtPassword);
        panel.add(new JLabel("No. Telepon:"));
        panel.add(txtTelepon);
        panel.add(new JLabel("Alamat:"));
        panel.add(new JScrollPane(txtAlamat));
        
        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        btnSimpan.addActionListener(e -> {
            if (validateInput(txtNim, txtNama, txtProdi, txtFakultas, txtAngkatan, txtPassword)) {
                Mahasiswa mhs = new Mahasiswa();
                mhs.setNim(txtNim.getText());
                mhs.setNama(txtNama.getText());
                mhs.setPassword(new String(txtPassword.getPassword()));
                mhs.setProgramStudi(txtProdi.getText());
                mhs.setFakultas(txtFakultas.getText());
                mhs.setAngkatan(Integer.parseInt(txtAngkatan.getText()));
                mhs.setEmail(txtEmail.getText());
                mhs.setNoTelepon(txtTelepon.getText());
                mhs.setAlamat(txtAlamat.getText());
                mhs.setStatus("Aktif");
                
                if (adminService.addMahasiswa(mhs)) {
                    JOptionPane.showMessageDialog(dialog, "Mahasiswa berhasil ditambahkan!");
                    dialog.dispose();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Gagal menambahkan mahasiswa!");
                }
            }
        });
        
        btnBatal.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private boolean validateInput(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field bertanda (*) harus diisi!");
                return false;
            }
        }
        return true;
    }
    
    private void editSelectedMahasiswa() {
        int selectedRow = tblMahasiswa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa yang akan diedit!");
            return;
        }
        
        String nim = (String) tableModel.getValueAt(selectedRow, 1);
        JOptionPane.showMessageDialog(this, 
            "Edit mahasiswa: " + nim + "\nFitur edit dalam pengembangan.", 
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void hapusSelectedMahasiswa() {
        int selectedRow = tblMahasiswa.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih mahasiswa yang akan dihapus!");
            return;
        }
        
        String nim = (String) tableModel.getValueAt(selectedRow, 1);
        String nama = (String) tableModel.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus mahasiswa:\n" + nim + " - " + nama + "?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (adminService.deleteMahasiswa(nim)) {
                JOptionPane.showMessageDialog(this, "Mahasiswa berhasil dihapus!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus mahasiswa!");
            }
        }
    }
}