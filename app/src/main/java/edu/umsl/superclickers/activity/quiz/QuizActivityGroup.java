package edu.umsl.superclickers.activity.quiz;

import android.app.ActivityManager;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.helper.QuizGET;
import edu.umsl.superclickers.activity.quiz.helper.QuizGroupController;
import edu.umsl.superclickers.activity.quiz.helper.QuizService;
import edu.umsl.superclickers.activity.quiz.view.QuizViewGroup;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.Quiz;
import edu.umsl.superclickers.quizdata.SelectedAnswer;

/**
 * Created by Austin on 4/22/2017.
 */

public class QuizActivityGroup extends AppCompatActivity implements
        QuizViewGroup.QuizController,
        QuizGroupController.GroupGradeListener {

    private final String TAG = QuizActivityGroup.class.getSimpleName();

    private String quizID;
    private String userID;
    private String courseID;
    private String groupID;
    private String sessionId;
    private String leader;

    private Intent timerService;
    private QuizGET quizGET;
    private QuizViewGroup quizViewGroup;
    private QuizGroupController qGroupController;
    private SessionManager session;
    private Handler mHandler;

    private boolean isLeader = false;
    private boolean hasChosen;

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
            leader = intent.getStringExtra("LEADER_ID");
            if (leader != null && leader.equals(userID)) {
                isLeader = true;
            }
        }
        // Session manager
        session = new SessionManager(getApplicationContext());
        timerService = new Intent(getBaseContext(), QuizService.class);

        sessionId = session.getActiveQuiz().getSessionId();

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
        // Check if quizGET exists
        if (fm.findFragmentByTag(FragmentConfig.KEY_GROUP_QUIZ_CONTROLLER) != null) {
            qGroupController = (QuizGroupController) fm.findFragmentByTag(FragmentConfig.KEY_GROUP_QUIZ_CONTROLLER);
        } else {
            qGroupController = new QuizGroupController();
            fm.beginTransaction()
                    .add(qGroupController, FragmentConfig.KEY_GROUP_QUIZ_CONTROLLER)
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
        startQuizTimer();
        if (!isLeader) {
            startPolling();
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
//                if (!isLeader) {
//                    quizViewGroup.nextQuestion();
//                }
                return true;
            case R.id.action_prev_question:
//                if (!isLeader) {
//                    quizViewGroup.prevQuestion();
//                }
                return true;
            case R.id.action_review_quiz:
                Log.d(TAG, "has chosen = " + hasChosen);
                if (isLeader) {
                    if (hasChosen) {
                        String value = "";
                        try {
                            JSONObject question = buildAnswersForPOST();
                            value = question.getString("answerValue");
                        } catch (JSONException e) {
                            Log.e(TAG, e.getMessage());
                        }
                        new AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_MinWidth)
                                .setTitle(Html.fromHtml("<h2>Submit Question?</h2>"))
                                .setMessage(Html.fromHtml("<h3>Are you sure you want to submit with " +
                                        "choice: " + value + "?</h3>"))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        submitQuestion();
                                        Log.d(TAG, "Question submitted");
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_input_add)
                                .show();
                    } else {
                        Toast.makeText(this, "Please make a selection.", Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(this, "Only leader can submit.", Toast.LENGTH_SHORT)
                            .show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void submitQuestion() {
        if (hasChosen) {
            JSONObject questionObj = buildAnswersForPOST();
            if (sessionId == null) {
                sessionId = session.getActiveQuiz().getSessionId();
            }
            hasChosen = false;
            if (qGroupController != null) {
                qGroupController.POSTGroupQuiz(quizID, groupID, sessionId, questionObj);
            }
        }

    }

    @Override
    public void postInfo(JSONObject response) {
        if (quizViewGroup != null) {
            try {
                String questionId = response.getString("question");
                JSONArray subAnswersArr = response.getJSONArray("submittedAnswers");

                String value = "none";
                boolean isCorrect = false;
                int points = 0;
                for (int i = 0; i < subAnswersArr.length(); i++) {
                    JSONObject gradedObj = subAnswersArr.getJSONObject(i);
                    value = gradedObj.getString("value");
                    points = gradedObj.getInt("points");
                    isCorrect = gradedObj.getBoolean("isCorrect");
                    if (!isCorrect) {
                        quizViewGroup.disableButton(valueToIndex(value));
                    }
                }
                if (isCorrect) {
                    String message = points > 0 ? "Congragulations!\n" : "Better luck next time!\n";
                    Toast.makeText(this, message + value + " is correct!" +
                                    "\nYou got " + points + " points for it.",
                            Toast.LENGTH_LONG).show();
                    if (getQuizIndex() >= getActiveQuiz().getQuestions().size() - 1) {
                        finishQuiz();
                    } else {
                        quizViewGroup.nextQuestion();
                    }
                } else {
                    Toast.makeText(this, "OOPS! " + value + " is not correct!",
                            Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    private JSONObject buildAnswersForPOST() {
        JSONObject questionObj = new JSONObject();
        try {
            // Build question object
            Question question = quizViewGroup.getQuestion();
            questionObj = new JSONObject();
            questionObj.put("question_id", question.get_id());
            // Build selected answer array
            for (SelectedAnswer sa : session.getSelectedAnswersFor(question.get_id())) {
                if (sa.getAllocatedPoints() != 0) {
                    questionObj.put("answerValue", sa.getValue());
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        Log.i(TAG, questionObj.toString());
        return questionObj;
    }

    @Override
    public void setGroupQuizStatus(JSONObject response) {
        // sync with leader
        if (quizViewGroup != null) {
            int questionsAnswered = 0;
            try {
                questionsAnswered = response.getInt("questionsAnswered");
                if (questionsAnswered > getQuizIndex()) {
                    if (getQuizIndex() >= getActiveQuiz().getQuestions().size() - 1) {
                        finishQuiz();
                    } else {
                        quizViewGroup.nextQuestion();
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "JSON error parsing group quiz progress");
            }
        }
    }

    @Override
    public void setSelectedAnswers(ArrayList<SelectedAnswer> selectedAnswers) {
        if (isLeader) {
            session.setSelectedAnswersFor(selectedAnswers);
            hasChosen = true;
        }
        // if leader then do this.
    }

    @Override
    public void setHasChosen(boolean flag) {
        hasChosen = flag;
    }

    public void finishQuiz() {
        // @TODO POST quiz for grading

        //Toast.makeText(getApplicationContext(), "Group Quiz Submitted", Toast.LENGTH_LONG).show();
        stopService(timerService);
        stopPolling();

        Intent quizIntent = new Intent(QuizActivityGroup.this, QuizResultActivityGroup.class);
        quizIntent.putExtra("QUIZ_ID", quizID);
        quizIntent.putExtra("COURSE_ID", courseID);
        quizIntent.putExtra("USER_ID", userID);
        quizIntent.putExtra("GROUP_ID", groupID);
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

    private Runnable checkGroupProgress = new Runnable() {
        @Override
        public void run() {
            if (qGroupController != null) {
                if (sessionId == null) {
                    sessionId = session.getActiveQuiz().getSessionId();
                }
                qGroupController.getGroupQuizStatus(quizID, groupID,
                        sessionId);
                mHandler.postDelayed(checkGroupProgress, 1500);
            }
        }
    };

    private void startPolling() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(checkGroupProgress, 500);
        }
    }

    void stopPolling() {
        if (mHandler != null) {
            mHandler.removeCallbacks(checkGroupProgress);
            mHandler = null;
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
                finishQuiz();
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

    private int valueToIndex(String value) {
        if (value.equals("A")) {
            return 0;
        } else if (value.equals("B")) {
            return 1;
        } else if (value.equals("C")) {
            return 2;
        } else if (value.equals("D")) {
            return 3;
        } else {
            return -1;
        }
    }

    @Override
    public void resetQuizActivity() {
        onRestart();
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
        Log.d(TAG, "Group QUiz Activity destoyed");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt("TIME_LEFT", )
    }

}
