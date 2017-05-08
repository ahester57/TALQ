package edu.umsl.superclickers.quizdata;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Austin on 5/1/2017.
 */

public class GradedAnswer {

    private String value;
    private int allocatedPoints;
    private String questionId;
    private boolean isCorrect;

    public GradedAnswer(JSONObject gradedObj, String questionId) throws JSONException {
        try {
            this.questionId = questionId;
            this.allocatedPoints = gradedObj.getInt("points");
            this.value = gradedObj.getString("value");
            this.isCorrect = gradedObj.getBoolean("isCorrect");

        } catch (JSONException e) {
            Log.e("JSONERror", e.getMessage());
            throw e;
        }
    }

    public String getValue() {
        return value;
    }

    public int getAllocatedPoints() { return allocatedPoints; }

    public String getQuestionId() {
        return questionId;
    }

    public boolean isCorrect() { return isCorrect; }
}
