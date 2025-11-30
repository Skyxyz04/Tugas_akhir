package Ui;

import Model.Mahasiswa;
import Model.Nilai;
import Service.AkademikService;
import Service.TranskripService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class KhsFrame extends JFrame {
    private Mahasiswa mahasiswa;
    private AkademikService akademikService;
    private TranskripService transkripService;
    private JTable tblKHS;
    private JLabel lblIPS;
    private JComboBox<String> cmbSemester;
    
    public KhsFrame(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
        this.akademikService = new AkademikService();
        this.transkripService = new TranskripService();
        initializeUI();
        loadKHS();
    }
    
    private void initializeUI() {
        setTitle("Kartu Hasil Studi - " + mahasiswa.getNama());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // KHS Table
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"No", "Kode", "Mata Kuliah", "SKS", "Nilai Huruf", "Nilai Angka", "Bobot"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblKHS = new JTable(model);
        tblKHS.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(tblKHS);
        
        // Summary Panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.add(new JLabel("Indeks Prestasi Semester (IPS):"));
        lblIPS = new JLabel("0.00");
        lblIPS.setFont(new Font("Arial", Font.BOLD, 16));
        lblIPS.setForeground(new Color(0, 102, 0));
        summaryPanel.add(lblIPS);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(summaryPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("KARTU HASIL STUDI (KHS)");
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
        cmbSemester = new JComboBox<>(new String[]{"2023/2024 Ganjil", "2023/2024 Genap"});
        infoPanel.add(cmbSemester);
        
        cmbSemester.addActionListener(e -> loadKHS());
        
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(infoPanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private void loadKHS() {
        DefaultTableModel model = (DefaultTableModel) tblKHS.getModel();
        model.setRowCount(0);
        
        String semester = (String) cmbSemester.getSelectedItem();
        List<Nilai> khs = akademikService.getKHS(mahasiswa.getNim(), semester);
        
        if (khs.isEmpty()) {
            model.addRow(new Object[]{"-", "-", "Tidak ada nilai untuk semester ini", "-", "-", "-", "-"});
            lblIPS.setText("0.00");
        } else {
            int no = 1;
            double totalBobot = 0;
            int totalSKS = 0;
            
            for (Nilai nilai : khs) {
                double bobot = nilai.getSks() * nilai.getNilaiAngka();
                totalBobot += bobot;
                totalSKS += nilai.getSks();
                
                model.addRow(new Object[]{
                    no++,
                    nilai.getKodeMk(),
                    nilai.getNamaMk(),
                    nilai.getSks(),
                    nilai.getNilaiHuruf(),
                    String.format("%.2f", nilai.getNilaiAngka()),
                    String.format("%.2f", bobot)
                });
            }
            
            // Hitung IPS
            double ips = totalSKS > 0 ? totalBobot / totalSKS : 0.0;
            lblIPS.setText(String.format("%.2f", ips));
        }
    }
}