package edu.umsl.hester.superclickers.quizdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.umsl.hester.superclickers.database.AnswerSchema;
import edu.umsl.hester.superclickers.database.QuestionSchema;

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
        this.availableAnswers = availableAnswers;
    }

    public Question(JSONObject qObj) throws JSONException {
        try {
            String qid = qObj.getString(QuestionSchema.KEY_ID);
            String qtitle = qObj.getString(QuestionSchema.KEY_TITLE);
            String qtext = qObj.getString(QuestionSchema.KEY_TEXT);
            int points = qObj.getInt(QuestionSchema.KEY_POINTS_POSS);
            JSONArray aArray = qObj.getJSONArray(QuestionSchema.KEY_AVAIL_ANSWERS);

            ArrayList<Answer> answers = new ArrayList<>();
            int j = 0;
            while (j < aArray.length()) {
                JSONObject aObj = aArray.getJSONObject(j);

                answers.add(new Answer(aObj));
                j++;
            }

            this.id = qid;
            this.title = qtitle;
            this.text = qtext;
            this.pointsPossible = points;
            this.availableAnswers = answers;

        } catch (JSONException e) {
            throw new JSONException(e.getMessage());
        }
    }

    public String getPointsPossible() {
        return String.valueOf(pointsPossible);
    }
    public void setPointsPossible(int newPoints) {
        this.pointsPossible = newPoints;
    }
    public String getQuestion() {
        return text;
    }

    public Answer getA() {
        try {
            return availableAnswers.get(0);
        } catch (IndexOutOfBoundsException e) {
            return new Answer(null, "A", "", 0);
        }
    }

    public Answer getB() {
        try {
            return availableAnswers.get(1);
        } catch (IndexOutOfBoundsException e) {
            return new Answer(null, "B", "", 1);
        }
    }

    public Answer getC() {
        try {
            return availableAnswers.get(2);
        } catch (IndexOutOfBoundsException e) {
            return new Answer(null, "C", "", 2);
        }
    }

    public Answer getD() {
        try {
            return availableAnswers.get(3);
        } catch (IndexOutOfBoundsException e) {
            return new Answer(null, "D", "", 3);
        }
    }

    String getAnswer() {
        return "yo";//answer;
    }
}
