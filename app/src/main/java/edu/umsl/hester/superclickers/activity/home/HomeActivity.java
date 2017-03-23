package edu.umsl.hester.superclickers.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.umsl.hester.superclickers.R;
import edu.umsl.hester.superclickers.activity.quiz.QuizActivity;
import edu.umsl.hester.superclickers.activity.login.LoginActivity;
import edu.umsl.hester.superclickers.userdata.User;
import edu.umsl.hester.superclickers.database.SQLiteHandler;
import edu.umsl.hester.superclickers.app.SessionManager;

/**
 * Created by Austin on 2/4/2017.
 *
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = HomeActivity.class.getSimpleName();
    private String quizID;

    private Spinner quiz_select_spinner;
    private SQLiteHandler db;
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
        quiz_select_spinner = (Spinner) findViewById(R.id.quiz_select_spinner);

        // database
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            //logoutUser();
            btnCreateGroup.setEnabled(false);
        } else {
            // Fetch user info from sqlite
            HashMap<String, String> userDetails = db.getUserDetails();
            this.user = new User(userDetails.get("name"), userDetails.get("email"), userDetails.get("uid"));
            textName.setText(user.getName());
            textEmail.setText(user.getEmail());
        }


        setQuizSpinner();


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
                i.putExtra("QUIZ_ID", quizID);
                startActivity(i);
                //finish();
                break;
            case R.id.btnGroups:
                Intent in = new Intent(HomeActivity.this, GroupActivity.class);
                startActivity(in);
                break;
        }
    }

    void setQuizSpinner() {
        List<String> quizzes = new ArrayList<>();
        quizzes.add("Danger Quiz");
        quizzes.add("Superhero Quiz");
        quizzes.add("Coffee Quiz");
        quizzes.add("Common Sense Quiz");
        ArrayAdapter<String> quizAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, quizzes);
        quizAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        quiz_select_spinner.setAdapter(quizAdapter);
        quiz_select_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String col = parent.getItemAtPosition(position).toString();
                setActiveQuiz(col);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setActiveQuiz("Danger Quiz");
            }
        });
    }

    void setActiveQuiz(String quiz) {
        switch (quiz) {
            case "Danger Quiz":
                quizID = "8e21fdc6-2a2a-4023-9a32-6313b3e142b1";
                break;
            case "Superhero Quiz":
                quizID = "0784ae31-8aa4-43c1-9dca-df33a2d5053e";
                break;
            case "Coffee Quiz":
                quizID = "e74f5367-022e-4b7b-8891-d86b9c1cc0a8";
                break;
            case "Common Sense Quiz":
                quizID = "ad8f435e-e3b6-49e1-b161-63326007bc30";
                break;
            default:
                quizID = "8e21fdc6-2a2a-4023-9a32-6313b3e142b1";

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
