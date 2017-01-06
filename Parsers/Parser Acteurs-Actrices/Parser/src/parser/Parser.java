/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author jacob
 */
public class Parser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedStream();
    }
    
    public static void BufferedStream() throws IOException{
        Pattern r = Pattern.compile("^(.*?)\\t(.*?)\\((.{4})\\)?(?:\\s*\\{([\\w!\\s:;\\/\\.\\-\\'”?`_&@$%^*<>~+=\\|\\,\\(\\)]*)(?:\\s*\\(#([\\d]*)\\.([\\d]*)\\))?})?(?:\\s*\\((.*?)\\))?(?:\\s*\\((.*?)\\))?(?:\\s*\\[(.*?)\\])?(?:\\s*\\<(.*?)\\>)?");
        
        BufferedWriter fw = new BufferedWriter(new FileWriter("C:/Users/jacob/stack/nhl/jaar2/Project Big Data/parsed content/actors.csv"));
        List<String> columns = Arrays.asList("name", "Movie/serie", "year", "Eps Name", "season", "episode", "voice", "credited as", "charcacter name", "billing position", "v or tv");
        Boolean addV = false;
        Boolean addTV = false;
        Boolean addAS = false;
        String prevName = "";
        String listString = "";

        for (String s : columns)
        {
            listString += s + ";";
        }
        
        fw.write(listString + "\n");
        
        try(BufferedReader br = new BufferedReader(new FileReader("C:/Users/jacob/stack/nhl/jaar2/Project Big Data/actors.list"))) {
            for(String line; (line = br.readLine()) != null; ) {
                listString = "";
                addV = false;
                addTV = false;
                addAS = false;
                Matcher m = r.matcher(line);
                
                if (m.find()) {
                    List<String> row = new ArrayList<String>(); 
                    
                    if("".equals(m.group(1))) row.add(prevName);
                    else{
                        row.add(m.group(1));
                        prevName = m.group(1);
                    }
                    
                     for (int i = 2; i <= 6; i++){
                         if ("".equals(m.group(i))){
                                row.add("null");
                            }else{
                                row.add(m.group(i));
                            } 
                     }
                    
                     if ("voice".equals(m.group(7))){
                         row.add(m.group(7));
                     }else{
                         row.add("null");
                         if ("V".equals(m.group(7))){
                                addV = true;
                            }else if ("TV".equals(m.group(7))){
                                addTV = true;
                            } 
                            else {
                                addAS = true;
                            }
                     }
                     
                     if (addAS){
                        row.add(m.group(7));
                        if ("V".equals(m.group(8))){
                           addV = true;
                        }else if ("TV".equals(m.group(8))){
                            addTV = true;
                        } 
                     }else{
                         if ("V".equals(m.group(8))){
                                row.add("null");
                                addV = true;
                            }else if ("TV".equals(m.group(8))){
                                row.add("null");
                                addTV = true;
                            }
                            else if ("".equals(m.group(8))){
                                row.add("null");
                            }else{
                                row.add(m.group(8));
                            }
                     }
                     
                     for (int i = 9; i <= 10; i++){
                         if ("".equals(m.group(i))){
                                row.add("null");
                            }else{
                                row.add(m.group(i));
                            } 
                     }
                     
                    if (addV) row.add("V");
                    else if (addTV) row.add("TV");
                    else row.add("null");

                    for (String s : row)
                    {
                        listString += s + ";";
                    }

                    fw.write(listString + "\n");
                }
            }
    }catch (Exception x){
        
    }
        fw.close();
}
    
}