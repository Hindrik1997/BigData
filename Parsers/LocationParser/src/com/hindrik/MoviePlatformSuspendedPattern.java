package com.hindrik;

import java.util.regex.Matcher;

public class MoviePlatformSuspendedPattern extends PatternSet {

    MoviePlatformSuspendedPattern() {
        super("([^\"].*)\\s+\\((.+)\\)\\s+\\((\\w+)\\)\\s+\\{\\{(\\w+)\\}\\}\\s+(.*)");
    }

    @Override
    protected Object process(String input, Matcher matcher) {
        Movie m = new Movie();

        m.set_title(matcher.group(1));
        m.set_yearOfRelease(matcher.group(2));
        m.set_medium(matcher.group(3));
        m.set_state(matcher.group(4));
        m.set_location(matcher.group(5));

        return m;
    }
}
