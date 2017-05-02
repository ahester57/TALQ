package edu.umsl.superclickers.userdata;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Austin on 5/1/2017.
 */

public class SimpleUser {

    private String userId;
    private String email;
    private String first;
    private String last;

    public SimpleUser(JSONObject uObj) {
        try {
            this.userId = uObj.getString("userID");
            this.email = uObj.getString("email");
            this.first = uObj.getString("first");
            this.last = uObj.getString("last");

        } catch (JSONException e) {
            Log.e("JSONERROR", e.getMessage());
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }
}
