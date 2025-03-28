package com.example.projekPam;

public class LeaderboardEntry {
    private final String nama;
    private final String username;
    private final int skor;
    private final int ranking;

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
