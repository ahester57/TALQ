package edu.umsl.superclickers.activity.quiz;


import android.app.Activity;
import android.app.ActivityManager;
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

import java.util.ArrayList;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.helper.QuizGET;
import edu.umsl.superclickers.activity.quiz.helper.QuizService;
import edu.umsl.superclickers.activity.quiz.view.QuizViewUser;
import edu.umsl.superclickers.activity.waitingroom.WaitingRoomActivity;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.Quiz;
import edu.umsl.superclickers.quizdata.SelectedAnswer;


public class QuizActivityUser extends AppCompatActivity implements
        QuizViewUser.QuizController {

    private final String TAG = QuizActivityUser.class.getSimpleName();

    private String quizID;
    private String userID;
    private String courseID;
    private String groupID;
    private String token;

    private Intent timerService;
    private QuizGET quizGET;
    private QuizViewUser quizViewUser;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_quiz);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            quizID = intent.getStringExtra("QUIZ_ID");
            userID = intent.getStringExtra("USER_ID");
            courseID = intent.getStringExtra("COURSE_ID");
            groupID = intent.getStringExtra("GROUP_ID");
            token = intent.getStringExtra("TOKEN");
        }
        // Session manager
        session = new SessionManager(getBaseContext());
        timerService = new Intent(getBaseContext(), QuizService.class);

        if (!session.isQuizRunning()) {
            session.clearActiveQuiz(); // i have my reasons
        } else {
            try {
                Quiz q = session.getActiveQuiz();
                if (q != null && !q.get_id().equals(quizID)) {
                    session.clearActiveQuiz();
                    stopService(timerService);
                } else if (session.isDoneWithIndividual()) {
                    skipToWaitingRoom();
                }
            } catch (IndexOutOfBoundsException e) {
                session.clearActiveQuiz();
            }
        }


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
        if (fm.findFragmentByTag(FragmentConfig.KEY_QUIZ_VIEW_USER) != null) {
            quizViewUser = (QuizViewUser) fm.findFragmentByTag(FragmentConfig.KEY_QUIZ_VIEW_USER);
        } else {
            // Add new quizFragment and reload quiz
            quizViewUser = new QuizViewUser();
            fm.beginTransaction()
                    .add(R.id.quiz_container, quizViewUser, FragmentConfig.KEY_QUIZ_VIEW_USER)
                    .commit();
            // Only try to reload quiz when fragment is not loaded



            reloadQuiz(quizID, userID, courseID);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_next_question:
                quizViewUser.nextQuestion();
                return true;
            case R.id.action_prev_question:
                quizViewUser.prevQuestion();
                return true;
            case R.id.action_review_quiz:
                // @TODO do we need this or can we do
                reviewQuiz();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void setSelectedAnswers(ArrayList<SelectedAnswer> selectedAnswers) {
        // add selected answers to database
        session.setSelectedAnswersFor(selectedAnswers);
    }


    public void reviewQuiz() {
        // @TODO Goto pending submit page
        Intent quizIntent = new Intent(QuizActivityUser.this, QuizReviewActivityUser.class);
        quizIntent.putExtra("QUIZ_ID", quizID);
        quizIntent.putExtra("COURSE_ID", courseID);
        quizIntent.putExtra("USER_ID", userID);
        quizIntent.putExtra("GROUP_ID", groupID);
        startActivityForResult(quizIntent, 0);

    }


    public void skipToWaitingRoom() {
        Intent waitIntent = new Intent(QuizActivityUser.this, WaitingRoomActivity.class);
        waitIntent.putExtra("QUIZ_ID", quizID);
        waitIntent.putExtra("COURSE_ID", courseID);
        waitIntent.putExtra("USER_ID", userID);
        waitIntent.putExtra("GROUP_ID", groupID);
        startActivity(waitIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "req: " + requestCode + ", result: " + resultCode);
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                finish();
            }
        }
    }

    void reloadQuiz(String quizID, String userID, String courseID) {
        if (!session.isQuizRunning()) {
            // Load quiz from internet
            //@TODO fix when quiz is over and started again
            quizViewUser.setQuizInfo(quizID, userID, courseID);
            quizViewUser.setResume(false);
            session.setActiveQuiz(quizID);
        } else {
            // get quiz from SQLite
            quizViewUser.setQuizInfo(quizID, userID, courseID);
            quizViewUser.setResume(true);
            quizViewUser.attachQuiz(getActiveQuiz(), getQuizIndex());
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

    Quiz getActiveQuiz() {
        return session.getActiveQuiz();
    }

    public int getQuizTime() {
        return quizViewUser.getQuizTime();
    }

    public String getQuizID() { return quizViewUser.getQuizID(); }

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
            quizViewUser.updateGUITimer(minutesLeft, secondsLeft);
            if (millisUntilFinished < 2000) {
                session.setDoneWithIndividualQuiz(true);
                skipToWaitingRoom();
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
    public void resetQuizActivity() {
        onRestart();
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
        Log.d(TAG, "QUiz Activity destoyed");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt("TIME_LEFT", )
    }

}
