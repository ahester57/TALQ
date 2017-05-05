package edu.umsl.superclickers.activity.login;


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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.umsl.superclickers.app.AppController;
import edu.umsl.superclickers.app.LoginConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.userdata.Course;
import edu.umsl.superclickers.userdata.User;

/**
 * Created by Austin on 3/21/2017.
 */

public class LoginController extends Fragment {

    private final String TAG = LoginController.class.getSimpleName();

    private ProgressDialog pDialog; //////
    private SessionManager session;

    private LoginListener lListener;

    interface LoginListener {
        void goToHome();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Login activity listener
        lListener = (LoginListener) getActivity();
        // progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        // Session manager
        session = new SessionManager(getActivity());
    }

    // check login credentials
    void checkLogin(final String ssoId, final String password) {
        String tag_str_req = "req_login";
        showDialog();
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.POST,
                LoginConfig.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Login Response: " + response);
                        hideDialog();
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");
                            if (!error) {
                                // LOGIN SUCCESSFUL
                                JSONObject user = jObj.getJSONObject("user");
                                String ssoId = user.getString("email");
                                verifyUser(ssoId);
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
                Log.e(TAG, "Login error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // put params to login url via POST
                Map<String, String> params = new HashMap<>();
                params.put("email", ssoId);
                params.put("password", password);

                return params;
            }
        };
        // end string request.. phew!
        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    // verifies if valid user and inserts into database
    void verifyUser(final String user_id) {
        String tag_str_req = "req_verify_user";
        String uri = String.format(LoginConfig.URL_USER_BY_SSO, user_id);
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
                                // user was found
                                // Extract user details
                                String _id = jObj.getString("_id");
                                String userID = jObj.getString("userID");
                                String email = jObj.getString("email");
                                String first = jObj.getString("first");
                                String last = jObj.getString("last");

                                JSONArray courseArr = jObj
                                        .getJSONArray("enrolledCourses");
                                // Handle enrolled courses
                                for (int i = 0; i < courseArr.length(); i++) {
                                    JSONObject courseObj = courseArr
                                            .getJSONObject(i);
                                    session.addCourseToDB(new Course(courseObj));
                                }
                                // create session
                                session.addUserToDB(new User(first, last,
                                            userID, email, _id));
                                session.setLogin(true);
                                lListener.goToHome();
                            } else {
                                // Error
                                String errMessage = "Oops! User not found.";
                                Toast.makeText(getActivity(), errMessage,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "JSON error: " + e.getMessage());
                            Toast.makeText(getActivity(), "Oops! User not found.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Quiz error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
        // end string request.. phew!
        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    private void showDialog() {
        if (pDialog != null && !pDialog.isShowing()) {
            pDialog.setMessage("Logging in...");
            pDialog.show();
        }
    }
    ////////////////
    private void hideDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
