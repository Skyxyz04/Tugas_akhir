package siasat.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private int mahasiswaId;
    private int dosenId;
    
    // Constructors
    public User() {}
    
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public User(String username, String password, String role, int mahasiswaId, int dosenId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.mahasiswaId = mahasiswaId;
        this.dosenId = dosenId;
    }
    
    // Getters and Setters
    public int getId() { 
        return id; 
    }
    
    public void setId(int id) { 
        this.id = id; 
    }
    
    public String getUsername() { 
        return username; 
    }
    
    public void setUsername(String username) { 
        this.username = username; 
    }
    
    public String getPassword() { 
        return password; 
    }
    
    public void setPassword(String password) { 
        this.password = password; 
    }
    
    public String getRole() { 
        return role; 
    }
    
    public void setRole(String role) { 
        this.role = role; 
    }
    
    public int getMahasiswaId() { 
        return mahasiswaId; 
    }
    
    public void setMahasiswaId(int mahasiswaId) { 
        this.mahasiswaId = mahasiswaId; 
    }
    
    public int getDosenId() { 
        return dosenId; 
    }
    
    public void setDosenId(int dosenId) { 
        this.dosenId = dosenId; 
    }
    
    // Validation methods
    public boolean isValid() {
        return username != null && !username.trim().isEmpty() &&
               password != null && !password.trim().isEmpty() &&
               role != null && !role.trim().isEmpty();
    }
    
    public boolean isMahasiswa() {
        return "MAHASISWA".equals(role) && mahasiswaId > 0;
    }
    
    public boolean isDosen() {
        return "DOSEN".equals(role) && dosenId > 0;
    }
    
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", mahasiswaId=" + mahasiswaId +
                ", dosenId=" + dosenId +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                username.equals(user.username);
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, username);
    }
}