package view;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private JButton logoutButton;
    private JLabel welcomeLabel;

    public DashboardFrame() {
        setTitle("FMS - Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        
        setLayout(new BorderLayout());
        
        welcomeLabel = new JLabel("Welcome to the Management System!", SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.CENTER);
        
        logoutButton = new JButton("Logout");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(logoutButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Getters so the controller can access the components
    public JButton getLogoutButton() {
        return logoutButton;
    }
}