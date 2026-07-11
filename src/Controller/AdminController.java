package controller;

import model.DBConnector;
import view.AdminDashboardFrame;
import view.AuthFrame;
import view.CustomComponents;
import view.CustomComponents.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

public class AdminController implements AdminDashboardFrame.TabSelectionListener {
    private AdminDashboardFrame frame;
    private String adminUsername;

    public AdminController(AdminDashboardFrame frame, String adminUsername) {
        this.frame = frame;
        this.adminUsername = adminUsername;
        
        // Register Tab Selection Hook
        this.frame.setTabSelectionListener(this);

        // Bind CRUD Action Listeners
        this.frame.getBtnAddNew().addActionListener(e -> handleAdd());
        this.frame.getBtnEdit().addActionListener(e -> handleEdit());
        this.frame.getBtnDelete().addActionListener(e -> handleDelete());
        this.frame.getBtnSaveChanges().addActionListener(e -> handleSave());
        this.frame.getBtnLogout().addActionListener(e -> handleLogout());

        // Initial Load (default is Students)
        tabSelected("Students");
    }

    @Override
    public void tabSelected(String tabName) {
        refreshTableData();
    }

    private void refreshTableData() {
        String activeTab = frame.getActiveTab();
        try (Connection conn = DBConnector.getConnection()) {
            if (activeTab.equals("Students")) {
                String sql = "SELECT full_name, student_id, degree, email, mobile FROM students";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                frame.setTableModel(buildTableModel(rs, new String[]{"Full Name", "Student ID", "Degree", "Email", "Mobile Number"}));
            } else if (activeTab.equals("Lecturers")) {
                // Use group concat to show all courses taught by each lecturer
                String sql = "SELECT l.full_name, l.department, GROUP_CONCAT(c.code SEPARATOR ', ') AS courses, l.email, l.mobile " +
                             "FROM lecturers l LEFT JOIN courses c ON l.full_name = c.lecturer_name " +
                             "GROUP BY l.username";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                frame.setTableModel(buildTableModel(rs, new String[]{"Full Name", "Department", "Courses teaching", "Email", "Mobile Number"}));
            } else if (activeTab.equals("Courses")) {
                String sql = "SELECT code, name, credits, lecturer_name FROM courses";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                frame.setTableModel(buildTableModel(rs, new String[]{"Course code", "Course name", "Credits", "Lecturer"}));
            } else if (activeTab.equals("Departments")) {
                String sql = "SELECT name, hod, degree, no_of_staff FROM departments";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                frame.setTableModel(buildTableModel(rs, new String[]{"Name", "HOD", "Degree", "No of Staff"}));
            } else if (activeTab.equals("Degrees")) {
                String sql = "SELECT name, department, no_of_students FROM degrees";
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                frame.setTableModel(buildTableModel(rs, new String[]{"Degree", "Department", "No of Students"}));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading data: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private DefaultTableModel buildTableModel(ResultSet rs, String[] columnNames) throws Exception {
        Vector<String> columns = new Vector<>();
        for (String name : columnNames) {
            columns.add(name);
        }
        Vector<Vector<Object>> data = new Vector<>();
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            for (int i = 1; i <= columnNames.length; i++) {
                row.add(rs.getObject(i));
            }
            data.add(row);
        }
        return new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private void handleAdd() {
        String activeTab = frame.getActiveTab();
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBackground(CustomComponents.COLOR_CARD);
        
        Map<String, JTextField> fields = new HashMap<>();

        if (activeTab.equals("Students")) {
            addLabelAndField(panel, "Username:", fields, "username");
            addLabelAndField(panel, "Password:", fields, "password");
            addLabelAndField(panel, "Full Name:", fields, "fullname");
            addLabelAndField(panel, "Student ID (e.g. ET/2022/011):", fields, "studentid");
            addLabelAndField(panel, "Degree:", fields, "degree");
            addLabelAndField(panel, "Email:", fields, "email");
            addLabelAndField(panel, "Mobile:", fields, "mobile");
        } else if (activeTab.equals("Lecturers")) {
            addLabelAndField(panel, "Username:", fields, "username");
            addLabelAndField(panel, "Password:", fields, "password");
            addLabelAndField(panel, "Full Name:", fields, "fullname");
            addLabelAndField(panel, "Department:", fields, "department");
            addLabelAndField(panel, "Email:", fields, "email");
            addLabelAndField(panel, "Mobile:", fields, "mobile");
        } else if (activeTab.equals("Courses")) {
            addLabelAndField(panel, "Course Code (e.g. ETEC 21062):", fields, "code");
            addLabelAndField(panel, "Course Name:", fields, "name");
            addLabelAndField(panel, "Credits:", fields, "credits");
            addLabelAndField(panel, "Lecturer Name:", fields, "lecturer");
        } else if (activeTab.equals("Departments")) {
            addLabelAndField(panel, "Department Name:", fields, "name");
            addLabelAndField(panel, "HOD Name:", fields, "hod");
            addLabelAndField(panel, "Degree:", fields, "degree");
            addLabelAndField(panel, "No of Staff:", fields, "staff");
        } else if (activeTab.equals("Degrees")) {
            addLabelAndField(panel, "Degree Name:", fields, "name");
            addLabelAndField(panel, "Department:", fields, "department");
            addLabelAndField(panel, "No of Students:", fields, "students");
        }

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add New " + activeTab, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try (Connection conn = DBConnector.getConnection()) {
                conn.setAutoCommit(false);
                try {
                    if (activeTab.equals("Students")) {
                        // Insert into Users
                        String sqlUser = "INSERT INTO users (username, password, role) VALUES (?, ?, 'Student')";
                        PreparedStatement psUser = conn.prepareStatement(sqlUser);
                        psUser.setString(1, fields.get("username").getText());
                        psUser.setString(2, fields.get("password").getText());
                        psUser.executeUpdate();

                        // Insert into Students
                        String sqlStd = "INSERT INTO students (username, full_name, student_id, degree, email, mobile) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement psStd = conn.prepareStatement(sqlStd);
                        psStd.setString(1, fields.get("username").getText());
                        psStd.setString(2, fields.get("fullname").getText());
                        psStd.setString(3, fields.get("studentid").getText());
                        psStd.setString(4, fields.get("degree").getText());
                        psStd.setString(5, fields.get("email").getText());
                        psStd.setString(6, fields.get("mobile").getText());
                        psStd.executeUpdate();
                    } else if (activeTab.equals("Lecturers")) {
                        // Insert into Users
                        String sqlUser = "INSERT INTO users (username, password, role) VALUES (?, ?, 'Lecturer')";
                        PreparedStatement psUser = conn.prepareStatement(sqlUser);
                        psUser.setString(1, fields.get("username").getText());
                        psUser.setString(2, fields.get("password").getText());
                        psUser.executeUpdate();

                        // Insert into Lecturers
                        String sqlLec = "INSERT INTO lecturers (username, full_name, department, email, mobile) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement psLec = conn.prepareStatement(sqlLec);
                        psLec.setString(1, fields.get("username").getText());
                        psLec.setString(2, fields.get("fullname").getText());
                        psLec.setString(3, fields.get("department").getText());
                        psLec.setString(4, fields.get("email").getText());
                        psLec.setString(5, fields.get("mobile").getText());
                        psLec.executeUpdate();
                    } else if (activeTab.equals("Courses")) {
                        String sql = "INSERT INTO courses (code, name, credits, lecturer_name) VALUES (?, ?, ?, ?)";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, fields.get("code").getText());
                        ps.setString(2, fields.get("name").getText());
                        ps.setInt(3, Integer.parseInt(fields.get("credits").getText()));
                        ps.setString(4, fields.get("lecturer").getText());
                        ps.executeUpdate();
                    } else if (activeTab.equals("Departments")) {
                        String sql = "INSERT INTO departments (name, hod, degree, no_of_staff) VALUES (?, ?, ?, ?)";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, fields.get("name").getText());
                        ps.setString(2, fields.get("hod").getText());
                        ps.setString(3, fields.get("degree").getText());
                        ps.setInt(4, Integer.parseInt(fields.get("staff").getText()));
                        ps.executeUpdate();
                    } else if (activeTab.equals("Degrees")) {
                        String sql = "INSERT INTO degrees (name, department, no_of_students) VALUES (?, ?, ?)";
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, fields.get("name").getText());
                        ps.setString(2, fields.get("department").getText());
                        ps.setInt(3, Integer.parseInt(fields.get("students").getText()));
                        ps.executeUpdate();
                    }
                    conn.commit();
                    refreshTableData();
                    JOptionPane.showMessageDialog(frame, "Record added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    conn.rollback();
                    throw ex;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error adding record: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEdit() {
        int selectedRow = frame.getTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, "Please select a row to edit.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String activeTab = frame.getActiveTab();
        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.setBackground(CustomComponents.COLOR_CARD);
        Map<String, JTextField> fields = new HashMap<>();

        try (Connection conn = DBConnector.getConnection()) {
            if (activeTab.equals("Students")) {
                String stdId = frame.getTable().getValueAt(selectedRow, 1).toString();
                addLabelAndField(panel, "Full Name:", fields, "fullname");
                addLabelAndField(panel, "Degree:", fields, "degree");
                addLabelAndField(panel, "Email:", fields, "email");
                addLabelAndField(panel, "Mobile:", fields, "mobile");

                // Pre-fill
                fields.get("fullname").setText(frame.getTable().getValueAt(selectedRow, 0).toString());
                fields.get("degree").setText(frame.getTable().getValueAt(selectedRow, 2).toString());
                fields.get("email").setText(frame.getTable().getValueAt(selectedRow, 3).toString());
                fields.get("mobile").setText(frame.getTable().getValueAt(selectedRow, 4).toString());

                int res = JOptionPane.showConfirmDialog(frame, panel, "Edit Student", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (res == JOptionPane.OK_OPTION) {
                    String sql = "UPDATE students SET full_name = ?, degree = ?, email = ?, mobile = ? WHERE student_id = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, fields.get("fullname").getText());
                    ps.setString(2, fields.get("degree").getText());
                    ps.setString(3, fields.get("email").getText());
                    ps.setString(4, fields.get("mobile").getText());
                    ps.setString(5, stdId);
                    ps.executeUpdate();
                }
            } else if (activeTab.equals("Lecturers")) {
                String name = frame.getTable().getValueAt(selectedRow, 0).toString();
                addLabelAndField(panel, "Department:", fields, "department");
                addLabelAndField(panel, "Email:", fields, "email");
                addLabelAndField(panel, "Mobile:", fields, "mobile");

                // Pre-fill
                fields.get("department").setText(frame.getTable().getValueAt(selectedRow, 1).toString());
                fields.get("email").setText(frame.getTable().getValueAt(selectedRow, 3).toString());
                fields.get("mobile").setText(frame.getTable().getValueAt(selectedRow, 4).toString());

                int res = JOptionPane.showConfirmDialog(frame, panel, "Edit Lecturer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (res == JOptionPane.OK_OPTION) {
                    String sql = "UPDATE lecturers SET department = ?, email = ?, mobile = ? WHERE full_name = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, fields.get("department").getText());
                    ps.setString(2, fields.get("email").getText());
                    ps.setString(3, fields.get("mobile").getText());
                    ps.setString(4, name);
                    ps.executeUpdate();
                }
            } else if (activeTab.equals("Courses")) {
                String code = frame.getTable().getValueAt(selectedRow, 0).toString();
                addLabelAndField(panel, "Course Name:", fields, "name");
                addLabelAndField(panel, "Credits:", fields, "credits");
                addLabelAndField(panel, "Lecturer Name:", fields, "lecturer");

                fields.get("name").setText(frame.getTable().getValueAt(selectedRow, 1).toString());
                fields.get("credits").setText(frame.getTable().getValueAt(selectedRow, 2).toString());
                fields.get("lecturer").setText(frame.getTable().getValueAt(selectedRow, 3).toString());

                int res = JOptionPane.showConfirmDialog(frame, panel, "Edit Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (res == JOptionPane.OK_OPTION) {
                    String sql = "UPDATE courses SET name = ?, credits = ?, lecturer_name = ? WHERE code = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, fields.get("name").getText());
                    ps.setInt(2, Integer.parseInt(fields.get("credits").getText()));
                    ps.setString(3, fields.get("lecturer").getText());
                    ps.setString(4, code);
                    ps.executeUpdate();
                }
            } else if (activeTab.equals("Departments")) {
                String name = frame.getTable().getValueAt(selectedRow, 0).toString();
                addLabelAndField(panel, "HOD Name:", fields, "hod");
                addLabelAndField(panel, "Degree:", fields, "degree");
                addLabelAndField(panel, "No of Staff:", fields, "staff");

                fields.get("hod").setText(frame.getTable().getValueAt(selectedRow, 1).toString());
                fields.get("degree").setText(frame.getTable().getValueAt(selectedRow, 2).toString());
                fields.get("staff").setText(frame.getTable().getValueAt(selectedRow, 3).toString());

                int res = JOptionPane.showConfirmDialog(frame, panel, "Edit Department", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (res == JOptionPane.OK_OPTION) {
                    String sql = "UPDATE departments SET hod = ?, degree = ?, no_of_staff = ? WHERE name = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, fields.get("hod").getText());
                    ps.setString(2, fields.get("degree").getText());
                    ps.setInt(3, Integer.parseInt(fields.get("staff").getText()));
                    ps.setString(4, name);
                    ps.executeUpdate();
                }
            } else if (activeTab.equals("Degrees")) {
                String name = frame.getTable().getValueAt(selectedRow, 0).toString();
                addLabelAndField(panel, "Department:", fields, "department");
                addLabelAndField(panel, "No of Students:", fields, "students");

                fields.get("department").setText(frame.getTable().getValueAt(selectedRow, 1).toString());
                fields.get("students").setText(frame.getTable().getValueAt(selectedRow, 2).toString());

                int res = JOptionPane.showConfirmDialog(frame, panel, "Edit Degree", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (res == JOptionPane.OK_OPTION) {
                    String sql = "UPDATE degrees SET department = ?, no_of_students = ? WHERE name = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, fields.get("department").getText());
                    ps.setInt(2, Integer.parseInt(fields.get("students").getText()));
                    ps.setString(3, name);
                    ps.executeUpdate();
                }
            }
            refreshTableData();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error editing record: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int selectedRow = frame.getTable().getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(frame, "Please select a row to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        String activeTab = frame.getActiveTab();
        try (Connection conn = DBConnector.getConnection()) {
            if (activeTab.equals("Students")) {
                String stdId = frame.getTable().getValueAt(selectedRow, 1).toString();
                // Get username to delete from users table (cascade will delete profile, but users table holds key)
                String getUsernameSql = "SELECT username FROM students WHERE student_id = ?";
                PreparedStatement psGet = conn.prepareStatement(getUsernameSql);
                psGet.setString(1, stdId);
                ResultSet rs = psGet.executeQuery();
                if (rs.next()) {
                    String username = rs.getString("username");
                    String sql = "DELETE FROM users WHERE username = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, username);
                    ps.executeUpdate();
                }
            } else if (activeTab.equals("Lecturers")) {
                String name = frame.getTable().getValueAt(selectedRow, 0).toString();
                String getUsernameSql = "SELECT username FROM lecturers WHERE full_name = ?";
                PreparedStatement psGet = conn.prepareStatement(getUsernameSql);
                psGet.setString(1, name);
                ResultSet rs = psGet.executeQuery();
                if (rs.next()) {
                    String username = rs.getString("username");
                    String sql = "DELETE FROM users WHERE username = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, username);
                    ps.executeUpdate();
                }
            } else if (activeTab.equals("Courses")) {
                String code = frame.getTable().getValueAt(selectedRow, 0).toString();
                String sql = "DELETE FROM courses WHERE code = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, code);
                ps.executeUpdate();
            } else if (activeTab.equals("Departments")) {
                String name = frame.getTable().getValueAt(selectedRow, 0).toString();
                String sql = "DELETE FROM departments WHERE name = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.executeUpdate();
            } else if (activeTab.equals("Degrees")) {
                String name = frame.getTable().getValueAt(selectedRow, 0).toString();
                String sql = "DELETE FROM degrees WHERE name = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.executeUpdate();
            }
            refreshTableData();
            JOptionPane.showMessageDialog(frame, "Record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error deleting record: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSave() {
        // Since we commit details immediately inside dialog edits, we just refresh here and show confirmation
        refreshTableData();
        JOptionPane.showMessageDialog(frame, "All changes saved to database.", "Changes Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleLogout() {
        frame.dispose();
        AuthFrame loginFrame = new AuthFrame();
        new AuthController(loginFrame);
        loginFrame.setVisible(true);
    }

    private void addLabelAndField(JPanel panel, String labelText, Map<String, JTextField> fields, String key) {
        JLabel label = new JLabel(labelText);
        label.setForeground(CustomComponents.COLOR_TEXT_PRIMARY);
        label.setFont(CustomComponents.FONT_SMALL);
        
        RoundedTextField field = new RoundedTextField();
        fields.put(key, field);
        
        panel.add(label);
        panel.add(field);
    }
}
