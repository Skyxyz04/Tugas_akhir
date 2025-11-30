package Service;

import Database.DatabaseConnection;
import java.sql.*;

public class DosenService {
    private Connection connection;
    
    public DosenService() {
        connection = DatabaseConnection.getConnection();
    }
    
    public int getTotalDosen() {
        // Implementasi asli nanti
        return 25;
    }
    
    public int getDosenAktif() {
        // Implementasi asli nanti
        return 22;
    }
}