package com.example.projekPam;

import com.google.firebase.Timestamp;

public class Materi {
    private String id;
    private String judul;
    private String image;
    private String deskripsi;
    private Timestamp created_at;

    // Constructor
    public Materi() {}

    public Materi(String id, String judul, String image, String deskripsi, Timestamp created_at) {
        this.id = id;
        this.judul = judul;
        this.image = image;
        this.deskripsi = deskripsi;
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

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}