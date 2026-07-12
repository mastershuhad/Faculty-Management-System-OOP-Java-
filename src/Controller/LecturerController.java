package controller;

import model.DBConnector;
import view.LecturerDashboardFrame;
import view.AuthFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class LecturerController implements LecturerDashboardFrame.LecturerTabSelectionListener {
    private LecturerDashboardFrame frame;
    private String lecturerUsername;
    private String lecturerFullName;

    public LecturerController(LecturerDashboardFrame frame, String lecturerUsername) {
        this.frame = frame;
        this.lecturerUsername = lecturerUsername;

        // Register Tab Hook
        this.frame.setLecturerTabSelectionListener(this);

        // Bind Action Listeners
        this.frame.getBtnSaveProfile().addActionListener(e -> handleSaveProfile());
        this.frame.getBtnUpdateGrade().addActionListener(e -> handleUpdateGrade());
        this.frame.getBtnLogout().addActionListener(e -> handleLogout());
        
        // Course selection change trigger
        this.frame.getComboCourses().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadCourseStudents();
            }
        });

        // Student selection change trigger
        this.frame.getComboStudentIds().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleStudentSelectionChange();
            }
        });

        // Initialize (Default: Profile Details)
        loadProfileData();
    }

    @Override
    public void tabSelected(String tabName) {
        if (tabName.equals("Profile Details")) {
            loadProfileData();
        } else if (tabName.equals("Grade Students")) {
            loadCoursesTaught();
        }
    }

    private void loadProfileData() {
        try (Connection conn = DBConnector.getConnection()) {
            String sql = "SELECT * FROM lecturers WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, lecturerUsername);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                this.lecturerFullName = rs.getString("full_name");
                String dept = rs.getString("department");
                String email = rs.getString("email");
                String mobile = rs.getString("mobile");

                frame.setProfileData(lecturerFullName, dept, email, mobile);
                
                String shortName = lecturerFullName.split(" ")[0];
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
            String sql = "UPDATE lecturers SET full_name = ?, email = ?, mobile = ? WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, email);
            ps.setString(3, mobile);
            ps.setString(4, lecturerUsername);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadProfileData(); // Refresh values
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating profile: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCoursesTaught() {
        // Ensure profile data (especially lecturerFullName) is loaded
        if (lecturerFullName == null) {
            loadProfileData();
        }

        try (Connection conn = DBConnector.getConnection()) {
            String sql = "SELECT code, name FROM courses WHERE lecturer_name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, lecturerFullName);
            ResultSet rs = ps.executeQuery();

            // Clear previous items
            frame.getComboCourses().removeAllItems();
            boolean hasCourses = false;
            java.util.List<String> list = new java.util.ArrayList<>();
            while (rs.next()) {
                String val = rs.getString("code") + " - " + rs.getString("name");
                list.add(val);
                hasCourses = true;
            }

            if (hasCourses) {
                frame.getComboCourses().addItem("All Courses");
                for (String c : list) {
                    frame.getComboCourses().addItem(c);
                }
                loadCourseStudents();
            } else {
                setEmptyStudentsTable();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading courses: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCourseStudents() {
        Object selectedItem = frame.getComboCourses().getSelectedItem();
        if (selectedItem == null) {
            setEmptyStudentsTable();
            return;
        }

        String courseStr = selectedItem.toString();

        try (Connection conn = DBConnector.getConnection()) {
            String sql;
            PreparedStatement ps;
            if (courseStr.equals("All Courses")) {
                sql = "SELECT s.student_id, s.full_name, ce.course_code, ce.grade " +
                      "FROM course_enrollments ce " +
                      "JOIN students s ON ce.student_id = s.student_id " +
                      "JOIN courses c ON ce.course_code = c.code " +
                      "WHERE c.lecturer_name = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, lecturerFullName);
            } else {
                String courseCode = courseStr.split(" - ")[0];
                sql = "SELECT s.student_id, s.full_name, ce.course_code, ce.grade " +
                      "FROM course_enrollments ce " +
                      "JOIN students s ON ce.student_id = s.student_id " +
                      "WHERE ce.course_code = ?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, courseCode);
            }
            ResultSet rs = ps.executeQuery();

            String[] columns = {"Student ID", "Student Name", "Course", "Grade"};
            Vector<Vector<Object>> data = new Vector<>();
            Vector<String> studentIdsList = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                String stdId = rs.getString(1);
                row.add(stdId);
                row.add(rs.getString(2));
                row.add(rs.getString(3));
                row.add(rs.getString(4));
                data.add(row);
                studentIdsList.add(stdId);
            }

            DefaultTableModel model = new DefaultTableModel(data, new Vector<>(java.util.Arrays.asList(columns))) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            frame.getTableStudents().setModel(model);

            // Populate student ID dropdown
            frame.getComboStudentIds().removeAllItems();
            for (String stdId : studentIdsList) {
                frame.getComboStudentIds().addItem(stdId);
            }

            if (!studentIdsList.isEmpty()) {
                frame.getComboStudentIds().setSelectedIndex(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading course students: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleStudentSelectionChange() {
        Object selectedStudent = frame.getComboStudentIds().getSelectedItem();
        if (selectedStudent == null) return;
        
        String studentId = selectedStudent.toString();
        JTable table = frame.getTableStudents();
        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i, 0).toString().equals(studentId)) {
                if (table.getSelectedRow() != i) {
                    table.setRowSelectionInterval(i, i);
                }
                String grade = table.getValueAt(i, 3).toString();
                if (!frame.getSelectedGrade().equals(grade)) {
                    frame.getComboGrades().setSelectedItem(grade);
                }
                break;
            }
        }
    }

    private void handleUpdateGrade() {
        String studentId = frame.getSelectedStudentId();
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please select a student from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object selectedItem = frame.getComboCourses().getSelectedItem();
        if (selectedItem == null) return;
        
        String courseCode;
        if (selectedItem.toString().equals("All Courses")) {
            int row = frame.getTableStudents().getSelectedRow();
            if (row >= 0) {
                courseCode = frame.getTableStudents().getValueAt(row, 2).toString();
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a student from the table to update grade.", "Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } else {
            courseCode = selectedItem.toString().split(" - ")[0];
        }
        
        String newGrade = frame.getSelectedGrade();

        try (Connection conn = DBConnector.getConnection()) {
            String sql = "UPDATE course_enrollments SET grade = ? WHERE student_id = ? AND course_code = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, newGrade);
            ps.setString(2, studentId);
            ps.setString(3, courseCode);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(frame, "Grade updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadCourseStudents(); // Refresh the list
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error updating grade: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setEmptyStudentsTable() {
        String[] columns = {"Student ID", "Student Name", "Course", "Grade"};
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, columns);
        frame.getTableStudents().setModel(model);
        frame.getComboStudentIds().removeAllItems();
    }

    private void handleLogout() {
        frame.dispose();
        AuthFrame loginFrame = new AuthFrame();
        new AuthController(loginFrame);
        loginFrame.setVisible(true);
    }
}
