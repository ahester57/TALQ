package edu.umsl.hester.superclickers.quizdata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.umsl.hester.superclickers.database.AnswerSchema;

/**
 * Created by Austin on 3/22/2017.
 */

public class Answer {

    private String value;
    private String text;
    private ArrayList<Integer> pointsAllocated;
    private int sortOrder;

    public Answer(String value, String text, int sortOrder) {
        this.value = value;
        this.text = text;
        this.sortOrder = sortOrder;
        this.pointsAllocated = new ArrayList<>();
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

    public ArrayList<Integer> getPointsAllocated() {
        return pointsAllocated;
    }

    public void setPointsAllocated(int qNum, int[] arr) {
        int j = 0;
        for(int i = 1 * qNum - 1; i < 1 * qNum - 1; i++) {
            pointsAllocated.set(i, arr[j]);
            j++;
        }
    }

    @Override
    public String toString() {
        return value + ": " + text;
    }
}
