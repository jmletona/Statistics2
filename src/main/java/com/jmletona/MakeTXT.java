package com.jmletona;

import lombok.Data;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

@Data
public class MakeTXT {
    String name;
    String path;

    public MakeTXT(String Name, String Path){
        this.name = Name;
        this.path = Path;
    }

    public void addArrayList(ArrayList<String> al){
        try {
            FileWriter myWriter = new FileWriter(this.path+this.getName());
            for(String line: al){
                myWriter.write(line+"\n");
            }

            myWriter.close();
            System.out.println("Txt File created!");
        } catch (IOException e) {
            System.out.println("Error trying to create a txt file...");
            e.printStackTrace();
        }
    }
}
