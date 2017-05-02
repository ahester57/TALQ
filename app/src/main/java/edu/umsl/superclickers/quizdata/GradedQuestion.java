package edu.umsl.superclickers.quizdata;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Austin on 5/1/2017.
 */

public class GradedQuestion {

    private String questionId;
    private ArrayList<GradedAnswer> gradedAnswers = new ArrayList<>();

    public GradedQuestion(JSONObject gradedObj) {
        try {
            this.questionId = gradedObj.getString("question");
            JSONArray answersArr = gradedObj.getJSONArray("submittedAnswers");

            for (int i = 0; i < answersArr.length(); i++) {
                JSONObject aObj = answersArr.getJSONObject(i);
                gradedAnswers.add(new GradedAnswer(aObj, questionId));
            }


        } catch (JSONException e) {
            Log.e("JSONERror", e.getMessage());
        }
    }

    public String getQuestionId() {
        return questionId;
    }

    public ArrayList<GradedAnswer> getGradedAnswers() { return gradedAnswers; }

    public int calcuatePoints() {
        int points = 0;
        for (GradedAnswer ga : gradedAnswers) {
            if (ga.isCorrect()) {
                points += ga.getAllocatedPoints();
            }
        }
        return points;
    }
}
