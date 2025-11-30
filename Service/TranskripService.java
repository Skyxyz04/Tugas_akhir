package Service;

import Database.DatabaseConnection;
import Model.Nilai;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TranskripService {
    private Connection connection;
    private AkademikService akademikService;
    
    public TranskripService() {
        connection = DatabaseConnection.getConnection();
        akademikService = new AkademikService();
    }
    
    public List<Nilai> getTranskrip(String nim) {
        List<Nilai> list = new ArrayList<>();
        String sql = "SELECT n.*, mk.nama as nama_mk, mk.sks FROM nilai n " +
                    "JOIN mata_kuliah mk ON n.kode_mk = mk.kode " +
                    "WHERE n.nim = ? ORDER BY n.semester, mk.kode";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Nilai nilai = new Nilai();
                nilai.setId(rs.getInt("id"));
                nilai.setNim(rs.getString("nim"));
                nilai.setKodeMk(rs.getString("kode_mk"));
                nilai.setNamaMk(rs.getString("nama_mk"));
                nilai.setSks(rs.getInt("sks"));
                nilai.setNilaiHuruf(rs.getString("nilai_huruf"));
                nilai.setSemester(rs.getString("semester"));
                
                // Set nilai angka dari nilai huruf
                nilai.setNilaiAngka(nilai.getNilaiAngkaFromHuruf());
                
                list.add(nilai);
            }
        } catch (SQLException e) {
            System.err.println("Error in getTranskrip: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    
    public double calculateIPK(String nim) {
        List<Nilai> transkrip = getTranskrip(nim);
        double totalBobot = 0;
        int totalSKS = 0;
        
        for (Nilai nilai : transkrip) {
            totalBobot += nilai.getBobot();
            totalSKS += nilai.getSks();
        }
        
        return totalSKS > 0 ? totalBobot / totalSKS : 0.0;
    }
    
    public double calculateIPS(String nim, String semester) {
        List<Nilai> khs = akademikService.getKHS(nim, semester);
        double totalBobot = 0;
        int totalSKS = 0;
        
        for (Nilai nilai : khs) {
            totalBobot += nilai.getBobot();
            totalSKS += nilai.getSks();
        }
        
        return totalSKS > 0 ? totalBobot / totalSKS : 0.0;
    }
    
    // Method tambahan untuk statistik transkrip
    public int getTotalSKS(String nim) {
        List<Nilai> transkrip = getTranskrip(nim);
        int totalSKS = 0;
        
        for (Nilai nilai : transkrip) {
            totalSKS += nilai.getSks();
        }
        
        return totalSKS;
    }
    
    public int getTotalMataKuliah(String nim) {
        return getTranskrip(nim).size();
    }
    
    public int getMataKuliahLulus(String nim) {
        List<Nilai> transkrip = getTranskrip(nim);
        int count = 0;
        
        for (Nilai nilai : transkrip) {
            if (nilai.getNilaiHuruf() != null && 
                !nilai.getNilaiHuruf().toUpperCase().equals("E") &&
                !nilai.getNilaiHuruf().toUpperCase().equals("D")) {
                count++;
            }
        }
        
        return count;
    }
    
    public String getPredikatKelulusan(double ipk) {
        if (ipk >= 3.51) return "Dengan Pujian (Cum Laude)";
        else if (ipk >= 3.01) return "Sangat Memuaskan";
        else if (ipk >= 2.51) return "Memuaskan";
        else if (ipk >= 2.00) return "Cukup";
        else return "Tidak Lulus";
    }
    
    // Method untuk mendapatkan ringkasan transkrip
    public TranskripSummary getTranskripSummary(String nim) {
        List<Nilai> transkrip = getTranskrip(nim);
        double ipk = calculateIPK(nim);
        int totalSKS = getTotalSKS(nim);
        int totalMK = getTotalMataKuliah(nim);
        int mkLulus = getMataKuliahLulus(nim);
        String predikat = getPredikatKelulusan(ipk);
        
        return new TranskripSummary(ipk, totalSKS, totalMK, mkLulus, predikat);
    }
    
    // Inner class untuk summary transkrip
    public static class TranskripSummary {
        private double ipk;
        private int totalSKS;
        private int totalMataKuliah;
        private int mataKuliahLulus;
        private String predikat;
        
        public TranskripSummary(double ipk, int totalSKS, int totalMataKuliah, 
                               int mataKuliahLulus, String predikat) {
            this.ipk = ipk;
            this.totalSKS = totalSKS;
            this.totalMataKuliah = totalMataKuliah;
            this.mataKuliahLulus = mataKuliahLulus;
            this.predikat = predikat;
        }
        
        // Getters
        public double getIpk() { return ipk; }
        public int getTotalSKS() { return totalSKS; }
        public int getTotalMataKuliah() { return totalMataKuliah; }
        public int getMataKuliahLulus() { return mataKuliahLulus; }
        public String getPredikat() { return predikat; }
        
        @Override
        public String toString() {
            return String.format("IPK: %.2f, SKS: %d, MK: %d/%d, Predikat: %s", 
                ipk, totalSKS, mataKuliahLulus, totalMataKuliah, predikat);
        }
    }
}