package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnector {
    private static final String URL_SERVER = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String URL_DB = "jdbc:mysql://localhost:3306/home?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL_DB, USER, PASSWORD);
            
            // Verify if new seed data exists (checking for student_id 'CT/2022/011')
            boolean needsInitialization = false;
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM students WHERE student_id = 'BT/2022/015'");
                if (!rs.next()) {
                    needsInitialization = true;
                }
                rs.close();
                stmt.close();
            } catch (Exception e) {
                // Table might not exist yet, initialize it
                needsInitialization = true;
            }

            if (needsInitialization) {
                System.out.println("Re-initializing database schema and seed data...");
                conn.close();
                initializeDatabase();
                conn = DriverManager.getConnection(URL_DB, USER, PASSWORD);
            }
            
            return conn;
        } catch (Exception e) {
            if (conn != null) {
                try { conn.close(); } catch (Exception ex) {}
            }
            System.out.println("Database 'home' not found or empty. Attempting to create and initialize database...");
            try {
                initializeDatabase();
            } catch (Exception ex) {
                throw new Exception("Failed to initialize database: " + ex.getMessage(), ex);
            }
            return DriverManager.getConnection(URL_DB, USER, PASSWORD);
        }
    }

    public static void initializeDatabase() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        BufferedReader reader = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL_SERVER, USER, PASSWORD);
            stmt = conn.createStatement();
            
            String path = "resources/database_setup.sql";
            reader = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                
                // Ignore full-line comments and empty lines
                if (trimmed.isEmpty() || trimmed.startsWith("--") || trimmed.startsWith("/*")) {
                    continue;
                }
                
                // Strip inline -- comments
                if (trimmed.contains("--")) {
                    trimmed = trimmed.substring(0, trimmed.indexOf("--")).trim();
                }
                
                if (trimmed.isEmpty()) {
                    continue;
                }
                
                sb.append(trimmed).append("\n");
                
                if (trimmed.endsWith(";")) {
                    String query = sb.toString().trim();
                    if (query.endsWith(";")) {
                        query = query.substring(0, query.length() - 1).trim();
                    }
                    if (!query.isEmpty()) {
                        stmt.execute(query);
                    }
                    sb = new StringBuilder();
                }
            }
            System.out.println("Database 'home' and all tables seeded successfully.");
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
            throw e; // Propagate the error so calling methods are aware of failure
        } finally {
            if (reader != null) {
                try { reader.close(); } catch (Exception e) {}
            }
            if (stmt != null) {
                try { stmt.close(); } catch (Exception e) {}
            }
            if (conn != null) {
                try { conn.close(); } catch (Exception e) {}
            }
        }
    }
}
