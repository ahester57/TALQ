package edu.umsl.superclickers.activity.home;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.login.LoginActivity;
import edu.umsl.superclickers.activity.quiz.QuizActivityUser;
import edu.umsl.superclickers.activity.quiz.QuizService;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.database.SQLiteHandlerUsers;
import edu.umsl.superclickers.database.UserSchema;
import edu.umsl.superclickers.quizdata.Quiz;
import edu.umsl.superclickers.userdata.User;

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
    private SQLiteHandlerUsers db;
    private SessionManager session;
    private HomeFragment hFragment;

    private ArrayList<String> quizzes;
    private HashMap<String, String> quizMap;

    private ArrayList<String> courseIds;

    // BroadcastReceiver for QuizService
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleQuizTimer(intent);
        }
    };

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
        db = new SQLiteHandlerUsers(getApplicationContext());
        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            //logoutUser();
            btnCreateGroup.setEnabled(false);
        } else {
            // Fetch user info from sqlite
            HashMap<String, String> userDetails = db.getUserDetails();

            textName.setText(userDetails.get(UserSchema.KEY_LAST) +
                    ", " + userDetails.get(UserSchema.KEY_FIRST));
            textEmail.setText(userDetails.get(UserSchema.KEY_USER_ID));
        }

        hFragment = new HomeFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(hFragment, "HOME_GET")
                .commit();

        hFragment.getQuizzesFor(userId);

        // @TODO download group info and put it SQLite

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
                Intent i = new Intent(HomeActivity.this, QuizActivityUser.class);
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
        // @TODO display running quizzes
    }

    void setQuizSpinner() {
        final List<String> spinQuizzes = this.quizzes;
        // @TODO switch spinner to recyclerView
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

    public void submitQuiz() {
        // @TODO POST quiz for grading
        Toast.makeText(getApplicationContext(), "Quiz Submitted", Toast.LENGTH_LONG).show();
        stopService(new Intent(getApplicationContext(), QuizService.class));
        session.clearDatabase();
    }

    void handleQuizTimer(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 4);

            if (millisUntilFinished < 2000) {
                submitQuiz();
            }
        }
    }

    // logout user,
    private void logoutUser() {
        session.setLogin(false);
        session.clearDatabase();

        db.deleteAllUsers();
        // Go to login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(QuizService.COUNTDOWN_BR));
        Log.d(TAG, "Registered broadcast reciever");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
        Log.d(TAG, "Unregistered broadcast reciever");

    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(br);
            Log.d(TAG, "Unregistered broadcast reciever");
        } catch (Exception e) {
            // Receiver stopped onPause
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "QUiz Activity destoyed");
        super.onDestroy();
    }
}
