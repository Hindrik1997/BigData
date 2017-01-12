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
        //de regex voor biography.list
        Pattern p = Pattern.compile("^NM: (.+)|^RN: (.+)|^HT: (.+)|^DB:\\s([^,]*)(?:\\,\\s(.*))?|^DD:(.*?)(?:,|$)(.*?)(?:\\((.*)\\)|$)");
        
        //naar deze csv file wordt de output geschreven
        BufferedWriter fw = new BufferedWriter(new FileWriter("biography.csv"));
        
        String listString = "";
        
        boolean first = false;
        List<String> row = new ArrayList<String>();
        
        //probeerd de file te vinden en te lezen, als dat niet lukt naar de catch
        try(BufferedReader br = new BufferedReader(new FileReader("biographies.list"))) 
        {
            for(String line; (line = br.readLine()) != null; ) 
            {
                Matcher m = p.matcher(line);
                 
                //als hij een match heeft met de regex, dan gaat hij deze if binnnen
                if (m.find()) 
                {                    
                    //voor alle 8 groepen kijken of hij niet leeg is en niet null, zo ja, dan zet hij de data op de goede plek in de row
                    for (int i = 1; i <= 8; i++)
                    {
                        if (!"".equals(m.group(i)) && m.group(i) != null)
                        {
                            row.set(i-1, m.group(i));
                        }
                    }                    
                 }
                
                //als de regex niet meer een match heeft, dan deze else binnen
                else
                {
                    //als de lijn bestaat uit --- (einde van een biografie), deze if binnen
                    if (line.contains("--------------------"))
                    {  
                        //als de eerste geweest is
                        if(first)
                        {
                            //de string leeg maken
                            listString = "";

                            for(int a=0;a<row.size();a++)
                            {
                                if (row.get(a) != null){
                                    listString+= row.get(a).trim();
                                }else{
                                    listString+= row.get(a);
                                }

                                if(a != row.size()-1) listString += "|";

                            }

                            //aan het einde van de regel een enter toevoegen
                            fw.write(listString + "\n");
                        }
                        
                        //als de eerste --- is geweest, deze op true zetten om een lege row te voorkomen
                        else
                        {
                            first = true;
                        }

                        //de rij leeg maken
                        row = new ArrayList<String>();

                        //de rij vullen met null's
                        for (int i = 0; i <= 7; i++)
                        {
                                row.add("null");
                        }
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