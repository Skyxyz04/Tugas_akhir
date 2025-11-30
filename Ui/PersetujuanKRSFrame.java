package Ui;

import Model.Admin;
import Service.AdminService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PersetujuanKRSFrame extends JFrame {
    private Admin admin;
    private AdminService adminService;
    private JTable tblKRS;
    private DefaultTableModel tableModel;
    
    public PersetujuanKRSFrame(Admin admin) {
        this.admin = admin;
        this.adminService = new AdminService();
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        setTitle("Persetujuan KRS - SIASAT UKSW");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "NIM", "Nama Mahasiswa", "Mata Kuliah", "SKS", "Semester", "Tanggal Pengajuan", "Aksi"}, 0
        );
        
        tblKRS = new JTable(tableModel);
        tblKRS.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(tblKRS);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("PERSETUJUAN KARTU RENCANA STUDI (KRS)");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(102, 0, 0));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSetujui = new JButton("Setujui Selected");
        JButton btnTolak = new JButton("Tolak Selected");
        JButton btnRefresh = new JButton("Refresh");
        
        btnSetujui.setBackground(new Color(0, 153, 0));
        btnTolak.setBackground(new Color(204, 0, 0));
        btnRefresh.setBackground(new Color(102, 102, 102));
        
        btnSetujui.setForeground(Color.WHITE);
        btnTolak.setForeground(Color.WHITE);
        btnRefresh.setForeground(Color.WHITE);
        
        btnSetujui.addActionListener(e -> setujuiKRS());
        btnTolak.addActionListener(e -> tolakKRS());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnSetujui);
        buttonPanel.add(btnTolak);
        buttonPanel.add(btnRefresh);
        
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(buttonPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Object[]> list = adminService.getKRSMenungguPersetujuan();
        
        for (Object[] row : list) {
            tableModel.addRow(new Object[]{
                row[0], // ID
                row[1], // NIM
                row[2], // Nama Mahasiswa
                row[3], // Mata Kuliah
                row[4], // SKS
                row[5], // Semester
                row[6], // Tanggal
                "✅ Setujui / ❌ Tolak"
            });
        }
        
        JOptionPane.showMessageDialog(this, 
            "Data KRS menunggu persetujuan: " + list.size() + " permohonan", 
            "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void setujuiKRS() {
        int selectedRow = tblKRS.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih KRS yang akan disetujui!");
            return;
        }
        
        int krsId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String nim = (String) tableModel.getValueAt(selectedRow, 1);
        String mk = (String) tableModel.getValueAt(selectedRow, 3);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Setujui KRS untuk:\nNIM: " + nim + "\nMata Kuliah: " + mk + "?",
            "Konfirmasi Persetujuan", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (adminService.approveKRS(krsId)) {
                JOptionPane.showMessageDialog(this, "KRS berhasil disetujui!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyetujui KRS!");
            }
        }
    }
    
    private void tolakKRS() {
        int selectedRow = tblKRS.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih KRS yang akan ditolak!");
            return;
        }
        
        int krsId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String nim = (String) tableModel.getValueAt(selectedRow, 1);
        String mk = (String) tableModel.getValueAt(selectedRow, 3);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Tolak KRS untuk:\nNIM: " + nim + "\nMata Kuliah: " + mk + "?",
            "Konfirmasi Penolakan", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (adminService.rejectKRS(krsId)) {
                JOptionPane.showMessageDialog(this, "KRS berhasil ditolak!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menolak KRS!");
            }
        }
    }
}