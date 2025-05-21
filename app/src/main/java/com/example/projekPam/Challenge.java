package com.example.projekPam;

import com.google.firebase.Timestamp;

public class Challenge {
    private String id;
    private String judul;
    private String image;
    private Timestamp date_start;
    private Timestamp date_end;
    private Timestamp created_at;

    public Challenge() {}

    public Challenge(String id, String judul, String image, Timestamp date_start, Timestamp date_end, Timestamp created_at) {
        this.id = id;
        this.judul = judul;
        this.image = image;
        this.date_start = date_start;
        this.date_end = date_end;
        this.created_at = created_at;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Timestamp getDate_start() {
        return date_start;
    }

    public void setDate_start(Timestamp date_start) {
        this.date_start = date_start;
    }

    public Timestamp getDate_end() {
        return date_end;
    }

    public void setDate_end(Timestamp date_end) {
        this.date_end = date_end;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}