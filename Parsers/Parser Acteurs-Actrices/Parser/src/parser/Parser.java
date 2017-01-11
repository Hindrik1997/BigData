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
        //nieuwe buffered writer aanmaken.
        BufferedWriter fw = new BufferedWriter(new FileWriter("actors.csv"));
        
        //kolommen
        List<String> columns = Arrays.asList("name", "movie", "serie", "year", "quarter", "state", "episode date", "episode name", "season", "episode", "platform", "voice", "credited as", "character name", "billing position", "gender");
        String listString = "";
        
        //kolommen in 1 string zetten.
        for (String s : columns)
        {
            listString += s + "|";
        }
        
        //kolommen naar file schrijven
        fw.write(listString + "\n");
        
        parseActors(fw, "actors", "m");     //mannen    (male)
        parseActors(fw, "actresses", "f");  //vrouwen   (female)
        
        fw.close();
    }
    
    public static void parseActors(BufferedWriter fw, String nameFile, String gender) throws IOException{
        //belangrijke informatie uit lijn in groups zetten
        Pattern p = Pattern.compile("^(.*?)\\t(.*?)\\((?:(\\d{4})|\\?{4})(?:\\/(.*?)?\\))?\\)?(?:\\s*\\{\\{(SUSPENDED)}})?(?:\\s*\\{(?:(?:\\((\\d{4}\\-\\d{2}\\-\\d{2})\\))|(.*?))(?:\\s*\\(#([\\d]*)\\.([\\d]*)\\))?})?(?:\\s*\\((V|TV|VG)\\))?(?:\\s*\\((voice)\\))?(?:\\s*\\((as.*?)\\))?(?:\\s*\\[(.*?)\\])?(?:\\s*\\<(.*?)\\>)?");
        //Regex om te kijken of het een serie is.
        Pattern isSerie = Pattern.compile("^\"(.*)\"$");
        
        String prevName = "";
        String listString = "";

        //probeer de file te vinden en te lezen. Als dat niet leuk naar de catch.
        try(BufferedReader br = new BufferedReader(new FileReader(nameFile + ".list"))) {
            //nieuwe lijn uit de file halen.
            for(String line; (line = br.readLine()) != null; ) {
                
                Matcher m = p.matcher(line);
                List<String> row = new ArrayList<String>();
                 
                if (m.find()){
                   //als de naam leeg is het dezelfde acteur als ervoor;
                   if("".equals(m.group(1))) row.add(prevName);
                   else{
                       //nieuwe naam van acteur
                       row.add(m.group(1));
                       prevName = m.group(1);
                   }

                   //matchen of het een film of serie is.
                   Matcher isS = isSerie.matcher(m.group(2).trim());

                   //als de regex een match heeft is het een serie
                   if (isS.find()){
                       row.add("null");        //film
                       row.add(isS.group(1)); //serie
                   }else{
                       row.add(m.group(2));    //film
                       row.add("null");        //serie
                   }

                   for (int i = 3; i <= 14; i++){
                       if ("".equals(m.group(i))){
                              row.add("null");
                          }else{
                              row.add(m.group(i));
                          } 
                   }
                   
                   listString = "";
                   
                   //row in 1 string zetten.
                   for (String s : row)
                   {
                       if (s != null){
                           s = s.trim(); //onnodige spaties etc. weghalen.
                       }
                       listString += s + "|";
                   }

                   //row in file zetten.
                   fw.write(listString + gender+ "|\n");
                }
            }
        }catch (Exception x){
            x.printStackTrace();
        }
    }
}