package Service;

import Database.DatabaseConnection;
import java.sql.*;

public class MataKuliahService {
    private Connection connection;
    
    public MataKuliahService() {
        connection = DatabaseConnection.getConnection();
    }
    
    public int getTotalMataKuliah() {
        // Implementasi asli nanti
        return 45;
    }
}