package Ui;

import Model.Admin;
import Service.AdminService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InputNilaiFrame extends JFrame {
    private Admin admin;
    private AdminService adminService;
    
    private JComboBox<String> cmbNim;
    private JComboBox<String> cmbKodeMk;
    private JComboBox<String> cmbNilai;
    private JComboBox<String> cmbSemester;
    
    public InputNilaiFrame(Admin admin) {
        this.admin = admin;
        this.adminService = new AdminService();
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        setTitle("Input Nilai - SIASAT UKSW");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("INPUT NILAI MAHASISWA");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(102, 0, 0));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 15));
        formPanel.setBorder(BorderFactory.createTitledBorder("Form Input Nilai"));
        
        cmbNim = new JComboBox<>();
        cmbKodeMk = new JComboBox<>();
        cmbNilai = new JComboBox<>(new String[]{"A", "B", "C", "D", "E"});
        cmbSemester = new JComboBox<>(new String[]{
            "2023/2024 Ganjil", "2023/2024 Genap",
            "2024/2025 Ganjil", "2024/2025 Genap"
        });
        
        JButton btnRefreshMhs = new JButton("Refresh");
        JButton btnRefreshMk = new JButton("Refresh");
        
        JPanel nimPanel = new JPanel(new BorderLayout());
        nimPanel.add(cmbNim, BorderLayout.CENTER);
        nimPanel.add(btnRefreshMhs, BorderLayout.EAST);
        
        JPanel mkPanel = new JPanel(new BorderLayout());
        mkPanel.add(cmbKodeMk, BorderLayout.CENTER);
        mkPanel.add(btnRefreshMk, BorderLayout.EAST);
        
        formPanel.add(new JLabel("NIM Mahasiswa:*"));
        formPanel.add(nimPanel);
        formPanel.add(new JLabel("Kode Mata Kuliah:*"));
        formPanel.add(mkPanel);
        formPanel.add(new JLabel("Nilai:*"));
        formPanel.add(cmbNilai);
        formPanel.add(new JLabel("Semester:*"));
        formPanel.add(cmbSemester);
        
        JButton btnSimpan = new JButton("Simpan Nilai");
        btnSimpan.setBackground(new Color(40, 167, 69));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton btnBatal = new JButton("Batal");
        btnBatal.setBackground(new Color(108, 117, 125));
        btnBatal.setForeground(Color.WHITE);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Event listeners
        btnSimpan.addActionListener(e -> simpanNilai());
        btnBatal.addActionListener(e -> dispose());
        btnRefreshMhs.addActionListener(e -> loadMahasiswaData());
        btnRefreshMk.addActionListener(e -> loadMataKuliahData());
    }
    
    private void loadData() {
        loadMahasiswaData();
        loadMataKuliahData();
    }
    
    private void loadMahasiswaData() {
        cmbNim.removeAllItems();
        cmbNim.addItem("-- Pilih Mahasiswa --");
        
        try {
            ResultSet rs = adminService.getDaftarMahasiswa();
            if (rs != null) {
                while (rs.next()) {
                    String nim = rs.getString("nim");
                    String nama = rs.getString("nama");
                    String prodi = rs.getString("program_studi");
                    cmbNim.addItem(nim + " - " + nama + " (" + prodi + ")");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadMataKuliahData() {
        cmbKodeMk.removeAllItems();
        cmbKodeMk.addItem("-- Pilih Mata Kuliah --");
        
        try {
            ResultSet rs = adminService.getDaftarMataKuliah();
            if (rs != null) {
                while (rs.next()) {
                    String kode = rs.getString("kode");
                    String nama = rs.getString("nama");
                    int sks = rs.getInt("sks");
                    cmbKodeMk.addItem(kode + " - " + nama + " (" + sks + " SKS)");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void simpanNilai() {
        if (cmbNim.getSelectedIndex() <= 0 || cmbKodeMk.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, 
                "Harap pilih mahasiswa dan mata kuliah!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String selectedNim = (String) cmbNim.getSelectedItem();
        String selectedKodeMk = (String) cmbKodeMk.getSelectedItem();
        String nim = selectedNim.split(" - ")[0];
        String kodeMk = selectedKodeMk.split(" - ")[0];
        String nilai = (String) cmbNilai.getSelectedItem();
        String semester = (String) cmbSemester.getSelectedItem();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Simpan nilai " + nilai + " untuk:\n" +
            "NIM: " + nim + "\n" +
            "Mata Kuliah: " + kodeMk + "\n" +
            "Semester: " + semester,
            "Konfirmasi Simpan Nilai",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = adminService.inputNilai(nim, kodeMk, nilai, semester);
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Nilai berhasil disimpan!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                cmbNilai.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Gagal menyimpan nilai!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}