package edu.umsl.hester.superclickers.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import edu.umsl.hester.superclickers.helper.SQLiteHandler;
import edu.umsl.hester.superclickers.helper.SessionManager;

/**
 * Created by Austin on 2/4/2017.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = RegisterActivity.class.getSimpleName();

    private Button btnRegister;
    private Button btnBack;
    private EditText inName;
    private EditText inEmail;
    private EditText inPassword;
    private EditText inConfrim;

    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inName = (EditText) findViewById(R.id.registerName);
        inEmail = (EditText) findViewById(R.id.registerEmail);
        inPassword = (EditText) findViewById(R.id.registerPassword);
        //inConfrim = (EditText) findViewById(R.id.registerConfirm);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());

        db = new SQLiteHandler(getApplicationContext());

        if (session.isLoggedIn()) {
            Intent intent = new Intent(RegisterActivity.this, QuizActivity.class);
            startActivity(intent);
            finish();
        }

        btnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnRegister:
                String name = inName.getText().toString().trim();
                String email = inEmail.getText().toString().trim();
                String password = inPassword.getText().toString().trim();
                //String confirm = inConfrim.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    //register
                    registerUser(name, email, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Enter your credentials now.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btnBack:
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
    }


    // Store new user, uploads to register URL
    private void registerUser(final String name, final String email, final String password) {
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
                                // store user in db

                                //store user
                                String uid = jObj.getString("uid");

                                JSONObject user = jObj.getJSONObject("user");
                                String name = user.getString("name");
                                String email = user.getString("email");
                                String created_at = user.getString("created_at");

                                // add user to sql database
                                db.addUser(name, email, uid, created_at);

                                Toast.makeText(getApplicationContext(), "User registered", Toast.LENGTH_LONG).show();

                                // Go to Quiz
                                Intent quizIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(quizIntent);
                                finish();


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
                Log.e(TAG, "Registration error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // put params to login url via POST
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

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

