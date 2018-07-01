package com.divineventures.journalapp;

public class EntryData {
    private String description,date,time;

    public EntryData(String description, String date, String time) {
        this.description = description;
        this.date = date;
        this.time = time;

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
