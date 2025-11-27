package siasat.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/siasat_uksw";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static Connection connection = null;
    
    // Static initializer untuk load driver
    static {
        initializeDriver();
    }
    
    private static void initializeDriver() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✅ MySQL JDBC Driver berhasil dimuat");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver tidak ditemukan!");
            System.err.println("💡 Pastikan file mysql-connector-java-x.x.x.jar sudah ditambahkan ke project");
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✅ Koneksi database MySQL berhasil!");
            }
        } catch (SQLException e) {
            handleConnectionError(e);
        }
        return connection;
    }
    
    private static void handleConnectionError(SQLException e) {
        System.err.println("❌ Error koneksi database: " + e.getMessage());
        System.err.println("🔧 Troubleshooting:");
        System.err.println("   1. Pastikan XAMPP MySQL berjalan");
        System.err.println("   2. Pastikan database 'siasat_uksw' sudah dibuat");
        System.err.println("   3. Cek username/password MySQL");
        System.err.println("   4. Cek port MySQL (default: 3306)");
        System.err.println("   5. Pastikan MySQL Connector/J sudah ditambahkan ke project");
        
        // Tampilkan detail error
        e.printStackTrace();
    }
    
    public static void testConnection() {
        System.out.println("🧪 Testing koneksi database...");
        System.out.println("   URL: " + URL);
        System.out.println("   Username: " + USERNAME);
        System.out.println("   Password: " + (PASSWORD.isEmpty() ? "[kosong]" : "***"));
        
        Connection testConn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            testConn = getConnection();
            if (testConn != null && !testConn.isClosed()) {
                System.out.println("✅ Test koneksi database BERHASIL");
                
                // Test query sederhana
                stmt = testConn.createStatement();
                rs = stmt.executeQuery("SELECT 1");
                if (rs.next()) {
                    System.out.println("✅ Test query database BERHASIL");
                }
                
                // Test apakah database ada dan bisa diakses
                testDatabaseExists();
                
            } else {
                System.err.println("❌ Test koneksi database GAGAL");
            }
        } catch (SQLException e) {
            System.err.println("❌ Test koneksi database GAGAL: " + e.getMessage());
        } finally {
            // Close hanya test resources, bukan connection utama
            closeTestResources(rs, stmt);
        }
    }
    
    private static void testDatabaseExists() {
        String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = 'siasat_uksw'";
        
        Connection testConn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            testConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", USERNAME, PASSWORD);
            stmt = testConn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                System.out.println("✅ Database 'siasat_uksw' ditemukan");
                testTablesExist();
            } else {
                System.err.println("❌ Database 'siasat_uksw' tidak ditemukan!");
                System.err.println("💡 Jalankan script SQL yang disediakan untuk membuat database");
            }
        } catch (SQLException e) {
            System.err.println("❌ Error test database existence: " + e.getMessage());
        } finally {
            closeTestResources(rs, stmt);
            if (testConn != null) {
                try {
                    testConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void testTablesExist() {
        String[] tables = {"mahasiswa", "dosen", "users", "mata_kuliah", "nilai"};
        boolean allTablesExist = true;
        
        Statement stmt = null;
        
        try {
            Connection conn = getConnection();
            stmt = conn.createStatement();
            
            for (String table : tables) {
                String sql = "SELECT 1 FROM " + table + " LIMIT 1";
                try {
                    stmt.executeQuery(sql);
                    System.out.println("   ✅ Tabel '" + table + "' ditemukan");
                } catch (SQLException e) {
                    System.err.println("   ❌ Tabel '" + table + "' tidak ditemukan");
                    allTablesExist = false;
                }
            }
            
            if (allTablesExist) {
                System.out.println("✅ Semua tabel database tersedia");
            } else {
                System.err.println("❌ Beberapa tabel tidak ditemukan!");
                System.err.println("💡 Jalankan script SQL lengkap untuk membuat semua tabel");
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error test tables existence: " + e.getMessage());
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void testDatabaseContent() {
        System.out.println("📊 Testing konten database...");
        
        // Buat koneksi baru untuk testing
        Connection testConn = null;
        
        try {
            testConn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            // Test data users
            testUsersData(testConn);
            
            // Test data mahasiswa
            testMahasiswaData(testConn);
            
            // Test data dosen
            testDosenData(testConn);
            
        } catch (SQLException e) {
            System.err.println("❌ Error test database content: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Tutup koneksi testing
            if (testConn != null) {
                try {
                    testConn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void testUsersData(Connection conn) throws SQLException {
        String sql = "SELECT u.username, u.password, u.role, " +
                    "COALESCE(m.nama, d.nama, 'Admin') as nama, " +
                    "COALESCE(m.nim, d.nidn, 'System') as identitas " +
                    "FROM users u " +
                    "LEFT JOIN mahasiswa m ON u.mahasiswa_id = m.id " +
                    "LEFT JOIN dosen d ON u.dosen_id = d.id " +
                    "ORDER BY u.role, u.username";
        
        System.out.println("=== DATA USER DI DATABASE ===");
        
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println("   👤 " + 
                                 "Username: " + rs.getString("username") + 
                                 " | Password: " + rs.getString("password") +
                                 " | Role: " + rs.getString("role") +
                                 " | Nama: " + rs.getString("nama") +
                                 " | ID: " + rs.getString("identitas"));
            }
            
            if (!hasData) {
                System.err.println("   ⚠️  Tidak ada data user di database!");
            }
        } finally {
            closeTestResources(rs, stmt);
        }
        
        System.out.println("=============================");
    }
    
    private static void testMahasiswaData(Connection conn) throws SQLException {
        String sql = "SELECT nim, nama, program_studi, angkatan FROM mahasiswa ORDER BY nim";
        
        System.out.println("=== DATA MAHASISWA ===");
        
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println("   🎓 " + 
                                 "NIM: " + rs.getString("nim") + 
                                 " | Nama: " + rs.getString("nama") +
                                 " | Prodi: " + rs.getString("program_studi") +
                                 " | Angkatan: " + rs.getString("angkatan"));
            }
            
            if (!hasData) {
                System.err.println("   ⚠️  Tidak ada data mahasiswa di database!");
            }
        } finally {
            closeTestResources(rs, stmt);
        }
        
        System.out.println("======================");
    }
    
    private static void testDosenData(Connection conn) throws SQLException {
        String sql = "SELECT nidn, nama, program_studi, jabatan FROM dosen ORDER BY nama";
        
        System.out.println("=== DATA DOSEN ===");
        
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.println("   👨‍🏫 " + 
                                 "NIDN: " + rs.getString("nidn") + 
                                 " | Nama: " + rs.getString("nama") +
                                 " | Prodi: " + rs.getString("program_studi") +
                                 " | Jabatan: " + rs.getString("jabatan"));
            }
            
            if (!hasData) {
                System.err.println("   ⚠️  Tidak ada data dosen di database!");
            }
        } finally {
            closeTestResources(rs, stmt);
        }
        
        System.out.println("===================");
    }
    
    private static void closeTestResources(ResultSet rs, Statement stmt) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            System.err.println("Error closing test resources: " + e.getMessage());
        }
    }
    
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("🔒 Koneksi database ditutup.");
            } catch (SQLException e) {
                System.err.println("❌ Error menutup koneksi: " + e.getMessage());
            }
        }
    }
    
    public static boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    public static String getDatabaseInfo() {
        return String.format("Database: %s, Connected: %s", URL, isConnected());
    }
}