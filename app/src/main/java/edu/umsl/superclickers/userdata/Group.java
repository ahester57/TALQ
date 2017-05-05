package edu.umsl.superclickers.userdata;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Group {

    private String groupName;
    private String groupId;
    private ArrayList<String> courseIds = new ArrayList<>();
    private ArrayList<SimpleUser> users = new ArrayList<>();

    public Group(String groupName, String groupId) {
        this.groupName = groupName;
        this.groupId = groupId;
    }

    public Group(JSONObject gObj) {
        try {
            this.groupId = gObj.getString("_id");
            this.groupName = gObj.getString("name");
            JSONArray cArr = gObj.getJSONArray("courseIds");
            for (int i = 0; i < cArr.length(); i++) {
                courseIds.add(cArr.getString(i));
            }
            JSONArray uArr = gObj.getJSONArray("users");
            for (int i = 0; i < uArr.length(); i++) {
                JSONObject uObj = uArr.getJSONObject(i);
                users.add(new SimpleUser(uObj));
            }
        } catch (JSONException e) {
            Log.e("JsONError", e.getMessage());
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public ArrayList<String> getCourseIds() {
        return courseIds;
    }

    public ArrayList<SimpleUser> getUsers() {
        return users;
    }
}
