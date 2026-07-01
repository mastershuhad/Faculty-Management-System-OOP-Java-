package model;

public class Lecturer {
    private String username;
    private String fullName;
    private String department;
    private String email;
    private String mobile;

    public Lecturer(String username, String fullName, String department, String email, String mobile) {
        this.username = username;
        this.fullName = fullName;
        this.department = department;
        this.email = email;
        this.mobile = mobile;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}
