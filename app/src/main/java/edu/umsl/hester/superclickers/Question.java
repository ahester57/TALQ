package edu.umsl.hester.superclickers;

import java.util.ArrayList;


class Question {

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

    boolean check(String guess) {
        return answer.equalsIgnoreCase(guess);
    }


    String getQuestion() { return question; }

    //ArrayList<String> getChoices() { return choices; }

    String getA() {
        return choices.get(0);
    }

    String getB() {
        return choices.get(1);
    }

    String getC() {
        return choices.get(2);
    }

    String getD() {
        return choices.get(3);
    }

    String getAnswer() { return answer; }
}
