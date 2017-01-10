package com.hindrik;

import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

class Main {

    private static BufferedWriter seriesOutput = null;
    private static BufferedWriter moviesOutput = null;
    final private static List<PatternSet> seriePatterns = new ArrayList<>();
    final private static List<PatternSet> moviePatterns = new ArrayList<>();
    private static boolean display = false;
    private static boolean start = false;
    private static String startString = null;

    public static void main(String[] args) {

        if(args.length < 4)
        {
            System.out.println("Please provide a filename to parse, and two output files as arguments, also a start string is required!");
            return;
        }

        startString = args[3];

        if(args.length == 5 && Objects.equals(args[4], "display"))
            display = true;

        setupRegex();

        try {
            seriesOutput = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1])));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            moviesOutput = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[2])));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            seriesOutput.write("Title|Year of release|Quarter|Episode title|Season nr|Episode nr|Rating Major|Rating Minor|Voters");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            seriesOutput.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            moviesOutput.write("Title|Year of release|Quarter|Medium|RatingMajor|RatingMinor|Voters");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            moviesOutput.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Stream<String> stream = Files.lines(Paths.get(args[0]), StandardCharsets.ISO_8859_1)) {
                stream.forEachOrdered(Main::processLocation);
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    private static void setupRegex()
    {
        seriePatterns.add(new SeriePattern());
        moviePatterns.add(new MoviePattern());
    }

    private static void processLocation(String string) {

        if(start || Objects.equals(string, startString))
        {
            start = true;
            process(string, seriePatterns, seriesOutput);
            process(string, moviePatterns, moviesOutput);
        }
    }

    private static void process(String string, List<PatternSet> patternSet, BufferedWriter output)
    {
        for(PatternSet pattern : patternSet)
        {
            Pair<Boolean, Object> result = pattern.match(string);
            if(result.getKey()) {
                if(display)
                    System.out.println(result.getValue().toString());
                try {
                    output.write(result.getValue().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    output.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

}