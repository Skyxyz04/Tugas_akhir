package siasat.model;

public class Dosen {
    private int id;
    private String nidn;
    private String nama;
    private String programStudi;
    private String fakultas;
    private String email;
    private String noTelepon;
    private String jabatan;
    
    // Constructors
    public Dosen() {}
    
    public Dosen(String nidn, String nama, String programStudi, String fakultas) {
        this.nidn = nidn;
        this.nama = nama;
        this.programStudi = programStudi;
        this.fakultas = fakultas;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNidn() { return nidn; }
    public void setNidn(String nidn) { this.nidn = nidn; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getProgramStudi() { return programStudi; }
    public void setProgramStudi(String programStudi) { this.programStudi = programStudi; }
    
    public String getFakultas() { return fakultas; }
    public void setFakultas(String fakultas) { this.fakultas = fakultas; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }
    
    public String getJabatan() { return jabatan; }
    public void setJabatan(String jabatan) { this.jabatan = jabatan; }
    
    @Override
    public String toString() {
        return "Dosen{" +
                "id=" + id +
                ", nidn='" + nidn + '\'' +
                ", nama='" + nama + '\'' +
                ", programStudi='" + programStudi + '\'' +
                ", fakultas='" + fakultas + '\'' +
                ", jabatan='" + jabatan + '\'' +
                '}';
    }
}