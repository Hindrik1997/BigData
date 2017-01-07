package com.hindrik;

import java.util.regex.Matcher;

public class SerieNoSeasonSupportPattern extends PatternSet{

    SerieNoSeasonSupportPattern() {
        super("\"(.*)\"\\s+\\((.+)\\)\\s+(?!\\{(.*)\\(\\#(\\d+)\\.(\\d+)\\)\\})\\s+(.*)");
    }

    @Override
    protected Object process(String input, Matcher matcher) {

        Serie s = new Serie();

        s.set_title(matcher.group(1));
        s.set_yearOfRelease(matcher.group(2));
        s.set_location(matcher.group(6));

        return s;
    }

}