package com.jmletona;

import lombok.Data;

@Data
public class Student {
    String Name;
    double Grade;

    public double getGrade() {
        return Grade;
    }

    public void setGrade(String grade) {
        Grade = Double.parseDouble(grade);
    }
}
