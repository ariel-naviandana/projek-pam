package com.example.projekPam;

public class Quiz {
    private String title;
    private String category;
    private String difficulty;
    private int questionCount;

    public Quiz(String title, String category, String difficulty, int questionCount) {
        this.title = title;
        this.category = category;
        this.difficulty = difficulty;
        this.questionCount = questionCount;
    }

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