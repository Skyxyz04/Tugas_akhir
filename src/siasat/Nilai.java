package Model;

public class Nilai {
    private int id;
    private String nim;
    private String kodeMk;
    private String nilaiHuruf;
    private Double nilaiAngka;
    private String semester;
    private String createdBy;
    
    // Field untuk join
    private String namaMahasiswa;
    private String namaMk;
    private int sks;
    
    // Constructors
    public Nilai() {}
    
    public Nilai(String nim, String kodeMk, String nilaiHuruf, String semester) {
        this.nim = nim;
        this.kodeMk = kodeMk;
        this.nilaiHuruf = nilaiHuruf;
        this.semester = semester;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }
    
    public String getKodeMk() { return kodeMk; }
    public void setKodeMk(String kodeMk) { this.kodeMk = kodeMk; }
    
    public String getNilaiHuruf() { return nilaiHuruf; }
    public void setNilaiHuruf(String nilaiHuruf) { this.nilaiHuruf = nilaiHuruf; }
    
    public Double getNilaiAngka() { return nilaiAngka; }
    public void setNilaiAngka(Double nilaiAngka) { this.nilaiAngka = nilaiAngka; }
    
    public void setNilaiAngka(double nilaiAngka) { this.nilaiAngka = nilaiAngka; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public String getNamaMahasiswa() { return namaMahasiswa; }
    public void setNamaMahasiswa(String namaMahasiswa) { this.namaMahasiswa = namaMahasiswa; }
    
    public String getNamaMk() { return namaMk; }
    public void setNamaMk(String namaMk) { this.namaMk = namaMk; }
    
    public int getSks() { return sks; }
    public void setSks(int sks) { this.sks = sks; }
    
    // Method untuk menghitung bobot nilai
    public double getBobot() {
        if (nilaiHuruf == null) return 0.0;
        
        switch (nilaiHuruf.toUpperCase()) {
            case "A": return 4.0 * sks;
            case "A-": return 3.7 * sks;
            case "B+": return 3.3 * sks;
            case "B": return 3.0 * sks;
            case "B-": return 2.7 * sks;
            case "C+": return 2.3 * sks;
            case "C": return 2.0 * sks;
            case "C-": return 1.7 * sks;
            case "D": return 1.0 * sks;
            case "E": return 0.0 * sks;
            default: return 0.0;
        }
    }
    
    // Method untuk mendapatkan nilai angka dari nilai huruf
    public double getNilaiAngkaFromHuruf() {
        if (nilaiHuruf == null) return 0.0;
        
        switch (nilaiHuruf.toUpperCase()) {
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D": return 1.0;
            case "E": return 0.0;
            default: return 0.0;
        }
    }
    
    // Method untuk mendapatkan kualitas nilai
    public String getKualitas() {
        if (nilaiHuruf == null) return "-";
        
        switch (nilaiHuruf.toUpperCase()) {
            case "A": return "Sangat Baik";
            case "A-": return "Sangat Baik";
            case "B+": return "Baik";
            case "B": return "Baik";
            case "B-": return "Baik";
            case "C+": return "Cukup";
            case "C": return "Cukup";
            case "C-": return "Cukup";
            case "D": return "Kurang";
            case "E": return "Gagal";
            default: return "-";
        }
    }

    public void setNilai(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}