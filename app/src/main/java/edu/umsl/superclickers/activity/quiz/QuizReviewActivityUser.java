package edu.umsl.superclickers.activity.quiz;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.helper.QuizService;
import edu.umsl.superclickers.activity.waitingroom.WaitingRoomActivity;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.Quiz;

/**
 * Created by Austin on 5/3/2017.
 */

public class QuizReviewActivityUser extends AppCompatActivity
        implements QuizReviewViewUser.ReviewListener {

    private static final String TAG = QuizReviewActivityUser.class.getSimpleName();

    private String quizID;
    private String userID;
    private String courseID;
    private String groupID;
    private Quiz curQuiz;

    private QuizReviewViewUser qReviewView;
    private SessionManager session;
    private Intent timerService;

    // BroadcastReceiver for QuizService
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleQuizTimer(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_quiz);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            quizID = intent.getStringExtra("QUIZ_ID");
            userID = intent.getStringExtra("USER_ID");
            courseID = intent.getStringExtra("COURSE_ID");
            groupID = intent.getStringExtra("GROUP_ID");
        }
        // Session manager
        session = new SessionManager(getBaseContext());
        timerService = new Intent(getBaseContext(), QuizService.class);

        curQuiz = session.getActiveQuiz();

        FragmentManager fm = getFragmentManager();

        // Check if fragment exists
        if (fm.findFragmentByTag(FragmentConfig.KEY_REVIEW_VIEW_USER) != null) {
            qReviewView = (QuizReviewViewUser) fm.findFragmentByTag(FragmentConfig.KEY_REVIEW_VIEW_USER);
        } else {
            // Add new quizFragment and reload quiz
            qReviewView = new QuizReviewViewUser();
            fm.beginTransaction()
                    .add(R.id.quiz_container, qReviewView, FragmentConfig.KEY_REVIEW_VIEW_USER)
                    .commit();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alt, menu);
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

    @Override
    public void submitQuiz() {

        stopService(timerService);

        Intent quizIntent = new Intent(QuizReviewActivityUser.this, WaitingRoomActivity.class);
        quizIntent.putExtra("QUIZ_ID", quizID);
        quizIntent.putExtra("COURSE_ID", courseID);
        quizIntent.putExtra("USER_ID", userID);
        quizIntent.putExtra("GROUP_ID", groupID);
        setResult(Activity.RESULT_OK);
        startActivity(quizIntent);

        finish();

    }

    void handleQuizTimer(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 4);

            if (millisUntilFinished < 2000) {
                submitQuiz();
            }
        }
    }

    @Override
    protected void onResume() {
        try {
            registerReceiver(br, new IntentFilter(QuizService.COUNTDOWN_BR));
            Log.d(TAG, "Registered broadcast reciever");
        } catch (Exception e) {
            // Receiver stopped onPause
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        try {
            unregisterReceiver(br);
            Log.d(TAG, "Unregistered broadcast reciever");
        } catch (Exception e) {
            // Receiver stopped onPause
        }
        super.onPause();
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
        Log.d(TAG, "Review Activity destoyed");
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
