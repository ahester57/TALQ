package edu.umsl.hester.superclickers.Model;

import java.util.ArrayList;


public class Question {

    private String question;
    private ArrayList<String> choices;
    private String answer;

    Question() {
        question = "What is 10 * log_10 (1000)?";
        choices = new ArrayList<>();

        choices.add("10");
        choices.add("20");
        choices.add("30");
        choices.add("100");

        answer = "30";



        // later, we will get this information from a server somewhere

    }

    Question(String s) {
        question = s;
        choices = new ArrayList<>();
        choices.add("its");
        choices.add("over");
        choices.add("good");
        choices.add("luck");

        answer = "over";
    }

    public boolean check(String guess) {
        return answer.equalsIgnoreCase(guess);
    }


    public String getQuestion() { return question; }

    //ArrayList<String> getChoices() { return choices; }

    public String getA() {
        return choices.get(0);
    }

    public String getB() {
        return choices.get(1);
    }

    public String getC() {
        return choices.get(2);
    }

    public String getD() {
        return choices.get(3);
    }

    String getAnswer() { return answer; }
}
