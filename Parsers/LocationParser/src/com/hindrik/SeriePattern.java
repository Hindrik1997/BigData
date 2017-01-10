package com.hindrik;

import java.util.regex.Matcher;

public class SeriePattern extends PatternSet {

    public SeriePattern() {
        super("\"(.+)\"\\s+(?:\\((\\d{4}|\\?{4})(?:\\/([IVXCM]+))?\\))\\s+(?:\\{(.+)\\s+\\(#(\\d+)\\.(\\d+)\\)\\})?\\s+(.+)");
    }

    @Override
    protected Object process(String input, Matcher matcher) {
        Serie s = new Serie();

        s.set_title(matcher.group(1));
        s.set_yearOfRelease(matcher.group(2));
        if(matcher.group(3) != null)
            s.set_quarter(matcher.group(3));
        if(matcher.group(4) != null)
            s.set_episodeName(matcher.group(4));
        if(matcher.group(5) != null)
            s.set_seasonNr(matcher.group(5));
        if(matcher.group(6) != null)
            s.set_episodeNr(matcher.group(6));

        s.set_location(matcher.group(7));

        return s;
    }
}
