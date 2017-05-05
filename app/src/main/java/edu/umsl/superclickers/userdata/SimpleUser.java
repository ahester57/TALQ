package edu.umsl.superclickers.userdata;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class SimpleUser {

    private String userId;
    private String email;
    private String first;
    private String last;

    public SimpleUser(String userId, String email, String first, String last) {
        this.userId = userId;
        this.email = email;
        this.first = first;
        this.last = last;
    }

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

    @Override
    public String toString() {
        return "SimpleUser{" +
                "userId='" + userId + '\'' +
                ", first='" + first + '\'' +
                ", last='" + last + '\'' +
                '}';
    }
}
