package siasat.model;

public class Nilai {
    private int id;
    private int mahasiswaId;
    private int mataKuliahId;
    private double nilai;
    private String grade;
    private String semester;
    private String tahunAjaran;
    
    // Additional fields for display
    private String mataKuliahKode;
    private String mataKuliahNama;
    private int sks;
    
    // Constructors
    public Nilai() {}
    
    public Nilai(int mahasiswaId, int mataKuliahId, double nilai, String grade, 
                 String semester, String tahunAjaran) {
        this.mahasiswaId = mahasiswaId;
        this.mataKuliahId = mataKuliahId;
        this.nilai = nilai;
        this.grade = grade;
        this.semester = semester;
        this.tahunAjaran = tahunAjaran;
    }
    
    // Getters and Setters
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public int getMahasiswaId() { 
        return mahasiswaId; 
    }
    
    public void setMahasiswaId(int mahasiswaId) { 
        this.mahasiswaId = mahasiswaId; 
    }
    
    public int getMataKuliahId() { 
        return mataKuliahId; 
    }
    
    public void setMataKuliahId(int mataKuliahId) { 
        this.mataKuliahId = mataKuliahId; 
    }
    
    public double getNilai() { 
        return nilai; 
    }
    
    public void setNilai(double nilai) { 
        this.nilai = nilai; 
    }
    
    public String getGrade() { 
        return grade; 
    }
    
    public void setGrade(String grade) { 
        this.grade = grade; 
    }
    
    public String getSemester() { 
        return semester; 
    }
    
    public void setSemester(String semester) { 
        this.semester = semester; 
    }
    
    public String getTahunAjaran() { 
        return tahunAjaran; 
    }
    
    public void setTahunAjaran(String tahunAjaran) { 
        this.tahunAjaran = tahunAjaran; 
    }
    
    public String getMataKuliahKode() { 
        return mataKuliahKode; 
    }
    
    public void setMataKuliahKode(String mataKuliahKode) { 
        this.mataKuliahKode = mataKuliahKode; 
    }
    
    public String getMataKuliahNama() { 
        return mataKuliahNama; 
    }
    
    public void setMataKuliahNama(String mataKuliahNama) { 
        this.mataKuliahNama = mataKuliahNama; 
    }
    
    public int getSks() { 
        return sks; 
    }
    
    public void setSks(int sks) { 
        this.sks = sks; 
    }
    
    // Business logic methods
    public boolean isValid() {
        return mahasiswaId > 0 && mataKuliahId > 0 && 
               nilai >= 0 && nilai <= 100 &&
               grade != null && !grade.trim().isEmpty() &&
               semester != null && !semester.trim().isEmpty() &&
               tahunAjaran != null && !tahunAjaran.trim().isEmpty();
    }
    
    public boolean isLulus() {
        return !grade.equals("E") && !grade.equals("D");
    }
    
    public double getBobot() {
        switch (grade) {
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
    
    public String getFormattedInfo() {
        return String.format("%s - %s: %s (%.2f)", 
                           mataKuliahKode, mataKuliahNama, grade, nilai);
    }
    
    public static String calculateGrade(double nilai) {
        if (nilai >= 85) return "A";
        else if (nilai >= 80) return "A-";
        else if (nilai >= 75) return "B+";
        else if (nilai >= 70) return "B";
        else if (nilai >= 65) return "B-";
        else if (nilai >= 60) return "C+";
        else if (nilai >= 55) return "C";
        else if (nilai >= 50) return "C-";
        else if (nilai >= 40) return "D";
        else return "E";
    }
    
    @Override
    public String toString() {
        return "Nilai{" +
                "id=" + id +
                ", mahasiswaId=" + mahasiswaId +
                ", mataKuliahId=" + mataKuliahId +
                ", nilai=" + nilai +
                ", grade='" + grade + '\'' +
                ", semester='" + semester + '\'' +
                ", tahunAjaran='" + tahunAjaran + '\'' +
                ", mataKuliahKode='" + mataKuliahKode + '\'' +
                ", mataKuliahNama='" + mataKuliahNama + '\'' +
                ", sks=" + sks +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nilai nilai = (Nilai) o;
        return id == nilai.id;
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id);
    }
}