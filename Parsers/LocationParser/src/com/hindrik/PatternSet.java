package com.hindrik;

import javafx.util.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class PatternSet {

    private Pattern _pattern;

    PatternSet(String pattern)
    {
        _pattern = Pattern.compile(pattern);
    }

    protected abstract Object process(String input, Matcher matcher);

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