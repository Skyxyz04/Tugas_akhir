package Ui;

import Model.Admin;
import Model.Dosen;
import Service.AdminService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManajemenDosenFrame extends JFrame {
    private Admin admin;
    private AdminService adminService;
    private JTable tblDosen;
    private DefaultTableModel tableModel;
    
    public ManajemenDosenFrame(Admin admin) {
        this.admin = admin;
        this.adminService = new AdminService();
        initializeUI();
        loadData();
    }
    
    private void initializeUI() {
        setTitle("Manajemen Dosen - SIASAT UKSW");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        
        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"No", "NIDN", "Nama", "Program Studi", "Fakultas", "Jabatan", "Status", "Email", "Aksi"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Hanya kolom aksi yang editable
            }
        };
        
        tblDosen = new JTable(tableModel);
        tblDosen.setRowHeight(30);
        
        // Tambahkan renderer untuk kolom aksi
        tblDosen.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        tblDosen.getColumnModel().getColumn(8).setCellEditor(new ButtonEditor(new JCheckBox(), adminService, this));
        
        JScrollPane scrollPane = new JScrollPane(tblDosen);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        
        JLabel lblTitle = new JLabel("MANAJEMEN DATA DOSEN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(102, 0, 0));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTambah = new JButton("Tambah Dosen");
        JButton btnRefresh = new JButton("Refresh");
        
        btnTambah.setBackground(new Color(0, 102, 204));
        btnRefresh.setBackground(new Color(102, 102, 102));
        btnTambah.setForeground(Color.WHITE);
        btnRefresh.setForeground(Color.WHITE);
        
        btnTambah.addActionListener(e -> showTambahDosenDialog());
        btnRefresh.addActionListener(e -> loadData());
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnRefresh);
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        List<Dosen> list = adminService.getAllDosen();
        
        int no = 1;
        for (Dosen dosen : list) {
            tableModel.addRow(new Object[]{
                no++,
                dosen.getNidn(),
                dosen.getNama(),
                dosen.getProgramStudi(),
                dosen.getFakultas(),
                dosen.getJabatan(),
                dosen.getStatus(),
                dosen.getEmail(),
                "Edit/Hapus"
            });
        }
    }
    
    private void showTambahDosenDialog() {
        JDialog dialog = new JDialog(this, "Tambah Dosen", true);
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JTextField txtNidn = new JTextField();
        JTextField txtNama = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtProdi = new JTextField();
        JTextField txtFakultas = new JTextField();
        JComboBox<String> cmbJabatan = new JComboBox<>(new String[]{"Dosen", "Ketua Prodi", "Dekan"});
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Aktif", "Non-Aktif"});
        JPasswordField txtPassword = new JPasswordField();
        
        panel.add(new JLabel("NIDN:*"));
        panel.add(txtNidn);
        panel.add(new JLabel("Nama:*"));
        panel.add(txtNama);
        panel.add(new JLabel("Email:*"));
        panel.add(txtEmail);
        panel.add(new JLabel("Program Studi:*"));
        panel.add(txtProdi);
        panel.add(new JLabel("Fakultas:*"));
        panel.add(txtFakultas);
        panel.add(new JLabel("Jabatan:*"));
        panel.add(cmbJabatan);
        panel.add(new JLabel("Status:*"));
        panel.add(cmbStatus);
        panel.add(new JLabel("Password:*"));
        panel.add(txtPassword);
        
        JButton btnSimpan = new JButton("Simpan");
        JButton btnBatal = new JButton("Batal");
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnSimpan);
        buttonPanel.add(btnBatal);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        btnSimpan.addActionListener(e -> {
            if (validateInput(txtNidn, txtNama, txtEmail, txtProdi, txtFakultas, txtPassword)) {
                Dosen dosen = new Dosen();
                dosen.setNidn(txtNidn.getText().trim());
                dosen.setNama(txtNama.getText().trim());
                dosen.setEmail(txtEmail.getText().trim());
                dosen.setProgramStudi(txtProdi.getText().trim());
                dosen.setFakultas(txtFakultas.getText().trim());
                dosen.setJabatan((String) cmbJabatan.getSelectedItem());
                dosen.setStatus((String) cmbStatus.getSelectedItem());
                dosen.setPassword(new String(txtPassword.getPassword()));
                
                if (adminService.addDosen(dosen)) {
                    JOptionPane.showMessageDialog(dialog, "Dosen berhasil ditambahkan!");
                    dialog.dispose();
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Gagal menambahkan dosen!");
                }
            }
        });
        
        btnBatal.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private boolean validateInput(JTextField... fields) {
        for (JTextField field : fields) {
            if (field.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field bertanda (*) harus diisi!");
                field.requestFocus();
                return false;
            }
        }
        return true;
    }
    
    // Method untuk refresh data dari luar
    public void refreshData() {
        loadData();
    }
}

// Kelas untuk render tombol di tabel
class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }
    
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}

// Kelas untuk handle klik tombol di tabel
class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;
    private AdminService adminService;
    private ManajemenDosenFrame frame;
    
    public ButtonEditor(JCheckBox checkBox, AdminService adminService, ManajemenDosenFrame frame) {
        super(checkBox);
        this.adminService = adminService;
        this.frame = frame;
        
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }
    
    public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int row, int column) {
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        return button;
    }
    
    public Object getCellEditorValue() {
        if (isPushed) {
            // Handle button click here
            JOptionPane.showMessageDialog(button, "Tombol diklik!");
        }
        isPushed = false;
        return label;
    }
    
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
    
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}