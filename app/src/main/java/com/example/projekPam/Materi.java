package com.example.projekPam;

public class Materi {
    private String namaMateri;
    private int imgMateriResId;

    // Constructor
    public Materi(String namaMateri, int imgMateriResId) {
        this.namaMateri = namaMateri;
        this.imgMateriResId = imgMateriResId;
    }

    // Getter dan Setter
    public String getNamaMateri() {
        return namaMateri;
    }

    public void setNamaMateri(String namaMateri) {
        this.namaMateri = namaMateri;
    }

    public int getImgMateriResId() {
        return imgMateriResId;
    }

    public void setImgMateriResId(int imgMateriResId) {
        this.imgMateriResId = imgMateriResId;
    }
}
