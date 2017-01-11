/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gerneparser;

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
public class GerneParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException{
        BufferedStream();
    }
    
    public static void BufferedStream() throws IOException{
        //belangrijke informatie uit lijn in groups zetten
        Pattern p = Pattern.compile("^(.*?)\\((?:(\\d{4})|\\?{4})(?:\\/(.*?)?\\))?\\)?(?:\\s*\\{\\{(SUSPENDED)}})?(?:\\s*\\{(.*?)(?:\\s*\\(#([\\d]*)\\.([\\d]*)\\))?})?(?:\\s*\\((V|TV|VG)\\))?(?:\\s*(.+))?");
        //Regex om te kijken of het een serie is.
        Pattern isSerie = Pattern.compile("^\"(.*)\"$");
        
        //nieuwe buffered writer aanmaken.
        BufferedWriter fw = new BufferedWriter(new FileWriter("genre.csv"));
        
        //kolommen
        List<String> columns = Arrays.asList("movie", "serie", "year", "quarter", "state", "eps name", "season", "episode", "platform", "genre");
        
        //Bool om te zien wanneer het de genre's beginnen
        boolean start = false;
        
        String prevName = "";
        String listString = "";
        
        //kolommen in 1 string zetten.
        for (String s : columns)
        {
            listString += s + "|";
        }
        
        //kolommen naar file schrijven
        fw.write(listString + "\n");
        
        //probeer de file te vinden en te lezen. Als dat niet lukt naar de catch.
        try(BufferedReader br = new BufferedReader(new FileReader("genres.list"))) {
            //nieuwe lijn uit de file halen.
            for(String line; (line = br.readLine()) != null; ) {
                
                if(!start)
                {
                    if(line.equals("8: THE GENRES LIST"))
                    {
                        start = true;
                    }
                }
                
                listString = "";
                 Matcher m = p.matcher(line);
                 List<String> row = new ArrayList<String>();
                 
                 if (m.find())
                 {
                     if(start)
                     {
                        //matchen of het een film of serie is.
                        Matcher isS = isSerie.matcher(m.group(1).trim());

                        //als de regex een match heeft is het een serie
                        if (isS.find()){
                            row.add("null");        //film
                            row.add(isS.group(1)); //serie
                        }else{
                            row.add(m.group(1));    //film
                            row.add("null");        //serie
                        }

                        for (int i = 2; i <= 9; i++){
                            if ("".equals(m.group(i)) || "????".equals(m.group(i))){
                                   row.add("null");
                               }else{
                                   row.add(m.group(i));
                               } 
                        }

                        //row in 1 string zetten.
                        for (String s : row)
                        {
                            if (s != null){
                                s = s.trim(); //onnodige spaties etc. weghalen.
                            }
                            listString += s + "|";
                        }

                        //row in file zetten.
                        fw.write(listString + "\n");
                    }
                 }
            }
        }
        catch (Exception x){
            x.printStackTrace();
        }
        
        //bufferwriter afsluiten
        fw.close();
    }
    
}
