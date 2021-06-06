package com.jmletona;

import lombok.Data;

import java.util.InputMismatchException;
import java.util.Scanner;

@Data
public class App{

    /***************************************************************************
     ****************** INI SETTINGS *******************************************
     ***************************************************************************/

    public static final String important="\033[35m"; // purple
    public static final String title="\033[34m"; //blue
    public static final String normal="\033[37m"; //white

    public static String[][] subjectData = {
            {"Ethical Hacking", "jmletona@gmail.com"},
            {"Machine Learning","hello@jmletona.com"},
            {"Project Manager","jmletona@gmail.com"}
    };

    public static Object[] objSubjects =initialize();

    /***************************************************************************
     ************************** MAIN *******************************************
     ***************************************************************************/
    public static void main(String[] args){
        actionsMenu();
    }

    //create a dynamic array of each Subject Objects with the initial info
    public static Object[] initialize(){
        Object[] objSubjects = new Object[subjectData.length];
        for(int i=0; i<subjectData.length; i++){
            objSubjects[i]= new Subject(i, subjectData[i][0], subjectData[i][1]);
        }
        return objSubjects;
    }

    /***************************************************************************
    ***************** MENU SECTIONS *******************************************
    ***************************************************************************/

    //create a dynamic options menu, scan user option and return it.
    public static int optionsMenu() {
        int selection;
        Scanner input = new Scanner(System.in);

        System.out.println(title+"\nSTATISTICS2\nChoose from these choices");
        System.out.println(title+"-------------------------");
        for(Object subject: objSubjects){
            System.out.println(normal+(((Subject) subject).getId()+1) + " - "+((Subject) subject).getName());
        }
        System.out.println(normal+(objSubjects.length+1)+" - Quit");

        selection = input.nextInt();
        return selection;
    }

    //create a Menu dynamic objects based.
    public static void actionsMenu(){
        int userChoice;

        while(true) {
            try {
                userChoice = optionsMenu();

                if(userChoice >= 1 && userChoice <= objSubjects.length){
                    actionsSubMenu((Subject) objSubjects[userChoice-1]);
                }else if(userChoice==objSubjects.length+1){
                    System.exit(0);
                }else{
                    System.out.format(important+"Only integer between 1 & %d\n\n", (objSubjects.length +1));
                }
            } catch (InputMismatchException e) {
                System.out.println(important+"Invalid Choice\n");
            }
        }
    }

    //create options menu for each Subject, scan user option and return it.
    public static int optionsSubMenu(Subject subject) {

        int selection;
        Scanner input = new Scanner(System.in);

        System.out.println(title+"\n"+subject.getName().toUpperCase()+" SUBJECT\nChoose from these choices");
        System.out.println(title+"-------------------------");
        System.out.println(normal+"1 - Load from file");
        System.out.println(normal+"2 - Add student");
        System.out.println(normal+"3 - Send report");
        System.out.println(normal+"4 - Show Students List");
        System.out.println(normal+"5 - Back to Main Menu");

        selection = input.nextInt();
        return selection;
    }

    //create a SubMenu for each Subject.
    public static void actionsSubMenu(Subject subject) {
        int userChoice;
        boolean exit = false;


        while(!exit) {
            try {
                userChoice = optionsSubMenu(subject);
                switch (userChoice) {
                    case 1 -> subject.loadData();
                    case 2 -> subject.addStudent();
                    case 3 -> subject.sendReport();
                    case 4 -> subject.showStudentsList();
                    case 5 -> exit = true;
                    default -> System.out.println(important + "Only integer between 1 & 4");
                }
            } catch (InputMismatchException e) {
                System.out.println(important+"Invalid Choice\n");

            }
        }
    }

}


