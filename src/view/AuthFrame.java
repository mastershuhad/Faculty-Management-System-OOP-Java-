package view;

import view.CustomComponents.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AuthFrame extends JFrame {
    private JPanel sidebarPanel;
    private JPanel formPanel;
    
    // Tabs
    private JLabel tabSignIn;
    private JLabel tabSignUp;
    private boolean isSignInTab = true;

    // Labels & Input Fields
    private JLabel lblUsername, lblPassword, lblConfirmPassword;
    private RoundedTextField txtUsername;
    private RoundedPasswordField txtPassword, txtConfirmPassword;

    // Student & Lecturer Specific Fields
    private JLabel lblFullName, lblStudentId, lblDegree, lblDept, lblEmail, lblMobile;
    private RoundedTextField txtFullName, txtStudentId, txtEmail, txtMobile;
    private SleekComboBox<String> comboDegree, comboDept;

    // Role Buttons
    private JLabel lblRole;
    private JButton btnAdmin, btnStudent, btnLecturer;
    private String selectedRole = "Admin"; // Default

    // Action Button
    private RoundedButton btnAction;

    public AuthFrame() {
        setTitle("Faculty Management System - Authentication");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(CustomComponents.COLOR_BG);

        // 1. Sidebar Panel Setup
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(CustomComponents.COLOR_SIDEBAR);
        sidebarPanel.setBounds(0, 0, 380, 550);
        sidebarPanel.setLayout(null);
        getContentPane().add(sidebarPanel);

        // Graduation Cap Icon
        JPanel capIcon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                // Draw Diamond top
                int[] xPoints = {60, 110, 60, 10};
                int[] yPoints = {20, 45, 70, 45};
                g2.fillPolygon(xPoints, yPoints, 4);
                // Draw Base
                g2.fill(new java.awt.geom.RoundRectangle2D.Double(35, 52, 50, 22, 10, 10));
                // Draw Tassel
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(60, 45, 25, 55);
                g2.fillOval(20, 53, 9, 9);
                g2.dispose();
            }
        };
        capIcon.setOpaque(false);
        capIcon.setBounds(130, 80, 120, 100);
        sidebarPanel.add(capIcon);

        // Sidebar Text
        JLabel lblTitle = new JLabel("Faculty Management", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(20, 200, 340, 30);
        sidebarPanel.add(lblTitle);

        JLabel lblTitleSub = new JLabel("System", SwingConstants.CENTER);
        lblTitleSub.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitleSub.setForeground(Color.WHITE);
        lblTitleSub.setBounds(20, 230, 340, 30);
        sidebarPanel.add(lblTitleSub);

        JLabel lblFaculty = new JLabel("Faculty of Computing & Technology", SwingConstants.CENTER);
        lblFaculty.setFont(CustomComponents.FONT_SUBTITLE);
        lblFaculty.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblFaculty.setBounds(20, 350, 340, 25);
        sidebarPanel.add(lblFaculty);

        JLabel lblMotto = new JLabel("Manage your academic journey", SwingConstants.CENTER);
        lblMotto.setFont(CustomComponents.FONT_SMALL);
        lblMotto.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        lblMotto.setBounds(20, 380, 340, 20);
        sidebarPanel.add(lblMotto);

        // 2. Form Panel Setup
        formPanel = new JPanel();
        formPanel.setBackground(CustomComponents.COLOR_BG);
        formPanel.setBounds(380, 0, 520, 550);
        formPanel.setLayout(null);
        getContentPane().add(formPanel);

        // Sign In Tab
        tabSignIn = new JLabel("Sign In", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (isSignInTab) {
                    g.setColor(CustomComponents.COLOR_PURPLE);
                    g.fillRect(10, getHeight() - 4, getWidth() - 20, 4);
                }
            }
        };
        tabSignIn.setFont(new Font("Arial", Font.BOLD, 18));
        tabSignIn.setForeground(Color.WHITE);
        tabSignIn.setBounds(70, 30, 120, 40);
        tabSignIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(tabSignIn);

        // Sign Up Tab
        tabSignUp = new JLabel("Sign Up", SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (!isSignInTab) {
                    g.setColor(CustomComponents.COLOR_PURPLE);
                    g.fillRect(10, getHeight() - 4, getWidth() - 20, 4);
                }
            }
        };
        tabSignUp.setFont(new Font("Arial", Font.BOLD, 18));
        tabSignUp.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
        tabSignUp.setBounds(210, 30, 120, 40);
        tabSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(tabSignUp);

        // Tab click behavior
        tabSignIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchTab(true);
            }
        });
        tabSignUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchTab(false);
            }
        });

        // Initialize Core Fields
        lblUsername = createLabel("Username");
        txtUsername = new RoundedTextField();
        lblPassword = createLabel("Password");
        txtPassword = new RoundedPasswordField();
        lblConfirmPassword = createLabel("Confirm Password");
        txtConfirmPassword = new RoundedPasswordField();

        // Initialize Extra Fields
        lblFullName = createLabel("Full Name");
        txtFullName = new RoundedTextField();
        lblStudentId = createLabel("Student ID");
        txtStudentId = new RoundedTextField();
        
        lblDegree = createLabel("Degree");
        String[] degrees = {"Engineering Technology", "Information Technology", "Computer Science", "Bio Systems Technology"};
        comboDegree = new SleekComboBox<>(degrees);
        
        lblDept = createLabel("Department");
        String[] depts = {"Applied Computing", "Software Engineering", "Computer Systems Engineering"};
        comboDept = new SleekComboBox<>(depts);

        lblEmail = createLabel("Email");
        txtEmail = new RoundedTextField();
        lblMobile = createLabel("Mobile Number");
        txtMobile = new RoundedTextField();

        // Add all to panel
        formPanel.add(lblUsername); formPanel.add(txtUsername);
        formPanel.add(lblPassword); formPanel.add(txtPassword);
        formPanel.add(lblConfirmPassword); formPanel.add(txtConfirmPassword);
        formPanel.add(lblFullName); formPanel.add(txtFullName);
        formPanel.add(lblStudentId); formPanel.add(txtStudentId);
        formPanel.add(lblDegree); formPanel.add(comboDegree);
        formPanel.add(lblDept); formPanel.add(comboDept);
        formPanel.add(lblEmail); formPanel.add(txtEmail);
        formPanel.add(lblMobile); formPanel.add(txtMobile);

        // Role Selection Label
        lblRole = createLabel("Role");
        formPanel.add(lblRole);

        // Role Toggle Buttons
        btnAdmin = createRoleButton("Admin", 50, 285);
        btnStudent = createRoleButton("Student", 170, 285);
        btnLecturer = createRoleButton("Lecturer", 290, 285);
        formPanel.add(btnAdmin);
        formPanel.add(btnStudent);
        formPanel.add(btnLecturer);

        // Action Button
        btnAction = new RoundedButton("Sign In", CustomComponents.COLOR_PURPLE, CustomComponents.COLOR_PURPLE_HOVER);
        btnAction.setBounds(50, 360, 420, 45);
        formPanel.add(btnAction);

        // Layout first state
        switchTab(true);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(CustomComponents.FONT_SUBTITLE);
        label.setForeground(Color.WHITE);
        return label;
    }

    private JButton createRoleButton(String role, int x, int y) {
        JButton btn = new JButton(role) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (selectedRole.equals(getText())) {
                    g2d.setColor(CustomComponents.COLOR_PURPLE);
                } else {
                    g2d.setColor(CustomComponents.COLOR_INPUT);
                }
                g2d.fill(new java.awt.geom.RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 15, 15));
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBounds(x, y, 110, 35);
        btn.setFont(CustomComponents.FONT_SMALL);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addActionListener(e -> {
            selectedRole = btn.getText();
            updateRoleButtons();
            // Re-layout dynamic fields immediately on role toggle if in Sign Up tab
            if (!isSignInTab) {
                arrangeSignUpLayout();
            }
        });
        return btn;
    }

    private void updateRoleButtons() {
        btnAdmin.repaint();
        btnStudent.repaint();
        btnLecturer.repaint();
    }

    private void hideAllExtraFields() {
        lblConfirmPassword.setVisible(false); txtConfirmPassword.setVisible(false);
        lblFullName.setVisible(false); txtFullName.setVisible(false);
        lblStudentId.setVisible(false); txtStudentId.setVisible(false);
        lblDegree.setVisible(false); comboDegree.setVisible(false);
        lblDept.setVisible(false); comboDept.setVisible(false);
        lblEmail.setVisible(false); txtEmail.setVisible(false);
        lblMobile.setVisible(false); txtMobile.setVisible(false);
    }

    private void arrangeSignUpLayout() {
        hideAllExtraFields();
        lblConfirmPassword.setVisible(true); txtConfirmPassword.setVisible(true);

        if (selectedRole.equals("Admin")) {
            // Standard Single Column Admin View
            lblUsername.setBounds(50, 85, 420, 20); txtUsername.setBounds(50, 105, 420, 35);
            lblPassword.setBounds(50, 150, 420, 20); txtPassword.setBounds(50, 170, 420, 35);
            lblConfirmPassword.setBounds(50, 215, 420, 20); txtConfirmPassword.setBounds(50, 235, 420, 35);

            lblRole.setBounds(50, 285, 420, 20);
            btnAdmin.setBounds(50, 310, 110, 35);
            btnStudent.setBounds(170, 310, 110, 35);
            btnLecturer.setBounds(290, 310, 110, 35);

            btnAction.setBounds(50, 370, 420, 45);
        } 
        else if (selectedRole.equals("Student")) {
            // Show all student details in 2 columns
            lblFullName.setVisible(true); txtFullName.setVisible(true);
            lblStudentId.setVisible(true); txtStudentId.setVisible(true);
            lblDegree.setVisible(true); comboDegree.setVisible(true);
            lblEmail.setVisible(true); txtEmail.setVisible(true);
            lblMobile.setVisible(true); txtMobile.setVisible(true);

            // Row 1: Username & Password
            lblUsername.setBounds(50, 80, 200, 20); txtUsername.setBounds(50, 100, 200, 35);
            lblPassword.setBounds(270, 80, 200, 20); txtPassword.setBounds(270, 100, 200, 35);

            // Row 2: Confirm Password & Full Name
            lblConfirmPassword.setBounds(50, 140, 200, 20); txtConfirmPassword.setBounds(50, 160, 200, 35);
            lblFullName.setBounds(270, 140, 200, 20); txtFullName.setBounds(270, 160, 200, 35);

            // Row 3: Student ID & Degree
            lblStudentId.setBounds(50, 200, 200, 20); txtStudentId.setBounds(50, 220, 200, 35);
            lblDegree.setBounds(270, 200, 200, 20); comboDegree.setBounds(270, 220, 200, 35);

            // Row 4: Email & Mobile
            lblEmail.setBounds(50, 260, 200, 20); txtEmail.setBounds(50, 280, 200, 35);
            lblMobile.setBounds(270, 260, 200, 20); txtMobile.setBounds(270, 280, 200, 35);

            // Role selection
            lblRole.setBounds(50, 335, 420, 20);
            btnAdmin.setBounds(50, 360, 110, 35);
            btnStudent.setBounds(170, 360, 110, 35);
            btnLecturer.setBounds(290, 360, 110, 35);

            btnAction.setBounds(50, 425, 420, 45);
        } 
        else if (selectedRole.equals("Lecturer")) {
            // Show lecturer details in 2 columns
            lblFullName.setVisible(true); txtFullName.setVisible(true);
            lblDept.setVisible(true); comboDept.setVisible(true);
            lblEmail.setVisible(true); txtEmail.setVisible(true);
            lblMobile.setVisible(true); txtMobile.setVisible(true);

            // Row 1: Username & Password
            lblUsername.setBounds(50, 80, 200, 20); txtUsername.setBounds(50, 100, 200, 35);
            lblPassword.setBounds(270, 80, 200, 20); txtPassword.setBounds(270, 100, 200, 35);

            // Row 2: Confirm Password & Full Name
            lblConfirmPassword.setBounds(50, 140, 200, 20); txtConfirmPassword.setBounds(50, 160, 200, 35);
            lblFullName.setBounds(270, 140, 200, 20); txtFullName.setBounds(270, 160, 200, 35);

            // Row 3: Department & Email
            lblDept.setBounds(50, 200, 200, 20); comboDept.setBounds(50, 220, 200, 35);
            lblEmail.setBounds(270, 200, 200, 20); txtEmail.setBounds(270, 220, 200, 35);

            // Row 4: Mobile
            lblMobile.setBounds(50, 260, 200, 20); txtMobile.setBounds(50, 280, 200, 35);

            // Role selection
            lblRole.setBounds(50, 335, 420, 20);
            btnAdmin.setBounds(50, 360, 110, 35);
            btnStudent.setBounds(170, 360, 110, 35);
            btnLecturer.setBounds(290, 360, 110, 35);

            btnAction.setBounds(50, 425, 420, 45);
        }
        formPanel.repaint();
    }

    private void switchTab(boolean toSignIn) {
        isSignInTab = toSignIn;
        if (isSignInTab) {
            tabSignIn.setForeground(Color.WHITE);
            tabSignUp.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
            hideAllExtraFields();

            // Simple Sign In Layout
            lblUsername.setBounds(50, 100, 420, 20); txtUsername.setBounds(50, 125, 420, 40);
            lblPassword.setBounds(50, 180, 420, 20); txtPassword.setBounds(50, 205, 420, 40);
            
            lblRole.setBounds(50, 260, 420, 20);
            btnAdmin.setBounds(50, 285, 110, 35);
            btnStudent.setBounds(170, 285, 110, 35);
            btnLecturer.setBounds(290, 285, 110, 35);
            
            btnAction.setText("Sign In");
            btnAction.setBounds(50, 360, 420, 45);
        } else {
            tabSignIn.setForeground(CustomComponents.COLOR_TEXT_SECONDARY);
            tabSignUp.setForeground(Color.WHITE);
            btnAction.setText("Sign Up");
            arrangeSignUpLayout();
        }
        formPanel.repaint();
    }

    // Getters for Controller interaction
    public String getUsername() { return txtUsername.getText().trim(); }
    public String getPassword() { return new String(txtPassword.getPassword()); }
    public String getConfirmPassword() { return new String(txtConfirmPassword.getPassword()); }
    public String getSelectedRole() { return selectedRole; }
    public RoundedButton getActionButton() { return btnAction; }
    public boolean isSignInMode() { return isSignInTab; }

    // Multi-detail getters for Sign Up
    public String getFullName() { return txtFullName.getText().trim(); }
    public String getStudentId() { return txtStudentId.getText().trim(); }
    public String getSelectedDegree() { return comboDegree.getSelectedItem().toString(); }
    public String getSelectedDept() { return comboDept.getSelectedItem().toString(); }
    public String getEmail() { return txtEmail.getText().trim(); }
    public String getMobile() { return txtMobile.getText().trim(); }

    public void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtFullName.setText("");
        txtStudentId.setText("");
        txtEmail.setText("");
        txtMobile.setText("");
    }
}
