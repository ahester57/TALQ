package edu.umsl.hester.superclickers.activity.login;

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

import edu.umsl.hester.superclickers.R;
import edu.umsl.hester.superclickers.activity.quiz.QuizActivity;
import edu.umsl.hester.superclickers.app.SessionManager;

/**
 * Created by Austin on 2/4/2017.
 *
 */

public class RegisterActivity extends AppCompatActivity implements
        View.OnClickListener, RegisterController.RegisterListener {


    private static final String TAG = RegisterActivity.class.getSimpleName();

    private EditText inName;
    private EditText inEmail;
    private EditText inPassword;
    private EditText inConfirm;

    private RegisterController rController;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inName = (EditText) findViewById(R.id.reg_name_text_edit);
        inEmail = (EditText) findViewById(R.id.reg_email_text_edit);
        inPassword = (EditText) findViewById(R.id.reg_pwd_edit);
        inConfirm = (EditText) findViewById(R.id.reg_pwd_confirm);
        Button btnBack = (Button) findViewById(R.id.reg_back_button);
        Button btnRegister = (Button) findViewById(R.id.register_button);

        SessionManager session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            Intent intent = new Intent(RegisterActivity.this, QuizActivity.class);
            startActivity(intent);
            finish();
        }

        rController = new RegisterController();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(rController, "REG_CONTROLLER")
                .commit();

        btnBack.setOnClickListener(this);
        btnRegister.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.register_button:
                String name = inName.getText().toString().trim();
                String email = inEmail.getText().toString().trim();
                String password = inPassword.getText().toString().trim();
                String confirm = inConfirm.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    if (password.equals(confirm)) {
                        rController.registerUser(name, email, password);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Ya done goofed. Passwords don't match.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Enter your credentials now.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.reg_back_button:
                onBackPressed();
                break;
        }
    }


    @Override
    public void goToLogin() {
        // Go to Quiz
        Intent quizIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(quizIntent);
        finish();
    }
}
