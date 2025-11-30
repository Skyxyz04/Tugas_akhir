package Model;

public class Jadwal {
    private int id;
    private String kodeMk;
    private String namaMk;
    private String hari;
    private String jamMulai;
    private String jamSelesai;
    private String ruangan;
    private String dosen;
    private String kelas;
    private String semester;
    
    // Constructor
    public Jadwal() {}
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getKodeMk() { return kodeMk; }
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }
    
    public String getNamaMk() { return namaMk; }
    public void setNamaMk(String namaMk) { this.namaMk = namaMk; }
    
    public String getHari() { return hari; }
    public void setHari(String hari) { this.hari = hari; }
    
    public String getJamMulai() { return jamMulai; }
    public void setJamMulai(String jamMulai) { this.jamMulai = jamMulai; }
    
    public String getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(String jamSelesai) { this.jamSelesai = jamSelesai; }
    
    public String getRuangan() { return ruangan; }
    public void setRuangan(String ruangan) { this.ruangan = ruangan; }
    
    public String getDosen() { return dosen; }
    public void setDosen(String dosen) { this.dosen = dosen; }
    
    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
}