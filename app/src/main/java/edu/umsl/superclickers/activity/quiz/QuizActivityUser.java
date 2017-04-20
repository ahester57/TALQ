package edu.umsl.superclickers.activity.quiz;


import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.home.HomeActivity;
import edu.umsl.superclickers.database.SQLiteHandlerQuizzes;
import edu.umsl.superclickers.quizdata.Answer;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.Quiz;
import edu.umsl.superclickers.userdata.User;


public class QuizActivityUser extends AppCompatActivity implements
        QuizUserFragment.QuizController {

    private final String TAG = getClass().getSimpleName();

    private Intent timerService;
    private QuizGET quizGET;
    private QuizUserFragment quizUserFragment;

    @Override
    public void submitQuiz(Quiz quiz) {
        SQLiteHandlerQuizzes db = SQLiteHandlerQuizzes.sharedInstance(getApplicationContext());
        db.removeQuiz(getQuizID());
        Toast.makeText(getApplicationContext(), "Quiz Submitted", Toast.LENGTH_LONG).show();
        stopService(timerService);
        Intent quizIntent = new Intent(QuizActivityUser.this, HomeActivity.class);
        startActivity(quizIntent);
        finish();
    }



    @Override
    public void startQuizTimer() {
        if (!isTimerRunning(QuizService.class)) {
            timerService.putExtra("QUIZ_TIME", getQuizTime());
            startService(timerService);

        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        Intent intent = getIntent();
        String quizID = intent.getStringExtra("QUIZ_ID");
        String userID = intent.getStringExtra("USER_ID");
        String courseID = intent.getStringExtra("COURSE_ID");


        timerService = new Intent(this, QuizService.class);

        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentByTag("QUIZ_GET") != null) {
            quizGET = (QuizGET) fm.findFragmentByTag("QUIZ_GET");
        } else {
            quizGET = new QuizGET();
            fm.beginTransaction()
                    .add(quizGET, "QUIZ_GET")
                    .commit();
        }
        if (fm.findFragmentByTag("QUIZ_GET") != null) {
            quizUserFragment = (QuizUserFragment) fm.findFragmentByTag("QUIZ_USER_FRAG");
        } else {
            quizUserFragment = new QuizUserFragment();
            fm.beginTransaction()
                    .add(R.id.quiz_container, quizUserFragment, "QUIZ_USER_FRAG")
                    .commit();
        }


        // Load quiz
        quizUserFragment.setQuizInfo(quizID, userID, courseID);



        // @TODO save quiz to SQLite
    }


    public int getQuizTime() {
        return quizUserFragment.getQuizTime();
    }

    public String getQuizID() { return quizUserFragment.getQuizID(); }

    @Override
    public QuizGET getQuizGET() {
        return quizGET;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt("TIME_LEFT", )
        // @TODO fix timer restarting
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUITimer(intent);
        }
    };

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

    void updateGUITimer(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 4);
            int secondsLeft = (int) millisUntilFinished / 1000;
            int minutesLeft = secondsLeft / 60;
            secondsLeft = secondsLeft % 60;
            quizUserFragment.updateGUITimer(minutesLeft, secondsLeft);
        }
    }

    private boolean isTimerRunning(Class<?> serviceClass) {
        ActivityManager actMan = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : actMan.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
