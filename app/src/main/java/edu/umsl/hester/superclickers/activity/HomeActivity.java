package edu.umsl.hester.superclickers.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

import edu.umsl.hester.superclickers.R;
import edu.umsl.hester.superclickers.app.AppController;
import edu.umsl.hester.superclickers.app.LoginConfig;
import edu.umsl.hester.superclickers.app.User;
import edu.umsl.hester.superclickers.helper.UserSQLiteHandler;
import edu.umsl.hester.superclickers.helper.SessionManager;

/**
 * Created by Austin on 2/4/2017.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = HomeActivity.class.getSimpleName();

    private TextView textName, textEmail;
    private Button btnLogout, btnPlay, btnCreateGroup;

    private ProgressDialog pDialog;
    private UserSQLiteHandler db;
    private SessionManager session;


    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textName = (TextView) findViewById(R.id.textName);
        textEmail = (TextView) findViewById(R.id.textEmail);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnCreateGroup = (Button) findViewById(R.id.btnMakeGroup);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // database
        db = new UserSQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetch user info from sqlite

        HashMap<String, String> userDetails = db.getUserDetails();
        this.user = new User(userDetails.get("name"), userDetails.get("email"));

        textName.setText(user.getName());
        textEmail.setText(user.getEmail());

        btnLogout.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnCreateGroup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnLogout:
                logoutUser();
                break;
            case R.id.btnPlay:
                Intent i = new Intent(HomeActivity.this, QuizActivity.class);
                startActivity(i);
                //finish();
                break;
            case R.id.btnMakeGroup:
                createGroup("best group");
                break;
        }
    }

    // Store new group, uploads to group URL
    private void createGroup(final String name) {
        String tag_str_req = "req_create_group";

        pDialog.setMessage("Makin group...");
        showDialog();
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.POST, LoginConfig.URL_CREATE_GROUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Group make Response: " + response.toString());
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

                                Toast.makeText(getApplicationContext(), "Group registered", Toast.LENGTH_LONG).show();



                            } else {
                                // Error loggin in
                                String errMessage = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(), errMessage,
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "JSON error: "
                                    + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Create group error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // put params to login url via POST
                Map<String, String> params = new HashMap<String, String>();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //logoutUser();
    }

    // logout user,
    private void logoutUser() {
        session.setLogin(false);

        db.deleteAllUsers();
        // Go to login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
