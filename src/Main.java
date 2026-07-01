import model.DBConnector;
import view.AuthFrame;
import controller.AuthController;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Use system look and feel as baseline, but our custom components override styling
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize Database and Seed Data in the background/startup
        SwingUtilities.invokeLater(() -> {
            try {
                // Auto-create database & tables if they don't exist
                DBConnector.initializeDatabase();
            } catch (Exception e) {
                System.err.println("DB Initialization Warning: " + e.getMessage());
            }

            // Launch Authentication View
            AuthFrame authFrame = new AuthFrame();
            new AuthController(authFrame);
            authFrame.setVisible(true);
        });
    }
}
