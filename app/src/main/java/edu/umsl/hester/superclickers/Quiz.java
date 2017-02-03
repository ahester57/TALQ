package edu.umsl.hester.superclickers;

import java.util.ArrayList;


class Quiz {

    private ArrayList<Question> questions;

    private int qNum;

    Quiz() {
        questions = new ArrayList<>();
        questions.add(new Question());
        qNum = 0;
    }

    Question getNextQuestion() {

        if (qNum <= questions.size() - 1) {
            Question q = questions.get(qNum);
            qNum++;
            return q;
        } else
            return new Question("clear");

    }
}
