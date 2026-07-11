package controller;

import model.DBConnector;
import view.AuthFrame;
import view.AdminDashboardFrame;
import view.StudentDashboardFrame;
import view.LecturerDashboardFrame;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthController {
    private AuthFrame frame;

    public AuthController(AuthFrame frame) {
        this.frame = frame;
        this.frame.getActionButton().addActionListener(e -> handleAuthAction());
    }

    private void handleAuthAction() {
        String username = frame.getUsername();
        String password = frame.getPassword();
        String role = frame.getSelectedRole();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Username and Password fields cannot be empty.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (frame.isSignInMode()) {
            // LOGIN LOGIC
            try (Connection conn = DBConnector.getConnection()) {
                String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, role);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // Login successful! Transition to correct dashboard
                    frame.dispose();
                    openDashboard(username, role);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials or role selected.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            // SIGN UP LOGIC
            String confirmPassword = frame.getConfirmPassword();
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Read additional dynamic fields
            String fullName = frame.getFullName();
            String studentId = frame.getStudentId();
            String degree = frame.getSelectedDegree();
            String dept = frame.getSelectedDept();
            String email = frame.getEmail();
            String mobile = frame.getMobile();

            // Validate based on role
            if (role.equals("Student")) {
                if (fullName.isEmpty() || studentId.isEmpty() || email.isEmpty() || mobile.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All student details (Full Name, Student ID, Email, Mobile) must be filled.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Validate Student ID Prefix based on degree
                String expectedPrefix = "";
                if (degree.equals("Engineering Technology")) {
                    expectedPrefix = "ET";
                } else if (degree.equals("Information Technology")) {
                    expectedPrefix = "CT";
                } else if (degree.equals("Computer Science")) {
                    expectedPrefix = "CS";
                } else if (degree.equals("Bio Systems Technology")) {
                    expectedPrefix = "BT";
                }
                
                String regex = "^" + expectedPrefix + "/\\d{4}/\\d{3}$";
                if (!studentId.matches(regex)) {
                    JOptionPane.showMessageDialog(frame, 
                        "Invalid Student ID format for your degree.\n" +
                        "For " + degree + ", it must be: " + expectedPrefix + "/YYYY/NNN (e.g. " + expectedPrefix + "/2022/011)", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (role.equals("Lecturer")) {
                if (fullName.isEmpty() || email.isEmpty() || mobile.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "All lecturer details (Full Name, Email, Mobile) must be filled.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            try (Connection conn = DBConnector.getConnection()) {
                // Check if username exists
                String checkSql = "SELECT * FROM users WHERE username = ?";
                PreparedStatement psCheck = conn.prepareStatement(checkSql);
                psCheck.setString(1, username);
                ResultSet rsCheck = psCheck.executeQuery();

                if (rsCheck.next()) {
                    JOptionPane.showMessageDialog(frame, "Username already exists.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // If Student, check if studentId already exists
                if (role.equals("Student")) {
                    String checkStdId = "SELECT * FROM students WHERE student_id = ?";
                    PreparedStatement psCheckStd = conn.prepareStatement(checkStdId);
                    psCheckStd.setString(1, studentId);
                    ResultSet rsCheckStd = psCheckStd.executeQuery();
                    if (rsCheckStd.next()) {
                        JOptionPane.showMessageDialog(frame, "Student ID already registered.", "Registration Failed", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                conn.setAutoCommit(false);
                try {
                    // 1. Insert into users
                    String sqlUser = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                    PreparedStatement psUser = conn.prepareStatement(sqlUser);
                    psUser.setString(1, username);
                    psUser.setString(2, password);
                    psUser.setString(3, role);
                    psUser.executeUpdate();

                    // 2. Insert into relevant profile table
                    if (role.equals("Student")) {
                        String sqlStudent = "INSERT INTO students (username, full_name, student_id, degree, email, mobile) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement psStudent = conn.prepareStatement(sqlStudent);
                        psStudent.setString(1, username);
                        psStudent.setString(2, fullName);
                        psStudent.setString(3, studentId);
                        psStudent.setString(4, degree);
                        psStudent.setString(5, email);
                        psStudent.setString(6, mobile);
                        psStudent.executeUpdate();
                    } else if (role.equals("Lecturer")) {
                        String sqlLecturer = "INSERT INTO lecturers (username, full_name, department, email, mobile) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement psLecturer = conn.prepareStatement(sqlLecturer);
                        psLecturer.setString(1, username);
                        psLecturer.setString(2, fullName);
                        psLecturer.setString(3, dept);
                        psLecturer.setString(4, email);
                        psLecturer.setString(5, mobile);
                        psLecturer.executeUpdate();
                    }
                    
                    conn.commit();
                    JOptionPane.showMessageDialog(frame, "Registration successful! You can now log in.", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                    frame.clearFields();
                } catch (Exception ex) {
                    conn.rollback();
                    throw ex;
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error registering: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void openDashboard(String username, String role) {
        if (role.equals("Admin")) {
            AdminDashboardFrame adminFrame = new AdminDashboardFrame();
            new AdminController(adminFrame, username);
            adminFrame.setVisible(true);
        } else if (role.equals("Student")) {
            StudentDashboardFrame studentFrame = new StudentDashboardFrame();
            new StudentController(studentFrame, username);
            studentFrame.setVisible(true);
        } else if (role.equals("Lecturer")) {
            LecturerDashboardFrame lecturerFrame = new LecturerDashboardFrame();
            new LecturerController(lecturerFrame, username);
            lecturerFrame.setVisible(true);
        }
    }
}
