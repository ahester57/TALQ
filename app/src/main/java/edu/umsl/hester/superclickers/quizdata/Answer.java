package edu.umsl.hester.superclickers.quizdata;

/**
 * Created by Austin on 3/22/2017.
 */

public class Answer {

    private String id;
    private String value;
    private String text;
    private int sortOrder;

    public Answer(String id, String value, String text, int sortOrder) {
        this.id = id;
        this.value = value;
        this.text = text;
        this.sortOrder = sortOrder;
    }

}
