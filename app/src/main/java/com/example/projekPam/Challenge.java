package com.example.projekPam;

public class Challenge {
    private String id;
    private String title;
    private String period;
    private int image;

    // konstruktor kosong firebase
    public Challenge() {}

    // konstruktor dengan parameter
    public Challenge(String title, String period, int image) {
        this.title = title;
        this.period = period;
        this.image = image;
    }

    // getter setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public String getPeriod() { return period; }
    public int getImage() { return image; }
}