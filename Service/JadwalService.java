package Service;

import Model.Jadwal;
import Database.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JadwalService {
    private Connection connection;
    
    public JadwalService() {
        connection = DatabaseConnection.getConnection();
    }
    
    public List<Jadwal> getJadwalByNim(String nim, String semester) {
        List<Jadwal> list = new ArrayList<>();
        String sql = "SELECT j.*, mk.nama as nama_mk FROM jadwal j " +
                    "JOIN mata_kuliah mk ON j.kode_mk = mk.kode " +
                    "JOIN krs k ON j.kode_mk = k.kode_mk " +
                    "WHERE k.nim = ? AND k.semester = ? AND k.status = 'Disetujui' " +
                    "ORDER BY FIELD(j.hari, 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'), j.jam_mulai";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nim);
            stmt.setString(2, semester);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Jadwal jadwal = new Jadwal();
                jadwal.setId(rs.getInt("id"));
                jadwal.setKodeMk(rs.getString("kode_mk"));
                jadwal.setNamaMk(rs.getString("nama_mk"));
                jadwal.setHari(rs.getString("hari"));
                jadwal.setJamMulai(rs.getString("jam_mulai"));
                jadwal.setJamSelesai(rs.getString("jam_selesai"));
                jadwal.setRuangan(rs.getString("ruangan"));
                jadwal.setDosen(rs.getString("dosen"));
                jadwal.setKelas(rs.getString("kelas"));
                jadwal.setSemester(rs.getString("semester"));
                list.add(jadwal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}