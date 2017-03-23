package edu.umsl.hester.superclickers.quizdata;

import java.io.Serializable;
import java.util.ArrayList;


public class Quiz implements Serializable{

    private String id;
    private String description;
    private String text;
    private String availableDate;
    private String expiryDate;
    private ArrayList<Question> questions;

    private int qNum;


    public Quiz(String id, String description, String text, String availableDate, String expiryDate, ArrayList<Question> questions, int qNum) {
        this.id = id;
        this.description = description;
        this.text = text;
        this.availableDate = availableDate;
        this.expiryDate = expiryDate;
        this.questions = questions;
        this.qNum = qNum;
    }


    public Question getNextQuestion() {

        Question q = questions.get(qNum);
        qNum = (qNum + 1) % questions.size();
        return q;


    }
}
