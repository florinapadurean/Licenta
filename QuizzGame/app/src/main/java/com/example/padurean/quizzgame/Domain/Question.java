package com.example.padurean.quizzgame.Domain;

/**
 * Created by Asus on 30.03.2017.
 */

public class Question {
    private String badAnswer1;
    private String badAnswer2;
    private String badAnswer3;
    private String goodAnswer;
    private String question;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getGoodAnswer() {
        return goodAnswer;
    }

    public void setGoodAnswer(String goodAnswer) {
        this.goodAnswer = goodAnswer;
    }

    public String getBadAnswer1() {
        return badAnswer1;
    }

    public void setBadAnswer1(String badAnswer1) {
        this.badAnswer1 = badAnswer1;
    }

    public String getBadAnswer2() {
        return badAnswer2;
    }

    public void setBadAnswer2(String badAnswer2) {
        this.badAnswer2 = badAnswer2;
    }

    public String getBadAnswer3() {
        return badAnswer3;
    }

    public void setBadAnswer3(String badAnswer3) {
        this.badAnswer3 = badAnswer3;
    }

    public Question() {

    }

    public Question(String badAnswer1, String badAnswer2, String goodAnswer, String badAnswer3, String question) {
        this.badAnswer1 = badAnswer1;
        this.badAnswer2 = badAnswer2;
        this.goodAnswer = goodAnswer;
        this.badAnswer3 = badAnswer3;
        this.question = question;
    }

    @Override
    public String toString() {
        return "Question{" +
                "badAnswer1='" + badAnswer1 + '\'' +
                ", badAnswer2='" + badAnswer2 + '\'' +
                ", badAnswer3='" + badAnswer3 + '\'' +
                ", goodAnswer='" + goodAnswer + '\'' +
                ", question='" + question + '\'' +
                '}';
    }
}
