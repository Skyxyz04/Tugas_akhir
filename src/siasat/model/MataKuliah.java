package siasat.model;

public class MataKuliah {
    private int id;
    private String kode;
    private String nama;
    private int sks;
    private String semester;
    private String programStudi;
    private int dosenPengampu;
    private String namaDosen;
    
    // Constructors
    public MataKuliah() {}
    
    public MataKuliah(String kode, String nama, int sks, String semester, String programStudi) {
        this.kode = kode;
        this.nama = nama;
        this.sks = sks;
        this.semester = semester;
        this.programStudi = programStudi;
    }
    
    public MataKuliah(String kode, String nama, int sks, String semester, String programStudi, int dosenPengampu) {
        this.kode = kode;
        this.nama = nama;
        this.sks = sks;
        this.semester = semester;
        this.programStudi = programStudi;
        this.dosenPengampu = dosenPengampu;
    }
    
    // Getters and Setters
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public String getKode() { 
        return kode; 
    }
    
    public void setKode(String kode) { 
        this.kode = kode; 
    }
    
    public String getNama() { 
        return nama; 
    }
    
    public void setNama(String nama) { 
        this.nama = nama; 
    }
    
    public int getSks() { 
        return sks; 
    }
    
    public void setSks(int sks) { 
        this.sks = sks; 
    }
    
    public String getSemester() { 
        return semester; 
    }
    
    public void setSemester(String semester) { 
        this.semester = semester; 
    }
    
    public String getProgramStudi() { 
        return programStudi; 
    }
    
    public void setProgramStudi(String programStudi) { 
        this.programStudi = programStudi; 
    }
    
    public int getDosenPengampu() { 
        return dosenPengampu; 
    }
    
    public void setDosenPengampu(int dosenPengampu) { 
        this.dosenPengampu = dosenPengampu; 
    }
    
    public String getNamaDosen() { 
        return namaDosen; 
    }
    
    public void setNamaDosen(String namaDosen) { 
        this.namaDosen = namaDosen; 
    }
    
    // Business logic methods
    public boolean isValid() {
        return kode != null && !kode.trim().isEmpty() &&
               nama != null && !nama.trim().isEmpty() &&
               sks > 0 && sks <= 6 &&
               semester != null && !semester.trim().isEmpty() &&
               programStudi != null && !programStudi.trim().isEmpty();
    }
    
    public String getFormattedInfo() {
        return String.format("%s - %s (%d SKS) - Semester %s", 
                           kode, nama, sks, semester);
    }
    
    public boolean isPraktikum() {
        return nama != null && (nama.toLowerCase().contains("praktikum") || 
                               nama.toLowerCase().contains("lab"));
    }
    
    public boolean isTeori() {
        return !isPraktikum();
    }
    
    public boolean isUntukProgramStudi(String programStudi) {
        return this.programStudi != null && this.programStudi.equalsIgnoreCase(programStudi);
    }
    
    public boolean isSemester(String semester) {
        return this.semester != null && this.semester.equalsIgnoreCase(semester);
    }
    
    @Override
    public String toString() {
        return "MataKuliah{" +
                "id=" + id +
                ", kode='" + kode + '\'' +
                ", nama='" + nama + '\'' +
                ", sks=" + sks +
                ", semester='" + semester + '\'' +
                ", programStudi='" + programStudi + '\'' +
                ", dosenPengampu=" + dosenPengampu +
                ", namaDosen='" + namaDosen + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MataKuliah that = (MataKuliah) o;
        return kode.equals(that.kode);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(kode);
    }
}