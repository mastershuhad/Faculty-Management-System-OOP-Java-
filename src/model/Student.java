package model;

public class Student {
    private String username;
    private String fullName;
    private String studentId;
    private String degree;
    private String email;
    private String mobile;

    public Student(String username, String fullName, String studentId, String degree, String email, String mobile) {
        this.username = username;
        this.fullName = fullName;
        this.studentId = studentId;
        this.degree = degree;
        this.email = email;
        this.mobile = mobile;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}
