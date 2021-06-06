package com.jmletona;

public class Student {
    String Name;
    double Grade;

    public double getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = Double.parseDouble(grade);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
