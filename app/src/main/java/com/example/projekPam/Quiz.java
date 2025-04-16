package com.example.projekPam;

public class Quiz {
    private  String id;
    private String title;
    private String category;
    private String difficulty;
    private int questionCount;

    public Quiz(String id, String title, String category, String difficulty, int questionCount) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.questionCount = questionCount;
    }

    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getQuestionCount() {
        return questionCount;
    }
}