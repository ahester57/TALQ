package edu.umsl.superclickers.quizdata;


/**
 * Created by Austin on 4/24/2017.
 */

public class SelectedAnswer {

    private String value;
    private int allocatedPoints;
    private String questionId;

    public SelectedAnswer(String value, int allocatedPoints, String questionId) {
        this.value = value;
        this.allocatedPoints = allocatedPoints;
        this.questionId = questionId;
    }

    public void setAllocatedPoints(int points) {
        allocatedPoints = points;
    }

    public String getValue() {
        return value;
    }

    public int getAllocatedPoints() { return allocatedPoints; }

    public String getQuestionId() {
        return questionId;
    }
}
