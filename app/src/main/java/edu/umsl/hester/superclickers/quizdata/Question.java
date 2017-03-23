package edu.umsl.hester.superclickers.quizdata;

import java.util.ArrayList;

public class Question {

    private String id;
    private String title;
    private String text;
    private int pointsPossible;
    private ArrayList<Answer> availableAnswers;


    public Question(String id, String title, String text, int pointsPossible, ArrayList<Answer> availableAnswers) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.pointsPossible = pointsPossible;
        this.availableAnswers = availableAnswers; // sort by sortOrder
    }

    public boolean check(String guess) {
        return true; //answer.equalsIgnoreCase(guess);
    }


    public String getQuestion() { return text; }

    //ArrayList<String> getChoices() { return choices; }

    public Answer getA() {
        try {
            return availableAnswers.get(0);
        } catch (IndexOutOfBoundsException e) {
            return new Answer(null, "A: ", "", 0);
        }
    }

    public Answer getB() {
        try {
            return availableAnswers.get(1);
        } catch (IndexOutOfBoundsException e) {
            return new Answer(null, "B: ", "", 1);
        }
    }

    public Answer getC() {
        try {
            return availableAnswers.get(2);
        } catch (IndexOutOfBoundsException e) {
            return new Answer(null, "C: ", "", 2);
        }
    }

    public Answer getD() {
        try {
            return availableAnswers.get(3);
        } catch (IndexOutOfBoundsException e) {
            return new Answer(null, "D: ", "", 3);
        }
    }

    String getAnswer() {
        return "yo";//answer;
    }
}
