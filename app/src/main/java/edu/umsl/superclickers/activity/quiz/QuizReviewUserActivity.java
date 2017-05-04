package edu.umsl.superclickers.activity.quiz;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.waitingroom.WaitingRoomActivity;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.Quiz;

/**
 * Created by Austin on 5/3/2017.
 */

public class QuizReviewUserActivity extends AppCompatActivity {

    private static final String TAG = QuizReviewUserActivity.class.getSimpleName();

    private String quizID;
    private String userID;
    private String courseID;
    private String groupID;
    private Quiz curQuiz;

    private SessionManager session;
    private Intent timerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        quizID = intent.getStringExtra("QUIZ_ID");
        userID = intent.getStringExtra("USER_ID");
        courseID = intent.getStringExtra("COURSE_ID");
        groupID = intent.getStringExtra("GROUP_ID");

        // Session manager
        session = new SessionManager(getBaseContext());
        timerService = new Intent(getBaseContext(), QuizService.class);

        curQuiz = session.getActiveQuiz();

//        FragmentManager fm = getFragmentManager();
//        // Check if quizGET exists
//        if (fm.findFragmentByTag(FragmentConfig.KEY_QUIZ_GET) != null) {
//            quizGET = (QuizGET) fm.findFragmentByTag(FragmentConfig.KEY_QUIZ_GET);
//        } else {
//            quizGET = new QuizGET();
//            fm.beginTransaction()
//                    .add(quizGET, FragmentConfig.KEY_QUIZ_GET)
//                    .commit();
//        }
//        // Check if fragment exists
//        if (fm.findFragmentByTag(FragmentConfig.KEY_QUIZ_VIEW_USER) != null) {
//            quizViewUser = (QuizViewUser) fm.findFragmentByTag(FragmentConfig.KEY_QUIZ_VIEW_USER);
//        } else {
//            // Add new quizFragment and reload quiz
//            quizViewUser = new QuizViewUser();
//            fm.beginTransaction()
//                    .add(R.id.quiz_container, quizViewUser, FragmentConfig.KEY_QUIZ_VIEW_USER)
//                    .commit();
//
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.action_submit_quiz:
                submitQuiz();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //@Override
    public void submitQuiz() {

        stopService(timerService);

        Intent quizIntent = new Intent(QuizReviewUserActivity.this, WaitingRoomActivity.class);
        quizIntent.putExtra("QUIZ_ID", quizID);
        quizIntent.putExtra("COURSE_ID", courseID);
        quizIntent.putExtra("USER_ID", userID);
        quizIntent.putExtra("GROUP_ID", groupID);
        setResult(Activity.RESULT_OK);
        startActivity(quizIntent);

        finish();

    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }
}
