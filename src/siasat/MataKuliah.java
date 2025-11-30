package Model;

public class MataKuliah {
    private String kode;
    private String nama;
    private int sks;
    private String semester; // Tambahkan field semester
    private String jurusan;
    private String status;
    
    // Constructors
    public MataKuliah() {}
    
    public MataKuliah(String kode, String nama, int sks, String semester, String jurusan) {
        this.kode = kode;
        this.nama = nama;
        this.sks = sks;
        this.semester = semester;
        this.jurusan = jurusan;
        this.status = "Aktif";
    }
    
    // Complete Constructor
    public MataKuliah(String kode, String nama, int sks, String semester, String jurusan, String status) {
        this.kode = kode;
        this.nama = nama;
        this.sks = sks;
        this.semester = semester;
        this.jurusan = jurusan;
        this.status = status;
    }
    
    // Getters and Setters
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public int getSks() { return sks; }
    public void setSks(int sks) { this.sks = sks; }
    
    public String getSemester() { return semester; } // Tambahkan getter semester
    public void setSemester(String semester) { this.semester = semester; } // Tambahkan setter semester
    
    public String getJurusan() { return jurusan; }
    public void setJurusan(String jurusan) { this.jurusan = jurusan; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return kode + " - " + nama + " (" + sks + " SKS)";
    }

    public Object getJenis() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setProgramStudi(String trim) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getProgramStudi() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setJenis(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setDosenPengampu(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setKuota(int aInt) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setDeskripsi(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}