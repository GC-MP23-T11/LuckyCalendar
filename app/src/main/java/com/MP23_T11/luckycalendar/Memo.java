package com.MP23_T11.luckycalendar;

public class Memo {
    private String date;
    private String dayOfWeek;
    private String content;

    // Constructor
    public Memo(String date, String dayOfWeek, String content) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.content = content;
    }

    // Getters and Setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
