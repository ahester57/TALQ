package edu.umsl.superclickers.activity.quiz;

import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.home.HomeActivity;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.Quiz;

/**
 * Created by Austin on 4/22/2017.
 */

public class QuizActivityGroup extends AppCompatActivity implements
        QuizViewGroup.QuizController {

    private final String TAG = getClass().getSimpleName();

    private Intent timerService;
    private QuizGET quizGET;
    private QuizViewGroup quizViewGroup;
    private SessionManager session;

    // BroadcastReceiver for QuizService
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUITimer(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Intent intent = getIntent();
        String quizID = intent.getStringExtra("QUIZ_ID");
        String userID = intent.getStringExtra("USER_ID");
        String courseID = intent.getStringExtra("COURSE_ID");

        // Session manager
        session = new SessionManager(getApplicationContext());
        timerService = new Intent(getBaseContext(), QuizService.class);

        FragmentManager fm = getFragmentManager();
        // Check if quizGET exists
        if (fm.findFragmentByTag(FragmentConfig.KEY_QUIZ_GET) != null) {
            quizGET = (QuizGET) fm.findFragmentByTag(FragmentConfig.KEY_QUIZ_GET);
        } else {
            quizGET = new QuizGET();
            fm.beginTransaction()
                    .add(quizGET, FragmentConfig.KEY_QUIZ_GET)
                    .commit();
        }
        // Check if fragment exists
        if (fm.findFragmentByTag(FragmentConfig.KEY_QUIZ_VIEW_GROUP) != null) {
            quizViewGroup = (QuizViewGroup) fm.findFragmentByTag(FragmentConfig.KEY_QUIZ_VIEW_GROUP);
        } else {
            // Add new quizFragment and reload quiz
            quizViewGroup = new QuizViewGroup();
            fm.beginTransaction()
                    .add(R.id.quiz_container, quizViewGroup, FragmentConfig.KEY_QUIZ_VIEW_GROUP)
                    .commit();
            // Only try to reload quiz when fragment is not loaded
            reloadQuiz(quizID, userID, courseID);
        }

    }

    @Override
    public void submitQuiz(Quiz quiz) {
        // @TODO POST quiz for grading

        Toast.makeText(getApplicationContext(), "Quiz Submitted", Toast.LENGTH_LONG).show();
        stopService(timerService);
        session.clearDatabase();
        Intent quizIntent = new Intent(QuizActivityGroup.this, HomeActivity.class);
        startActivity(quizIntent);
        finish();
    }

    void reloadQuiz(String quizID, String userID, String courseID) {
        if (!session.isQuizRunning()) {
            // Load quiz from internet
            //@TODO fix when quiz is over and started again
            quizViewGroup.setQuizInfo(quizID, userID, courseID);
            quizViewGroup.setResume(false);
            session.setActiveQuiz(quizID);
        } else {
            // get quiz from SQLite
            quizViewGroup.setQuizInfo(quizID, userID, courseID);
            quizViewGroup.setResume(true);
            quizViewGroup.attachQuiz(getActiveQuiz(), getQuizIndex());
        }
    }

    @Override
    public void startQuizTimer() {
        if (!isTimerRunning(QuizService.class)) {
            timerService.putExtra("QUIZ_TIME", getQuizTime());
            timerService.putExtra("QUIZ_ID", getQuizID());
            startService(timerService);
        }
    }

    @Override
    public void setQuizIndex(int qNum) {
        session.setQuizIndex(qNum);
    }

    @Override
    public int getQuizIndex() {
        return session.getQuizIndex();
    }

    Quiz getQuiz(String quizId) {
        return session.getQuiz(quizId);
    }

    Quiz getActiveQuiz() {
        return session.getActiveQuiz();
    }

    public int getQuizTime() {
        return quizViewGroup.getQuizTime();
    }

    public String getQuizID() { return quizViewGroup.getQuizID(); }

    @Override
    public QuizGET getQuizGET() {
        return quizGET;
    }

    void updateGUITimer(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 4);
            int secondsLeft = (int) millisUntilFinished / 1000 - 1;
            int minutesLeft = secondsLeft / 60;
            secondsLeft = secondsLeft % 60;
            quizViewGroup.updateGUITimer(minutesLeft, secondsLeft);
            if (millisUntilFinished < 2000) {
                submitQuiz(quizViewGroup.getCurQuiz());
            }
        }
    }

    private boolean isTimerRunning(Class<?> serviceClass) {
        ActivityManager actMan = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : actMan.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d(TAG, "Timer running ");
                return true;
            }
        }
        Log.d(TAG, "Timer NOT running ");
        return false;
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt("TIME_LEFT", )
    }

}