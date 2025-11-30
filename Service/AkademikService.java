package Service;

import Database.DatabaseConnection;
import Model.MataKuliah;
import Model.Nilai;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AkademikService {
    private Connection connection;
    
    public AkademikService() {
        connection = DatabaseConnection.getConnection();
    }
    
    public List<MataKuliah> getMataKuliahByProdi(String programStudi) {
        List<MataKuliah> list = new ArrayList<>();
        String sql = "SELECT * FROM mata_kuliah WHERE program_studi = ? ORDER BY semester, kode";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, programStudi);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                MataKuliah mk = new MataKuliah();
                mk.setKode(rs.getString("kode"));
                mk.setNama(rs.getString("nama"));
                mk.setSks(rs.getInt("sks"));
                mk.setSemester(rs.getString("semester"));
                mk.setJenis(rs.getString("jenis"));
                mk.setProgramStudi(rs.getString("program_studi"));
                mk.setDosenPengampu(rs.getString("dosen_pengampu"));
                mk.setKuota(rs.getInt("kuota"));
                mk.setDeskripsi(rs.getString("deskripsi"));
                list.add(mk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<Nilai> getKHS(String nim, String semester) {
        List<Nilai> list = new ArrayList<>();
        String sql = "SELECT n.*, mk.nama as nama_mk, mk.sks FROM nilai n " +
                    "JOIN mata_kuliah mk ON n.kode_mk = mk.kode " +
                    "WHERE n.nim = ? AND n.semester = ? ORDER BY mk.kode";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nim);
            stmt.setString(2, semester);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Nilai nilai = new Nilai();
                nilai.setKodeMk(rs.getString("kode_mk"));
                nilai.setNamaMk(rs.getString("nama_mk"));
                nilai.setSks(rs.getInt("sks"));
                nilai.setNilaiHuruf(rs.getString("nilai_huruf"));
                nilai.setNilaiAngka(rs.getDouble("nilai_angka"));
                nilai.setSemester(rs.getString("semester"));
                list.add(nilai);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public List<MataKuliah> getAllMataKuliah() {
        List<MataKuliah> list = new ArrayList<>();
        String sql = "SELECT * FROM mata_kuliah ORDER BY program_studi, semester";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                MataKuliah mk = new MataKuliah();
                mk.setKode(rs.getString("kode"));
                mk.setNama(rs.getString("nama"));
                mk.setSks(rs.getInt("sks"));
                mk.setSemester(rs.getString("semester"));
                mk.setJenis(rs.getString("jenis"));
                mk.setProgramStudi(rs.getString("program_studi"));
                mk.setDosenPengampu(rs.getString("dosen_pengampu"));
                mk.setKuota(rs.getInt("kuota"));
                mk.setDeskripsi(rs.getString("deskripsi"));
                list.add(mk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}