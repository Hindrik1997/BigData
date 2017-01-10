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

        Pattern rSeries = Pattern.compile("(?:\\\")([^\\\"]+)(?:\\\")(?:[\\s]*(?:\\())(\\d{4})(?:\\/)?([IVXCM]*)?(?:\\)?[\\s]*)(?:(?:\\{)+(?:(?:([^\\}]*)(?:\\()(?:#)?([^\\)]*)\\))*)(?:\\})+)?(?:[\\s]*)(\\{\\{[\\S]*\\}\\})?(?:[\\s]*)(\\d{4})?(?:.*)");
        Pattern rMovies = Pattern.compile("([^\\\"]+)\\s(?:(?:\\())(\\d{4})(?:\\/)?([IVXCM]*)?(?:\\)?)(?:\\s*(?:\\()([TV]*)?(?:\\)))?(?:(?:[\\s]*)(\\{\\{[\\S]*\\}\\})?)(?:.*)?");

        Pattern rEpisodeDate = Pattern.compile("(\\d\\d\\d\\d)(\\-)(\\d\\d)(\\-)(\\d\\d)");
        Pattern rEpisodeSeason = Pattern.compile("([0-9]+)(?:\\.)([0-9]+)");

        BufferedWriter fws = new BufferedWriter(new FileWriter("series.csv"));
        BufferedWriter fwm = new BufferedWriter(new FileWriter("movies.csv"));

        List<String> seriecolumns = Arrays.asList("Title", "SerieStarted", "Quarter", "EpisodeName", "EpisodeDate", "SeasonNr", "EpisodeNr", "EpisodeYear", "State");
        List<String> moviecolumns = Arrays.asList("Title", "Year", "Quarter", "Medium", "State");
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

                    for(int i=1;i<4;i++){
                        if ("".equals(ms.group(i))) row.add("null");
                        else row.add(ms.group(i));
                    }

                    if(ms.group(4)!= null)
                    {
                        if ("".equals(ms.group(4))) row.add("null");
                        else row.add(ms.group(4));

                        if(ms.group(5) != null){
                            Matcher mDate = rEpisodeDate.matcher(ms.group(5).toString());
                            Matcher mSeason = rEpisodeSeason.matcher(ms.group(5).toString());

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
                        }
                        else { for(int i=0;i<3;i++){row.add("null");} }

                        String state = "null";
                        if(ms.group(6)!=null){
                            state =  "Suspended";
                        }
                        if("".equals(ms.group(7))) row.add("null");
                        else row.add(ms.group(7));

                        row.add(state);
                    }

                    else {
                        for(int i=4;i<7;i++)row.add("null");

                        if("".equals(ms.group(7))) row.add("null");
                        else row.add(ms.group(7));

                        if(ms.group(6) != null) row.add("Suspended");
                        else row.add("null");
                    }


                    for (String s : row)
                    {
                        listString += s + "|";
                    }

                    fws.write(listString + "\n");
                }
                else if (mm.find() && !(line.startsWith("\""))) {
                    List<String> row = new ArrayList<String>();

                    for (int i = 1; i <= 4; i++){
                        if ("".equals(mm.group(i))) row.add("null");
                        else row.add(mm.group(i));
                    }

                    if(mm.group(5) != null) row.add("Suspended");
                    else row.add("null");

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
