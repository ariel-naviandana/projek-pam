package com.example.projekPam;

public class LeaderboardEntry {
    private String nama;
    private String username;
    private int skor;
    private int ranking;
    private String image;

    public LeaderboardEntry(String nama, String username, int skor, int ranking, String image) {
        this.nama = nama;
        this.username = username;
        this.skor = skor;
        this.ranking = ranking;
        this.image = image;
    }

    public String getNama() {
        return nama;
    }

    public String getUsername() {
        return username;
    }

    public int getSkor() {
        return skor;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getImage() {
        return image;
    }
}