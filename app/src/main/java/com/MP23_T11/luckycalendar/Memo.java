package com.MP23_T11.luckycalendar;

public class Memo {
    private String date;
    private String dayOfWeek;
    private String emotion;
    private String content;

    // Constructor
    public Memo(String date, String dayOfWeek, String emotion, String content) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.emotion = emotion;
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

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

