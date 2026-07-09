package model;

public class Degree {
    private String name;
    private String department;
    private int noOfStudents;

    public Degree(String name, String department, int noOfStudents) {
        this.name = name;
        this.department = department;
        this.noOfStudents = noOfStudents;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public int getNoOfStudents() { return noOfStudents; }
    public void setNoOfStudents(int noOfStudents) { this.noOfStudents = noOfStudents; }
}
