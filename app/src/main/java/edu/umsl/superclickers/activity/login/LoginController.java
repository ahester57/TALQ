package edu.umsl.superclickers.activity.login;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
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
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.database.SQLiteHandlerUsers;

/**
 * Created by Austin on 3/21/2017.
 */

public class LoginController extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private Context lContext;

    private ProgressDialog pDialog; //////
    private SessionManager session;
    private SQLiteHandlerUsers db;

    private LoginListener lDelegate;

    interface LoginListener {
        void goToHome();
    }

    public void setContext(Context context) {
        lContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lDelegate = (LoginListener) getActivity();

        // progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        // SQL handler
        db = new SQLiteHandlerUsers(getActivity());
        // Session manager
        session = new SessionManager(getActivity());
    }

    // verify login credentials
    void checkLogin(final String email, final String password) {
        String tag_str_req = "req_login";

        pDialog.setMessage("Loggin in...");
        showDialog();

        // new string request
        StringRequest strReq = new StringRequest(Request.Method.POST, LoginConfig.URL_LOGIN,
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
                                // create session
                                session.setLogin(true);

                                //store user
                                String uid = jObj.getString("uid");

                                JSONObject user = jObj.getJSONObject("user");
                                String name = user.getString("name");
                                String email = user.getString("email");
                                String created_at = user.getString("created_at");

                                // add user to sql database
                                db.addUser(name, email, uid, created_at);

                                lDelegate.goToHome();


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
                params.put("email", email);
                params.put("password", password);

                return params;
            }
        };
        // end string request.. phew!



        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }
    ////////////////
    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
