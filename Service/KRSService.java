package Service;

import Database.DatabaseConnection;
import Model.KRS;
import Model.MataKuliah;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class KRSService {
    private Connection connection;
    
    public KRSService() {
        connection = DatabaseConnection.getConnection();
        createTablesIfNotExists();
    }
    
    private void createTablesIfNotExists() {
        createMataKuliahTable();
        createKRSTable();
        insertSampleMataKuliah();
    }
    
    private void createMataKuliahTable() {
        String sql = "CREATE TABLE IF NOT EXISTS mata_kuliah (" +
                    "kode VARCHAR(10) PRIMARY KEY, " +
                    "nama VARCHAR(100) NOT NULL, " +
                    "sks INT NOT NULL, " +
                    "semester VARCHAR(10), " +
                    "jurusan VARCHAR(50), " +
                    "status VARCHAR(20) DEFAULT 'Aktif'" +
                    ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ Mata kuliah table created/verified");
        } catch (SQLException e) {
            System.out.println("❌ Error creating mata kuliah table: " + e.getMessage());
        }
    }
    
    private void createKRSTable() {
        String sql = "CREATE TABLE IF NOT EXISTS krs (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nim VARCHAR(15) NOT NULL, " +
                    "kode_mk VARCHAR(10) NOT NULL, " +
                    "semester VARCHAR(20) NOT NULL, " +
                    "tahun_akademik VARCHAR(20) NOT NULL, " +
                    "status VARCHAR(20) DEFAULT 'Diajukan', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (kode_mk) REFERENCES mata_kuliah(kode)" +
                    ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ KRS table created/verified");
        } catch (SQLException e) {
            System.out.println("❌ Error creating KRS table: " + e.getMessage());
            // Fallback tanpa foreign key
            createKRSTableFallback();
        }
    }
    
    private void createKRSTableFallback() {
        String sql = "CREATE TABLE IF NOT EXISTS krs (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "nim VARCHAR(15) NOT NULL, " +
                    "kode_mk VARCHAR(10) NOT NULL, " +
                    "semester VARCHAR(20) NOT NULL, " +
                    "tahun_akademik VARCHAR(20) NOT NULL, " +
                    "status VARCHAR(20) DEFAULT 'Diajukan', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ KRS table created with fallback");
        } catch (SQLException e) {
            System.out.println("❌ Error creating fallback KRS table: " + e.getMessage());
        }
    }
    
    private void insertSampleMataKuliah() {
        if (isMataKuliahTableEmpty()) {
            String[][] sampleData = {
                {"MK001", "Pemrograman Dasar", "3", "1", "Informatika"},
                {"MK002", "Basis Data", "3", "2", "Informatika"},
                {"MK003", "Struktur Data", "3", "3", "Informatika"},
                {"MK004", "Algoritma", "3", "2", "Informatika"},
                {"MK005", "Matematika Diskrit", "3", "1", "Informatika"},
                {"MK006", "Sistem Operasi", "3", "4", "Informatika"},
                {"MK007", "Jaringan Komputer", "3", "5", "Informatika"},
                {"MK008", "Pemrograman Web", "3", "4", "Informatika"},
                {"MK009", "Pemrograman Mobile", "3", "5", "Informatika"},
                {"MK010", "Kecerdasan Buatan", "3", "6", "Informatika"}
            };
            
            String sql = "INSERT INTO mata_kuliah (kode, nama, sks, semester, jurusan) VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                for (String[] data : sampleData) {
                    pstmt.setString(1, data[0]);
                    pstmt.setString(2, data[1]);
                    pstmt.setInt(3, Integer.parseInt(data[2]));
                    pstmt.setString(4, data[3]);
                    pstmt.setString(5, data[4]);
                    pstmt.executeUpdate();
                }
                System.out.println("✅ Sample mata kuliah data inserted");
            } catch (SQLException e) {
                System.out.println("❌ Error inserting sample data: " + e.getMessage());
            }
        }
    }
    
    private boolean isMataKuliahTableEmpty() {
        String sql = "SELECT COUNT(*) FROM mata_kuliah";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() && rs.getInt(1) == 0;
        } catch (SQLException e) {
            return true;
        }
    }
    
    // === KRS OPERATIONS ===
    public boolean addKRS(String nim, String kodeMk, String semester, String tahunAkademik) {
        // Check if already exists
        if (isKRSExists(nim, kodeMk, semester, tahunAkademik)) {
            return false;
        }
        
        String sql = "INSERT INTO krs (nim, kode_mk, semester, tahun_akademik, status) VALUES (?, ?, ?, ?, 'Diajukan')";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nim);
            stmt.setString(2, kodeMk);
            stmt.setString(3, semester);
            stmt.setString(4, tahunAkademik);
            
            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                System.out.println("✅ KRS added for NIM: " + nim + ", MK: " + kodeMk);
            }
            return result;
        } catch (SQLException e) {
            System.out.println("❌ Error adding KRS: " + e.getMessage());
            return false;
        }
    }
    
    public boolean addKRS(KRS krs) {
        return addKRS(krs.getNim(), krs.getKodeMk(), krs.getSemester(), krs.getTahunAkademik());
    }
    
    public List<KRS> getKRSByNIM(String nim) {
        List<KRS> krsList = new ArrayList<>();
        String sql = "SELECT k.*, m.nama as nama_mk, m.sks " +
                    "FROM krs k " +
                    "LEFT JOIN mata_kuliah m ON k.kode_mk = m.kode " +
                    "WHERE k.nim = ? " +
                    "ORDER BY k.semester, k.kode_mk";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                KRS krs = resultSetToKRS(rs);
                krs.setNamaMk(rs.getString("nama_mk"));
                krs.setSks(rs.getInt("sks"));
                krsList.add(krs);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting KRS by NIM: " + e.getMessage());
        }
        return krsList;
    }
    
    public List<KRS> getKRSByNim(String nim, String semester) {
        List<KRS> allKRS = getKRSByNIM(nim);
        if (semester != null && !semester.isEmpty()) {
            allKRS.removeIf(krs -> !semester.equals(krs.getSemester()));
        }
        return allKRS;
    }
    
    public List<KRS> getAllKRS() {
        List<KRS> krsList = new ArrayList<>();
        String sql = "SELECT k.*, m.nama as nama_mk, m.sks " +
                    "FROM krs k " +
                    "LEFT JOIN mata_kuliah m ON k.kode_mk = m.kode " +
                    "ORDER BY k.nim, k.semester, k.kode_mk";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                KRS krs = resultSetToKRS(rs);
                krs.setNamaMk(rs.getString("nama_mk"));
                krs.setSks(rs.getInt("sks"));
                krsList.add(krs);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting all KRS: " + e.getMessage());
        }
        return krsList;
    }
    
    public List<KRS> getKRSByStatus(String status) {
        List<KRS> krsList = new ArrayList<>();
        String sql = "SELECT k.*, m.nama as nama_mk, m.sks " +
                    "FROM krs k " +
                    "LEFT JOIN mata_kuliah m ON k.kode_mk = m.kode " +
                    "WHERE k.status = ? " +
                    "ORDER BY k.nim, k.semester";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                KRS krs = resultSetToKRS(rs);
                krs.setNamaMk(rs.getString("nama_mk"));
                krs.setSks(rs.getInt("sks"));
                krsList.add(krs);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting KRS by status: " + e.getMessage());
        }
        return krsList;
    }
    
    public boolean updateKRSStatus(int krsId, String status) {
        String sql = "UPDATE krs SET status = ? WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, krsId);
            
            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                System.out.println("✅ KRS status updated: " + krsId + " -> " + status);
            }
            return result;
        } catch (SQLException e) {
            System.out.println("❌ Error updating KRS status: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteKRS(int krsId) {
        String sql = "DELETE FROM krs WHERE id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, krsId);
            
            boolean result = stmt.executeUpdate() > 0;
            if (result) {
                System.out.println("✅ KRS deleted: " + krsId);
            }
            return result;
        } catch (SQLException e) {
            System.out.println("❌ Error deleting KRS: " + e.getMessage());
            return false;
        }
    }
    
    public boolean isKRSExists(String nim, String kodeMk, String semester, String tahunAkademik) {
        String sql = "SELECT COUNT(*) FROM krs WHERE nim = ? AND kode_mk = ? AND semester = ? AND tahun_akademik = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nim);
            stmt.setString(2, kodeMk);
            stmt.setString(3, semester);
            stmt.setString(4, tahunAkademik);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error checking KRS existence: " + e.getMessage());
            return false;
        }
    }
    
    public int getTotalSKSByNIMSemester(String nim, String semester, String tahunAkademik) {
        String sql = "SELECT SUM(m.sks) as total_sks " +
                    "FROM krs k " +
                    "LEFT JOIN mata_kuliah m ON k.kode_mk = m.kode " +
                    "WHERE k.nim = ? AND k.semester = ? AND k.tahun_akademik = ? AND k.status != 'Ditolak'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nim);
            stmt.setString(2, semester);
            stmt.setString(3, tahunAkademik);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total_sks");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting total SKS: " + e.getMessage());
        }
        return 0;
    }
    
    // === MATA KULIAH OPERATIONS ===
    public List<MataKuliah> getAllMataKuliah() {
        List<MataKuliah> list = new ArrayList<>();
        String sql = "SELECT * FROM mata_kuliah ORDER BY semester, kode";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                MataKuliah mk = resultSetToMataKuliah(rs);
                list.add(mk);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting mata kuliah: " + e.getMessage());
        }
        return list;
    }
    
    public List<MataKuliah> getMataKuliahBySemester(String semester) {
        List<MataKuliah> list = new ArrayList<>();
        String sql = "SELECT * FROM mata_kuliah WHERE semester = ? ORDER BY kode";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, semester);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                MataKuliah mk = resultSetToMataKuliah(rs);
                list.add(mk);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting mata kuliah by semester: " + e.getMessage());
        }
        return list;
    }
    
    public MataKuliah getMataKuliahByKode(String kode) {
        String sql = "SELECT * FROM mata_kuliah WHERE kode = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, kode);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return resultSetToMataKuliah(rs);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting mata kuliah by kode: " + e.getMessage());
        }
        return null;
    }
    
    // === HELPER METHODS ===
    private KRS resultSetToKRS(ResultSet rs) throws SQLException {
        KRS krs = new KRS();
        krs.setId(rs.getInt("id"));
        krs.setNim(rs.getString("nim"));
        krs.setKodeMk(rs.getString("kode_mk"));
        krs.setSemester(rs.getString("semester"));
        krs.setTahunAkademik(rs.getString("tahun_akademik"));
        krs.setStatus(rs.getString("status"));
        krs.setCreatedAt(rs.getTimestamp("created_at"));
        return krs;
    }
    
    private MataKuliah resultSetToMataKuliah(ResultSet rs) throws SQLException {
        MataKuliah mk = new MataKuliah();
        mk.setKode(rs.getString("kode"));
        mk.setNama(rs.getString("nama"));
        mk.setSks(rs.getInt("sks"));
        mk.setSemester(rs.getString("semester"));
        mk.setJurusan(rs.getString("jurusan"));
        mk.setStatus(rs.getString("status"));
        return mk;
    }
    
    // === COMPATIBILITY METHODS ===
    public int getTotalSKS(String nim, String semester) {
        return getTotalSKSByNIMSemester(nim, semester, "2023/2024");
    }
    
    public boolean isMataKuliahTaken(String nim, String kodeMk, String semester) {
        return isKRSExists(nim, kodeMk, semester, "2023/2024");
    }
    
    public boolean submitKRS(String nim, String semester) {
        try {
            List<KRS> krsList = getKRSByNim(nim, semester);
            boolean allSuccess = true;
            
            for (KRS krs : krsList) {
                if ("Diajukan".equals(krs.getStatus())) {
                    if (!updateKRSStatus(krs.getId(), "Disetujui")) {
                        allSuccess = false;
                    }
                }
            }
            
            if (allSuccess) {
                System.out.println("✅ KRS submitted for NIM: " + nim + ", Semester: " + semester);
            }
            return allSuccess;
        } catch (Exception e) {
            System.out.println("❌ Error submitting KRS: " + e.getMessage());
            return false;
        }
    }
    
    public boolean validateKRS(String nim, String semester) {
        int totalSKS = getTotalSKS(nim, semester);
        return totalSKS <= 24;
    }
    
    public boolean canAddMoreSKS(String nim, String semester, int additionalSKS) {
        int currentSKS = getTotalSKS(nim, semester);
        return (currentSKS + additionalSKS) <= 24;
    }
    
    // Method untuk mendapatkan KRS yang menunggu persetujuan (untuk admin)
    public List<Object[]> getKRSMenungguPersetujuan() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT k.id, k.nim, m.nama as nama_mahasiswa, k.kode_mk, mk.nama as nama_mk, " +
                    "k.semester, k.tahun_akademik, k.status, k.created_at " +
                    "FROM krs k " +
                    "LEFT JOIN mahasiswa m ON k.nim = m.nim " +
                    "LEFT JOIN mata_kuliah mk ON k.kode_mk = mk.kode " +
                    "WHERE k.status = 'Diajukan' " +
                    "ORDER BY k.created_at";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("nim"),
                    rs.getString("nama_mahasiswa"),
                    rs.getString("kode_mk"),
                    rs.getString("nama_mk"),
                    rs.getString("semester"),
                    rs.getString("tahun_akademik"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error getting KRS menunggu persetujuan: " + e.getMessage());
        }
        return list;
    }
}