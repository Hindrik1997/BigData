package com.hindrik;

import java.util.Objects;

class Serie {

    private String _title = "null";
    private String _yearOfRelease = "null";
    private String _quarter = "null";
    private String _episodeName = "null";
    private String _seasonNr = "null";
    private String _episodeNr = "null";
    private String _ratingMajor = "null";
    private String _ratingMinor = "null";
    private String _voters = "null";

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

    void set_ratingMinor(String _ratingMinor) {
        this._ratingMinor = _ratingMinor.trim();
        if(this._ratingMinor.isEmpty())
            this._ratingMinor = "null";
    }

    void set_ratingMajor(String _ratingMajor) {
        this._ratingMajor = _ratingMajor.trim();
        if(this._ratingMajor.isEmpty())
            this._ratingMajor = "null";
    }

    void set_voters(String _voters) {
        this._voters = _voters.trim();
        if(this._voters.isEmpty())
            this._voters = "null";
    }

    @Override
    public String toString() {
        return _title + "|" + _yearOfRelease + "|" + _quarter + "|" + _episodeName + "|" + _seasonNr + "|" + _episodeNr + "|" + _ratingMajor + "|" + _ratingMinor + "|" + _voters;
    }
}