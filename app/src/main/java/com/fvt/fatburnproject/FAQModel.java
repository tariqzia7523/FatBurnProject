package com.fvt.fatburnproject;

import java.io.Serializable;

public class FAQModel implements Serializable {
    String id;
    String question;
    String answer;

    public FAQModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
