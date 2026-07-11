package view;

import view.CustomComponents.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class LecturerDashboardFrame extends JFrame {
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Sidebar items
    private String[] tabs = {"Profile Details", "Grade Students"};
    private Map<String, JPanel> tabButtons = new HashMap<>();
    private String activeTab = "Profile Details";
    private JLabel lblWelcomeName;
    
    // Panels
    private JPanel profilePanel;
    private JPanel gradingPanel;

    // Profile Fields
    private RoundedTextField txtFullName;
    private RoundedTextField txtDepartment;
    private RoundedTextField txtEmail;
    private RoundedTextField txtMobile;
    private RoundedButton btnSaveProfile;

    // Grading Fields
    private SleekComboBox<String> comboCourses;
    private SleekTable tableStudents;
    private SleekComboBox<String> comboStudentIds;
    private SleekComboBox<String> comboGrades;
    private RoundedButton btnUpdateGrade;

    // Logout
    private JButton btnLogout;

    public LecturerDashboardFrame() {
        setTitle("Faculty Management System - Lecturer Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(CustomComponents.COLOR_BG);

        // 1. Sidebar Panel Setup
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(CustomComponents.COLOR_SIDEBAR);
        sidebarPanel.setBounds(0, 0, 260, 600);
        sidebarPanel.setLayout(null);
        getContentPane().add(sidebarPanel);

        // Avatar Icon
        JPanel profileIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CustomComponents.COLOR_PURPLE);
                g2.fillOval(10, 10, 50, 50);
                g2.setColor(Color.WHITE);
                g2.fillOval(25, 20, 20, 20);
                g2.fill(new java.awt.geom.RoundRectangle2D.Double(18, 43, 34, 15, 8, 8));
                g2.dispose();
            }
        };
        profileIcon.setOpaque(false);
        profileIcon.setBounds(10, 20, 70, 70);
        sidebarPanel.add(profileIcon);

        lblWelcomeName = new JLabel("Welcome, Lecturer");
        lblWelcomeName.setFont(CustomComponents.FONT_SUBTITLE);
        lblWelcomeName.setForeground(Color.WHITE);
        lblWelcomeName.setBounds(90, 40, 160, 25);
        sidebarPanel.add(lblWelcomeName);

        // Tabs setup
        int yOffset = 120;
        for (String tabName : tabs) {
            final String name = tabName;
            JPanel btnPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (activeTab.equals(name)) {
                        g2d.setColor(CustomComponents.COLOR_PURPLE);
                        g2d.fillRoundRect(15, 5, getWidth() - 30, getHeight() - 10, 15, 15);
                    }
                    g2d.dispose();
                }
            };
            btnPanel.setOpaque(false);
            btnPanel.setBounds(0, yOffset, 260, 50);
            btnPanel.setLayout(null);
            btnPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Icons
            JLabel lblIcon = new JLabel();
            lblIcon.setBounds(30, 12, 26, 26);
            lblIcon.setIcon(new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(activeTab.equals(name) ? Color.WHITE : CustomComponents.COLOR_TEXT_SECONDARY);
                    
                    if (name.equals("Profile Details")) {
                        g2.fillOval(x+4, y, 10, 10);
                        g2.fillRoundRect(x, y+11, 18, 8, 4, 4);
                    } else if (name.equals("Grade Students")) {
                        g2.drawRoundRect(x, y, 18, 14, 4, 4);
                        g2.drawLine(x+5, y+5, x+13, y+5);
                        g2.drawLine(x+5, y+9, x+13, y+9);
                    }
                    g2.dispose();
                }
                @Override public int getIconWidth() { return 18; }
                @Override public int getIconHeight() { return 18; }
            });
            btnPanel.add(lblIcon);

            JLabel lblText = new JLabel(tabName);
            lblText.setFont(CustomComponents.FONT_SUBTITLE);
            lblText.setForeground(activeTab.equals(tabName) ? Color.WHITE : CustomComponents.COLOR_TEXT_SECONDARY);
            lblText.setBounds(70, 10, 180, 30);
            btnPanel.add(lblText);

            btnPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    switchPanel(name);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (!activeTab.equals(name)) {
                        lblText.setForeground(Color.WHITE);
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (!activeTab.equals(name)) {
                        lblText.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
                    }
                }
            });

            sidebarPanel.add(btnPanel);
            tabButtons.put(tabName, btnPanel);
            yOffset += 55;
        }

        // Logout
        btnLogout = new JButton("Logout") {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CustomComponents.COLOR_TEXT_SECONDARY);
                g2.drawOval(15, 10, 20, 20);
                g2.drawLine(25, 5, 25, 20);
                g2.dispose();
            }
        };
        btnLogout.setBounds(20, 500, 220, 40);
        btnLogout.setFont(CustomComponents.FONT_SUBTITLE);
        btnLogout.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sidebarPanel.add(btnLogout);

        // 2. Card Panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBounds(260, 0, 740, 600);
        contentPanel.setBackground(CustomComponents.COLOR_BG);
        getContentPane().add(contentPanel);

        setupProfilePanel();
        setupGradingPanel();

        contentPanel.add(profilePanel, "Profile Details");
        contentPanel.add(gradingPanel, "Grade Students");

        cardLayout.show(contentPanel, "Profile Details");
    }

    private void setupProfilePanel() {
        profilePanel = new JPanel();
        profilePanel.setBackground(CustomComponents.COLOR_BG);
        profilePanel.setLayout(null);

        JLabel lblTitle = new JLabel("Profile Details");
        lblTitle.setFont(CustomComponents.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(50, 30, 300, 35);
        profilePanel.add(lblTitle);

        // Full Name
        JLabel lblName = new JLabel("Full Name");
        lblName.setFont(CustomComponents.FONT_SUBTITLE);
        lblName.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblName.setBounds(50, 100, 150, 30);
        profilePanel.add(lblName);

        txtFullName = new RoundedTextField();
        txtFullName.setBounds(200, 100, 450, 35);
        profilePanel.add(txtFullName);

        // Department (non-editable in profile to preserve integrity)
        JLabel lblDept = new JLabel("Department");
        lblDept.setFont(CustomComponents.FONT_SUBTITLE);
        lblDept.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblDept.setBounds(50, 160, 150, 30);
        profilePanel.add(lblDept);

        txtDepartment = new RoundedTextField();
        txtDepartment.setBounds(200, 160, 450, 35);
        txtDepartment.setEditable(false);
        txtDepartment.setBackground(CustomComponents.COLOR_SIDEBAR);
        profilePanel.add(txtDepartment);

        // Email
        JLabel lblMail = new JLabel("Email");
        lblMail.setFont(CustomComponents.FONT_SUBTITLE);
        lblMail.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblMail.setBounds(50, 220, 150, 30);
        profilePanel.add(lblMail);

        txtEmail = new RoundedTextField();
        txtEmail.setBounds(200, 220, 450, 35);
        profilePanel.add(txtEmail);

        // Mobile Number
        JLabel lblMob = new JLabel("Mobile Number");
        lblMob.setFont(CustomComponents.FONT_SUBTITLE);
        lblMob.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblMob.setBounds(50, 280, 150, 30);
        profilePanel.add(lblMob);

        txtMobile = new RoundedTextField();
        txtMobile.setBounds(200, 280, 450, 35);
        profilePanel.add(txtMobile);

        // Save Button
        btnSaveProfile = new RoundedButton("Save changes", CustomComponents.COLOR_PURPLE, CustomComponents.COLOR_PURPLE_HOVER);
        btnSaveProfile.setBounds(200, 360, 450, 45);
        profilePanel.add(btnSaveProfile);
    }

    private void setupGradingPanel() {
        gradingPanel = new JPanel();
        gradingPanel.setBackground(CustomComponents.COLOR_BG);
        gradingPanel.setLayout(null);

        JLabel lblTitle = new JLabel("Grade Students");
        lblTitle.setFont(CustomComponents.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(50, 30, 300, 35);
        gradingPanel.add(lblTitle);

        // Select Course ComboBox
        JLabel lblSelectCourse = new JLabel("Select Course:");
        lblSelectCourse.setFont(CustomComponents.FONT_SUBTITLE);
        lblSelectCourse.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblSelectCourse.setBounds(50, 85, 120, 30);
        gradingPanel.add(lblSelectCourse);

        comboCourses = new SleekComboBox<>();
        comboCourses.setBounds(180, 85, 300, 35);
        gradingPanel.add(comboCourses);

        // Students Table
        tableStudents = new SleekTable();
        JScrollPane sp = new JScrollPane(tableStudents);
        sp.setBounds(50, 140, 640, 280);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(CustomComponents.COLOR_BG);
        gradingPanel.add(sp);

        // Edit Grading controls
        JLabel lblStudentId = new JLabel("Student ID:");
        lblStudentId.setFont(CustomComponents.FONT_SUBTITLE);
        lblStudentId.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblStudentId.setBounds(50, 440, 100, 30);
        gradingPanel.add(lblStudentId);

        comboStudentIds = new SleekComboBox<>();
        comboStudentIds.setBounds(150, 440, 150, 35);
        gradingPanel.add(comboStudentIds);

        JLabel lblGrade = new JLabel("Grade:");
        lblGrade.setFont(CustomComponents.FONT_SUBTITLE);
        lblGrade.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblGrade.setBounds(330, 440, 70, 30);
        gradingPanel.add(lblGrade);

        String[] grades = {"A+", "A", "A-", "B+", "B", "B-", "C+", "C", "D", "F", "Pending"};
        comboGrades = new SleekComboBox<>(grades);
        comboGrades.setBounds(410, 440, 110, 35);
        gradingPanel.add(comboGrades);

        btnUpdateGrade = new RoundedButton("Update Grade", CustomComponents.COLOR_PURPLE, CustomComponents.COLOR_PURPLE_HOVER);
        btnUpdateGrade.setBounds(540, 440, 150, 35);
        gradingPanel.add(btnUpdateGrade);

        // Fill selection fields on table row click
        tableStudents.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tableStudents.getSelectedRow();
                if (row >= 0) {
                    String stdId = tableStudents.getValueAt(row, 0).toString();
                    String grade = tableStudents.getValueAt(row, 3).toString();
                    comboStudentIds.setSelectedItem(stdId);
                    comboGrades.setSelectedItem(grade);
                }
            }
        });
    }

    public void switchPanel(String tabName) {
        this.activeTab = tabName;
        cardLayout.show(contentPanel, tabName);

        for (Map.Entry<String, JPanel> entry : tabButtons.entrySet()) {
            entry.getValue().repaint();
            for (Component c : entry.getValue().getComponents()) {
                if (c instanceof JLabel && !((JLabel) c).getText().isEmpty()) {
                    if (entry.getKey().equals(tabName)) {
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
                    }
                }
            }
        }

        if (lecturerTabSelectionListener != null) {
            lecturerTabSelectionListener.tabSelected(tabName);
        }
    }

    public interface LecturerTabSelectionListener {
        void tabSelected(String tabName);
    }
    
    private LecturerTabSelectionListener lecturerTabSelectionListener;

    public void setLecturerTabSelectionListener(LecturerTabSelectionListener listener) {
        this.lecturerTabSelectionListener = listener;
    }

    public void setWelcomeName(String name) {
        lblWelcomeName.setText("Welcome, " + name);
    }

    // Profile Getters / Setters
    public void setProfileData(String fullName, String dept, String email, String mobile) {
        txtFullName.setText(fullName);
        txtDepartment.setText(dept);
        txtEmail.setText(email);
        txtMobile.setText(mobile);
    }

    public String getFullName() { return txtFullName.getText().trim(); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getMobile() { return txtMobile.getText().trim(); }
    public RoundedButton getBtnSaveProfile() { return btnSaveProfile; }

    // Grading Getters / Setters
    public SleekComboBox<String> getComboCourses() { return comboCourses; }
    public SleekTable getTableStudents() { return tableStudents; }
    public SleekComboBox<String> getComboStudentIds() { return comboStudentIds; }
    public SleekComboBox<String> getComboGrades() { return comboGrades; }
    public String getSelectedStudentId() { return comboStudentIds.getSelectedItem() != null ? comboStudentIds.getSelectedItem().toString() : ""; }
    public String getSelectedGrade() { return comboGrades.getSelectedItem().toString(); }
    public RoundedButton getBtnUpdateGrade() { return btnUpdateGrade; }
    public JButton getBtnLogout() { return btnLogout; }
}
