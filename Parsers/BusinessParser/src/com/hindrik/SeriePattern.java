package com.hindrik;

import java.util.regex.Matcher;

class SeriePattern extends PatternSet {

    SeriePattern() {
        super("(?:[\\d+\\.*]{10})\\s*(\\d+)\\s*(?:(\\d)\\.(\\d))\\s*(?:([\"].*[\"]))\\s*(?:\\((\\d{4}|\\?{4})(?:\\/([IVXCM]*))?\\))\\s*(?:\\{(.*)\\s*\\(#(\\d*)\\.(\\d*)\\)\\})?");
    }

    @Override
    protected Object process(String input, Matcher matcher) {
        Serie s = new Serie();

        s.set_voters(matcher.group(1));
        s.set_ratingMajor(matcher.group(2));
        s.set_ratingMinor(matcher.group(3));
        s.set_title(matcher.group(4));

        if(matcher.group(5) != null)
            s.set_yearOfRelease(matcher.group(5));

        if(matcher.group(6) != null)
            s.set_quarter(matcher.group(6));

        if(matcher.group(7) != null)
            s.set_episodeName(matcher.group(7));

        if(matcher.group(8) != null)
            s.set_seasonNr(matcher.group(8));

        if(matcher.group(9) != null)
            s.set_episodeNr(matcher.group(9));

        return s;
    }
}
