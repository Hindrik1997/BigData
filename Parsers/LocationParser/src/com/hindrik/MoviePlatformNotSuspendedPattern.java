package com.hindrik;
import java.util.regex.Matcher;

public class MoviePlatformNotSuspendedPattern extends PatternSet {

    MoviePlatformNotSuspendedPattern() {
        super("([^\"].*)\\s+\\((.+)\\)\\s+\\((.+)\\)\\s+([^{].*)");
    }

    @Override
    protected Object process(String input, Matcher matcher) {
        Movie m = new Movie();

        m.set_title(matcher.group(1));
        m.set_yearOfRelease(matcher.group(2));
        m.set_medium(matcher.group(3));
        m.set_location(matcher.group(4));

        return m;
    }

}
