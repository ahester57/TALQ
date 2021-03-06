package edu.umsl.superclickers.quizdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.umsl.superclickers.database.schema.QuestionSchema;

public class Question {

    private String _id;
    private String title;
    private String text;
    private String sessionId;
    private int pointsPossible;
    private int maxPoints;
    private ArrayList<Answer> availableAnswers;

    public Question(String id, String title, String text, int pointsPossible, ArrayList<Answer> availableAnswers) {
        this._id = id;
        this.title = title;
        this.text = text;
        this.pointsPossible = pointsPossible;
        this.maxPoints = pointsPossible;
        this.availableAnswers = availableAnswers; // sort by sortOrder
    }

    public Question(JSONObject qObj, String sessionId) throws JSONException {
        try {
            String qid = qObj.getString(QuestionSchema.KEY_QUID);
            String qtitle = qObj.getString(QuestionSchema.KEY_TITLE);
            String qtext = qObj.getString(QuestionSchema.KEY_TEXT);
            int points = qObj.getInt(QuestionSchema.KEY_POINTS_POSS);
            JSONArray aArray = qObj.getJSONArray(QuestionSchema.KEY_AVAIL_ANSWERS);

            ArrayList<Answer> answers = new ArrayList<>();
            int j = 0;
            while (j < aArray.length()) {
                JSONObject aObj = aArray.getJSONObject(j);

                answers.add(new Answer(aObj, qid));
                j++;
            }

            this._id = qid;
            this.title = qtitle;
            this.text = qtext;
            this.pointsPossible = points;
            this.availableAnswers = answers;
            this.sessionId = sessionId;

        } catch (JSONException e) {
            throw new JSONException(e.getMessage());
        }

    }

    public int getPointsPossible() {
        return pointsPossible;
    }

    public int getMaxPoints() { return maxPoints; }

    public void setPointsPossible(int newPoints) {
        this.pointsPossible = newPoints;
    }

    public String getQuestion() { return text; }

    public ArrayList<Answer> getAnswers() {
        if (availableAnswers != null) {
            return availableAnswers;
        }
        return null;
    }

    public Answer getA() {
        try {
            return availableAnswers.get(0);
        } catch (IndexOutOfBoundsException e) {
            return new Answer("A", "", 0, _id);
        }
    }

    public Answer getB() {
        try {
            return availableAnswers.get(1);
        } catch (IndexOutOfBoundsException e) {
            return new Answer("B", "", 1, _id);
        }
    }

    public Answer getC() {
        try {
            return availableAnswers.get(2);
        } catch (IndexOutOfBoundsException e) {
            return new Answer("C", "", 2, _id);
        }
    }

    public Answer getD() {
        try {
            return availableAnswers.get(3);
        } catch (IndexOutOfBoundsException e) {
            return new Answer("D", "", 3, _id);
        }
    }

    public String get_id() {
        return _id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    String getAnswer() {
        return "yo";//answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
