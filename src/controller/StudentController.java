package controller;

import model.DBConnector;
import view.StudentDashboardFrame;
import view.AuthFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class StudentController implements StudentDashboardFrame.StudentTabSelectionListener {
    private StudentDashboardFrame frame;
    private String studentUsername;
    private String studentId;

    public StudentController(StudentDashboardFrame frame, String studentUsername) {
        this.frame = frame;
        this.studentUsername = studentUsername;

        // Register Tab Hook
        this.frame.setStudentTabSelectionListener(this);

        // Bind Action Listeners
        this.frame.getBtnSaveProfile().addActionListener(e -> handleSaveProfile());
        this.frame.getBtnLogout().addActionListener(e -> handleLogout());

        // Initialize (Default tab: Profile Details)
        loadProfileData();
    }

    @Override
    public void tabSelected(String tabName) {
        if (tabName.equals("Profile Details")) {
            loadProfileData();
        } else if (tabName.equals("Time table")) {
            loadTimetableData();
        } else if (tabName.equals("Course Enrolled")) {
            loadCoursesData();
        }
    }

    private void loadProfileData() {
        try (Connection conn = DBConnector.getConnection()) {
            String sql = "SELECT * FROM students WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, studentUsername);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("full_name");
                this.studentId = rs.getString("student_id");
                String degree = rs.getString("degree");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile");

                frame.setProfileData(fullName, studentId, degree, email, mobile);
                
                // Set sidebar greeting
                String shortName = fullName.split(" ")[0];
                frame.setWelcomeName(shortName);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading profile: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSaveProfile() {
        String fullName = frame.getFullName();
        String email = frame.getEmail();
        String mobile = frame.getMobile();

        if (fullName.isEmpty() || email.isEmpty() || mobile.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Required fields cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try (Connection conn = DBConnector.getConnection()) {
            String sql = "UPDATE students SET full_name = ?, email = ?, mobile = ? WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, mobile);
            ps.setString(4, studentUsername);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadProfileData(); // Refresh fields and greeting name
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating profile: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTimetableData() {
        try (Connection conn = DBConnector.getConnection()) {
            String sql = "SELECT * FROM timetable";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            String[] columns = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
            Vector<Vector<Object>> data = new Vector<>();
            
            int rowIndex = 0;
            while (rs.next()) {
                // Add Interval separator row before slot 3 (index 2 in UI timetable table)
                if (rowIndex == 2) {
                    Vector<Object> interval = new Vector<>();
                    interval.add("12.00");
                    interval.add("Interval");
                    interval.add("Interval");
                    interval.add("Interval");
                    interval.add("Interval");
                    interval.add("Interval");
                    data.add(interval);
                }
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("time_slot"));
                row.add(rs.getString("monday"));
                row.add(rs.getString("tuesday"));
                row.add(rs.getString("wednesday"));
                row.add(rs.getString("thursday"));
                row.add(rs.getString("friday"));
                data.add(row);
                rowIndex++;
            }

            DefaultTableModel model = new DefaultTableModel(data, new Vector<>(java.util.Arrays.asList(columns))) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            frame.setTimetableModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading timetable: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCoursesData() {
        try (Connection conn = DBConnector.getConnection()) {
            String sql = "SELECT ce.course_code, c.name, c.credits, ce.grade " +
                         "FROM course_enrollments ce " +
                         "JOIN courses c ON ce.course_code = c.code " +
                         "JOIN students s ON ce.student_id = s.student_id " +
                         "WHERE s.username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, studentUsername);
            ResultSet rs = ps.executeQuery();

            String[] columns = {"Course code", "Course name", "Credits", "Grade"};
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString(1));
                row.add(rs.getString(2));
                row.add(rs.getInt(3));
                row.add(rs.getString(4));
                data.add(row);
            }

            DefaultTableModel model = new DefaultTableModel(data, new Vector<>(java.util.Arrays.asList(columns))) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            frame.setCoursesModel(model);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading courses: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLogout() {
        frame.dispose();
        AuthFrame loginFrame = new AuthFrame();
        new AuthController(loginFrame);
        loginFrame.setVisible(true);
    }
}
