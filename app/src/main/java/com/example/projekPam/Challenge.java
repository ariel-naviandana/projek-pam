package com.example.projekPam;

public class Challenge {
    private String id;
    private String title;
    private String period;
    private String imageUrl;

    // konstruktor kosong firebase
    public Challenge() {}

    // konstruktor dengan parameter
    public Challenge(String title, String period, String imageUrl) {
        this.title = title;
        this.period = period;
        this.imageUrl = imageUrl;
    }

    // getter setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public String getPeriod() { return period; }
    public String getImageUrl() { return imageUrl; }
}