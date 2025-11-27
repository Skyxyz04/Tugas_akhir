package siasat.model;

public class Mahasiswa {
    private int id;
    private String nim;
    private String nama;
    private String programStudi;
    private String fakultas;
    private String angkatan;
    private String email;
    private String noTelepon;
    
    // Constructors
    public Mahasiswa() {}
    
    public Mahasiswa(String nim, String nama, String programStudi, String fakultas, String angkatan) {
        this.nim = nim;
        this.nama = nama;
        this.programStudi = programStudi;
        this.fakultas = fakultas;
        this.angkatan = angkatan;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getProgramStudi() { return programStudi; }
    public void setProgramStudi(String programStudi) { this.programStudi = programStudi; }
    
    public String getFakultas() { return fakultas; }
    public void setFakultas(String fakultas) { this.fakultas = fakultas; }
    
    public String getAngkatan() { return angkatan; }
    public void setAngkatan(String angkatan) { this.angkatan = angkatan; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    
    @Override
    public String toString() {
        return "Mahasiswa{" +
                "id=" + id +
                ", nim='" + nim + '\'' +
                ", nama='" + nama + '\'' +
                ", programStudi='" + programStudi + '\'' +
                ", fakultas='" + fakultas + '\'' +
                ", angkatan='" + angkatan + '\'' +
                '}';
    }
}