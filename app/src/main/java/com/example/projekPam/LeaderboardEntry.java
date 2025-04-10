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

    public String getNama() { return nama; }

    public void setNama(String nama) { this.nama = nama; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public int getSkor() { return skor; }

    public void setSkor(int skor) { this.skor = skor; }

    public int getRanking() { return ranking; }

    public void setRanking(int ranking) { this.ranking = ranking; }
}