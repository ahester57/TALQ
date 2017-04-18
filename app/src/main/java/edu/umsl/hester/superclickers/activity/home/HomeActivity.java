package edu.umsl.hester.superclickers.activity.home;

import android.app.FragmentManager;
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
import edu.umsl.hester.superclickers.activity.quiz.QuizGET;
import edu.umsl.hester.superclickers.userdata.User;
import edu.umsl.hester.superclickers.database.SQLiteHandler;
import edu.umsl.hester.superclickers.app.SessionManager;

/**
 * Created by Austin on 2/4/2017.
 *
 */

public class HomeActivity extends AppCompatActivity implements
        View.OnClickListener, HomeFragment.HomeController {

    private final String TAG = HomeActivity.class.getSimpleName();

    private String userId = "arh5w6";
    private String quizID;
    private String courseId;

    private Spinner quiz_select_spinner;
    private SQLiteHandler db;
    private SessionManager session;
    private HomeFragment hFragment;

    private ArrayList<String> quizzes;
    private HashMap<String, String> quizMap;

    private ArrayList<String> courseIds;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView textName = (TextView) findViewById(R.id.name_text_view);
        TextView textEmail = (TextView) findViewById(R.id.email_text_view);
        Button btnLogout = (Button) findViewById(R.id.logout_button);
        Button btnPlay = (Button) findViewById(R.id.play_button);
        Button btnCreateGroup = (Button) findViewById(R.id.groups_button);
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

        hFragment = new HomeFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(hFragment, "HOME_GET")
                .commit();

        hFragment.getQuizzesFor(userId);


        btnLogout.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnCreateGroup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.logout_button:
                logoutUser();
                break;
            case R.id.play_button:
                Intent i = new Intent(HomeActivity.this, QuizActivity.class);
                i.putExtra("QUIZ_ID", quizID);
                i.putExtra("USER_ID", userId);
                i.putExtra("COURSE_ID", courseId);
                startActivity(i);
                //finish();
                break;
            case R.id.groups_button:
                Intent in = new Intent(HomeActivity.this, GroupActivity.class);
                startActivity(in);
                break;
        }
    }

    @Override
    public void setQuizzes(ArrayList<String> quizzes, ArrayList<String> quizIds, ArrayList<String> courseIds) {
        quizMap = new HashMap<>();
        this.quizzes = quizzes;
        this.courseIds = courseIds;

        for (int i = 0; i < quizzes.size(); i++) {
            quizMap.put(quizzes.get(i), quizIds.get(i));
        }
        setQuizSpinner();
    }

    void setQuizSpinner() {
        final List<String> spinQuizzes = this.quizzes;


        ArrayAdapter<String> quizAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, spinQuizzes);
        quizAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        quiz_select_spinner.setAdapter(quizAdapter);
        quiz_select_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String col = parent.getItemAtPosition(position).toString();
                setActiveQuiz(col, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setActiveQuiz(spinQuizzes.get(0), 0);
            }
        });
    }

    void setActiveQuiz(String quiz, int pos) {

        quizID = quizMap.get(quiz);
        courseId = courseIds.get(pos);
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
