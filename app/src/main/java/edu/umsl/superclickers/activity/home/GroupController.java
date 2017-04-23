package edu.umsl.superclickers.activity.home;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.umsl.superclickers.app.AppController;
import edu.umsl.superclickers.app.LoginConfig;
import edu.umsl.superclickers.database.SQLiteHandlerUsers;

/**
 * Created by Austin on 3/21/2017.
 */

public class GroupController extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private ProgressDialog pDialog;
    private SQLiteHandlerUsers db;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // @TODO
        db = SQLiteHandlerUsers.sharedInstance(getActivity());
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
    }

    // Store new group, uploads to group URL
    void createGroup(final String userId, final String name) {
        String tag_str_req = "req_create_group";

        pDialog.setMessage("Makin group...");
        showDialog();
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.POST, LoginConfig.URL_CREATE_GROUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Group make Response: " + response);
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");

                            if (!error) {
                                // group create SUCCESSFUL
                                // store group in db

                                //store user
                                String guid = jObj.getString("guid");

                                JSONObject group = jObj.getJSONObject("group");
                                String name = group.getString("name");
                                String created_at = group.getString("created_at");

                                // add user to sql database
                                db.addGroup(name, guid, created_at);
                                joinGroup(userId, name);

                                Toast.makeText(getActivity(), "Group registered", Toast.LENGTH_LONG).show();



                            } else {
                                // Error loggin in
                                String errMessage = jObj.getString("error_msg");
                                Toast.makeText(getActivity(), errMessage,
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "JSON error: "
                                    + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Create group error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // put params to login url via POST
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("user_id", userId);

                return params;
            }
        };
        // end string request.. phew!

        Log.d(TAG, "we made it");

        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    // add user to group, uploads to group URL
    void joinGroup(final String userId, final String name) {
        String tag_str_req = "req_join_group";

        pDialog.setMessage("Joinin group...");
        showDialog();
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.POST, LoginConfig.URL_JOIN_GROUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Group add Response: " + response);
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");

                            if (!error) {
                                // group add SUCCESSFUL
                                // store group in db

                                //store user


                                JSONObject user_group = jObj.getJSONObject("user_group");
                                String guid = user_group.getString("group_id");
                                String created_at = user_group.getString("created_at");

                                db.addUserToGroup(userId, guid, created_at);

                                Toast.makeText(getActivity(), "Group joined", Toast.LENGTH_LONG).show();



                            } else {
                                // Error loggin in
                                String errMessage = jObj.getString("error_msg");
                                Toast.makeText(getActivity(), errMessage,
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "JSON error: "
                                    + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Join group error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // put params to login url via POST
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("name", name);

                return params;
            }
        };
        // end string request.. phew!

        Log.d(TAG, "we made it");

        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
