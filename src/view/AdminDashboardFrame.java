package view;

import view.CustomComponents.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class AdminDashboardFrame extends JFrame {
    private JPanel sidebarPanel;
    private JPanel contentPanel;
    
    // Sidebar tabs
    private String[] tabs = {"Students", "Lecturers", "Courses", "Departments", "Degrees"};
    private Map<String, JPanel> tabButtons = new HashMap<>();
    private String activeTab = "Students";
    
    // Content Header Title
    private JLabel lblContentTitle;
    
    // Action Buttons
    private RoundedButton btnAddNew;
    private RoundedButton btnEdit;
    private RoundedButton btnDelete;
    private RoundedButton btnSaveChanges;
    
    // Main Data Table
    private SleekTable table;
    private JScrollPane scrollPane;
    
    // Logout Button
    private JButton btnLogout;

    public AdminDashboardFrame() {
        setTitle("Faculty Management System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setLayout(null);
        getContentPane().setBackground(CustomComponents.COLOR_BG);

        // 1. Sidebar Setup
        sidebarPanel = new JPanel();
        sidebarPanel.setBackground(CustomComponents.COLOR_SIDEBAR);
        sidebarPanel.setBounds(0, 0, 260, 600);
        sidebarPanel.setLayout(null);
        getContentPane().add(sidebarPanel);

        // Admin Icon Profile Panel
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

        JLabel lblGreeting = new JLabel("Welcome, Admin");
        lblGreeting.setFont(CustomComponents.FONT_SUBTITLE);
        lblGreeting.setForeground(Color.WHITE);
        lblGreeting.setBounds(90, 40, 160, 25);
        sidebarPanel.add(lblGreeting);

        // Sidebar Navigation Tabs
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

            JLabel lblIcon = new JLabel();
            lblIcon.setBounds(30, 12, 26, 26);
            // Draw geometric icons inline
            lblIcon.setIcon(new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(activeTab.equals(name) ? Color.BLACK : CustomComponents.COLOR_TEXT_SECONDARY);
                    
                    if (name.equals("Students")) {
                        g2.fillOval(x+4, y, 10, 10);
                        g2.fillRoundRect(x, y+11, 18, 8, 4, 4);
                    } else if (name.equals("Lecturers")) {
                        g2.fillOval(x+4, y, 10, 10);
                        g2.drawRoundRect(x, y+11, 18, 8, 4, 4);
                    } else if (name.equals("Courses")) {
                        g2.drawRect(x, y+2, 18, 14);
                        g2.drawLine(x, y+6, x+18, y+6);
                        g2.drawLine(x, y+11, x+18, y+11);
                    } else if (name.equals("Departments")) {
                        int[] xp = {x+9, x+18, x};
                        int[] yp = {y, y+8, y+8};
                        g2.fillPolygon(xp, yp, 3);
                        g2.fillRect(x+2, y+9, 4, 9);
                        g2.fillRect(x+7, y+9, 4, 9);
                        g2.fillRect(x+12, y+9, 4, 9);
                    } else if (name.equals("Degrees")) {
                        int[] xp = {x+9, x+18, x+9, x};
                        int[] yp = {y, y+6, y+12, y+6};
                        g2.fillPolygon(xp, yp, 4);
                        g2.fillRect(x+6, y+12, 6, 6);
                    }
                    g2.dispose();
                }
                @Override public int getIconWidth() { return 18; }
                @Override public int getIconHeight() { return 18; }
            });
            btnPanel.add(lblIcon);

            JLabel lblText = new JLabel(tabName);
            lblText.setFont(CustomComponents.FONT_SUBTITLE);
            lblText.setForeground(activeTab.equals(tabName) ? Color.white : CustomComponents.COLOR_TEXT_SECONDARY);
            lblText.setBounds(70, 10, 180, 30);
            btnPanel.add(lblText);

            btnPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    setActiveTab(name);
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

        // Logout Button Setup
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

        // 2. Content Panel Setup
        contentPanel = new JPanel();
        contentPanel.setBackground(CustomComponents.COLOR_BG);
        contentPanel.setBounds(260, 0, 740, 600);
        contentPanel.setLayout(null);
        getContentPane().add(contentPanel);

        // Panel Title
        lblContentTitle = new JLabel("Students");
        lblContentTitle.setFont(CustomComponents.FONT_TITLE);
        lblContentTitle.setForeground(Color.WHITE);
        lblContentTitle.setBounds(40, 25, 250, 35);
        contentPanel.add(lblContentTitle);

        // Top Action Buttons
        btnAddNew = new RoundedButton("Add new", CustomComponents.COLOR_PURPLE, CustomComponents.COLOR_PURPLE_HOVER);
        btnAddNew.setBounds(260, 80, 130, 35);
        contentPanel.add(btnAddNew);

        btnEdit = new RoundedButton("Edit", CustomComponents.COLOR_INPUT, CustomComponents.COLOR_BORDER);
        btnEdit.setBounds(405, 80, 130, 35);
        contentPanel.add(btnEdit);

        btnDelete = new RoundedButton("Delete", CustomComponents.COLOR_INPUT, CustomComponents.COLOR_BORDER);
        btnDelete.setBounds(550, 80, 130, 35);
        contentPanel.add(btnDelete);

        // ScrollPane and Table setup
        table = new SleekTable();
        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(40, 140, 660, 340);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CustomComponents.COLOR_BG);
        contentPanel.add(scrollPane);

        // Save Changes bottom button
        btnSaveChanges = new RoundedButton("Save changes", CustomComponents.COLOR_PURPLE, CustomComponents.COLOR_PURPLE_HOVER);
        btnSaveChanges.setBounds(40, 500, 660, 45);
        contentPanel.add(btnSaveChanges);
    }

    public void setActiveTab(String tabName) {
        this.activeTab = tabName;
        lblContentTitle.setText(tabName);
        
        // Redraw sidebar items
        for (Map.Entry<String, JPanel> entry : tabButtons.entrySet()) {
            entry.getValue().repaint();
            // Get label inside panel and color it
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
        
        // Notify controller to load the relevant data (via event hook in controller)
        if (tabSelectionListener != null) {
            tabSelectionListener.tabSelected(tabName);
        }
    }

    public interface TabSelectionListener {
        void tabSelected(String tabName);
    }
    
    private TabSelectionListener tabSelectionListener;

    public void setTabSelectionListener(TabSelectionListener listener) {
        this.tabSelectionListener = listener;
    }

    // Set Table Data Model
    public void setTableModel(DefaultTableModel model) {
        table.setModel(model);
    }
    
    public SleekTable getTable() { return table; }

    // Action button getters
    public RoundedButton getBtnAddNew() { return btnAddNew; }
    public RoundedButton getBtnEdit() { return btnEdit; }
    public RoundedButton getBtnDelete() { return btnDelete; }
    public RoundedButton getBtnSaveChanges() { return btnSaveChanges; }
    public JButton getBtnLogout() { return btnLogout; }
    public String getActiveTab() { return activeTab; }
}
