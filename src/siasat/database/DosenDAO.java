package siasat.database;

import siasat.model.Dosen;
import siasat.model.MataKuliah;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DosenDAO {
    
    // Method untuk mendapatkan dosen by ID
    public Dosen getDosenById(int id) {
        String sql = "SELECT * FROM dosen WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Dosen dosen = extractDosenFromResultSet(rs);
                System.out.println("ðŸ” Dosen ditemukan: " + dosen.getNama());
                return dosen;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error get dosen by id: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return null;
    }
    
    // Method untuk mendapatkan dosen by NIDN
    public Dosen getDosenByNidn(String nidn) {
        String sql = "SELECT * FROM dosen WHERE nidn = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, nidn);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Dosen dosen = extractDosenFromResultSet(rs);
                System.out.println("ðŸ” Dosen ditemukan: " + dosen.getNidn() + " - " + dosen.getNama());
                return dosen;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error get dosen by nidn: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return null;
    }
    
    // Method untuk mendapatkan semua dosen
    public List<Dosen> getAllDosen() {
        List<Dosen> dosenList = new ArrayList<>();
        String sql = "SELECT * FROM dosen ORDER BY nama";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                dosenList.add(extractDosenFromResultSet(rs));
            }
            
            System.out.println("ðŸ“Š Total dosen: " + dosenList.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get all dosen: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, null);
        }
        return dosenList;
    }
    
    // Method untuk mendapatkan dosen by program studi
    public List<Dosen> getDosenByProgramStudi(String programStudi) {
        List<Dosen> dosenList = new ArrayList<>();
        String sql = "SELECT * FROM dosen WHERE program_studi = ? ORDER BY nama";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, programStudi);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                dosenList.add(extractDosenFromResultSet(rs));
            }
            
            System.out.println("ðŸ“Š Total dosen " + programStudi + ": " + dosenList.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get dosen by program studi: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return dosenList;
    }
    
    // Method untuk mendapatkan dosen by jabatan
    public List<Dosen> getDosenByJabatan(String jabatan) {
        List<Dosen> dosenList = new ArrayList<>();
        String sql = "SELECT * FROM dosen WHERE jabatan LIKE ? ORDER BY nama";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, "%" + jabatan + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                dosenList.add(extractDosenFromResultSet(rs));
            }
            
            System.out.println("ðŸ“Š Total dosen jabatan " + jabatan + ": " + dosenList.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get dosen by jabatan: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return dosenList;
    }
    
    // Method untuk menambahkan dosen baru
    public boolean addDosen(Dosen dosen) {
        if (!dosen.isValid()) {
            System.err.println("âŒ Data dosen tidak valid");
            return false;
        }
        
        String sql = "INSERT INTO dosen (nidn, nama, program_studi, fakultas, email, no_telepon, jabatan) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, dosen.getNidn());
            pstmt.setString(2, dosen.getNama());
            pstmt.setString(3, dosen.getProgramStudi());
            pstmt.setString(4, dosen.getFakultas());
            pstmt.setString(5, dosen.getEmail());
            pstmt.setString(6, dosen.getNoTelepon());
            pstmt.setString(7, dosen.getJabatan());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("âœ… Dosen berhasil ditambahkan: " + dosen.getNidn() + " - " + dosen.getNama());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error add dosen: " + e.getMessage());
            if (e.getSQLState().equals("23000")) {
                System.err.println("âš ï¸  NIDN sudah digunakan: " + dosen.getNidn());
            }
        } finally {
            closeResources(null, pstmt, null);
        }
        return false;
    }
    
    // Method untuk update dosen
    public boolean updateDosen(Dosen dosen) {
        if (!dosen.isValid()) {
            System.err.println("âŒ Data dosen tidak valid");
            return false;
        }
        
        String sql = "UPDATE dosen SET nama = ?, program_studi = ?, fakultas = ?, email = ?, no_telepon = ?, jabatan = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, dosen.getNama());
            pstmt.setString(2, dosen.getProgramStudi());
            pstmt.setString(3, dosen.getFakultas());
            pstmt.setString(4, dosen.getEmail());
            pstmt.setString(5, dosen.getNoTelepon());
            pstmt.setString(6, dosen.getJabatan());
            pstmt.setInt(7, dosen.getId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("âœ… Dosen berhasil diupdate: " + dosen.getNidn() + " - " + dosen.getNama());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error update dosen: " + e.getMessage());
        } finally {
            closeResources(null, pstmt, null);
        }
        return false;
    }
    
    // Method untuk mendapatkan mata kuliah yang diampu dosen
    public List<MataKuliah> getMataKuliahByDosen(int dosenId) {
        List<MataKuliah> matkulList = new ArrayList<>();
        String sql = "SELECT mk.*, d.nama as nama_dosen " +
                    "FROM mata_kuliah mk " +
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
            System.err.println("âŒ Error get mata kuliah by dosen: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return matkulList;
    }
    
    // Method untuk mendapatkan statistik dosen
    public java.util.Map<String, Integer> getDosenStatistics() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        
        // Total dosen
        String sqlTotal = "SELECT COUNT(*) as total FROM dosen";
        // Dosen by program studi
        String sqlProdi = "SELECT program_studi, COUNT(*) as count FROM dosen GROUP BY program_studi";
        // Dosen by jabatan
        String sqlJabatan = "SELECT jabatan, COUNT(*) as count FROM dosen GROUP BY jabatan";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            
            // Total dosen
            rs = stmt.executeQuery(sqlTotal);
            if (rs.next()) {
                stats.put("TOTAL", rs.getInt("total"));
            }
            
            // By program studi
            rs = stmt.executeQuery(sqlProdi);
            while (rs.next()) {
                stats.put("PRODI_" + rs.getString("program_studi"), rs.getInt("count"));
            }
            
            // By jabatan
            rs = stmt.executeQuery(sqlJabatan);
            while (rs.next()) {
                String jabatan = rs.getString("jabatan");
                if (jabatan != null) {
                    stats.put("JABATAN_" + jabatan, rs.getInt("count"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("âŒ Error get dosen statistics: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, null);
        }
        return stats;
    }
    
    // Method untuk mencari dosen by nama
    public List<Dosen> searchDosenByName(String nama) {
        List<Dosen> dosenList = new ArrayList<>();
        String sql = "SELECT * FROM dosen WHERE nama LIKE ? ORDER BY nama";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, "%" + nama + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                dosenList.add(extractDosenFromResultSet(rs));
            }
            
            System.out.println("ðŸ” Hasil pencarian dosen '" + nama + "': " + dosenList.size() + " ditemukan");
        } catch (SQLException e) {
            System.err.println("âŒ Error search dosen by name: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return dosenList;
    }
    
    // Helper method untuk extract dosen dari ResultSet
    private Dosen extractDosenFromResultSet(ResultSet rs) throws SQLException {
        Dosen dosen = new Dosen();
        dosen.setId(rs.getInt("id"));
        dosen.setNidn(rs.getString("nidn"));
        dosen.setNama(rs.getString("nama"));
        dosen.setProgramStudi(rs.getString("program_studi"));
        dosen.setFakultas(rs.getString("fakultas"));
        dosen.setEmail(rs.getString("email"));
        dosen.setNoTelepon(rs.getString("no_telepon"));
        dosen.setJabatan(rs.getString("jabatan"));
        return dosen;
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