package siasat;

import siasat.komponen.HeaderPanel;
import siasat.komponen.LoginFormPanel;
import siasat.komponen.FooterPanel;
import siasat.model.User;
import siasat.model.Mahasiswa;
import siasat.model.Dosen;
import siasat.database.DatabaseConnection;
import siasat.database.UserDAO;
import siasat.database.MahasiswaDAO;
import siasat.database.DosenDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.prefs.Preferences;

public class SiasatLoginUI extends JFrame {
    private HeaderPanel headerPanel;
    private LoginFormPanel loginFormPanel;
    private FooterPanel footerPanel;
    private JPanel mainPanel;
    
    private UserDAO userDAO;
    private MahasiswaDAO mahasiswaDAO;
    private DosenDAO dosenDAO;
    private Preferences prefs;
    
    // Constants
    private static final String PREF_USERNAME = "last_username";
    private static final String PREF_REMEMBER_ME = "remember_me";
    
    public SiasatLoginUI() {
        initializeApplication();
    }
    
    private void initializeApplication() {
        System.out.println("ðŸš€ Memulai aplikasi SIASAT UKSW...");
        
        // Initialize preferences
        prefs = Preferences.userNodeForPackage(SiasatLoginUI.class);
        
        // Initialize database connection
        initializeDatabase();
        
        // Initialize DAOs
        userDAO = new UserDAO();
        mahasiswaDAO = new MahasiswaDAO();
        dosenDAO = new DosenDAO();
        
        // Initialize UI components
        initializeComponents();
        setupUI();
        setupEventHandlers();
        loadSavedPreferences();
        
        System.out.println("âœ… Aplikasi SIASAT berhasil diinisialisasi");
    }
    
    private void initializeDatabase() {
        System.out.println("ðŸ”Œ Menginisialisasi koneksi database...");
        DatabaseConnection.testConnection();
        DatabaseConnection.testDatabaseContent();
    }
    
    private void initializeComponents() {
        headerPanel = new HeaderPanel();
        loginFormPanel = new LoginFormPanel();
        footerPanel = new FooterPanel();
        mainPanel = new JPanel();
    }
    
    private void setupUI() {
        setTitle("SISTEM INFORMASI AKADEMIK TERPADU - UKSW");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 700));
        setMinimumSize(new Dimension(800, 600));
        setResizable(true);
        
        setupMainPanel();
        setupWindowProperties();
        setupWindowListener();
        
        // Set application icon
        setApplicationIcon();
    }
    
    private void setupMainPanel() {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        // Configure header
        headerPanel.setTitle("SISTEM INFORMASI AKADEMIK TERPADU (SIASAT)");
        headerPanel.setLogoText("UNIVERSITAS KRISTEN SATYA WACANA");
        headerPanel.showDateTimeInfo();
        headerPanel.showConnectionStatus(DatabaseConnection.isConnected());
        
        // Configure login form
        loginFormPanel.setFormTitle("LOGIN SIASAT UKSW");
        loginFormPanel.setHelpText("Gunakan username dan password yang telah diberikan");
        loginFormPanel.setVersionText("SIASAT v2.0 - Â© 2024 UKSW");
        loginFormPanel.setFieldPlaceholders("Masukkan username...", "Masukkan password...");
        
        // Configure footer
        footerPanel.setCopyrightText("Â© 2024 Universitas Kristen Satya Wacana - Sistem Informasi Akademik Terpadu");
        footerPanel.setVersion("v2.0.0");
        footerPanel.updateDatabaseStatus(DatabaseConnection.isConnected());
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(loginFormPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void setupWindowProperties() {
        pack();
        centerWindow();
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Start maximized
    }
    
    private void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = getSize();
        int x = (screenSize.width - windowSize.width) / 2;
        int y = (screenSize.height - windowSize.height) / 2;
        setLocation(x, y);
    }
    
    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cleanup();
            }
            
            @Override
            public void windowOpened(WindowEvent e) {
                onWindowOpened();
            }
        });
        
        // Add global key listener for F1 help
        setupGlobalKeyListeners();
    }
    
    private void setupGlobalKeyListeners() {
        KeyStroke f1Key = KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0);
        getRootPane().registerKeyboardAction(
            e -> showHelpDialog(),
            f1Key,
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        KeyStroke escapeKey = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(
            e -> confirmExit(),
            escapeKey,
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    private void setApplicationIcon() {
        try {
            // You can add an icon file later
            // setIconImage(Toolkit.getDefaultToolkit().getImage("path/to/icon.png"));
        } catch (Exception e) {
            System.out.println("â„¹ï¸  Icon aplikasi tidak ditemukan, menggunakan default");
        }
    }
    
    private void setupEventHandlers() {
        // Login button action
        loginFormPanel.setLoginButtonListener(e -> performLogin());
        
        // Reset button action
        loginFormPanel.setResetButtonListener(e -> resetLoginForm());
        
        // Help button action
        loginFormPanel.setHelpButtonListener(e -> showHelpDialog());
        
        // Password field enter key action
        loginFormPanel.setPasswordFieldActionListener(e -> performLogin());
        
        // Auto-focus on username field when window opens
        SwingUtilities.invokeLater(() -> {
            loginFormPanel.focusUsernameField();
            checkDatabaseConnection();
        });
    }
    
    private void loadSavedPreferences() {
        boolean rememberMe = prefs.getBoolean(PREF_REMEMBER_ME, false);
        if (rememberMe) {
            String lastUsername = prefs.get(PREF_USERNAME, "");
            if (!lastUsername.isEmpty()) {
                // You can implement "Remember Me" functionality here
                System.out.println("ðŸ“ Memuat preferensi pengguna terakhir");
            }
        }
    }
    
    private void savePreferences() {
        // You can implement "Remember Me" functionality here
        prefs.putBoolean(PREF_REMEMBER_ME, false); // Default to false for security
    }
    
    private void onWindowOpened() {
        System.out.println("ðŸªŸ Window aplikasi terbuka");
        footerPanel.showTemporaryMessage("Sistem SIASAT siap digunakan", 3000);
    }
    
    private void checkDatabaseConnection() {
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return DatabaseConnection.isConnected();
            }
            
            @Override
            protected void done() {
                try {
                    boolean connected = get();
                    footerPanel.updateDatabaseStatus(connected);
                    headerPanel.showConnectionStatus(connected);
                    
                    if (!connected) {
                        footerPanel.showTemporaryMessage("âš ï¸  Periksa koneksi database", 5000);
                    }
                } catch (Exception e) {
                    footerPanel.updateDatabaseStatus(false);
                    headerPanel.showConnectionStatus(false);
                }
            }
        };
        worker.execute();
    }
    
    private void performLogin() {
        // Validate input first
        if (!loginFormPanel.validateInput()) {
            return;
        }
        
        String username = loginFormPanel.getUsername();
        String password = loginFormPanel.getPassword();
        
        System.out.println("ðŸ” Attempt login - Username: " + username);
        
        // Show loading state
        loginFormPanel.setLoadingState(true);
        footerPanel.showTemporaryMessage("Memproses login...", 0);
        
        // Perform login in background thread
        SwingWorker<User, Void> loginWorker = new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() throws Exception {
                try {
                    // Test database connection first
                    if (!DatabaseConnection.isConnected()) {
                        throw new Exception("Koneksi database terputus");
                    }
                    
                    return userDAO.authenticate(username, password);
                } catch (Exception e) {
                    System.err.println("âŒ Error during login: " + e.getMessage());
                    return null;
                }
            }
            
            @Override
            protected void done() {
                loginFormPanel.setLoadingState(false);
                
                try {
                    User authenticatedUser = get();
                    
                    if (authenticatedUser != null) {
                        handleSuccessfulLogin(authenticatedUser);
                    } else {
                        handleFailedLogin();
                    }
                } catch (Exception e) {
                    handleLoginError(e.getMessage());
                }
            }
        };
        
        loginWorker.execute();
    }
    
    private void handleSuccessfulLogin(User user) {
        System.out.println("âœ… Login BERHASIL untuk user: " + user.getUsername() + 
                         " dengan role: " + user.getRole());
        
        // Get additional user data based on role
        SwingWorker<Void, Void> userDataWorker = new SwingWorker<Void, Void>() {
            private Mahasiswa mahasiswa = null;
            private Dosen dosen = null;
            
            @Override
            protected Void doInBackground() throws Exception {
                if (user.isMahasiswa()) {
                    mahasiswa = mahasiswaDAO.getMahasiswaById(user.getMahasiswaId());
                    if (mahasiswa != null) {
                        System.out.println("ðŸ“š Data mahasiswa ditemukan: " + mahasiswa.getNama());
                    }
                } else if (user.isDosen()) {
                    dosen = dosenDAO.getDosenById(user.getDosenId());
                    if (dosen != null) {
                        System.out.println("ðŸ‘¨â€ðŸ« Data dosen ditemukan: " + dosen.getNama());
                    }
                }
                return null;
            }
            
            @Override
            protected void done() {
                try {
                    get(); // Check for exceptions
                    
                    String welcomeMessage = "Selamat datang " + 
                        (mahasiswa != null ? mahasiswa.getNama() : 
                         dosen != null ? dosen.getNama() : user.getUsername()) + "!";
                    
                    showSuccessMessage(welcomeMessage);
                    openDashboard(user, mahasiswa, dosen);
                    
                } catch (Exception e) {
                    handleLoginError("Error mengambil data user: " + e.getMessage());
                }
            }
        };
        
        userDataWorker.execute();
    }
    
    private void handleFailedLogin() {
        System.out.println("âŒ Login GAGAL - User tidak ditemukan atau password salah");
        
        // Show detailed error message with available credentials
        String errorMessage = "<html><div style='text-align: center;'>" +
                            "<b>Login Gagal!</b><br><br>" +
                            "Username atau password salah.<br><br>" +
                            "<b>Contoh credentials untuk testing:</b><br>" +
                            "â€¢ <b>Mahasiswa:</b> 672024107 / evan<br>" +
                            "â€¢ <b>Dosen:</b> dosen001 / dosen123<br>" +
                            "â€¢ <b>Admin:</b> admin / admin123<br><br>" +
                            "Pastikan database sudah berisi data sample." +
                            "</div></html>";
        
        JOptionPane.showMessageDialog(this, errorMessage, "Login Gagal", JOptionPane.ERROR_MESSAGE);
        
        footerPanel.showTemporaryMessage("Login gagal - periksa username/password", 3000);
        loginFormPanel.clearForm();
    }
    
    private void handleLoginError(String errorMessage) {
        System.err.println("âŒ Login error: " + errorMessage);
        
        String userMessage = "<html><div style='text-align: center;'>" +
                           "<b>Error Sistem!</b><br><br>" +
                           errorMessage + "<br><br>" +
                           "Pastikan:<br>" +
                           "1. Database MySQL berjalan<br>" +
                           "2. Database 'siasat_uksw' sudah dibuat<br>" +
                           "3. Data sample sudah dimasukkan" +
                           "</div></html>";
        
        JOptionPane.showMessageDialog(this, userMessage, "System Error", JOptionPane.ERROR_MESSAGE);
        
        footerPanel.showTemporaryMessage("Error sistem - " + errorMessage, 5000);
        loginFormPanel.setLoadingState(false);
    }
    
    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Login Berhasil", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void resetLoginForm() {
        loginFormPanel.clearForm();
        footerPanel.showTemporaryMessage("Form login telah direset", 2000);
    }
    
    private void showHelpDialog() {
        String helpMessage = "<html><div style='text-align: left; padding: 10px;'>" +
                           "<h2 style='color: #003366;'>Bantuan Login SIASAT</h2>" +
                           "<hr>" +
                           "<b>Cara Login:</b><br>" +
                           "1. Masukkan username dan password<br>" +
                           "2. Klik tombol Login atau tekan Enter<br>" +
                           "3. Pilih 'Tampilkan Password' jika perlu<br><br>" +
                           
                           "<b>Credentials Testing:</b><br>" +
                           "â€¢ <b>Mahasiswa:</b> Gunakan NIM sebagai username<br>" +
                           "â€¢ <b>Dosen:</b> Gunakan kode dosen (dosen001, dll)<br>" +
                           "â€¢ <b>Admin:</b> Gunakan username 'admin'<br><br>" +
                           
                           "<b>Keyboard Shortcuts:</b><br>" +
                           "â€¢ <b>F1:</b> Bantuan ini<br>" +
                           "â€¢ <b>Enter:</b> Login<br>" +
                           "â€¢ <b>Esc:</b> Keluar aplikasi<br><br>" +
                           
                           "<b>Jika mengalami masalah:</b><br>" +
                           "1. Periksa koneksi database<br>" +
                           "2. Pastikan XAMPP MySQL berjalan<br>" +
                           "3. Hubungi administrator sistem" +
                           "</div></html>";
        
        JOptionPane.showMessageDialog(this, helpMessage, "Bantuan SIASAT", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void confirmExit() {
        int result = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin keluar dari aplikasi SIASAT?",
            "Konfirmasi Keluar",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (result == JOptionPane.YES_OPTION) {
            cleanup();
            System.exit(0);
        }
    }
    
    private void openDashboard(User user, Mahasiswa mahasiswa, Dosen dosen) {
        System.out.println("ðŸš€ Membuka dashboard untuk user: " + user.getUsername());
        
        // Save preferences before opening dashboard
        savePreferences();
        
        // Create and show dashboard
        DashboardUI dashboard = new DashboardUI(user, mahasiswa, dosen);
        dashboard.setVisible(true);
        
        // Close login window
        this.dispose();
    }
    
    private void cleanup() {
        System.out.println("ðŸ§¹ Membersihkan resources aplikasi...");
        
        // Stop timers
        if (footerPanel != null) {
            footerPanel.cleanup();
        }
        
        // Close database connection
        DatabaseConnection.closeConnection();
        
        // Save preferences
        savePreferences();
        
        System.out.println("âœ… Aplikasi SIASAT ditutup dengan bersih");
    }
    
    public static void main(String[] args) {
        // Set system look and feel for native appearance
        setSystemLookAndFeel();
        
        // Enable anti-aliasing for better text rendering
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Create and show GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new SiasatLoginUI().setVisible(true);
            } catch (Exception e) {
                showFatalError(e);
            }
        });
    }
    
    private static void setSystemLookAndFeel() {
       
    try {
        // Gunakan getSystemLookAndFeelClassName() bukan getSystemLookAndFeel()
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        // Improve button appearance
        UIManager.put("Button.arc", 8);
        UIManager.put("Component.arc", 8);
        UIManager.put("ProgressBar.arc", 8);
        UIManager.put("TextComponent.arc", 5);
        
        // Set better font
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 12));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 12));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 12));
        UIManager.put("PasswordField.font", new Font("Segoe UI", Font.PLAIN, 12));
        
    } catch (Exception e) {
        System.err.println("⚠️  Gagal set system look and feel: " + e.getMessage());
    }
}
    
    private static void showFatalError(Exception e) {
        String errorMessage = "<html><div style='text-align: center;'>" +
                            "<b>Fatal Error!</b><br><br>" +
                            "Aplikasi tidak dapat dijalankan:<br>" +
                            e.getMessage() + "<br><br>" +
                            "Pastikan:<br>" +
                            "1. Java 8+ terinstall<br>" +
                            "2. Memory cukup<br>" +
                            "3. File tidak corrupt" +
                            "</div></html>";
        
        JOptionPane.showMessageDialog(null, errorMessage, "Fatal Error", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}