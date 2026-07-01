package view;

import view.CustomComponents.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class StudentDashboardFrame extends JFrame {
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Sidebar items
    private String[] tabs = {"Profile Details", "Time table", "Course Enrolled"};
    private Map<String, JPanel> tabButtons = new HashMap<>();
    private String activeTab = "Profile Details";
    private JLabel lblWelcomeName;
    
    // Sub-panels (swapped via CardLayout)
    private JPanel profilePanel;
    private JPanel timetablePanel;
    private JPanel coursesPanel;

    // Profile Details Form Fields
    private RoundedTextField txtFullName;
    private RoundedTextField txtStudentId;
    private RoundedTextField txtDegree;
    private RoundedTextField txtEmail;
    private RoundedTextField txtMobile;
    private RoundedButton btnSaveProfile;

    // Timetable SleekTable
    private SleekTable tableTimetable;
    
    // Courses SleekTable
    private SleekTable tableCourses;

    // Logout Button
    private JButton btnLogout;

    public StudentDashboardFrame() {
        setTitle("Faculty Management System - Student Dashboard");
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

        // Profile Avatar
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

        lblWelcomeName = new JLabel("Welcome, Kumar");
        lblWelcomeName.setFont(CustomComponents.FONT_SUBTITLE);
        lblWelcomeName.setForeground(Color.WHITE);
        lblWelcomeName.setBounds(90, 40, 160, 25);
        sidebarPanel.add(lblWelcomeName);

        // Navigation Tabs in Sidebar
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
                    } else if (name.equals("Time table")) {
                        g2.drawRect(x, y, 18, 18);
                        g2.drawLine(x+4, y, x+4, y+18);
                        g2.drawLine(x+9, y, x+9, y+18);
                        g2.drawLine(x+14, y, x+14, y+18);
                    } else if (name.equals("Course Enrolled")) {
                        g2.drawRect(x, y+2, 18, 14);
                        g2.drawLine(x+9, y+2, x+9, y+16);
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

        // 2. Right Content Panel (Card Layout)
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBounds(260, 0, 740, 600);
        contentPanel.setBackground(CustomComponents.COLOR_BG);
        getContentPane().add(contentPanel);

        // Initializing sub-panels
        setupProfilePanel();
        setupTimetablePanel();
        setupCoursesPanel();

        contentPanel.add(profilePanel, "Profile Details");
        contentPanel.add(timetablePanel, "Time table");
        contentPanel.add(coursesPanel, "Course Enrolled");

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

        // Student ID (non-editable in UI dashboard to prevent system key break)
        JLabel lblId = new JLabel("Student ID");
        lblId.setFont(CustomComponents.FONT_SUBTITLE);
        lblId.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblId.setBounds(50, 160, 150, 30);
        profilePanel.add(lblId);

        txtStudentId = new RoundedTextField();
        txtStudentId.setBounds(200, 160, 450, 35);
        txtStudentId.setEditable(false);
        txtStudentId.setBackground(CustomComponents.COLOR_SIDEBAR);
        profilePanel.add(txtStudentId);

        // Degree
        JLabel lblDeg = new JLabel("Degree");
        lblDeg.setFont(CustomComponents.FONT_SUBTITLE);
        lblDeg.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblDeg.setBounds(50, 220, 150, 30);
        profilePanel.add(lblDeg);

        txtDegree = new RoundedTextField();
        txtDegree.setBounds(200, 220, 450, 35);
        txtDegree.setEditable(false);
        txtDegree.setBackground(CustomComponents.COLOR_SIDEBAR);
        profilePanel.add(txtDegree);

        // Email
        JLabel lblMail = new JLabel("Email");
        lblMail.setFont(CustomComponents.FONT_SUBTITLE);
        lblMail.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblMail.setBounds(50, 280, 150, 30);
        profilePanel.add(lblMail);

        txtEmail = new RoundedTextField();
        txtEmail.setBounds(200, 280, 450, 35);
        profilePanel.add(txtEmail);

        // Mobile Number
        JLabel lblMob = new JLabel("Mobile Number");
        lblMob.setFont(CustomComponents.FONT_SUBTITLE);
        lblMob.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblMob.setBounds(50, 340, 150, 30);
        profilePanel.add(lblMob);

        txtMobile = new RoundedTextField();
        txtMobile.setBounds(200, 340, 450, 35);
        profilePanel.add(txtMobile);

        // Save Changes
        btnSaveProfile = new RoundedButton("Save changes", CustomComponents.COLOR_PURPLE, CustomComponents.COLOR_PURPLE_HOVER);
        btnSaveProfile.setBounds(200, 420, 450, 45);
        profilePanel.add(btnSaveProfile);
    }

    private void setupTimetablePanel() {
        timetablePanel = new JPanel();
        timetablePanel.setBackground(CustomComponents.COLOR_BG);
        timetablePanel.setLayout(null);

        JLabel lblTitle = new JLabel("Time table");
        lblTitle.setFont(CustomComponents.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(50, 30, 300, 35);
        timetablePanel.add(lblTitle);

        tableTimetable = new SleekTable() {
            // Enable custom grid layout with Interval panel
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        JScrollPane sp = new JScrollPane(tableTimetable);
        sp.setBounds(50, 100, 640, 420);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(CustomComponents.COLOR_BG);
        timetablePanel.add(sp);
    }

    private void setupCoursesPanel() {
        coursesPanel = new JPanel();
        coursesPanel.setBackground(CustomComponents.COLOR_BG);
        coursesPanel.setLayout(null);

        JLabel lblTitle = new JLabel("Courses Enrolled");
        lblTitle.setFont(CustomComponents.FONT_TITLE);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(50, 30, 300, 35);
        coursesPanel.add(lblTitle);

        tableCourses = new SleekTable();
        JScrollPane sp = new JScrollPane(tableCourses);
        sp.setBounds(50, 100, 640, 420);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(CustomComponents.COLOR_BG);
        coursesPanel.add(sp);
    }

    public void switchPanel(String tabName) {
        this.activeTab = tabName;
        cardLayout.show(contentPanel, tabName);

        // Repaint sidebar selections
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
        
        // Notify tab selection listener
        if (studentTabSelectionListener != null) {
            studentTabSelectionListener.tabSelected(tabName);
        }
    }

    public interface StudentTabSelectionListener {
        void tabSelected(String tabName);
    }
    
    private StudentTabSelectionListener studentTabSelectionListener;

    public void setStudentTabSelectionListener(StudentTabSelectionListener listener) {
        this.studentTabSelectionListener = listener;
    }

    // Set Welcome Header User Name
    public void setWelcomeName(String name) {
        lblWelcomeName.setText("Welcome, " + name);
    }

    // Getters / Setters for profile values
    public void setProfileData(String fullName, String studentId, String degree, String email, String mobile) {
        txtFullName.setText(fullName);
        txtStudentId.setText(studentId);
        txtDegree.setText(degree);
        txtEmail.setText(email);
        txtMobile.setText(mobile);
    }

    public String getFullName() { return txtFullName.getText().trim(); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getMobile() { return txtMobile.getText().trim(); }

    public RoundedButton getBtnSaveProfile() { return btnSaveProfile; }
    public JButton getBtnLogout() { return btnLogout; }

    public void setTimetableModel(DefaultTableModel model) {
        tableTimetable.setModel(model);
        // Custom styling for Interval row
        tableTimetable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                setForeground(CustomComponents.COLOR_TEXT_PRIMARY);
                if (row == 2) { // Interval row
                    c.setBackground(CustomComponents.COLOR_PURPLE);
                    c.setFont(CustomComponents.FONT_SUBTITLE);
                } else {
                    if (row % 2 == 0) c.setBackground(CustomComponents.COLOR_CARD);
                    else c.setBackground(new Color(26, 26, 32));
                }
                return c;
            }
        });
    }

    public void setCoursesModel(DefaultTableModel model) {
        tableCourses.setModel(model);
    }
}
