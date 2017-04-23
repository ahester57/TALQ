package edu.umsl.superclickers.quizdata;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import edu.umsl.superclickers.database.schema.QuizSchema;


public class Quiz implements Serializable{

    private String _id;
    private String sessionId;
    private String description;
    private String text;
    private String availableDate;
    private String expiryDate;
    private ArrayList<Question> questions;
    private boolean timed;
    private int timedLength;

    private int qNum;


    public Quiz(String id, String description, String text, String availableDate, String expiryDate,
                ArrayList<Question> questions, String sessionId, boolean timed, int length) {
        this._id = id;
        this.description = description;
        this.text = text;
        this.availableDate = availableDate;
        this.expiryDate = expiryDate;
        this.questions = questions;
        this.sessionId = sessionId;
        this.qNum = 0;
        this.timed = timed;
        this.timedLength = length;
    }

    public Quiz(JSONObject jObj, String sessionId) throws JSONException{
        try {

            String id = jObj.getString(QuizSchema.KEY_QID);
            String desc = jObj.getString(QuizSchema.KEY_DESC);
            String text = jObj.getString(QuizSchema.KEY_TEXT);
            String avail = jObj.getString(QuizSchema.KEY_AVAIL_DATE);
            String exp = jObj.getString(QuizSchema.KEY_EXPIRY_DATE);
            boolean timed = jObj.getBoolean(QuizSchema.KEY_TIMED);
            int length = jObj.getInt(QuizSchema.KEY_LENGTH);
            JSONArray qArray = jObj.getJSONArray(QuizSchema.KEY_QUESTIONS);

            ArrayList<Question> questions = new ArrayList<>();
            int i = 0;
            while (i < qArray.length()) {
                JSONObject qObj = qArray.getJSONObject(i);

                questions.add(new Question(qObj, sessionId));
                i++;
            }

            this._id = id;
            this.description = desc;
            this.text = text;
            this.availableDate = avail;
            this.expiryDate = exp;
            this.questions = questions;
            this.sessionId = sessionId;
            this.qNum = 0;
            this.timed = timed;
            this.timedLength = length;

        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException(e.getMessage());
        }
    }

    public String get_id() {
        return _id;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getSessionId() { return sessionId; }

    public ArrayList<Question> getQuestions() {
        return questions;
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

    public void setqNum(int n) {
        qNum = n;
    }

    public int getqNum() {
        return qNum;
    }

    public Question getPrevQuestion() {
        qNum = (qNum - 1) % questions.size();
        if(qNum < 0) {
            qNum += questions.size(); // BUT -1 MOD 10 = 9!!!! stupid % not following rules of math
        }
        Question q = questions.get(qNum);
        return q;
    }

    public boolean getTimed() { return timed; }

    public int getTimedLength() { return timedLength; }

    @Override
    public String toString() {
        return "Quiz{" +
                "_id='" + _id + '\'' +
                ", description='" + description + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
