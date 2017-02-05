package edu.umsl.hester.superclickers.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

import edu.umsl.hester.superclickers.R;
import edu.umsl.hester.superclickers.helper.SQLiteHandler;
import edu.umsl.hester.superclickers.helper.SessionManager;

/**
 * Created by Austin on 2/4/2017.
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textName, textEmail;
    private Button btnLogout, btnPlay;

    private SQLiteHandler db;
    private SessionManager session;

    private String name, email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        textName = (TextView) findViewById(R.id.textName);
        textEmail = (TextView) findViewById(R.id.textEmail);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnPlay = (Button) findViewById(R.id.btnPlay);

        // database
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetch user info from sqlite
        HashMap<String, String> user = db.getUserDetails();
        name = user.get("name");
        email = user.get("email");

        textName.setText(name);
        textEmail.setText(email);

        btnLogout.setOnClickListener(this);
        btnPlay.setOnClickListener(this);

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
        }
    }

    @Override
    public void onBackPressed() {
        logoutUser();
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
