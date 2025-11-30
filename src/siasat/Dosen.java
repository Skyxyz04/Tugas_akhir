package Model;

public class Dosen {
    private String nidn;
    private String nama;
    private String email;
    private String programStudi;
    private String fakultas;
    private String jabatan;
    private String status;
    private String password;
    
    // Constructors
    public Dosen() {}
    
    public Dosen(String nidn, String nama, String email, String programStudi, String fakultas, String jabatan) {
        this.nidn = nidn;
        this.nama = nama;
        this.email = email;
        this.programStudi = programStudi;
        this.fakultas = fakultas;
        this.jabatan = jabatan;
        this.status = "Aktif";
    }
    
    // Getters and Setters
    public String getNidn() { return nidn; }
    public void setNidn(String nidn) { this.nidn = nidn; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getProgramStudi() { return programStudi; }
    public void setProgramStudi(String programStudi) { this.programStudi = programStudi; }
    
    public String getFakultas() { return fakultas; }
    public void setFakultas(String fakultas) { this.fakultas = fakultas; }
    
    public String getJabatan() { return jabatan; }
    public void setJabatan(String jabatan) { this.jabatan = jabatan; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNoTelepon() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getAlamat() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setNoTelepon(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setAlamat(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}