package Ui;

import Model.Mahasiswa;
import Model.Nilai;
import Service.TranskripService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TranskripFrame extends JFrame {
    private Mahasiswa mahasiswa;
    private TranskripService transkripService;
    private JTable tblTranskrip;
    private DefaultTableModel tableModel;
    
    public TranskripFrame(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
        this.transkripService = new TranskripService();
        initializeUI();
        loadTranskrip();
    }
    
    private void initializeUI() {
        setTitle("Transkrip Nilai - " + mahasiswa.getNama());
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Transkrip Table - SIMPAN REFERENSI ke variable
        tableModel = new DefaultTableModel(
            new Object[]{"No", "Semester", "Kode", "Mata Kuliah", "SKS", "Nilai Huruf", "Nilai Angka", "Bobot"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tblTranskrip = new JTable(tableModel);
        tblTranskrip.setRowHeight(25);
        tblTranskrip.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Set column widths
        tblTranskrip.getColumnModel().getColumn(0).setPreferredWidth(40);  // No
        tblTranskrip.getColumnModel().getColumn(1).setPreferredWidth(100); // Semester
        tblTranskrip.getColumnModel().getColumn(2).setPreferredWidth(80);  // Kode
        tblTranskrip.getColumnModel().getColumn(3).setPreferredWidth(200); // Mata Kuliah
        tblTranskrip.getColumnModel().getColumn(4).setPreferredWidth(50);  // SKS
        tblTranskrip.getColumnModel().getColumn(5).setPreferredWidth(80);  // Nilai Huruf
        tblTranskrip.getColumnModel().getColumn(6).setPreferredWidth(80);  // Nilai Angka
        tblTranskrip.getColumnModel().getColumn(7).setPreferredWidth(80);  // Bobot
        
        JScrollPane scrollPane = new JScrollPane(tblTranskrip);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("TRANSKRIP NILAI");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 51, 102));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Data Mahasiswa"));
        
        infoPanel.add(new JLabel("NIM:"));
        infoPanel.add(new JLabel(mahasiswa.getNim()));
        infoPanel.add(new JLabel("Nama:"));
        infoPanel.add(new JLabel(mahasiswa.getNama()));
        infoPanel.add(new JLabel("Program Studi:"));
        infoPanel.add(new JLabel(mahasiswa.getProgramStudi()));
        
        double ipk = transkripService.calculateIPK(mahasiswa.getNim());
        
        JPanel ipkPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        ipkPanel.add(new JLabel("Indeks Prestasi Kumulatif (IPK):"));
        JLabel lblIPK = new JLabel(String.format("%.2f", ipk));
        lblIPK.setFont(new Font("Arial", Font.BOLD, 20));
        lblIPK.setForeground(new Color(0, 102, 0));
        ipkPanel.add(lblIPK);
        
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(infoPanel, BorderLayout.CENTER);
        headerPanel.add(ipkPanel, BorderLayout.SOUTH);
        
        return headerPanel;
    }
    
    private void loadTranskrip() {
        try {
            // Gunakan referensi tableModel yang sudah disimpan
            tableModel.setRowCount(0);
            
            List<Nilai> transkrip = transkripService.getTranskrip(mahasiswa.getNim());
            
            if (transkrip.isEmpty()) {
                // Tambahkan pesan jika tidak ada data
                tableModel.addRow(new Object[]{
                    "-", "-", "-", "Belum ada data transkrip", "-", "-", "-", "-"
                });
                JOptionPane.showMessageDialog(this, 
                    "Belum ada data transkrip nilai.", 
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Tambahkan data transkrip
                int no = 1;
                double totalBobot = 0;
                int totalSKS = 0;
                
                for (Nilai nilai : transkrip) {
                    double bobot = nilai.getSks() * nilai.getNilaiAngka();
                    totalBobot += bobot;
                    totalSKS += nilai.getSks();
                    
                    tableModel.addRow(new Object[]{
                        no++,
                        nilai.getSemester(),
                        nilai.getKodeMk(),
                        nilai.getNamaMk(),
                        nilai.getSks(),
                        nilai.getNilaiHuruf(),
                        String.format("%.2f", nilai.getNilaiAngka()),
                        String.format("%.2f", bobot)
                    });
                }
                
                // Tampilkan summary
                JOptionPane.showMessageDialog(this, 
                    "Transkrip berhasil dimuat!\n" +
                    "Total Mata Kuliah: " + transkrip.size() + "\n" +
                    "Total SKS: " + totalSKS + "\n" +
                    "IPK: " + String.format("%.2f", totalBobot / totalSKS),
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Error loading transkrip: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error memuat transkrip: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}