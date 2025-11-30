package Service;

import Database.DatabaseConnection;
import Model.Mahasiswa;
import java.sql.*;

public class MahasiswaService {
    private Connection connection;
    
    public MahasiswaService() {
        connection = DatabaseConnection.getConnection();
    }
    
    // Method untuk update data pribadi
    public boolean updateDataPribadi(String nim, String email, String noTelepon, String alamat) {
        String sql = "UPDATE mahasiswa SET email = ?, no_telepon = ?, alamat = ? WHERE nim = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email.isEmpty() ? null : email);
            stmt.setString(2, noTelepon.isEmpty() ? null : noTelepon);
            stmt.setString(3, alamat.isEmpty() ? null : alamat);
            stmt.setString(4, nim);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Method untuk ubah password
    public boolean ubahPassword(String nim, String passwordLama, String passwordBaru) {
        // First verify old password
        String verifySql = "SELECT password FROM mahasiswa WHERE nim = ?";
        String updateSql = "UPDATE mahasiswa SET password = ? WHERE nim = ?";
        
        try {
            // Verify old password
            PreparedStatement verifyStmt = connection.prepareStatement(verifySql);
            verifyStmt.setString(1, nim);
            ResultSet rs = verifyStmt.executeQuery();
            
            if (rs.next()) {
                String currentPassword = rs.getString("password");
                
                // In real application, you should use password hashing!
                // This is just for demonstration
                if (currentPassword.equals(passwordLama)) {
                    // Update password
                    PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                    updateStmt.setString(1, passwordBaru); // In real app, hash this password!
                    updateStmt.setString(2, nim);
                    
                    return updateStmt.executeUpdate() > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Method untuk mendapatkan data mahasiswa terbaru
    public Mahasiswa getMahasiswaByNim(String nim) {
        String sql = "SELECT * FROM mahasiswa WHERE nim = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setNim(rs.getString("nim"));
                mahasiswa.setNama(rs.getString("nama"));
                mahasiswa.setProgramStudi(rs.getString("program_studi"));
                mahasiswa.setFakultas(rs.getString("fakultas"));
                mahasiswa.setAngkatan(rs.getInt("angkatan"));
                mahasiswa.setStatus(rs.getString("status"));
                mahasiswa.setEmail(rs.getString("email"));
                mahasiswa.setNoTelepon(rs.getString("no_telepon"));
                mahasiswa.setAlamat(rs.getString("alamat"));
                return mahasiswa;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getTotalMahasiswa() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getMahasiswaAktif() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}