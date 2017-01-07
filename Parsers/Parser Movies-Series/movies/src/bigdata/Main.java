package bigdata;

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
import java.io.*;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedStream();
    }

    public static void BufferedStream() throws IOException {


        Pattern rMovies = Pattern.compile("([^\\\"]+)\\s(?:(?:\\())(\\d\\d\\d\\d)(?:\\/)?([IVXCM]*)?(?:\\)?)(?:\\s*(?:\\()([A-Z]+)(?:\\)))?(?:.)*");
        Pattern rSeries = Pattern.compile("(?:\\\")([^\\\"]+)(?:\\\")(?:[\\s]*(?:\\())(\\d{4})(?:\\/)?([IVXCM]*)?(?:\\)?[\\s]*)(?:\\{)([^\\}\\(]*)(?:(?:\\()(?:#)?([^\\)]*)(?:\\)))?(?:\\})(?:[\\s]*)(\\d{4})?");
        Pattern episodeDate = Pattern.compile("(\\d\\d\\d\\d)(\\-)(\\d\\d)(\\-)(\\d\\d)");
        Pattern episodeSeason = Pattern.compile("([0-9]+)(?:\\.)([0-9]+)");

        BufferedWriter fwm = new BufferedWriter(new FileWriter("movies.csv"));
        BufferedWriter fws = new BufferedWriter(new FileWriter("series.csv"));
        List<String> moviecolumns = Arrays.asList("Title", "Year", "Quarter", "Video or TV");
        List<String> seriecolumns = Arrays.asList("Title", "Series Start Year", "Quarter", "Episode Name", "Episode Date", "Season", "Episode no.", "Episode Launch Year");
        String listString = "";

        for (String s : moviecolumns)
        {
            listString += s + "|";
        }

        fwm.write(listString + "\n");
        listString = "";

        for (String s : seriecolumns)
        {
            listString += s + "|";
        }

        fws.write(listString + "\n");

        try(BufferedReader br = new BufferedReader(new FileReader("movies.list"))) {
            for(String line; (line = br.readLine()) != null; ) {
                listString = "";
                Matcher mm = rMovies.matcher(line);
                Matcher ms = rSeries.matcher(line);
                if (mm.find()) {
                    List<String> row = new ArrayList<String>();

                    for (int i = 1; i <= 4; i++){
                        if ("".equals(mm.group(i))){
                            row.add("null");
                        }else{
                            row.add(mm.group(i));
                        }
                    }

                    for (String s : row)
                    {
                        listString += s + "|";
                    }

                    fwm.write(listString + "\n");
                }
                else if(ms.find()){
                    List<String> row = new ArrayList<String>();

                    row.add(ms.group(1));
                    row.add(ms.group(2));

                    if ("".equals(ms.group(3))){
                        row.add("null");
                    }else{
                        row.add(ms.group(3));
                    }
                    if ("".equals(ms.group(4))){
                        row.add("null");
                    }else{
                        row.add(ms.group(4));
                    }
                    if(ms.group(5) != null){
                        Matcher mDate = episodeDate.matcher(ms.group(5).toString());
                        Matcher mSeason = episodeSeason.matcher(ms.group(5).toString());

                        if(mDate.find()){
                            row.add(mDate.group(0));
                            row.add("null");
                            row.add("null");
                        }
                        else if(mSeason.find()){
                            row.add("null");
                            row.add(mSeason.group(1));
                            row.add(mSeason.group(2));
                        }
                        else{
                            row.add(ms.group(5));
                        }
                    }
                    else { for(int i=0;i<3;i++){row.add("null");} }

                    if("".equals(ms.group(6))){
                    row.add("null");
                    }
                    else{
                        row.add(ms.group(6));
                    }

                    for (String s : row)
                    {
                        listString += s + "|";
                    }

                    fws.write(listString + "\n");
                }
            }
        }catch (Exception x){

        }
        fwm.close();
        fws.close();
    }
}
