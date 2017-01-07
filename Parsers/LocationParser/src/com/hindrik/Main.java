package com.hindrik;

import javafx.util.Pair;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Main {

    private static BufferedWriter seriesOutput = null;
    private static BufferedWriter moviesOutput = null;
    private static List<PatternSet> seriePatterns = new ArrayList<>();
    private static List<PatternSet> moviePatterns = new ArrayList<>();
    private static boolean display = false;

    public static void main(String[] args) {

        if(args.length < 3)
        {
            System.out.println("Please provide a filename to parse, and two output files as arguments");
            return;
        }

        if(args.length == 4 && Objects.equals(args[3], "display"))
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

        try (Stream<String> stream = Files.lines(Paths.get(args[0]), StandardCharsets.ISO_8859_1)) {
                stream.forEachOrdered(Main::processLocation);
        } catch (IOException io) {
            io.printStackTrace();
        }

        try {
            seriesOutput.write("Title,Year of release,Episode title,Season nr,Episode nr,Location");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            seriesOutput.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            moviesOutput.write("Title,Year of release,Medium,State,Location");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            moviesOutput.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void setupRegex()
    {
        seriePatterns.add(new SerieSeasonSupportPattern());
        seriePatterns.add(new SerieNoSeasonSupportPattern());
        moviePatterns.add(new MoviePlatformSuspendedPattern());
        moviePatterns.add(new MoviePlatformNotSuspendedPattern());
        moviePatterns.add(new MovieNoPlatformNotSuspendedPattern());
    }

    static void processLocation(String string) {
        process(string, seriePatterns, seriesOutput);
        process(string, moviePatterns, moviesOutput);
    }

    static void process(String string, List<PatternSet> patternSet, BufferedWriter output)
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