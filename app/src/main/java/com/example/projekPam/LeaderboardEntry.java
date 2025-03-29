package com.example.projekPam;

public class LeaderboardEntry {
    private String nama;
    private String username;
    private int skor;
    private int ranking;

    public LeaderboardEntry(String nama, String username, int skor, int ranking) {
        this.nama = nama;
        this.username = username;
        this.skor = skor;
        this.ranking = ranking;
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
}