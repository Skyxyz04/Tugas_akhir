package Model;

public class Admin {
    private int id;
    private String username;
    private String password;
    private String nama;
    private String email;
    private String role;
    
    // Constructors
    public Admin(int par, String administrator, String username1, String password1) {}
    
    public Admin(String username, String nama, String role) {
        this.username = username;
        this.nama = nama;
        this.role = role;
    }

    public Admin() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    @Override
    public String toString() {
        return "Admin{username='" + username + "', nama='" + nama + "', role='" + role + "'}";
    }
}