package Service;

import Database.DatabaseConnection;
import Model.Admin;
import Model.Mahasiswa;
import Model.Dosen;
import Model.MataKuliah;
import Model.Nilai;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class AdminService {
    private Connection connection;
    
    public AdminService() {
        connection = DatabaseConnection.getConnection();
    }
    
    // === AUTHENTICATION ===
    public Admin login(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin();
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setNama(rs.getString("nama"));
                admin.setRole(rs.getString("role"));
                admin.setEmail(rs.getString("email"));
                return admin;
            }
        } catch (SQLException e) {
            showError("Login Error", e);
        }
        return null;
    }
    
    // === TABLE INITIALIZATION ===
    public void initializeAdminTable() {
        String createTableSQL = 
            "CREATE TABLE IF NOT EXISTS admin (" +
            "id INT PRIMARY KEY AUTO_INCREMENT, " +
            "username VARCHAR(50) UNIQUE NOT NULL, " +
            "password VARCHAR(100) NOT NULL, " +
            "nama VARCHAR(100) NOT NULL, " +
            "email VARCHAR(100), " +
            "role VARCHAR(20) DEFAULT 'admin', " +
            "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
        
        String insertAdminSQL = 
            "INSERT IGNORE INTO admin (username, password, nama, email, role) VALUES (?, ?, ?, ?, ?)";
        
        try {
            // Create table
            try (PreparedStatement stmt = connection.prepareStatement(createTableSQL)) {
                stmt.execute();
            }
            
            // Insert default admin
            try (PreparedStatement stmt = connection.prepareStatement(insertAdminSQL)) {
                stmt.setString(1, "admin");
                stmt.setString(2, "admin123");
                stmt.setString(3, "Administrator System");
                stmt.setString(4, "admin@siasat.uksw.edu");
                stmt.setString(5, "superadmin");
                stmt.execute();
            }
            
            System.out.println("✅ Admin table initialized successfully");
            
        } catch (SQLException e) {
            showError("Database Initialization Error", e);
        }
    }
    
    // === MAHASISWA MANAGEMENT ===
    public List<Mahasiswa> getAllMahasiswa() {
        List<Mahasiswa> list = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa ORDER BY nim";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapToMahasiswa(rs));
            }
        } catch (SQLException e) {
            showError("Get Mahasiswa Error", e);
        }
        return list;
    }
    
    public boolean addMahasiswa(Mahasiswa mhs) {
        String sql = 
            "INSERT INTO mahasiswa (nim, nama, email, program_studi, fakultas, angkatan, status, password, no_telepon, alamat) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            prepareMahasiswaStatement(stmt, mhs);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Add Mahasiswa Error", e);
            JOptionPane.showMessageDialog(null, 
                "Error: " + e.getMessage() + "\nMungkin NIM sudah ada.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean updateMahasiswa(Mahasiswa mhs) {
        String sql = 
            "UPDATE mahasiswa SET nama=?, email=?, program_studi=?, fakultas=?, angkatan=?, " +
            "status=?, no_telepon=?, alamat=? WHERE nim=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, mhs.getNama());
            stmt.setString(2, mhs.getEmail());
            stmt.setString(3, mhs.getProgramStudi());
            stmt.setString(4, mhs.getFakultas());
            stmt.setInt(5, mhs.getAngkatan());
            stmt.setString(6, mhs.getStatus());
            stmt.setString(7, mhs.getNoTelepon());
            stmt.setString(8, mhs.getAlamat());
            stmt.setString(9, mhs.getNim());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Update Mahasiswa Error", e);
            return false;
        }
    }
    
    public boolean deleteMahasiswa(String nim) {
        String sql = "DELETE FROM mahasiswa WHERE nim = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nim);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Delete Mahasiswa Error", e);
            return false;
        }
    }
    
    public Mahasiswa getMahasiswaByNim(String nim) {
        String sql = "SELECT * FROM mahasiswa WHERE nim = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapToMahasiswa(rs);
            }
        } catch (SQLException e) {
            showError("Get Mahasiswa By NIM Error", e);
        }
        return null;
    }
    
    // Method untuk mendapatkan semester mahasiswa berdasarkan NIM
    public String getSemesterByNim(String nim) {
        String sql = "SELECT semester FROM mahasiswa WHERE nim = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("semester");
            }
        } catch (SQLException e) {
            showError("Get Semester Error", e);
        }
        return null;
    }
    
    // Method untuk update semester mahasiswa
    public boolean updateSemester(String nim, String semester) {
        String sql = "UPDATE mahasiswa SET semester = ? WHERE nim = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, semester);
            stmt.setString(2, nim);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Update Semester Error", e);
            return false;
        }
    }
    
    private Mahasiswa mapToMahasiswa(ResultSet rs) throws SQLException {
        Mahasiswa mhs = new Mahasiswa();
        mhs.setNim(rs.getString("nim"));
        mhs.setNama(rs.getString("nama"));
        mhs.setEmail(rs.getString("email"));
        mhs.setProgramStudi(rs.getString("program_studi"));
        mhs.setFakultas(rs.getString("fakultas"));
        mhs.setAngkatan(rs.getInt("angkatan"));
        mhs.setStatus(rs.getString("status"));
        mhs.setNoTelepon(rs.getString("no_telepon"));
        mhs.setAlamat(rs.getString("alamat"));
        mhs.setSemester(rs.getString("semester")); // FIXED: Menambahkan mapping semester
        return mhs;
    }
    
    private void prepareMahasiswaStatement(PreparedStatement stmt, Mahasiswa mhs) throws SQLException {
        // Set default values
        if (mhs.getStatus() == null) mhs.setStatus("Aktif");
        if (mhs.getPassword() == null) mhs.setPassword("mahasiswa123");
        if (mhs.getSemester() == null) mhs.setSemester("1"); // Default semester
        
        stmt.setString(1, mhs.getNim());
        stmt.setString(2, mhs.getNama());
        stmt.setString(3, mhs.getEmail());
        stmt.setString(4, mhs.getProgramStudi());
        stmt.setString(5, mhs.getFakultas());
        stmt.setInt(6, mhs.getAngkatan());
        stmt.setString(7, mhs.getStatus());
        stmt.setString(8, mhs.getPassword());
        stmt.setString(9, mhs.getNoTelepon());
        stmt.setString(10, mhs.getAlamat());
    }
    
    // === DOSEN MANAGEMENT ===
    public List<Dosen> getAllDosen() {
        List<Dosen> list = new ArrayList<>();
        String sql = "SELECT * FROM dosen ORDER BY nidn";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapToDosen(rs));
            }
        } catch (SQLException e) {
            showError("Get Dosen Error", e);
        }
        return list;
    }
    
    public boolean addDosen(Dosen dosen) {
        String sql = 
            "INSERT INTO dosen (nidn, nama, email, program_studi, fakultas, status, password, no_telepon, alamat) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            prepareDosenStatement(stmt, dosen);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Add Dosen Error", e);
            return false;
        }
    }
    
    public boolean updateDosen(Dosen dosen) {
        String sql = 
            "UPDATE dosen SET nama=?, email=?, program_studi=?, fakultas=?, status=?, " +
            "no_telepon=?, alamat=? WHERE nidn=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dosen.getNama());
            stmt.setString(2, dosen.getEmail());
            stmt.setString(3, dosen.getProgramStudi());
            stmt.setString(4, dosen.getFakultas());
            stmt.setString(5, dosen.getStatus());
            stmt.setString(6, dosen.getNoTelepon());
            stmt.setString(7, dosen.getAlamat());
            stmt.setString(8, dosen.getNidn());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Update Dosen Error", e);
            return false;
        }
    }
    
    public boolean deleteDosen(String nidn) {
        return executeUpdate("DELETE FROM dosen WHERE nidn = ?", nidn) > 0;
    }
    
    private Dosen mapToDosen(ResultSet rs) throws SQLException {
        Dosen dosen = new Dosen();
        dosen.setNidn(rs.getString("nidn"));
        dosen.setNama(rs.getString("nama"));
        dosen.setEmail(rs.getString("email"));
        dosen.setProgramStudi(rs.getString("program_studi"));
        dosen.setFakultas(rs.getString("fakultas"));
        dosen.setStatus(rs.getString("status"));
        dosen.setNoTelepon(rs.getString("no_telepon"));
        dosen.setAlamat(rs.getString("alamat"));
        return dosen;
    }
    
    private void prepareDosenStatement(PreparedStatement stmt, Dosen dosen) throws SQLException {
        if (dosen.getStatus() == null) dosen.setStatus("Aktif");
        if (dosen.getPassword() == null) dosen.setPassword("dosen123");
        
        stmt.setString(1, dosen.getNidn());
        stmt.setString(2, dosen.getNama());
        stmt.setString(3, dosen.getEmail());
        stmt.setString(4, dosen.getProgramStudi());
        stmt.setString(5, dosen.getFakultas());
        stmt.setString(6, dosen.getStatus());
        stmt.setString(7, dosen.getPassword());
        stmt.setString(8, dosen.getNoTelepon());
        stmt.setString(9, dosen.getAlamat());
    }
    
    // === MATA KULIAH MANAGEMENT ===
    public List<MataKuliah> getAllMataKuliah() {
        List<MataKuliah> list = new ArrayList<>();
        String sql = "SELECT * FROM mata_kuliah ORDER BY kode";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapToMataKuliah(rs));
            }
        } catch (SQLException e) {
            showError("Get Mata Kuliah Error", e);
        }
        return list;
    }
    
    public boolean addMataKuliah(MataKuliah mk) {
        String sql = 
            "INSERT INTO mata_kuliah (kode, nama, sks, semester, jurusan, status) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, mk.getKode());
            stmt.setString(2, mk.getNama());
            stmt.setInt(3, mk.getSks());
            stmt.setString(4, mk.getSemester());
            stmt.setString(5, mk.getJurusan());
            stmt.setString(6, "Aktif");
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Add Mata Kuliah Error", e);
            return false;
        }
    }
    
    public boolean updateMataKuliah(MataKuliah mk) {
        String sql = 
            "UPDATE mata_kuliah SET nama=?, sks=?, semester=?, jurusan=?, status=? WHERE kode=?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, mk.getNama());
            stmt.setInt(2, mk.getSks());
            stmt.setString(3, mk.getSemester());
            stmt.setString(4, mk.getJurusan());
            stmt.setString(5, mk.getStatus());
            stmt.setString(6, mk.getKode());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Update Mata Kuliah Error", e);
            return false;
        }
    }
    
    public boolean deleteMataKuliah(String kode) {
        return executeUpdate("DELETE FROM mata_kuliah WHERE kode = ?", kode) > 0;
    }
    
    private MataKuliah mapToMataKuliah(ResultSet rs) throws SQLException {
        MataKuliah mk = new MataKuliah();
        mk.setKode(rs.getString("kode"));
        mk.setNama(rs.getString("nama"));
        mk.setSks(rs.getInt("sks"));
        mk.setSemester(rs.getString("semester"));
        mk.setJurusan(rs.getString("jurusan"));
        mk.setStatus(rs.getString("status"));
        return mk;
    }
    
    // === NILAI MANAGEMENT ===
    public boolean inputNilai(String nim, String kodeMk, String nilai, String semester) {
        String sql = 
            "INSERT INTO nilai (nim, kode_mk, nilai, semester) VALUES (?, ?, ?, ?) " +
            "ON DUPLICATE KEY UPDATE nilai = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nim);
            stmt.setString(2, kodeMk);
            stmt.setString(3, nilai);
            stmt.setString(4, semester);
            stmt.setString(5, nilai);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            showError("Input Nilai Error", e);
            return false;
        }
    }
    
    public List<Nilai> getAllNilai() {
        List<Nilai> list = new ArrayList<>();
        String sql = 
            "SELECT n.*, m.nama as nama_mahasiswa, mk.nama as nama_mk " +
            "FROM nilai n " +
            "JOIN mahasiswa m ON n.nim = m.nim " +
            "JOIN mata_kuliah mk ON n.kode_mk = mk.kode " +
            "ORDER BY n.semester, n.nim";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(mapToNilai(rs));
            }
        } catch (SQLException e) {
            showError("Get Nilai Error", e);
        }
        return list;
    }
    
    private Nilai mapToNilai(ResultSet rs) throws SQLException {
        Nilai nilai = new Nilai();
        nilai.setId(rs.getInt("id"));
        nilai.setNim(rs.getString("nim"));
        nilai.setKodeMk(rs.getString("kode_mk"));
        nilai.setNilai(rs.getString("nilai"));
        nilai.setSemester(rs.getString("semester"));
        nilai.setNamaMahasiswa(rs.getString("nama_mahasiswa"));
        nilai.setNamaMk(rs.getString("nama_mk"));
        return nilai;
    }
    
    // === KRS MANAGEMENT ===
    public List<Object[]> getPendingKRS() {
        List<Object[]> list = new ArrayList<>();
        String sql = 
            "SELECT k.id, k.nim, m.nama as nama_mahasiswa, k.kode_mk, mk.nama as nama_mk, " +
            "k.semester, k.tahun_akademik, k.status, k.created_at " +
            "FROM krs k " +
            "LEFT JOIN mahasiswa m ON k.nim = m.nim " +
            "LEFT JOIN mata_kuliah mk ON k.kode_mk = mk.kode " +
            "WHERE k.status = 'Diajukan' " +
            "ORDER BY k.created_at";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
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
            showError("Get Pending KRS Error", e);
        }
        return list;
    }
    
    public boolean approveKRS(int krsId) {
        return executeUpdate("UPDATE krs SET status = 'Disetujui' WHERE id = ?", krsId) > 0;
    }
    
    public boolean rejectKRS(int krsId) {
        return executeUpdate("UPDATE krs SET status = 'Ditolak' WHERE id = ?", krsId) > 0;
    }
    
    // === STATISTICS ===
    public int getTotalMahasiswa() {
        return getCount("SELECT COUNT(*) as total FROM mahasiswa");
    }
    
    public int getTotalDosen() {
        return getCount("SELECT COUNT(*) as total FROM dosen");
    }
    
    public int getTotalMataKuliah() {
        return getCount("SELECT COUNT(*) as total FROM mata_kuliah");
    }
    
    public int getMahasiswaAktif() {
        return getCount("SELECT COUNT(*) as total FROM mahasiswa WHERE status = 'Aktif'");
    }
    
    public int getDosenAktif() {
        return getCount("SELECT COUNT(*) as total FROM dosen WHERE status = 'Aktif'");
    }
    
    // === HELPER METHODS ===
    private int getCount(String sql) {
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            return rs.next() ? rs.getInt("total") : 0;
        } catch (SQLException e) {
            showError("Count Error", e);
            return 0;
        }
    }
    
    private int executeUpdate(String sql, Object... params) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            showError("Execute Update Error", e);
            return 0;
        }
    }
    
    // === COMBOBOX DATA ===
    public ResultSet getActiveMahasiswa() {
        return getResultSet("SELECT nim, nama, program_studi FROM mahasiswa WHERE status = 'Aktif' ORDER BY nim");
    }
    
    public ResultSet getActiveMataKuliah() {
        return getResultSet("SELECT kode, nama, sks FROM mata_kuliah WHERE status = 'Aktif' ORDER BY kode");
    }
    
    private ResultSet getResultSet(String sql) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            showError("Get ResultSet Error", e);
            return null;
        }
    }
    
    // === ERROR HANDLING ===
    private void showError(String title, SQLException e) {
        System.err.println("❌ " + title + ": " + e.getMessage());
        JOptionPane.showMessageDialog(null, 
            title + ": " + e.getMessage(), 
            "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    public void createAdminTableIfNotExists() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ResultSet getDaftarMataKuliah() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public ResultSet getDaftarMahasiswa() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<Object[]> getKRSMenungguPersetujuan() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}