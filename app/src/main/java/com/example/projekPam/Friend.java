package com.example.projekPam;

public class Friend {
    private String id;
    private String username;
    private String fullname;
    private  String image;

    public Friend() {}

    public Friend(String id) {
        this.id = id;
    }

    public Friend(String id, String username, String fullname, String image) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname (String fullname) {
        this.fullname = fullname;
    }

    public String getImage() {
        return image;
    }
}
