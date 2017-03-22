package edu.umsl.hester.superclickers.activity.login;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import edu.umsl.hester.superclickers.activity.home.HomeActivity;
import edu.umsl.hester.superclickers.app.AppController;
import edu.umsl.hester.superclickers.app.LoginConfig;
import edu.umsl.hester.superclickers.database.SQLiteHandler;
import edu.umsl.hester.superclickers.app.SessionManager;


/**
 * Created by Austin on 2/2/2017.
 *
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText userEmail;
    private EditText userPass;
    private Button login;
    private Button register;
    private Button skip;

    private LoginController lController;

    private ProgressDialog pDialog; //////
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login_button);
        register = (Button) findViewById(R.id.new_user_button);
        skip = (Button) findViewById(R.id.skip_login_button);
        userEmail = (EditText) findViewById(R.id.email_text_edit);
        userPass = (EditText) findViewById(R.id.pwd_text_edit);

        // progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQL handler
        db = new SQLiteHandler(getApplicationContext());
        // Session manager
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            // user logged in
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }

        LoginFragment loginFragment = new LoginFragment();
        lController = new LoginController();
        lController.setContext(getApplicationContext());

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(loginFragment, "LOGIN_FRAGMENT");
        ft.add(lController, "LOGIN_CONTROLLER");
        ft.commit();


        login.setOnClickListener(this);
        register.setOnClickListener(this);
        skip.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.login_button:
                String email = userEmail.getText().toString().trim();
                String password = userPass.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty()) {
                    //login
                    checkLogin(email, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Enter some credentials now.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.new_user_button:
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);

                break;
            case R.id.skip_login_button:
                Toast.makeText(getApplicationContext(), "Continuing without loggin in...",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

/////////////
    // verify login credentials
    private void checkLogin(final String email, final String password) {
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

                                // Go to Quiz
                                Intent quizIntent = new Intent(LoginActivity.this, HomeActivity.class);
                                quizIntent.putExtra("USER_NAME", userEmail.getText().toString());
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
                Log.e(TAG, "Login error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(),
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
//////////
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
