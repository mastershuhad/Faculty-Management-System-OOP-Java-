package model;

public class Department {
    private String name;
    private String hod;
    private String degree;
    private int noOfStaff;

    public Department(String name, String hod, String degree, int noOfStaff) {
        this.name = name;
        this.hod = hod;
        this.degree = degree;
        this.noOfStaff = noOfStaff;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHod() { return hod; }
    public void setHod(String hod) { this.hod = hod; }

    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }

    public int getNoOfStaff() { return noOfStaff; }
    public void setNoOfStaff(int noOfStaff) { this.noOfStaff = noOfStaff; }
}
