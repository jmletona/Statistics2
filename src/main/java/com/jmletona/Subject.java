package com.jmletona;

import lombok.Data;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Subject {

    //initialization of variables
    private static final String important = "\033[35m"; // purple
    public static final String normal="\033[37m"; //white
    private String name;
    private String email;
    private int id;

    List<Student> studentsList = new ArrayList<>();

    //getters and setters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getId() {
        return id;
    }

    //constructor
    public Subject(Integer Id, String Name, String Email) {
        this.name = Name;
        this.id = Id;
        this.email = Email;
    }



    //load data from file
    public void loadData() {
        String filename = (this.getName().toLowerCase(Locale.ROOT)).replaceAll("\\s", "");//generate filename, subject name based.
        try {
            File myObj = new File("./src/main/java/com/jmletona/subjectsData/"
                    + filename
                    + ".txt");

            Scanner myReader = new Scanner(myObj);
            System.out.println(important + "File Name: " + myObj.getName());

            while (myReader.hasNextLine()) {
                Student newStudentEntry = new Student();
                newStudentEntry.setName(myReader.nextLine());
                newStudentEntry.setGrade(myReader.nextLine());
                studentsList.add(newStudentEntry);
            }
            myReader.close();
            System.out.println(important+studentsList.size()+" Total students - "+ this.getName()+".");
        } catch (FileNotFoundException e) {
            System.out.println(important + "An error occurred loading file..."
                    + e
                    + "Expected file: "
                    + ("./src/main/java/com/jmletona/subjectsData/"
                    + filename
                    + ".txt"));
        }


    }

    /***************************************************************************
     ****************** ADD NEW STUDENTS METHODS********************************
     ***************************************************************************/
    //insert student object in List
    public void addStudent() {
        System.out.println("Adding new one to " + name + "...");

        while (true) {
            try {
                Student newStudent = new Student();
                newStudent.setName(insertName());
                newStudent.setGrade(insertGrade());
                studentsList.add(newStudent);
                System.out.println(important + studentsList.size()+" Total students - "+ this.getName()+".");
                break;
            } catch (InputMismatchException e) {
                System.out.println(important + "Invalid Choice\n");
            }
        }
    }

    //insert Dats to student Object
    private String insertName() {
        String newName;
        while(true) {
            Scanner input = new Scanner(System.in);
            System.out.println(important+"New Student name: ");
            newName = input.next();
            if(isName(newName)) {
                break;
            }else {
                System.out.println(important+"Invalid Name...");
            }
        }
        return newName;
    }
    private String insertGrade() {
        String newGrade;
        while(true) {
            Scanner input = new Scanner(System.in);
            System.out.println(important+"New Student grade: ");
            newGrade = input.next();
            System.out.println(isGrade(newGrade));
            if(isGrade(newGrade)) {
                break;
            }else {
                System.out.println(important+"Invalid Grade...");
            }
        }
        return newGrade;
    }

    //validate Dats
    private boolean isName(String text) {
        String regex;
        regex = "^([A-ZÁ-Ú][a-záéíóú]+)(\\s[A-ZÁ-Ú][a-záéíóú]+)*";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
    private boolean isGrade(String text) {
        String regex;
        regex = "^[0-9]\\.[0-9]{1,2}|^[0-9]|^10|^10.00|^10.0";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }

    /***************************************************************************
     *********************** SEND REPORT METHODS********************************
     ***************************************************************************/

    public void sendReport(){
        ArrayList<String> alReport = generateReport();
        if(studentsList.size() != 0) {
            printReport(alReport);

            String filesPath = "./src/main/java/com/jmletona/subjectsReports/";

            MakeTXT reportTXT = new MakeTXT((this.getName().toLowerCase(Locale.ROOT)).replaceAll("\\s", "")+".txt",filesPath);//generate filename, subject name based.
            reportTXT.addArrayList(alReport);

            MakePDF reportPDF = new MakePDF((this.getName().toLowerCase(Locale.ROOT)).replaceAll("\\s", "")+".pdf",filesPath);
            reportPDF.addArrayList(alReport);

            mailReport(alReport, reportTXT.getName(),reportPDF.getName(), filesPath);
        }else{
            System.out.println("Students list is empty! nothing to send!");
        }


    }
    public void mailReport(ArrayList<String> alReport, String txtFile, String pdfFile, String filesPath){

        try {
            Mail m = new Mail("config/settings.prop");
            m.sendEmail("Report of "+ this.getName(), alReport, this.getEmail(), txtFile, pdfFile, filesPath);
            System.out.println("Email Sent!!");
        } catch (InvalidParameterException | IOException | MessagingException ex) {
            System.out.println("Error trying to sending email.");
            System.out.println(ex.getMessage());
        }
    }

    public void printReport(ArrayList<String> alReport){
        if (studentsList.size() != 0) {
            for(String line: alReport){
                System.out.println(line);
            }
        }else{
            System.out.println(important + "Student list is empty.");
        }
    }

    public ArrayList<String> generateReport() {
        ArrayList<String> alReport = new ArrayList<>();
        if (studentsList.size() != 0) {
            System.out.println(important);
            alReport.add("---------------------- Subject ----------");
            alReport.add(this.getName());
            alReport.add("---------------------- Students ---------");
            alReport.add(studentsList.size()+" Students");
            for(Student student: studentsList){//print all students in this Subject
                alReport.add(student.getName()+", "+student.getGrade());
            }
            alReport.add("---------------------- Average ---------");
            alReport.add("Average, "+String.format("%.2f", avg()));
            alReport.add("---------------------- Max Grade -------");
            for(Student student: maxValue()){//print max grades values and names
                alReport.add(student.getName()+", "+ student.getGrade());
            }
            alReport.add("---------------------- Min Grade -------");
            for(Student student: minValue()){//print min grades values and names
                alReport.add(student.getName()+", "+student.getGrade());
            }
            alReport.add("---------------------- Max Repeated ----");
            for(Student student: mostRepeated()){//print most repeated grades values and names
                alReport.add(student.getName()+", "+student.getGrade());
            }
            alReport.add("----------------------------------------");
            alReport.add("Sending report by email to "+this.getEmail()+"...");
        }
        return alReport;
    }

    /***************************************************************************
     ****************** STATISTICS METHODS *************************************
     **************************************************************************/

    //students grade max value
    private List<Student> maxValue() {
        double maxGrade = 0.0;

        //finding max Grade
        for (Student student : studentsList) {
            if (maxGrade < student.getGrade()) {
                maxGrade = student.getGrade();
            }
        }
        List<Student> studentsRepeatedGradesList = new ArrayList<>();

        //adding max value like a title of the ArrayList of Students
        Student titleGrades = new Student();
        titleGrades.setName("MaxGrade");
        titleGrades.setGrade(String.valueOf(maxGrade));
        studentsRepeatedGradesList.add(titleGrades);

        //adding students with max grade
        for (Student student : studentsList) { //finding max Grade
            if (maxGrade == student.getGrade()) {
                studentsRepeatedGradesList.add(student);
            }
        }

        return studentsRepeatedGradesList;
    }

    //students grade min value
    private List<Student> minValue() {
        double minGrade = 10.0;

        //finding min Grade
        for (Student student : studentsList) {
            if (minGrade > student.getGrade()) {
                minGrade = student.getGrade();
            }
        }
        List<Student> studentsRepeatedGradesList = new ArrayList<>();

        //adding min value like a title of the ArrayList of Students
        Student titleGrades = new Student();
        titleGrades.setName("MinGrade");
        titleGrades.setGrade(String.valueOf(minGrade));
        studentsRepeatedGradesList.add(titleGrades);

        //adding students with min grade
        for (Student student : studentsList) { //finding min Grade
            if (minGrade == student.getGrade()) {
                studentsRepeatedGradesList.add(student);
            }
        }

        return studentsRepeatedGradesList;
    }

     //student grade point average
    private double avg() {
        double total = studentsList.stream().mapToDouble(Student::getGrade).sum();
        return total / studentsList.size();
    }

    //show student list (name, grade)
    public void showStudentsList() {
        for (Student student : studentsList) {
            System.out.println(important+ student.Name + "\t\t" + student.getGrade());
        }
    }

    /***************************************************************************
     ****************** MOST REPEATED METHOD ***********************************
     ***************************************************************************/
    //most repeated grade and who have it
    public List<Student> mostRepeated() {
        ArrayList<Double> arrangeList = sortGradesList();//sort
        double maxRepeated = mostRepeatedGrade(arrangeList);//get max Repetition
        return mostRepeatedNames(maxRepeated, arrangeList);//return max repetition and Names who have it
    }

    //Get who have the max repetition
    private List<Student> mostRepeatedNames(double maxRepeated, ArrayList<Double> arrangeList) {
        //ArrayList<Double> mostRepeatedResult = new ArrayList<>();

        List<Student> studentsRepeatedGradesList = new ArrayList<>();
        //adding max Repetition like a title of the ArrayList of Students
        Student titleGrades = new Student();
        titleGrades.setName("Max Repetition");
        titleGrades.setGrade(String.valueOf(maxRepeated));
        studentsRepeatedGradesList.add(titleGrades);


        int counter = 0;
        double aux = arrangeList.get(0);
        for (Double aDouble : arrangeList) {
            if (aux == aDouble) {
                counter++;
            } else {
                counter = 1;
                aux = aDouble;
            }
            if (maxRepeated == counter) {
                addStudentsToList(aDouble, studentsRepeatedGradesList);
            }
        }
        return studentsRepeatedGradesList;
    }
    //add to max Repetition List the Students objects who have Max Repetition
    private void addStudentsToList(Double aDouble, List<Student> studentsRepeatedGradesList) {
        for(Student student: studentsList){
            if (student.getGrade() == aDouble){
                studentsRepeatedGradesList.add(student);
            }
        }
    }

    //max Repetition Grade
    private double mostRepeatedGrade(ArrayList<Double> arrangeList) {
        int counter = 0;
        double maxRepeated = 0.0;
        double aux = arrangeList.get(0);
        for (Double aDouble : arrangeList) {
            if (aux == aDouble) {
                counter++;
            } else {
                counter = 1;
                aux = aDouble;
            }
            if (maxRepeated < counter) {
                maxRepeated = counter;
            }
        }
        return maxRepeated;
    }

    //sort Grades List
    private ArrayList<Double> sortGradesList() {
        ArrayList<Double> arrangeList = new ArrayList<>();
        for (Student student : studentsList) {
            arrangeList.add(student.getGrade());
        }
        Collections.sort(arrangeList);
        return arrangeList;
    }

}

