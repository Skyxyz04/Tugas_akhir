package Ui;

import Model.Mahasiswa;
import Model.Jadwal;
import Service.JadwalService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class JadwalFrame extends JFrame {
    private Mahasiswa mahasiswa;
    private JadwalService jadwalService;
    private JTable tblJadwal;
    private DefaultTableModel tableModel;
    
    public JadwalFrame(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
        this.jadwalService = new JadwalService();
        initializeUI();
        loadJadwal();
    }
    
    private void initializeUI() {
        setTitle("SIASAT UKSW - Jadwal Kuliah");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("JADWAL KULIAH");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 51, 102));
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.add(new JLabel("NIM: " + mahasiswa.getNim()));
        infoPanel.add(new JLabel(" | Nama: " + mahasiswa.getNama()));
        infoPanel.add(new JLabel(" | Semester: 2023/2024 Ganjil"));
        
        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(infoPanel, BorderLayout.CENTER);
        
        // Jadwal table - SIMPAN REFERENSI ke variable
        tableModel = new DefaultTableModel(
            new Object[]{"No", "Hari", "Jam", "Mata Kuliah", "SKS", "Kelas", "Ruangan", "Dosen"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat tabel tidak bisa diedit
            }
        };
        
        tblJadwal = new JTable(tableModel);
        tblJadwal.setRowHeight(30);
        tblJadwal.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        // Set column widths
        tblJadwal.getColumnModel().getColumn(0).setPreferredWidth(40);  // No
        tblJadwal.getColumnModel().getColumn(1).setPreferredWidth(80);  // Hari
        tblJadwal.getColumnModel().getColumn(2).setPreferredWidth(100); // Jam
        tblJadwal.getColumnModel().getColumn(3).setPreferredWidth(200); // Mata Kuliah
        tblJadwal.getColumnModel().getColumn(4).setPreferredWidth(40);  // SKS
        tblJadwal.getColumnModel().getColumn(5).setPreferredWidth(60);  // Kelas
        tblJadwal.getColumnModel().getColumn(6).setPreferredWidth(80);  // Ruangan
        tblJadwal.getColumnModel().getColumn(7).setPreferredWidth(150); // Dosen
        
        JScrollPane scrollPane = new JScrollPane(tblJadwal);
        
        // Refresh button
        JButton btnRefresh = new JButton("Refresh Jadwal");
        btnRefresh.addActionListener(e -> loadJadwal());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnRefresh);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void loadJadwal() {
        try {
            // Gunakan referensi tableModel yang sudah disimpan
            tableModel.setRowCount(0);
            
            List<Jadwal> jadwalList = jadwalService.getJadwalByNim(mahasiswa.getNim(), "2023/2024 Ganjil");
            
            if (jadwalList.isEmpty()) {
                // Tambahkan pesan jika tidak ada jadwal
                tableModel.addRow(new Object[]{
                    "-", "-", "-", "Tidak ada jadwal kuliah", "-", "-", "-", "-"
                });
                JOptionPane.showMessageDialog(this, 
                    "Tidak ada jadwal kuliah untuk semester ini.", 
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Tambahkan data jadwal
                int no = 1;
                for (Jadwal j : jadwalList) {
                    String jam = j.getJamMulai() + " - " + j.getJamSelesai();
                    tableModel.addRow(new Object[]{
                        no++,
                        j.getHari(),
                        jam,
                        j.getNamaMk(),
                        "3", // Default SKS, bisa disesuaikan
                        j.getKelas(),
                        j.getRuangan(),
                        j.getDosen()
                    });
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Jadwal berhasil dimuat: " + jadwalList.size() + " mata kuliah", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Error loading jadwal: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error memuat jadwal: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}