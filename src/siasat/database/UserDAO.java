package siasat.database;

import siasat.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    // Method untuk authentication user
    public User authenticate(String username, String password) {
        String sql = "SELECT u.*, m.nim, m.nama as mhs_nama, m.program_studi as mhs_prodi, " +
                    "d.nidn, d.nama as dosen_nama, d.program_studi as dosen_prodi " +
                    "FROM users u " +
                    "LEFT JOIN mahasiswa m ON u.mahasiswa_id = m.id " +
                    "LEFT JOIN dosen d ON u.dosen_id = d.id " +
                    "WHERE u.username = ? AND u.password = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setMahasiswaId(rs.getInt("mahasiswa_id"));
                user.setDosenId(rs.getInt("dosen_id"));
                
                System.out.println("ðŸ” User ditemukan: " + user.getUsername() + " - Role: " + user.getRole());
                return user;
            } else {
                System.out.println("âŒ User tidak ditemukan: " + username);
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error authentication: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(rs, pstmt, null);
        }
        return null;
    }
    
    // Method untuk mendapatkan user by username
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setMahasiswaId(rs.getInt("mahasiswa_id"));
                user.setDosenId(rs.getInt("dosen_id"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error get user by username: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return null;
    }
    
    // Method untuk mendapatkan user by ID
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setMahasiswaId(rs.getInt("mahasiswa_id"));
                user.setDosenId(rs.getInt("dosen_id"));
                return user;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error get user by id: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return null;
    }
    
    // Method untuk menambahkan user baru
    public boolean addUser(User user) {
        if (!user.isValid()) {
            System.err.println("âŒ Data user tidak valid");
            return false;
        }
        
        String sql = "INSERT INTO users (username, password, role, mahasiswa_id, dosen_id) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            
            if (user.getMahasiswaId() > 0) {
                pstmt.setInt(4, user.getMahasiswaId());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            
            if (user.getDosenId() > 0) {
                pstmt.setInt(5, user.getDosenId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("âœ… User berhasil ditambahkan: " + user.getUsername());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error add user: " + e.getMessage());
            if (e.getSQLState().equals("23000")) { // Duplicate entry
                System.err.println("âš ï¸  Username sudah digunakan: " + user.getUsername());
            }
        } finally {
            closeResources(null, pstmt, null);
        }
        return false;
    }
    
    // Method untuk update user
    public boolean updateUser(User user) {
        if (!user.isValid()) {
            System.err.println("âŒ Data user tidak valid");
            return false;
        }
        
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, mahasiswa_id = ?, dosen_id = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            
            if (user.getMahasiswaId() > 0) {
                pstmt.setInt(4, user.getMahasiswaId());
            } else {
                pstmt.setNull(4, Types.INTEGER);
            }
            
            if (user.getDosenId() > 0) {
                pstmt.setInt(5, user.getDosenId());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            
            pstmt.setInt(6, user.getId());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("âœ… User berhasil diupdate: " + user.getUsername());
                return true;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error update user: " + e.getMessage());
        } finally {
            closeResources(null, pstmt, null);
        }
        return false;
    }
    
    // Method untuk delete user
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, userId);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("âœ… User berhasil dihapus, ID: " + userId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error delete user: " + e.getMessage());
        } finally {
            closeResources(null, pstmt, null);
        }
        return false;
    }
    
    // Method untuk mendapatkan semua users
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.*, m.nama as mahasiswa_nama, d.nama as dosen_nama FROM users u " +
                    "LEFT JOIN mahasiswa m ON u.mahasiswa_id = m.id " +
                    "LEFT JOIN dosen d ON u.dosen_id = d.id " +
                    "ORDER BY u.role, u.username";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setMahasiswaId(rs.getInt("mahasiswa_id"));
                user.setDosenId(rs.getInt("dosen_id"));
                users.add(user);
            }
            
            System.out.println("ðŸ“Š Total users: " + users.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get all users: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, null);
        }
        return users;
    }
    
    // Method untuk mendapatkan users by role
    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY username";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, role);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setMahasiswaId(rs.getInt("mahasiswa_id"));
                user.setDosenId(rs.getInt("dosen_id"));
                users.add(user);
            }
            
            System.out.println("ðŸ“Š Total " + role + " users: " + users.size());
        } catch (SQLException e) {
            System.err.println("âŒ Error get users by role: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return users;
    }
    
    // Method untuk mengecek apakah username sudah ada
    public boolean isUsernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            
            return rs.next();
        } catch (SQLException e) {
            System.err.println("âŒ Error check username exists: " + e.getMessage());
        } finally {
            closeResources(rs, pstmt, null);
        }
        return false;
    }
    
    // Method untuk mendapatkan total users
    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) as total FROM users";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error get total users: " + e.getMessage());
        } finally {
            closeResources(rs, stmt, null);
        }
        return 0;
    }
    
    // Method untuk mendapatkan statistik users by role
    public java.util.Map<String, Integer> getUserStatistics() {
        java.util.Map<String, Integer> stats = new java.util.HashMap<>();
        String sql = "SELECT role, COUNT(*) as count FROM users GROUP BY role";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                stats.put(rs.getString("role"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("âŒ Error get user statistics: " + e.getMessage());
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
            // Jangan close connection di sini, biarkan DatabaseConnection yang mengelola
        } catch (SQLException e) {
            System.err.println("âŒ Error closing resources: " + e.getMessage());
        }
    }
}