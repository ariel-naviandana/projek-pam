package com.example.projekPam;

import com.google.firebase.Timestamp;

public class Challenge {
    private String id;
    private String judul;
//    private int image;
    private Timestamp date_start;
    private Timestamp date_end;
    private Timestamp created_at;

    public Challenge() {}

    public Challenge(String id, String judul, Timestamp date_start, Timestamp date_end) {
        this.id = id;
        this.judul = judul;
        this.date_start = date_start;
        this.date_end = date_end;
        this.created_at = Timestamp.now();
    }

    public void setDate_start(Timestamp date_start) {
        this.date_start = date_start;
    }

    public void setDate_end(Timestamp date_end) {
        this.date_end = date_end;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

//    public int getImage() {
//        return image;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }

    public String getJudul() {
        return judul;
    }

    public Timestamp getDate_start() {
        return date_start;
    }

    public Timestamp getDate_end() {
        return date_end;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setId(String id) {
        this.id = id;
    }
}