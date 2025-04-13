package com.example.projekPam;

public class Question {
    private String question;
    private String type;
    private String answer;

    public Question(String question, String type, String answer) {
        this.question = question;
        this.type = type;
        this.answer = answer;
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
}