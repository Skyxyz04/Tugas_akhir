package Ui;

import Model.Mahasiswa;
import Service.MahasiswaService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataPribadiFrame extends JFrame {
    private Mahasiswa mahasiswa;
    private MahasiswaService mahasiswaService;
    
    // Components for data display
    private JLabel lblNim, lblNama, lblProdi, lblFakultas, lblAngkatan, lblStatus, lblEmail, lblTelepon, lblAlamat;
    
    public DataPribadiFrame(Mahasiswa mahasiswa) {
        this.mahasiswa = mahasiswa;
        this.mahasiswaService = new MahasiswaService();
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Data Pribadi - " + mahasiswa.getNama());
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel lblTitle = new JLabel("DATA PRIBADI MAHASISWA");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(0, 51, 102));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Data panel
        JPanel dataPanel = new JPanel(new GridLayout(10, 2, 10, 10));
        dataPanel.setBorder(BorderFactory.createTitledBorder("Informasi Pribadi"));
        
        dataPanel.add(createLabel("NIM:"));
        lblNim = createValueLabel(mahasiswa.getNim());
        dataPanel.add(lblNim);
        
        dataPanel.add(createLabel("Nama:"));
        lblNama = createValueLabel(mahasiswa.getNama());
        dataPanel.add(lblNama);
        
        dataPanel.add(createLabel("Program Studi:"));
        lblProdi = createValueLabel(mahasiswa.getProgramStudi());
        dataPanel.add(lblProdi);
        
        dataPanel.add(createLabel("Fakultas:"));
        lblFakultas = createValueLabel(mahasiswa.getFakultas());
        dataPanel.add(lblFakultas);
        
        dataPanel.add(createLabel("Angkatan:"));
        lblAngkatan = createValueLabel(String.valueOf(mahasiswa.getAngkatan()));
        dataPanel.add(lblAngkatan);
        
        dataPanel.add(createLabel("Status:"));
        lblStatus = createValueLabel((String) mahasiswa.getStatus());
        dataPanel.add(lblStatus);
        
        dataPanel.add(createLabel("Email:"));
        lblEmail = createValueLabel(mahasiswa.getEmail() != null ? mahasiswa.getEmail() : "-");
        dataPanel.add(lblEmail);
        
        dataPanel.add(createLabel("No. Telepon:"));
        lblTelepon = createValueLabel(mahasiswa.getNoTelepon() != null ? mahasiswa.getNoTelepon() : "-");
        dataPanel.add(lblTelepon);
        
        dataPanel.add(createLabel("Alamat:"));
        lblAlamat = createValueLabel(mahasiswa.getAlamat() != null ? mahasiswa.getAlamat() : "-");
        dataPanel.add(lblAlamat);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnEdit = new JButton("Edit Data");
        JButton btnUbahPassword = new JButton("Ubah Password");
        
        // Add action listeners
        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editData();
            }
        });
        
        btnUbahPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ubahPassword();
            }
        });
        
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnUbahPassword);
        
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        mainPanel.add(dataPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }
    
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }
    
    private void editData() {
        // Create edit dialog
        JDialog editDialog = new JDialog(this, "Edit Data Pribadi", true);
        editDialog.setSize(400, 400);
        editDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Fields that can be edited
        JTextField txtEmail = new JTextField(mahasiswa.getEmail() != null ? mahasiswa.getEmail() : "");
        JTextField txtTelepon = new JTextField(mahasiswa.getNoTelepon() != null ? mahasiswa.getNoTelepon() : "");
        JTextArea txtAlamat = new JTextArea(mahasiswa.getAlamat() != null ? mahasiswa.getAlamat() : "");
        txtAlamat.setRows(3);
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);
        
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        
        panel.add(new JLabel("No. Telepon:"));
        panel.add(txtTelepon);
        
        panel.add(new JLabel("Alamat:"));
        panel.add(scrollAlamat);
        
        // Read-only fields (for information)
        panel.add(new JLabel("NIM (tidak dapat diubah):"));
        JTextField txtNim = new JTextField(mahasiswa.getNim());
        txtNim.setEditable(false);
        panel.add(txtNim);
        
        panel.add(new JLabel("Nama (tidak dapat diubah):"));
        JTextField txtNama = new JTextField(mahasiswa.getNama());
        txtNama.setEditable(false);
        panel.add(txtNama);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSave = new JButton("Simpan");
        JButton btnCancel = new JButton("Batal");
        
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveData(txtEmail.getText(), txtTelepon.getText(), txtAlamat.getText());
                editDialog.dispose();
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editDialog.dispose();
            }
        });
        
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        
        editDialog.add(panel, BorderLayout.CENTER);
        editDialog.add(buttonPanel, BorderLayout.SOUTH);
        editDialog.setVisible(true);
    }
    
    private void saveData(String email, String telepon, String alamat) {
        try {
            // Update mahasiswa object
            mahasiswa.setEmail(email.isEmpty() ? null : email);
            mahasiswa.setNoTelepon(telepon.isEmpty() ? null : telepon);
            mahasiswa.setAlamat(alamat.isEmpty() ? null : alamat);
            
            // Save to database
            boolean success = mahasiswaService.updateDataPribadi(
                mahasiswa.getNim(), 
                email, 
                telepon, 
                alamat
            );
            
            if (success) {
                // Update display
                lblEmail.setText(email.isEmpty() ? "-" : email);
                lblTelepon.setText(telepon.isEmpty() ? "-" : telepon);
                lblAlamat.setText(alamat.isEmpty() ? "-" : alamat);
                
                JOptionPane.showMessageDialog(this, 
                    "Data berhasil diperbarui!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Gagal memperbarui data!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void ubahPassword() {
        JDialog passwordDialog = new JDialog(this, "Ubah Password", true);
        passwordDialog.setSize(350, 200);
        passwordDialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPasswordField txtPasswordLama = new JPasswordField();
        JPasswordField txtPasswordBaru = new JPasswordField();
        JPasswordField txtKonfirmasiPassword = new JPasswordField();
        
        panel.add(new JLabel("Password Lama:"));
        panel.add(txtPasswordLama);
        
        panel.add(new JLabel("Password Baru:"));
        panel.add(txtPasswordBaru);
        
        panel.add(new JLabel("Konfirmasi Password:"));
        panel.add(txtKonfirmasiPassword);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnSavePassword = new JButton("Simpan Password");
        JButton btnCancel = new JButton("Batal");
        
        btnSavePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changePassword(
                    new String(txtPasswordLama.getPassword()),
                    new String(txtPasswordBaru.getPassword()),
                    new String(txtKonfirmasiPassword.getPassword())
                );
                passwordDialog.dispose();
            }
        });
        
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordDialog.dispose();
            }
        });
        
        buttonPanel.add(btnSavePassword);
        buttonPanel.add(btnCancel);
        
        passwordDialog.add(panel, BorderLayout.CENTER);
        passwordDialog.add(buttonPanel, BorderLayout.SOUTH);
        passwordDialog.setVisible(true);
    }
    
    private void changePassword(String passwordLama, String passwordBaru, String konfirmasiPassword) {
        // Validations
        if (passwordLama.isEmpty() || passwordBaru.isEmpty() || konfirmasiPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Semua field harus diisi!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!passwordBaru.equals(konfirmasiPassword)) {
            JOptionPane.showMessageDialog(this, 
                "Password baru dan konfirmasi password tidak sama!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (passwordBaru.length() < 6) {
            JOptionPane.showMessageDialog(this, 
                "Password baru minimal 6 karakter!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            boolean success = mahasiswaService.ubahPassword(
                mahasiswa.getNim(), 
                passwordLama, 
                passwordBaru
            );
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Password berhasil diubah!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Password lama salah atau gagal mengubah password!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Method to refresh data (if needed from outside)
    public void refreshData(Mahasiswa updatedMahasiswa) {
        this.mahasiswa = updatedMahasiswa;
        updateDisplay();
    }
    
    private void updateDisplay() {
        lblNim.setText(mahasiswa.getNim());
        lblNama.setText(mahasiswa.getNama());
        lblProdi.setText(mahasiswa.getProgramStudi());
        lblFakultas.setText(mahasiswa.getFakultas());
        lblAngkatan.setText(String.valueOf(mahasiswa.getAngkatan()));
        lblStatus.setText(mahasiswa.getStatus()); // PERBAIKAN: Hapus casting (String)
        lblEmail.setText(mahasiswa.getEmail() != null ? mahasiswa.getEmail() : "-");
        lblTelepon.setText(mahasiswa.getNoTelepon() != null ? mahasiswa.getNoTelepon() : "-");
        lblAlamat.setText(mahasiswa.getAlamat() != null ? mahasiswa.getAlamat() : "-");
    }
}