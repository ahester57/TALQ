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
import edu.umsl.hester.superclickers.app.User;
import edu.umsl.hester.superclickers.helper.SQLiteHandler;
import edu.umsl.hester.superclickers.helper.SessionManager;

/**
 * Created by Austin on 2/4/2017.
 *
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = HomeActivity.class.getSimpleName();

    private SessionManager session;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView textName = (TextView) findViewById(R.id.textName);
        TextView textEmail = (TextView) findViewById(R.id.textEmail);
        Button btnLogout = (Button) findViewById(R.id.btnLogout);
        Button btnPlay = (Button) findViewById(R.id.btnPlay);
        Button btnCreateGroup = (Button) findViewById(R.id.btnGroups);

        // database
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            //logoutUser();
            btnCreateGroup.setEnabled(false);
        }

        // Fetch user info from sqlite

        HashMap<String, String> userDetails = db.getUserDetails();
        this.user = new User(userDetails.get("name"), userDetails.get("email"), userDetails.get("uid"));

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
            case R.id.btnGroups:
                Intent in = new Intent(HomeActivity.this, GroupActivity.class);
                startActivity(in);
                break;
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

        //db.deleteAllUsers();
        // Go to login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
