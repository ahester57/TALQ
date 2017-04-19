package edu.umsl.superclickers.activity.login;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.home.HomeActivity;
import edu.umsl.superclickers.app.SessionManager;


/**
 * Created by Austin on 2/2/2017.
 *
 */

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener, LoginController.LoginListener {


    private final String TAG = LoginActivity.class.getSimpleName();

    private EditText ssoId;
    private EditText userPass;
    private Button login;
    private Button register;
    private Button skip;

    private LoginController lController;
    private SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.login_button);
        register = (Button) findViewById(R.id.new_user_button);
        skip = (Button) findViewById(R.id.skip_login_button);
        ssoId = (EditText) findViewById(R.id.sso_text_edit);
        userPass = (EditText) findViewById(R.id.pwd_text_edit);


        // Session manager
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            // user logged in
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }


        lController = new LoginController();
        lController.setContext(getApplicationContext());

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        ft.add(lController, "LOGIN_CONTROLLER");
        ft.commit();

        userPass.setOnClickListener(this);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
        skip.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.login_button:
                tryLogin();
                break;
            case R.id.pwd_text_edit:
                tryLogin();
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

    private void tryLogin() {
        String email = ssoId.getText().toString().trim();
        String password = userPass.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            //login
            lController.checkLogin(email, password);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Enter some credentials now.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void goToHome() {
        // Go to Quiz
        Intent quizIntent = new Intent(LoginActivity.this, HomeActivity.class);
        quizIntent.putExtra("USER_NAME", ssoId.getText().toString());
        startActivity(quizIntent);
        finish();
    }





}
