package edu.umsl.superclickers.activity.waitingroom;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.QuizActivityGroup;
import edu.umsl.superclickers.activity.quiz.helper.QuizService;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.GradedQuiz;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.Quiz;
import edu.umsl.superclickers.quizdata.SelectedAnswer;

/**
 * Created by Austin on 4/22/2017.
 */

public class WaitingRoomActivity extends AppCompatActivity
        implements WaitingRoomController.WaitListener,
        WaitingRoomView.WaitRoomListener {

    private final static String TAG = WaitingRoomActivity.class.getSimpleName();

    private String quizID;
    private String userID;
    private String courseID;
    private String groupID;
    private String leader;
    private Quiz curQuiz;

    private WaitingRoomView wFragment;
    private WaitingRoomController wController;
    private SessionManager session;
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_quiz);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            quizID = intent.getStringExtra("QUIZ_ID");
            courseID = intent.getStringExtra("COURSE_ID");
            userID = intent.getStringExtra("USER_ID");
            groupID = intent.getStringExtra("GROUP_ID");
        }
        curQuiz = session.getActiveQuiz();
        session.setDoneWithIndividualQuiz(true);

        FragmentManager fm = getFragmentManager();
        // Check if quizGET exists
        if (fm.findFragmentByTag(FragmentConfig.KEY_WAITING_ROOM) != null) {
            wFragment = (WaitingRoomView) fm.findFragmentByTag(FragmentConfig.KEY_WAITING_ROOM);
        } else {
            wFragment = new WaitingRoomView();
            fm.beginTransaction()
                    .add(R.id.quiz_container, wFragment, FragmentConfig.KEY_WAITING_ROOM)
                    .commit();
        }
        if (fm.findFragmentByTag(FragmentConfig.KEY_WAITING_CONTROLLER) != null) {
            wController = (WaitingRoomController) fm.findFragmentByTag(FragmentConfig.KEY_WAITING_CONTROLLER);
        } else {
            wController = new WaitingRoomController();
            fm.beginTransaction()
                    .add(wController, FragmentConfig.KEY_WAITING_CONTROLLER)
                    .commit();
            // if WaitingRoomController DNE, post quiz for grading
            JSONObject quizPOSTObj = buildAnswersForPOST();
            wController.POSTQuiz(courseID, userID,
                    curQuiz.getSessionId(),
                    quizPOSTObj);
        }

        Intent timerService = new Intent(getBaseContext(), QuizService.class);
        stopService(timerService);
        Log.d(TAG, "Waiting room created.");
    }

    private Runnable checkGroupStatus = new Runnable() {
        @Override
        public void run() {
            if (wController != null) {
                wController.getGroupStatus(groupID, courseID,
                        curQuiz.get_id(), curQuiz.getSessionId());
                mHandler.postDelayed(checkGroupStatus, 3500);
            }
        }
    };

    private void startPolling() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
            mHandler.postDelayed(checkGroupStatus, 500);
        }
    }

    void stopPolling() {
        if (mHandler != null) {
            mHandler.removeCallbacks(checkGroupStatus);
            mHandler = null;
        }
    }

    @Override
    public void startGroupQuiz() {
        if (leader != null) {
            Intent quizIntent = new Intent(WaitingRoomActivity.this, QuizActivityGroup.class);
            session.setQuizIndex(0);
            quizIntent.putExtra("QUIZ_ID", quizID);
            quizIntent.putExtra("COURSE_ID", courseID);
            quizIntent.putExtra("USER_ID", userID);
            quizIntent.putExtra("GROUP_ID", groupID);
            quizIntent.putExtra("LEADER_ID", leader);
            startActivity(quizIntent);
            finish();
        } else {
            Toast.makeText(this, "For some reason there is no leader even though you're done",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void postInfo(JSONObject response) {
        GradedQuiz gradedQuiz = null;
        try {
            gradedQuiz = new GradedQuiz(response);
        } catch (JSONException e) {
            Log.e("JSONError", e.getMessage());
        }
        if( wFragment != null) {
            if (gradedQuiz != null) {
                wFragment.setTextQuizInfo("You got " + gradedQuiz.calculateTotalPoints() +
                        " points.\n\nYou can do better probably.");
            } else {
                wFragment.setTextQuizInfo("You've already taken this quiz.");
            }
        }
        // start checking group status once graded
        startPolling();
    }

    @Override
    public void setGroupStatus(String response) {
        JSONObject gObj;
        try {
            gObj = new JSONObject(response);

            JSONArray statusArr = gObj.getJSONArray("status");
            if (statusArr.length() >= 4) {
                stopPolling(); // @TODO fix this
                startGroupQuiz();
            }

            JSONObject leaderObj =gObj.getJSONObject("leader");
            leader = leaderObj.getString("userId");

        } catch (JSONException e) {
            Log.e("JSONError", e.getMessage());
        }
        if (wFragment != null) {
            wFragment.setTextGroupStatus(response);
        }
    }

    private JSONObject buildAnswersForPOST() {
        JSONObject postObj = new JSONObject();
        try {
            // Build question array
            JSONArray questionArr = new JSONArray();
            for (Question q : curQuiz.getQuestions()) {
                JSONObject questionObj = new JSONObject();
                questionObj.put("id", q.get_id());
                // Build selected answer array
                JSONArray subAnswerArr = new JSONArray();
                for (SelectedAnswer sa : session.getSelectedAnswersFor(q.get_id())) {
                    JSONObject subAnswerObj = new JSONObject();
                    if (sa.getAllocatedPoints() != 0) {
                        subAnswerObj.put("value", sa.getValue());
                        subAnswerObj.put("allocatedPoints", sa.getAllocatedPoints());
                        subAnswerArr.put(subAnswerObj);
                    }
                }
                questionObj.put("submittedAnswers", subAnswerArr);
                questionArr.put(questionObj);
            }
            // and finally put it all together
            postObj.put("id", curQuiz.get_id());
            postObj.put("questions", questionArr);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        Log.i(TAG, postObj.toString());
        return postObj;
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
                startGroupQuiz();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        //session.clearActiveQuiz();
        stopPolling();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //session.clearActiveQuiz();

        super.onBackPressed();
    }
}
