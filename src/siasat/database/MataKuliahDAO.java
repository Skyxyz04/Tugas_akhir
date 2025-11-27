package siasat.database;

import siasat.model.MataKuliah;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MataKuliahDAO {
    
    // Method untuk mendapatkan semua mata kuliah
    public List<MataKuliah> getAllMataKuliah() {
        List<MataKuliah> matkulList = new ArrayList<>();
        String sql = "SELECT mk.*, d.nama as nama_dosen " +
                    "FROM mata_kuliah mk " +
                    "LEFT JOIN dosen d ON mk.dosen_pengampu = d.id " +
                    "ORDER BY mk.program_studi, mk.semester, mk.kode";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                MataKuliah matkul = new MataKuliah();
                matkul.setId(rs.getInt("id"));
                matkul.setKode(rs.getString("kode"));
                matkul.setNama(rs.getString("nama"));
                matkul.setSks(rs.getInt("sks"));
                matkul.setSemester(rs.getString("semester"));
                matkul.setProgramStudi(rs.getString("program_studi"));
                matkul.setDosenPengampu(rs.getInt("dosen_pengampu"));
                matkul.setNamaDosen(rs.getString("nama_dosen"));
                matkulList.add(matkul);
            }
            
            System.out.println("ðŸ“š Total mata kuliah: " + matkulList.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get all mata kuliah: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, null);
        }
        return matkulList;
    }
    
    // Method untuk mendapatkan mata kuliah by ID
    public MataKuliah getMataKuliahById(int id) {
        String sql = "SELECT mk.*, d.nama as nama_dosen FROM mata_kuliah mk " +
                    "LEFT JOIN dosen d ON mk.dosen_pengampu = d.id " +
                    "WHERE mk.id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                MataKuliah matkul = new MataKuliah();
                matkul.setId(rs.getInt("id"));
                matkul.setKode(rs.getString("kode"));
                matkul.setNama(rs.getString("nama"));
                matkul.setSks(rs.getInt("sks"));
                matkul.setSemester(rs.getString("semester"));
                matkul.setProgramStudi(rs.getString("program_studi"));
                matkul.setDosenPengampu(rs.getInt("dosen_pengampu"));
                matkul.setNamaDosen(rs.getString("nama_dosen"));
                return matkul;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error get mata kuliah by id: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return null;
    }
    
    // Method untuk mendapatkan mata kuliah by kode
    public MataKuliah getMataKuliahByKode(String kode) {
        String sql = "SELECT mk.*, d.nama as nama_dosen FROM mata_kuliah mk " +
                    "LEFT JOIN dosen d ON mk.dosen_pengampu = d.id " +
                    "WHERE mk.kode = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, kode);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                MataKuliah matkul = new MataKuliah();
                matkul.setId(rs.getInt("id"));
                matkul.setKode(rs.getString("kode"));
                matkul.setNama(rs.getString("nama"));
                matkul.setSks(rs.getInt("sks"));
                matkul.setSemester(rs.getString("semester"));
                matkul.setProgramStudi(rs.getString("program_studi"));
                matkul.setDosenPengampu(rs.getInt("dosen_pengampu"));
                matkul.setNamaDosen(rs.getString("nama_dosen"));
                return matkul;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error get mata kuliah by kode: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return null;
    }
    
    // Method untuk mendapatkan mata kuliah by program studi
    public List<MataKuliah> getMataKuliahByProgramStudi(String programStudi) {
        List<MataKuliah> matkulList = new ArrayList<>();
        String sql = "SELECT mk.*, d.nama as nama_dosen FROM mata_kuliah mk " +
                    "LEFT JOIN dosen d ON mk.dosen_pengampu = d.id " +
                    "WHERE mk.program_studi = ? " +
                    "ORDER BY mk.semester, mk.kode";
        
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
                matkul.setNamaDosen(rs.getString("nama_dosen"));
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
    
    // Method untuk mendapatkan mata kuliah by semester
    public List<MataKuliah> getMataKuliahBySemester(String semester) {
        List<MataKuliah> matkulList = new ArrayList<>();
        String sql = "SELECT mk.*, d.nama as nama_dosen FROM mata_kuliah mk " +
                    "LEFT JOIN dosen d ON mk.dosen_pengampu = d.id " +
                    "WHERE mk.semester = ? " +
                    "ORDER BY mk.program_studi, mk.kode";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, semester);
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
                matkul.setNamaDosen(rs.getString("nama_dosen"));
                matkulList.add(matkul);
            }
            
            System.out.println("ðŸ“š Total mata kuliah semester " + semester + ": " + matkulList.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get mata kuliah by semester: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return matkulList;
    }
    
    // Method untuk mendapatkan mata kuliah by dosen pengampu
    public List<MataKuliah> getMataKuliahByDosenPengampu(int dosenId) {
        List<MataKuliah> matkulList = new ArrayList<>();
        String sql = "SELECT mk.*, d.nama as nama_dosen FROM mata_kuliah mk " +
                    "JOIN dosen d ON mk.dosen_pengampu = d.id " +
                    "WHERE mk.dosen_pengampu = ? " +
                    "ORDER BY mk.semester, mk.kode";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, dosenId);
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
                matkul.setNamaDosen(rs.getString("nama_dosen"));
                matkulList.add(matkul);
            }
            
            System.out.println("ðŸ“š Total mata kuliah yang diampu dosen ID " + dosenId + ": " + matkulList.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get mata kuliah by dosen pengampu: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return matkulList;
    }
    
    // Method untuk menambahkan mata kuliah baru
    public boolean addMataKuliah(MataKuliah matkul) {
        if (!matkul.isValid()) {
            System.err.println("âŒ Data mata kuliah tidak valid");
            return false;
        }
        
        String sql = "INSERT INTO mata_kuliah (kode, nama, sks, semester, program_studi, dosen_pengampu) VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, matkul.getKode());
            pstmt.setString(2, matkul.getNama());
            pstmt.setInt(3, matkul.getSks());
            pstmt.setString(4, matkul.getSemester());
            pstmt.setString(5, matkul.getProgramStudi());
            
            if (matkul.getDosenPengampu() > 0) {
                pstmt.setInt(6, matkul.getDosenPengampu());
            } else {
                pstmt.setNull(6, Types.INTEGER);
            }
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("âœ… Mata kuliah berhasil ditambahkan: " + matkul.getKode() + " - " + matkul.getNama());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error add mata kuliah: " + e.getMessage());
            if (e.getSQLState().equals("23000")) {
                System.err.println("âš ï¸  Kode mata kuliah sudah digunakan: " + matkul.getKode());
            }
        } finally {
            closeResources(null, pstmt, null);
        }
        return false;
    }
    
    // Method untuk update mata kuliah
    public boolean updateMataKuliah(MataKuliah matkul) {
        if (!matkul.isValid()) {
            System.err.println("âŒ Data mata kuliah tidak valid");
            return false;
        }
        
        String sql = "UPDATE mata_kuliah SET nama = ?, sks = ?, semester = ?, program_studi = ?, dosen_pengampu = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, matkul.getNama());
            pstmt.setInt(2, matkul.getSks());
            pstmt.setString(3, matkul.getSemester());
            pstmt.setString(4, matkul.getProgramStudi());
            
            if (matkul.getDosenPengampu() > 0) {
                pstmt.setInt(5, matkul.getDosenPengampu());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            
            pstmt.setInt(6, matkul.getId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("âœ… Mata kuliah berhasil diupdate: " + matkul.getKode() + " - " + matkul.getNama());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error update mata kuliah: " + e.getMessage());
        } finally {
            closeResources(null, pstmt, null);
        }
        return false;
    }
    
    // Method untuk menghapus mata kuliah
    public boolean deleteMataKuliah(int id) {
        String sql = "DELETE FROM mata_kuliah WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, id);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("âœ… Mata kuliah berhasil dihapus, ID: " + id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error delete mata kuliah: " + e.getMessage());
        } finally {
            closeResources(null, pstmt, null);
        }
        return false;
    }
    
    // Method untuk mencari mata kuliah by nama
    public List<MataKuliah> searchMataKuliahByName(String nama) {
        List<MataKuliah> matkulList = new ArrayList<>();
        String sql = "SELECT mk.*, d.nama as nama_dosen FROM mata_kuliah mk " +
                    "LEFT JOIN dosen d ON mk.dosen_pengampu = d.id " +
                    "WHERE mk.nama LIKE ? " +
                    "ORDER BY mk.program_studi, mk.semester";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, "%" + nama + "%");
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
                matkul.setNamaDosen(rs.getString("nama_dosen"));
                matkulList.add(matkul);
            }
            
            System.out.println("ðŸ” Hasil pencarian mata kuliah '" + nama + "': " + matkulList.size() + " ditemukan");
        } catch (SQLException e) {
            System.err.println("âŒ Error search mata kuliah by name: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return matkulList;
    }
    
    // Method untuk mendapatkan statistik mata kuliah
    public java.util.Map<String, Integer> getMataKuliahStatistics() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        
        // Total mata kuliah
        String sqlTotal = "SELECT COUNT(*) as total FROM mata_kuliah";
        // Mata kuliah by program studi
        String sqlProdi = "SELECT program_studi, COUNT(*) as count FROM mata_kuliah GROUP BY program_studi";
        // Mata kuliah by semester
        String sqlSemester = "SELECT semester, COUNT(*) as count FROM mata_kuliah GROUP BY semester";
        // Total SKS
        String sqlTotalSKS = "SELECT SUM(sks) as total_sks FROM mata_kuliah";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            
            // Total mata kuliah
            rs = stmt.executeQuery(sqlTotal);
            if (rs.next()) {
                stats.put("TOTAL", rs.getInt("total"));
            }
            
            // By program studi
            rs = stmt.executeQuery(sqlProdi);
            while (rs.next()) {
                stats.put("PRODI_" + rs.getString("program_studi"), rs.getInt("count"));
            }
            
            // By semester
            rs = stmt.executeQuery(sqlSemester);
            while (rs.next()) {
                stats.put("SEMESTER_" + rs.getString("semester"), rs.getInt("count"));
            }
            
            // Total SKS
            rs = stmt.executeQuery(sqlTotalSKS);
            if (rs.next()) {
                stats.put("TOTAL_SKS", rs.getInt("total_sks"));
            }
            
        } catch (SQLException e) {
            System.err.println("âŒ Error get mata kuliah statistics: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, null);
        }
        return stats;
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