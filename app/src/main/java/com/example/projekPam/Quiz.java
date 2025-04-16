package com.example.projekPam;

import android.os.Parcel;
import android.os.Parcelable;

public class Quiz implements Parcelable {
    private String id;
    private String title;
    private String idMateri;
    private String materiTitle;
    private String difficulty;
    private String description;
    private int questionCount;

    public Quiz(String id, String title, String idMateri, String materiTitle, String difficulty, String description, int questionCount) {
        this.id = id;
        this.title = title;
        this.idMateri = idMateri;
        this.materiTitle = materiTitle;
        this.difficulty = difficulty;
        this.description = description;
        this.questionCount = questionCount;
    }

    protected Quiz(Parcel in) {
        id = in.readString();
        title = in.readString();
        idMateri = in.readString();
        materiTitle = in.readString();
        difficulty = in.readString();
        description = in.readString();
        questionCount = in.readInt();
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in) {
            return new Quiz(in);
        }

        @Override
        public Quiz[] newArray(int size) {
            return new Quiz[size];
        }
    };

    public String getId() { return id; }

    public String getTitle() { return title; }

    public String getIdMateri() { return idMateri; }

    public String getMateriTitle() { return materiTitle; }

    public String getDifficulty() { return difficulty; }

    public String getDescription() { return description; }

    public int getQuestionCount() { return questionCount; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(idMateri);
        dest.writeString(materiTitle);
        dest.writeString(difficulty);
        dest.writeString(description);
        dest.writeInt(questionCount);
    }
}