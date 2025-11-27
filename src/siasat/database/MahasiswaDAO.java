package siasat.database;

import siasat.model.Mahasiswa;
import siasat.model.MataKuliah;
import siasat.model.Nilai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaDAO {
    
    // Method untuk mendapatkan mahasiswa by ID
    public Mahasiswa getMahasiswaById(int id) {
        String sql = "SELECT * FROM mahasiswa WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Mahasiswa mahasiswa = extractMahasiswaFromResultSet(rs);
                System.out.println("ðŸ” Mahasiswa ditemukan: " + mahasiswa.getNama());
                return mahasiswa;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error get mahasiswa by id: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return null;
    }
    
    // Method untuk mendapatkan mahasiswa by NIM
    public Mahasiswa getMahasiswaByNim(String nim) {
        String sql = "SELECT * FROM mahasiswa WHERE nim = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, nim);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Mahasiswa mahasiswa = extractMahasiswaFromResultSet(rs);
                System.out.println("ðŸ” Mahasiswa ditemukan: " + mahasiswa.getNim() + " - " + mahasiswa.getNama());
                return mahasiswa;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error get mahasiswa by nim: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return null;
    }
    
    // Method untuk mendapatkan semua mahasiswa
    public List<Mahasiswa> getAllMahasiswa() {
        List<Mahasiswa> mahasiswaList = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa ORDER BY nim";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                mahasiswaList.add(extractMahasiswaFromResultSet(rs));
            }
            
            System.out.println("ðŸ“Š Total mahasiswa: " + mahasiswaList.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get all mahasiswa: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, null);
        }
        return mahasiswaList;
    }
    
    // Method untuk mendapatkan mahasiswa by program studi
    public List<Mahasiswa> getMahasiswaByProgramStudi(String programStudi) {
        List<Mahasiswa> mahasiswaList = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa WHERE program_studi = ? ORDER BY nim";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, programStudi);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                mahasiswaList.add(extractMahasiswaFromResultSet(rs));
            }
            
            System.out.println("ðŸ“Š Total mahasiswa " + programStudi + ": " + mahasiswaList.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get mahasiswa by program studi: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return mahasiswaList;
    }
    
    // Method untuk mendapatkan mahasiswa by angkatan
    public List<Mahasiswa> getMahasiswaByAngkatan(String angkatan) {
        List<Mahasiswa> mahasiswaList = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa WHERE angkatan = ? ORDER BY nim";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, angkatan);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                mahasiswaList.add(extractMahasiswaFromResultSet(rs));
            }
            
            System.out.println("ðŸ“Š Total mahasiswa angkatan " + angkatan + ": " + mahasiswaList.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get mahasiswa by angkatan: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return mahasiswaList;
    }
    
    // Method untuk menambahkan mahasiswa baru
    public boolean addMahasiswa(Mahasiswa mahasiswa) {
        if (!mahasiswa.isValid()) {
            System.err.println("âŒ Data mahasiswa tidak valid");
            return false;
        }
        
        String sql = "INSERT INTO mahasiswa (nim, nama, program_studi, fakultas, angkatan, email, no_telepon) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, mahasiswa.getNim());
            pstmt.setString(2, mahasiswa.getNama());
            pstmt.setString(3, mahasiswa.getProgramStudi());
            pstmt.setString(4, mahasiswa.getFakultas());
            pstmt.setString(5, mahasiswa.getAngkatan());
            pstmt.setString(6, mahasiswa.getEmail());
            pstmt.setString(7, mahasiswa.getNoTelepon());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("âœ… Mahasiswa berhasil ditambahkan: " + mahasiswa.getNim() + " - " + mahasiswa.getNama());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error add mahasiswa: " + e.getMessage());
            if (e.getSQLState().equals("23000")) {
                System.err.println("âš ï¸  NIM sudah digunakan: " + mahasiswa.getNim());
            }
        } finally {
            closeResources(null, pstmt, null);
        }
        return false;
    }
    
    // Method untuk update mahasiswa
    public boolean updateMahasiswa(Mahasiswa mahasiswa) {
        if (!mahasiswa.isValid()) {
            System.err.println("âŒ Data mahasiswa tidak valid");
            return false;
        }
        
        String sql = "UPDATE mahasiswa SET nama = ?, program_studi = ?, fakultas = ?, angkatan = ?, email = ?, no_telepon = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, mahasiswa.getNama());
            pstmt.setString(2, mahasiswa.getProgramStudi());
            pstmt.setString(3, mahasiswa.getFakultas());
            pstmt.setString(4, mahasiswa.getAngkatan());
            pstmt.setString(5, mahasiswa.getEmail());
            pstmt.setString(6, mahasiswa.getNoTelepon());
            pstmt.setInt(7, mahasiswa.getId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("âœ… Mahasiswa berhasil diupdate: " + mahasiswa.getNim() + " - " + mahasiswa.getNama());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error update mahasiswa: " + e.getMessage());
        } finally {
            closeResources(null, pstmt, null);
        }
        return false;
    }
    
    // Method untuk mendapatkan nilai mahasiswa
    public List<Nilai> getNilaiByMahasiswaId(int mahasiswaId) {
        List<Nilai> nilaiList = new ArrayList<>();
        String sql = "SELECT n.*, mk.kode as mk_kode, mk.nama as mk_nama, mk.sks " +
                    "FROM nilai n " +
                    "JOIN mata_kuliah mk ON n.mata_kuliah_id = mk.id " +
                    "WHERE n.mahasiswa_id = ? " +
                    "ORDER BY n.tahun_ajaran, n.semester";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, mahasiswaId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Nilai nilai = new Nilai();
                nilai.setId(rs.getInt("id"));
                nilai.setMahasiswaId(rs.getInt("mahasiswa_id"));
                nilai.setMataKuliahId(rs.getInt("mata_kuliah_id"));
                nilai.setNilai(rs.getDouble("nilai"));
                nilai.setGrade(rs.getString("grade"));
                nilai.setSemester(rs.getString("semester"));
                nilai.setTahunAjaran(rs.getString("tahun_ajaran"));
                nilai.setMataKuliahKode(rs.getString("mk_kode"));
                nilai.setMataKuliahNama(rs.getString("mk_nama"));
                nilai.setSks(rs.getInt("sks"));
                
                nilaiList.add(nilai);
            }
            
            System.out.println("ðŸ“Š Total nilai untuk mahasiswa ID " + mahasiswaId + ": " + nilaiList.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get nilai by mahasiswa id: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return nilaiList;
    }
    
    // Method untuk mendapatkan mata kuliah by program studi
    public List<MataKuliah> getMataKuliahByProgramStudi(String programStudi) {
        List<MataKuliah> matkulList = new ArrayList<>();
        String sql = "SELECT * FROM mata_kuliah WHERE program_studi = ? ORDER BY semester, kode";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, programStudi);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                MataKuliah matkul = new MataKuliah();
                matkul.setId(rs.getInt("id"));
                matkul.setKode(rs.getString("kode"));
                matkul.setNama(rs.getString("nama"));
                matkul.setSks(rs.getInt("sks"));
                matkul.setSemester(rs.getString("semester"));
                matkul.setProgramStudi(rs.getString("program_studi"));
                matkul.setDosenPengampu(rs.getInt("dosen_pengampu"));
                matkulList.add(matkul);
            }
            
            System.out.println("ðŸ“š Total mata kuliah " + programStudi + ": " + matkulList.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get mata kuliah by program studi: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return matkulList;
    }
    
    // Method untuk menghitung IPK
    public double calculateIPK(int mahasiswaId) {
        String sql = "SELECT n.nilai, n.grade, mk.sks FROM nilai n " +
                    "JOIN mata_kuliah mk ON n.mata_kuliah_id = mk.id " +
                    "WHERE n.mahasiswa_id = ?";
        double totalBobot = 0;
        int totalSKS = 0;
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, mahasiswaId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String grade = rs.getString("grade");
                int sks = rs.getInt("sks");
                
                double bobot = convertGradeToBobot(grade);
                totalBobot += bobot * sks;
                totalSKS += sks;
            }
            
            if (totalSKS > 0) {
                double ipk = totalBobot / totalSKS;
                System.out.println("ðŸ“ˆ IPK mahasiswa ID " + mahasiswaId + ": " + String.format("%.2f", ipk));
                return ipk;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error calculate IPK: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return 0.0;
    }
    
    // Method untuk mendapatkan total SKS
    public int getTotalSKS(int mahasiswaId) {
        String sql = "SELECT SUM(mk.sks) as total_sks FROM nilai n " +
                    "JOIN mata_kuliah mk ON n.mata_kuliah_id = mk.id " +
                    "WHERE n.mahasiswa_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, mahasiswaId);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int totalSKS = rs.getInt("total_sks");
                System.out.println("ðŸ“š Total SKS mahasiswa ID " + mahasiswaId + ": " + totalSKS);
                return totalSKS;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error get total SKS: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return 0;
    }
    
    // Method untuk mendapatkan statistik mahasiswa
    public java.util.Map<String, Integer> getMahasiswaStatistics() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        
        // Total mahasiswa
        String sqlTotal = "SELECT COUNT(*) as total FROM mahasiswa";
        // Mahasiswa by program studi
        String sqlProdi = "SELECT program_studi, COUNT(*) as count FROM mahasiswa GROUP BY program_studi";
        // Mahasiswa by angkatan
        String sqlAngkatan = "SELECT angkatan, COUNT(*) as count FROM mahasiswa GROUP BY angkatan";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            
            // Total mahasiswa
            rs = stmt.executeQuery(sqlTotal);
            if (rs.next()) {
                stats.put("TOTAL", rs.getInt("total"));
            }
            
            // By program studi
            rs = stmt.executeQuery(sqlProdi);
            while (rs.next()) {
                stats.put("PRODI_" + rs.getString("program_studi"), rs.getInt("count"));
            }
            
            // By angkatan
            rs = stmt.executeQuery(sqlAngkatan);
            while (rs.next()) {
                stats.put("ANGKATAN_" + rs.getString("angkatan"), rs.getInt("count"));
            }
            
        } catch (SQLException e) {
            System.err.println("âŒ Error get mahasiswa statistics: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, null);
        }
        return stats;
    }
    
    // Helper method untuk extract mahasiswa dari ResultSet
    private Mahasiswa extractMahasiswaFromResultSet(ResultSet rs) throws SQLException {
        Mahasiswa mhs = new Mahasiswa();
        mhs.setId(rs.getInt("id"));
        mhs.setNim(rs.getString("nim"));
        mhs.setNama(rs.getString("nama"));
        mhs.setProgramStudi(rs.getString("program_studi"));
        mhs.setFakultas(rs.getString("fakultas"));
        mhs.setAngkatan(rs.getString("angkatan"));
        mhs.setEmail(rs.getString("email"));
        mhs.setNoTelepon(rs.getString("no_telepon"));
        return mhs;
    }
    
    // Helper method untuk konversi grade ke bobot
    private double convertGradeToBobot(String grade) {
        switch (grade) {
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D": return 1.0;
            case "E": return 0.0;
            default: return 0.0;
        }
    }
    
    // Helper method untuk menutup resources
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            // Jangan close connection di sini
        } catch (SQLException e) {
            System.err.println("âŒ Error closing resources: " + e.getMessage());
        }
    }
} 