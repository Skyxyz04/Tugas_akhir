package Ui;

import Model.Admin;
import Model.Nilai;
import Service.AdminService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManajemenNilaiFrame extends JFrame {
    private Admin admin;
    private AdminService adminService;
    private JTable tblNilai;
    private DefaultTableModel tableModel;
    
    public ManajemenNilaiFrame(Admin admin) {
        this.admin = admin;
        this.adminService = new AdminService();
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        setTitle("Manajemen Nilai - SIASAT UKSW");
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("MANAJEMEN NILAI MAHASISWA");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(102, 0, 0));
        
        JButton btnRefresh = new JButton("Refresh");
        JButton btnInputNilai = new JButton("Input Nilai Baru");
        
        btnRefresh.setBackground(new Color(108, 117, 125));
        btnInputNilai.setBackground(new Color(40, 167, 69));
        btnRefresh.setForeground(Color.WHITE);
        btnInputNilai.setForeground(Color.WHITE);
        
        btnRefresh.addActionListener(e -> loadData());
        btnInputNilai.addActionListener(e -> {
            new InputNilaiFrame(admin).setVisible(true);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnInputNilai);
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"No", "NIM", "Nama Mahasiswa", "Kode MK", "Mata Kuliah", "SKS", "Nilai", "Semester"}, 0
        );
        
        tblNilai = new JTable(tableModel);
        tblNilai.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(tblNilai);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Nilai> list = adminService.getAllNilai();
        
        int no = 1;
        for (Nilai nilai : list) {
            tableModel.addRow(new Object[]{
                no++,
                nilai.getNim(),
                nilai.getNamaMahasiswa(),
                nilai.getKodeMk(),
                nilai.getNamaMk(),
                nilai.getSks(),
                nilai.getNilaiHuruf(),
                nilai.getSemester()
            });
        }
        
        JOptionPane.showMessageDialog(this, 
            "Data nilai berhasil dimuat: " + list.size() + " records", 
            "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }
}