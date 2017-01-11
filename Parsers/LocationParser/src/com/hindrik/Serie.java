package com.hindrik;

import java.util.Objects;

/**
 * Wrapper class for the data of a serie. Provides "null" in case a serie doesn't have the specified attribute.
 * Overrides the toString()
 */
class Serie {

    private String _title = "null";
    private String _yearOfRelease = "null";
    private String _quarter = "null";
    private String _episodeName = "null";
    private String _seasonNr = "null";
    private String _episodeNr = "null";
    private String _location = "null";

    void set_title(String _title) {
        this._title = _title;
    }

    void set_yearOfRelease(String _yearOfRelease) {
        this._yearOfRelease = _yearOfRelease.trim();
        if(this._yearOfRelease.isEmpty() || (Objects.equals(this._yearOfRelease, "????")))
            this._yearOfRelease = "null";
    }

    void set_quarter(String _quarter) {
        this._quarter = _quarter.trim();
        if(this._quarter.isEmpty())
            this._quarter = "null";
    }

    void set_episodeName(String _episodeName) {
        this._episodeName = _episodeName.trim();
        if(this._episodeName.isEmpty())
            this._episodeName = "null";
    }

    void set_seasonNr(String _seasonNr) {
        this._seasonNr = _seasonNr.trim();
        if(this._seasonNr.isEmpty())
            this._seasonNr = "null";
    }

    void set_episodeNr(String _episodeNr) {
        this._episodeNr = _episodeNr.trim();
        if(this._episodeNr.isEmpty())
            this._episodeNr = "null";
    }

    void set_location(String _location) {
        this._location = _location.trim();
        if(this._location.isEmpty())
            this._location = "null";
    }

    /**
     * Override for the tostring method for text representation of the object.
     * @return Text representation of the serie.
     */
    @Override
    public String toString() {
        return _title + "|" + _yearOfRelease + "|" + _quarter + "|" + _episodeName + "|" + _seasonNr + "|" + _episodeNr + "|" + _location;
    }
}