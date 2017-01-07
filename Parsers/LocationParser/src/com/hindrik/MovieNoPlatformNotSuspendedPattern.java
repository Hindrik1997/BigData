package com.hindrik;

import java.util.regex.Matcher;

public class MovieNoPlatformNotSuspendedPattern extends PatternSet {
    MovieNoPlatformNotSuspendedPattern() {
        super("([^\"].*)\\s+\\((.+)\\)\\s+\\((.+)\\)\\s+([^{].*)");
    }

    @Override
    protected Object process(String input, Matcher matcher) {
        Movie m = new Movie();

        m.set_title(matcher.group(1));
        m.set_yearOfRelease(matcher.group(2));
        m.set_location(matcher.group(3));

        return m;
    }
}
