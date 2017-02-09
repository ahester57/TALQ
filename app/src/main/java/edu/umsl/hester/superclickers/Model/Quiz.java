package edu.umsl.hester.superclickers.Model;

import java.io.Serializable;
import java.util.ArrayList;


public class Quiz implements Serializable{

    private ArrayList<Question> questions;

    private int qNum;

    public Quiz() {
        questions = new ArrayList<>();
        questions.add(new Question());
        questions.add(new Question("all clear"));
        qNum = 0;
    }

    public Question getNextQuestion() {

        Question q = questions.get(qNum);
        qNum = (qNum + 1) % questions.size();
        return q;


    }
}
