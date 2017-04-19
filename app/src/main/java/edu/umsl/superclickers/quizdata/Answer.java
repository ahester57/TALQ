package edu.umsl.superclickers.quizdata;

import org.json.JSONException;
import org.json.JSONObject;

import edu.umsl.superclickers.database.AnswerSchema;

/**
 * Created by Austin on 3/22/2017.
 */

public class Answer {

    private String value;
    private String text;
    private int sortOrder;

    public Answer(String value, String text, int sortOrder) {
        this.value = value;
        this.text = text;
        this.sortOrder = sortOrder;
    }

    public Answer(JSONObject aObj) throws JSONException {
        try {
            String avalue = aObj.getString(AnswerSchema.KEY_VALUE);
            String atext = aObj.getString(AnswerSchema.KEY_TEXT);
            int sortOrder = aObj.getInt(AnswerSchema.KEY_SORT_ORDER);

            this.value = avalue;
            this.text = atext;
            this.sortOrder = sortOrder;

        } catch (JSONException e) {
            throw new JSONException(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return value + ": " + text;
    }

}
