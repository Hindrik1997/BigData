package com.hindrik;

import java.util.Objects;

class Movie {

    private String _title = "null";
    private String _yearOfRelease = "null";
    private String _quarter = "null";
    private String _medium = "null";
    private String _state = "null";
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

    void set_medium(String _location) {
        this._medium = _location.trim();
        if(this._medium.isEmpty())
            this._medium = "null";
    }

    void set_state(String _location) {
        this._state = _location.trim();
        if(this._state.isEmpty())
            this._state = "null";
    }

    void set_location(String _location) {
        this._location = _location.trim();
        if(this._location.isEmpty())
            this._location = "null";
    }

    @Override
    public String toString() {
        return _title + "|" + _yearOfRelease + "|" + _quarter + "|" + _medium + "|" + _state + "|" + _location;
    }

}
