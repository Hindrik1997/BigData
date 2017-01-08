/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package biografieparser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 *
 * @author Romeo
 */
public class BiografieParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException{
        BufferedStream();
    }
    
    public static void BufferedStream() throws IOException{
        Pattern p = Pattern.compile("^NM: (.+)|^RN: (.+)|^HT: (.+)|^DB:\\s([^,]*)(?:\\,\\s(.*))?|^DD:(.*?)(?:,|$)(.*?)(?:\\((.*)\\)|$)");
        
        BufferedWriter fw = new BufferedWriter(new FileWriter("biography.cvs"));
        List<String> columns = Arrays.asList("Name", "Real name", "Height", "Date birth", "Location", "Date death", "Location", "Reason");
        String listString = "";
        
        for (String s : columns)
        {
            listString += s + ";";
        }
        
        fw.write(listString + "\n");
        
		boolean first = false;
		List<String> row = new ArrayList<String>();
        
        try(BufferedReader br = new BufferedReader(new FileReader("biographies.list"))) {
            for(String line; (line = br.readLine()) != null; ) {
                Matcher m = p.matcher(line);
                 
                if (m.find()) 
                {
                    if (!first){
                            first = true;
                            for (int i = 0; i <= 7; i++){
                                    row.add("null");
                            }
                    }
                    for (int i = 1; i <= 8; i++)
                    {
                        if (!"".equals(m.group(i)) && m.group(i) != null)
                        {
                            row.set(i-1, m.group(i));
                        }
                    }                    
                 }else{
                    if (first && line.contains("--------------------")){                      
                        listString = "";
                            for (String s : row)
                            {
                                    listString += s + ";";
                            }
                            fw.write(listString + "\n");
                            row = new ArrayList<String>();
                            for (int i = 0; i <= 7; i++){
                                    row.add("null");
                            }
                     }
             }   
            }
        }
        fw.close();
    }
    
}