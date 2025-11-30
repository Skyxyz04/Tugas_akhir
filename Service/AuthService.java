package Service;

import Database.DatabaseConnection;
import Model.Admin;
import Model.Mahasiswa;
import Model.Dosen;
import java.sql.*;
import javax.swing.JOptionPane;

public class AuthService {
    private Connection connection;
    
    public AuthService() {
        connection = DatabaseConnection.getConnection();
    }
    
    public Mahasiswa loginMahasiswa(String username, String password) {
        String sql = "SELECT * FROM mahasiswa WHERE (nim = ? OR email = ?) AND password = ? AND status = 'Aktif'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setString(3, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Mahasiswa mhs = new Mahasiswa();
                mhs.setNim(rs.getString("nim"));
                mhs.setNama(rs.getString("nama"));
                mhs.setPassword(rs.getString("password"));
                mhs.setProgramStudi(rs.getString("program_studi"));
                mhs.setFakultas(rs.getString("fakultas"));
                mhs.setAngkatan(rs.getInt("angkatan"));
                mhs.setStatus(rs.getString("status"));
                mhs.setEmail(rs.getString("email"));
                mhs.setNoTelepon(rs.getString("no_telepon"));
                mhs.setAlamat(rs.getString("alamat"));
                return mhs;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Admin loginAdmin(String username, String password) {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin(1, "Administrator", username, password);
                admin.setId(rs.getInt("id"));
                admin.setUsername(rs.getString("username"));
                admin.setNama(rs.getString("nama"));
                admin.setRole(rs.getString("role"));
                admin.setEmail(rs.getString("email"));
                return admin;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Dosen loginDosen(String username, String password) {
        String sql = "SELECT * FROM dosen WHERE (nidn = ? OR email = ?) AND password = ? AND status = 'Aktif'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, username);
            stmt.setString(3, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Dosen dosen = new Dosen();
                dosen.setNidn(rs.getString("nidn"));
                dosen.setNama(rs.getString("nama"));
                dosen.setEmail(rs.getString("email"));
                dosen.setProgramStudi(rs.getString("program_studi"));
                dosen.setFakultas(rs.getString("fakultas"));
                dosen.setJabatan(rs.getString("jabatan"));
                dosen.setStatus(rs.getString("status"));
                return dosen;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}