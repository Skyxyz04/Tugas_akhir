package Model;

public class Mahasiswa {
    private String nim;
    private String nama;
    private String email;
    private String programStudi;
    private String fakultas;
    private int angkatan;
    private String status;
    private String password;
    private String noTelepon;
    private String alamat;
    private String jurusan;
    private String semester;
    private String tahunMasuk;
    
    // Constructors
    public Mahasiswa() {}
    
    public Mahasiswa(String nim, String nama, String programStudi, String fakultas, int angkatan) {
        this.nim = nim;
        this.nama = nama;
        this.programStudi = programStudi;
        this.fakultas = fakultas;
        this.angkatan = angkatan;
        this.status = "Aktif";
        this.password = "mahasiswa123";
    }
    
    // Complete Constructor
    public Mahasiswa(String nim, String nama, String email, String programStudi, String fakultas, 
                    int angkatan, String status, String password, String noTelepon, String alamat) {
        this.nim = nim;
        this.nama = nama;
        this.email = email;
        this.programStudi = programStudi;
        this.fakultas = fakultas;
        this.angkatan = angkatan;
        this.status = status;
        this.password = password;
        this.noTelepon = noTelepon;
        this.alamat = alamat;
    }
    
    // Getters and Setters
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getProgramStudi() { return programStudi; }
    public void setProgramStudi(String programStudi) { this.programStudi = programStudi; }
    
    public String getFakultas() { return fakultas; }
    public void setFakultas(String fakultas) { this.fakultas = fakultas; }
    
    public int getAngkatan() { return angkatan; }
    public void setAngkatan(int angkatan) { this.angkatan = angkatan; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    
    public String getJurusan() { return jurusan; }
    public void setJurusan(String jurusan) { this.jurusan = jurusan; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public String getTahunMasuk() { return tahunMasuk; }
    public void setTahunMasuk(String tahunMasuk) { this.tahunMasuk = tahunMasuk; }
    
    @Override
    public String toString() {
        return nim + " - " + nama + " (" + programStudi + ")";
    }
}