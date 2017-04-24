package edu.umsl.superclickers.quizdata;

import org.json.JSONException;
import org.json.JSONObject;

import edu.umsl.superclickers.database.schema.QuizSchema;

/**
 * Created by Austin on 4/23/2017.
 */

public class QuizListItem {

    private String _id;
    private String description;
    private String text;
    private String availableDate;
    private String expiryDate;
    private boolean timed;
    private int timedLength;

    public QuizListItem(JSONObject jObj) throws JSONException {
        try {

            String id = jObj.getString(QuizSchema.KEY_QID);
            String desc = jObj.getString(QuizSchema.KEY_DESC);
            String text = jObj.getString(QuizSchema.KEY_TEXT);
            String avail = jObj.getString(QuizSchema.KEY_AVAIL_DATE);
            String exp = jObj.getString(QuizSchema.KEY_EXPIRY_DATE);
            boolean timed = jObj.getBoolean(QuizSchema.KEY_TIMED);
            int length = jObj.getInt(QuizSchema.KEY_LENGTH);




            this._id = id;
            this.description = desc;
            this.text = text;
            this.availableDate = avail;
            this.expiryDate = exp;

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

    public boolean getTimed() { return timed; }

    public int getTimedLength() { return timedLength; }
}
