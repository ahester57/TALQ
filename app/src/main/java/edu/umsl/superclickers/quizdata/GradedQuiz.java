package edu.umsl.superclickers.quizdata;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Austin on 5/1/2017.
 *
 */

public class GradedQuiz {

    private String sessionId;
    private String userId;
    private String courseId;
    private String quizId;
    private int totalPoints;
    private ArrayList<GradedQuestion> gradedQuestions = new ArrayList<>();

    public GradedQuiz(JSONObject gradedQuizObj) throws JSONException {
        try {
            this.sessionId = gradedQuizObj.getString("sessionId");
            this.userId = gradedQuizObj.getString("user");
            this.courseId = gradedQuizObj.getString("course");
            this.quizId = gradedQuizObj.getString("quizId");
            JSONArray answers = gradedQuizObj.getJSONArray("answers");

            for (int i = 0; i < answers.length(); i++) {
                JSONObject jObj = answers.getJSONObject(i);
                gradedQuestions.add(new GradedQuestion(jObj));
            }

            this.totalPoints = calculateTotalPoints();
        } catch (JSONException e) {
            Log.e("JSONError", e.getMessage());
            throw e;
        }
    }

    public int calculateTotalPoints() {
        int points = 0;
        for (GradedQuestion ga : gradedQuestions) {
            points += ga.calcuatePoints();

        }
        return points;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getQuizId() {
        return quizId;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public ArrayList<GradedQuestion> getGradedQuestions() {
        return gradedQuestions;
    }
}
