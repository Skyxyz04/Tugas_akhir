package Model;

import java.util.Date;

public class KRS {
    private int id;
    private String nim;
    private String kodeMk;
    private String namaMk;
    private int sks;
    private String semester;
    private String tahunAkademik;
    private String status;
    private Date createdAt;
    
    public KRS() {}
    
    public KRS(String nim, String kodeMk, String semester, String tahunAkademik) {
        this.nim = nim;
        this.kodeMk = kodeMk;
        this.semester = semester;
        this.tahunAkademik = tahunAkademik;
        this.status = "Diajukan";
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    
    public String getKodeMk() { return kodeMk; }
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }
    
    public String getNamaMk() { return namaMk; }
    public void setNamaMk(String namaMk) { this.namaMk = namaMk; }
    
    public int getSks() { return sks; }
    public void setSks(int sks) { this.sks = sks; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public String getTahunAkademik() { return tahunAkademik; }
    public void setTahunAkademik(String tahunAkademik) { this.tahunAkademik = tahunAkademik; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public void setNamaMahasiswa(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}