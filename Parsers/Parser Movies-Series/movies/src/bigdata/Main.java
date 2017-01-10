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

/**
 * @author denny
 * parses the movies.list file and separates the data into .csv files for movies and series
 */

public class Main {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        BufferedStream();
    }

    public static void BufferedStream() throws IOException {

        Pattern rSeries = Pattern.compile("(?:\\\")([^\\\"]+)(?:\\\")(?:[\\s]*(?:\\())(\\d{4}|\\?{4})(?:\\/)?([IVXCM]+)?(?:\\)?[\\s]*)(?:(?:\\{)+(?:(?:([^\\}]*)(?:\\()(?:#)?([^\\)]*)\\))*)(?:\\})+)?(?:[\\s]*)(\\{\\{[\\S]*\\}\\})?(?:[\\s]*)(\\d{4}|\\?{4})?(?:.*)");
        Pattern rMovies = Pattern.compile("([^\\\"]+)\\s(?:(?:\\())(\\d{4}|\\?{4})(?:\\/)?([IVXCM]+)?(?:\\)?)(?:\\s*(?:\\()([TVG]+)?(?:\\)))?(?:(?:[\\s]*)(\\{\\{[\\S]*\\}\\})?)(?:.*)?");

        Pattern rEpisodeDate = Pattern.compile("(\\d{4})(\\-)(\\d{2})(\\-)(\\d{2})");
        Pattern rEpisodeSeason = Pattern.compile("([0-9]+)(?:\\.)([0-9]+)");

        BufferedWriter fws = new BufferedWriter(new FileWriter("series.csv"));
        BufferedWriter fwm = new BufferedWriter(new FileWriter("movies.csv"));

        // Generate the headers and specify columns for csv files
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

                //Try to match the line to both regex to check whether it's a movie or series
                Matcher ms = rSeries.matcher(line);
                Matcher mm = rMovies.matcher(line);

                if(ms.find()){
                    List<String> row = new ArrayList<String>();

                    for(int i=1;i<4;i++){
                        if ("".equals(ms.group(i)) || "????".equals(ms.group(i))) row.add("null");
                        else row.add(ms.group(i));
                    }

                    if(ms.group(4)!= null)
                    {
                        if ("".equals(ms.group(4))) row.add("null");
                        else row.add(ms.group(4).trim());

                        if(ms.group(5) != null){

                            // Check whether the episode has a season and episode number or is specified by release date;
                            // Make the other null
                            Matcher mDate = rEpisodeDate.matcher(ms.group(5));
                            Matcher mSeason = rEpisodeSeason.matcher(ms.group(5));

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

                        //if the episode has only a name add null for date, season and episode
                        else { for(int i=0;i<3;i++){row.add("null");} }

                        String state = "null";
                        if(ms.group(6)!=null){
                            state =  "Suspended";
                        }

                        if("".equals(ms.group(7)) ||"????".equals(ms.group(7))) row.add("null");
                        else row.add(ms.group(7));

                        row.add(state);
                    }

                    else {
                        // if group 4 is not found, it means it's not an episode but the series itself, or a suspended series
                        for(int i=4;i<7;i++)row.add("null");

                        if("".equals(ms.group(7)) ||"????".equals(ms.group(7))) row.add("null");
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

                // if there is no match for series try to find a match for movies;
                // prevent that any incomplete/corrupt series entries get picked up by movies
                // (all series name start with quotes at the beginning of the line, movies don't)
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
