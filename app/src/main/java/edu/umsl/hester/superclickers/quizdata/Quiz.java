package edu.umsl.hester.superclickers.quizdata;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import edu.umsl.hester.superclickers.database.QuizSchema;


public class Quiz implements Serializable {

    private String _id;
    private String description;
    private String text;
    private String availableDate;
    private String expiryDate;
    private ArrayList<Question> questions;

    private int qNum;

    public Quiz(String id, String description, String text, String availableDate, String expiryDate, ArrayList<Question> questions, int qNum) {
        this._id = id;
        this.description = description;
        this.text = text;
        this.availableDate = availableDate;
        this.expiryDate = expiryDate;
        this.questions = questions;
        this.qNum = qNum;
    }

    public Quiz(JSONObject jObj) throws JSONException{
        try {
            String id = jObj.getString(QuizSchema.KEY_QID);
            String desc = jObj.getString(QuizSchema.KEY_DESC);
            String text = jObj.getString(QuizSchema.KEY_TEXT);
            String avail = jObj.getString(QuizSchema.KEY_AVAIL_DATE);
            String exp = jObj.getString(QuizSchema.KEY_EXPIRY_DATE);
            JSONArray qArray = jObj.getJSONArray(QuizSchema.KEY_QUESTIONS);

            ArrayList<Question> questions = new ArrayList<>();
            int i = 0;
            while (i < qArray.length()) {
                JSONObject qObj = qArray.getJSONObject(i);

                questions.add(new Question(qObj));
                i++;
            }

            this._id = id;
            this.description = desc;
            this.text = text;
            this.availableDate = avail;
            this.expiryDate = exp;
            this.questions = questions;
            this.qNum = 0;

        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException(e.getMessage());
        }
    }

    public Question getQuestion() {
        Question q = questions.get(qNum);
        return q;
    }

    public Question getNextQuestion() {
        qNum = (qNum + 1) % questions.size();
        Question q = questions.get(qNum);
        return q;
    }

    public int getqNum() {
        return qNum;
    }

    public Question getPrevQuestion() {
        qNum = (qNum - 1) % questions.size();
        if(qNum < 0) {
            qNum += questions.size();
        }
        Question q = questions.get(qNum);
        return q;
    }
}
