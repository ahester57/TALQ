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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.umsl.superclickers.app.AppController;
import edu.umsl.superclickers.app.LoginConfig;

/**
 * Created by Austin on 3/21/2017.
 */

public class RegisterController extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private ProgressDialog pDialog;
    private RegisterListener rDelegate;

    interface RegisterListener {
        void goToLogin();
        void registerUser(final String name, final String ssoId, final String pwd);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        rDelegate = (RegisterListener) getActivity();
    }

    // Store new user, uploads to register URL
    void registerUser(final String name, final String userId, final String password) {
        String tag_str_req = "req_register";

        pDialog.setMessage("Registerin...");
        showDialog();
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.POST, LoginConfig.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Register Response: " + response);
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");

                            if (!error) {
                                // REGISTER SUCCESSFUL
                                // store user in remote db

                                Log.d(TAG, "User " + userId + " stored in remote database.");
                                Toast.makeText(getActivity(), "User registered", Toast.LENGTH_LONG).show();
                                rDelegate.goToLogin();

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
                Log.e(TAG, "Registration error: " + error.getMessage());
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
                params.put("email", userId);
                params.put("password", password);

                return params;
            }
        };
        // end string request.. phew!
        Log.d(TAG, "we made it");

        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    // checks if userId in class list
    // if found, registers user with given credentials
    void getUserDetails(final String userId, final String pwd) {
        String tag_str_req = "req_name";
        String uri = String.format(LoginConfig.URL_USER_BY_SSO, userId);
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
                                String name = jObj.getString("first") + " " + jObj.getString("last");
                                rDelegate.registerUser(name, userId, pwd);

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
