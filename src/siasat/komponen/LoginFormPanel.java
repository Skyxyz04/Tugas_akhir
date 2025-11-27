package siasat.komponen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class LoginFormPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton resetButton;
    private JButton helpButton;
    private JLabel titleLabel;
    private JLabel helpLabel;
    private JLabel versionLabel;
    private JCheckBox showPasswordCheckbox;
    private JPanel mainFormPanel;
    private JPanel buttonPanel;
    
    public LoginFormPanel() {
        initComponents();
        setupUI();
        setupEventHandlers();
    }
    
    private void initComponents() {
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        resetButton = new JButton("Reset");
        helpButton = new JButton("?");
        titleLabel = new JLabel("LOGIN MAHASISWA", JLabel.CENTER);
        helpLabel = new JLabel("Gunakan NIM sebagai username", JLabel.CENTER);
        versionLabel = new JLabel("SIASAT v1.0 - UKSW", JLabel.CENTER);
        showPasswordCheckbox = new JCheckBox("Tampilkan Password");
        mainFormPanel = new JPanel();
        buttonPanel = new JPanel();
    }
    
    private void setupUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 248, 248));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        setupMainFormPanel();
        setupVersionPanel();
    }
    
    private void setupMainFormPanel() {
        mainFormPanel.setLayout(new GridBagLayout());
        mainFormPanel.setBackground(new Color(248, 248, 248));
        mainFormPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Title
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 51, 102));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainFormPanel.add(titleLabel, gbc);
        
        // Spacer
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainFormPanel.add(createSpacer(10), gbc);
        
        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        mainFormPanel.add(userLabel, gbc);
        
        usernameField.setPreferredSize(new Dimension(250, 40));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        mainFormPanel.add(usernameField, gbc);
        
        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainFormPanel.add(passLabel, gbc);
        
        passwordField.setPreferredSize(new Dimension(250, 40));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridx = 1; gbc.gridy = 3;
        mainFormPanel.add(passwordField, gbc);
        
        // Show Password Checkbox
        showPasswordCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        showPasswordCheckbox.setBackground(new Color(248, 248, 248));
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.insets = new Insets(0, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;
        mainFormPanel.add(showPasswordCheckbox, gbc);
        
        // Buttons
        setupButtonPanel();
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        mainFormPanel.add(buttonPanel, gbc);
        
        // Help text
        helpLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        helpLabel.setForeground(Color.GRAY);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 5, 10);
        mainFormPanel.add(helpLabel, gbc);
        
        add(mainFormPanel, BorderLayout.CENTER);
    }
    
    private void setupButtonPanel() {
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(new Color(248, 248, 248));
        
        setupLoginButton();
        setupResetButton();
        setupHelpButton();
        
        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(helpButton);
    }
    
    private void setupLoginButton() {
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setPreferredSize(new Dimension(120, 40));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 82, 184));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 102, 204));
            }
        });
    }
    
    private void setupResetButton() {
        resetButton.setBackground(new Color(220, 220, 220));
        resetButton.setForeground(Color.DARK_GRAY);
        resetButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        resetButton.setPreferredSize(new Dimension(120, 40));
        resetButton.setFocusPainted(false);
        resetButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        resetButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        resetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                resetButton.setBackground(new Color(200, 200, 200));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                resetButton.setBackground(new Color(220, 220, 220));
            }
        });
    }
    
    private void setupHelpButton() {
        helpButton.setBackground(new Color(255, 165, 0));
        helpButton.setForeground(Color.WHITE);
        helpButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        helpButton.setPreferredSize(new Dimension(40, 40));
        helpButton.setFocusPainted(false);
        helpButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        helpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        helpButton.setToolTipText("Bantuan Login");
        
        // Hover effect
        helpButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                helpButton.setBackground(new Color(255, 140, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                helpButton.setBackground(new Color(255, 165, 0));
            }
        });
    }
    
    private void setupVersionPanel() {
        JPanel versionPanel = new JPanel();
        versionPanel.setBackground(new Color(248, 248, 248));
        versionPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        versionLabel.setForeground(Color.GRAY);
        versionPanel.add(versionLabel);
        
        add(versionPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        // Show password checkbox
        showPasswordCheckbox.addActionListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('â€¢');
            }
        });
        
        // Enter key support
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });
        
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButton.doClick();
                }
            }
        });
    }
    
    // Public methods untuk akses dari luar
    public String getUsername() {
        return usernameField.getText().trim();
    }
    
    public String getPassword() {
        return new String(passwordField.getPassword());
    }
    
    public void clearForm() {
        usernameField.setText("");
        passwordField.setText("");
        showPasswordCheckbox.setSelected(false);
        passwordField.setEchoChar('â€¢');
        usernameField.requestFocusInWindow();
    }
    
    public void focusUsernameField() {
        usernameField.requestFocusInWindow();
    }
    
    public void setLoginButtonListener(ActionListener listener) {
        for (ActionListener al : loginButton.getActionListeners()) {
            loginButton.removeActionListener(al);
        }
        loginButton.addActionListener(listener);
    }
    
    public void setResetButtonListener(ActionListener listener) {
        for (ActionListener al : resetButton.getActionListeners()) {
            resetButton.removeActionListener(al);
        }
        resetButton.addActionListener(listener);
    }
    
    public void setHelpButtonListener(ActionListener listener) {
        for (ActionListener al : helpButton.getActionListeners()) {
            helpButton.removeActionListener(listener);
        }
        helpButton.addActionListener(listener);
    }
    
    public void setPasswordFieldActionListener(ActionListener listener) {
        for (ActionListener al : passwordField.getActionListeners()) {
            passwordField.removeActionListener(al);
        }
        passwordField.addActionListener(listener);
    }
    
    // Customization methods
    public void setFormTitle(String title) {
        titleLabel.setText(title);
    }
    
    public void setHelpText(String helpText) {
        helpLabel.setText(helpText);
    }
    
    public void setVersionText(String versionText) {
        versionLabel.setText(versionText);
    }
    
    public void setTheme(Color backgroundColor, Color primaryColor, Color textColor) {
        setBackground(backgroundColor);
        mainFormPanel.setBackground(backgroundColor);
        buttonPanel.setBackground(backgroundColor);
        titleLabel.setForeground(primaryColor);
        loginButton.setBackground(primaryColor);
    }
    
    public void showHelpButton(boolean show) {
        helpButton.setVisible(show);
    }
    
    public void showVersionInfo(boolean show) {
        versionLabel.setVisible(show);
    }
    
    public void setFieldPlaceholders(String usernamePlaceholder, String passwordPlaceholder) {
        setPlaceholder(usernameField, usernamePlaceholder);
        setPlaceholder(passwordField, passwordPlaceholder);
    }
    
    private void setPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }
    
    private Component createSpacer(int height) {
        return Box.createRigidArea(new Dimension(0, height));
    }
    
    // Validation methods
    public boolean validateInput() {
        String username = getUsername();
        String password = getPassword();
        
        if (username.isEmpty() || password.isEmpty()) {
            showError("Username dan password harus diisi!");
            return false;
        }
        
        if (username.length() < 3) {
            showError("Username harus minimal 3 karakter!");
            return false;
        }
        
        if (password.length() < 3) {
            showError("Password harus minimal 3 karakter!");
            return false;
        }
        
        return true;
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validasi Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Method untuk menampilkan loading state
    public void setLoadingState(boolean loading) {
        loginButton.setEnabled(!loading);
        resetButton.setEnabled(!loading);
        usernameField.setEnabled(!loading);
        passwordField.setEnabled(!loading);
        
        if (loading) {
            loginButton.setText("Loading...");
            loginButton.setBackground(Color.GRAY);
        } else {
            loginButton.setText("Login");
            loginButton.setBackground(new Color(0, 102, 204));
        }
    }
}