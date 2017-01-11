package com.hindrik;

import java.util.regex.Matcher;

/**
 * Pattern for searching through movies in locations.list
 */
class MoviePattern extends PatternSet{

    MoviePattern() {
        super("(?:[\\d+\\.*]{10})\\s*(\\d+)\\s*(?:(\\d)\\.(\\d))\\s*(?:([^\"]+))\\s*(?:\\((\\d{4}|\\?{4})(?:\\/([IVXCM]+))?\\))\\s*(?:\\((TV|V)\\))?");
    }

    /**
     * Overriden function.See the base class.
     * @see SeriePattern#process(String, Matcher)
     * @param input input string to match
     * @param matcher input matcher
     * @return a pair of bool and a movie object.
     */
    @Override
    protected Object process(String input, Matcher matcher) {
        Movie m = new Movie();

        m.set_ratingMajor(matcher.group(2));
        m.set_ratingMinor(matcher.group(3));
        m.set_title(matcher.group(4));

        if(matcher.group(5) != null)
            m.set_yearOfRelease(matcher.group(5));

        if(matcher.group(6) != null)
            m.set_quarter(matcher.group(6));

        if(matcher.group(7) != null)
            m.set_medium(matcher.group(7));

        return m;
    }
}
