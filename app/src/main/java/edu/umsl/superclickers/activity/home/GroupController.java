package edu.umsl.superclickers.activity.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.umsl.superclickers.app.AppController;
import edu.umsl.superclickers.app.GroupConfig;
import edu.umsl.superclickers.userdata.Group;

/**
 * Created by Austin on 3/21/2017.
 */

public class GroupController extends Fragment {

    private final String TAG = GroupController.class.getSimpleName();

    private GroupListener gListener;

    interface GroupListener {
        void setGroup(Group group);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gListener = (GroupListener) getActivity();
    }

    // @TODO getGroupById, list all groups

    void getGroupForUser(final String user_id, final String courseId) {
        String tag_str_req = "req_group";
        String uri = String.format(GroupConfig.URL_GROUP_FOR_USER, user_id, courseId);
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.GET, uri,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, " Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);

                            String error;
                            try {
                                error = jObj.getString("error");
                            } catch (JSONException e) {
                                error = "false";
                            }

                            // if no errors
                            if (error.equals("false")) {
                                // Group was found
                                Group group = new Group(jObj);

                                gListener.setGroup(group);



                            } else {
                                // Error
                                String errMessage = "error";
                                Toast.makeText(getActivity(), errMessage,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "JSON error: "
                                    + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Group error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();

            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    void getGroupForCourse(final String courseId) {
        String tag_str_req = "req_group_courses";
        String uri = String.format(GroupConfig.URL_GROUPS_FOR_COURSE, courseId);
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.GET, uri,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, " Response: " + response);
                        try {
                            JSONArray jArr = new JSONArray(response);

                            String error;
                            try {
                                error = jArr.getJSONObject(2).getString("message");
                            } catch (JSONException e) {
                                error = "false";
                            }

                            // if no errors
                            if (error.equals("false")) {
                                // Course was found

                                for (int i = 0; i < jArr.length(); i++) {
                                    JSONObject gObj = jArr.getJSONObject(i);
                                    String guid = gObj.getString("_id");
                                    getGroupById(guid);
                                }

                            } else {
                                // Error
                                String errMessage = "error";
                                Toast.makeText(getActivity(), errMessage,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "JSON error: "
                                    + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Group error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();

            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    void getGroupById(final String groupId) {
        String tag_str_req = "req_group_id";
        String uri = String.format(GroupConfig.URL_GROUP_BY_ID, groupId);
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.GET, uri,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, " Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);

                            String error;
                            try {
                                error = jObj.getString("error");
                            } catch (JSONException e) {
                                error = "false";
                            }

                            // if no errors
                            if (error.equals("false")) {
                                // Group was found
                                Group group = new Group(jObj);

                                gListener.setGroup(group);

                            } else {
                                // Error
                                String errMessage = "error";
                                Toast.makeText(getActivity(), errMessage,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "JSON error: "
                                    + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Group error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();

            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }


}
