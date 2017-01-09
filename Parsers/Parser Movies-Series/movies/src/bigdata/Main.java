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

        Pattern rSeries = Pattern.compile("(?:\\\")([^\\\"]+)(?:\\\")(?:[\\s]*(?:\\())(\\d{4})(?:\\/)?([IVXCM]*)?(?:\\)?[\\s]*)(?:\\{)+(?:(?:([^\\}]*)(?:\\()(?:#)?([^\\)]*)\\))*)(?:\\})+(?:[\\s]*)(\\{\\{[\\S]*\\}\\})?(?:[\\s]*)(\\d{4})?(?:.*)");
        Pattern rMovies = Pattern.compile("([^\\\"]+)\\s(?:(?:\\())(\\d{4})(?:\\/)?([IVXCM]*)?(?:\\)?)(?:\\s*(?:\\()([TV]*)?(?:\\)))?(?:(?:[\\s]*)(\\{\\{[\\S]*\\}\\})?)(?:.*)?");

        Pattern episodeDate = Pattern.compile("(\\d\\d\\d\\d)(\\-)(\\d\\d)(\\-)(\\d\\d)");
        Pattern episodeSeason = Pattern.compile("([0-9]+)(?:\\.)([0-9]+)");

        BufferedWriter fws = new BufferedWriter(new FileWriter("series.csv"));
        BufferedWriter fwm = new BufferedWriter(new FileWriter("movies.csv"));

        List<String> seriecolumns = Arrays.asList("Title", "SerieStarted", "Quarter", "EpisodeName", "EpisodeDate", "SeasonNr", "EpisodeNr", "EpisodeYear", "Suspended");
        List<String> moviecolumns = Arrays.asList("Title", "Year", "Quarter", "Medium", "Suspended");
        String listString = "";

        for (String s : seriecolumns)
        {
            listString += s + "|";
        }

        fws.write(listString + "\n");
        listString = "";

        for (String s : moviecolumns)
        {
            listString += s + "|";
        }

        fwm.write(listString + "\n");

        try(BufferedReader br = new BufferedReader(new FileReader("movies.list"))) {
            for(String line; (line = br.readLine()) != null; ) {
                listString = "";
                Matcher ms = rSeries.matcher(line);
                Matcher mm = rMovies.matcher(line);

                if(ms.find()){
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
                            else { for(int i=0;i<3;i++){row.add("null");} }
                        }
                        else { for(int i=0;i<3;i++){row.add("null");} }

                        String suspended = "";
                        if(ms.group(6)!=null){
                            suspended =  "true";
                        }
                        else {
                            suspended = "false";
                        }
                        if("".equals(ms.group(7))){
                            row.add("null");
                        }
                        else{
                            row.add(ms.group(7));
                        }
                        row.add(suspended);


                    for (String s : row)
                    {
                        listString += s + "|";
                    }

                    fws.write(listString + "\n");
                }
                else if (mm.find() && !(line.contains("\""))) {
                    List<String> row = new ArrayList<String>();

                    for (int i = 1; i <= 4; i++){
                        if ("".equals(mm.group(i))){
                            row.add("null");
                        }else{
                            row.add(mm.group(i));
                        }
                    }

                    if(mm.group(5) != null) row.add("true");
                    else row.add("false");

                    for (String s : row)
                    {
                        listString += s + "|";
                    }

                    fwm.write(listString + "\n");
                }
            }
        }

        catch (Exception x){

        }

        fws.close();
        fwm.close();
    }
}
