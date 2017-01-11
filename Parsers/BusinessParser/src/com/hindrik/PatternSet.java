package com.hindrik;

import javafx.util.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract base class for a patternset. This class takes care of the basics and thus provides an easy way to exten
 * the program to support many types of regexes
 */
abstract class PatternSet {

    final private Pattern _pattern;

    /**
     * Constructor to which the pattern should be provided
     * @param pattern pattern to use
     */
    PatternSet(String pattern)
    {
        _pattern = Pattern.compile(pattern);
    }


    /**
     * Function that should be implemented in extending classes, returns a Pair of a boolean and an object and
     * if the object has matched or not. In which case it will return null for the object and false for the bool.
     * @param input input string to match
     * @param matcher input matcher
     * @return a pair which represents wether the string has matched and returns an object representing the result
     */
    protected abstract Object process(@SuppressWarnings("UnusedParameters") String input, Matcher matcher);

    Pair<Boolean, Object> match(String input)
    {
         Pair<Boolean, Object> pair = new Pair<>(false, null);
         Matcher matcher = _pattern.matcher(input);
         if(matcher.find())
         {
             Object o = process(input,matcher);
             pair = new Pair<>(true, o);
         }
         return pair;
    }
}