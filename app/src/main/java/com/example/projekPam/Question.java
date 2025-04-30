package com.example.projekPam;

import java.util.List;

public class Question {
    private String id;
    private String question;
    private String type;
    private String answer;
    private List<String> options;
    private String imageUrl;

    public Question(String id, String question, String type, String answer, List<String> options, String imageUrl) {
        this.id = id;
        this.question = question;
        this.type = type;
        this.answer = answer;
        this.options = options;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getType() {
        return type;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getImageUrl() { return imageUrl; }
}