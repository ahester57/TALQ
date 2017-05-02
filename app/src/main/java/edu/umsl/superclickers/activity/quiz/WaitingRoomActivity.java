package edu.umsl.superclickers.activity.quiz;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.Quiz;
import edu.umsl.superclickers.quizdata.SelectedAnswer;

/**
 * Created by Austin on 4/22/2017.
 */

public class WaitingRoomActivity extends AppCompatActivity
        implements WaitingRoomController.WaitListener {

    private final static String TAG = WaitingRoomActivity.class.getSimpleName();

    private String userID;
    private String quizID;
    private Quiz curQuiz;

    private ArrayList<SelectedAnswer> selectedAnswers = new ArrayList<>();

    private WaitingRoomView wFragment;
    private WaitingRoomController wController;
    private SessionManager session;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        FragmentManager fm = getFragmentManager();
        // Check if quizGET exists
        if (fm.findFragmentByTag(FragmentConfig.KEY_WAITING_ROOM) != null) {
            wFragment = (WaitingRoomView) fm.findFragmentByTag(FragmentConfig.KEY_WAITING_ROOM);
        } else {
            wFragment = new WaitingRoomView();
            fm.beginTransaction()
                    .add(wFragment, FragmentConfig.KEY_WAITING_ROOM)
                    .commit();
        }
        if (fm.findFragmentByTag(FragmentConfig.KEY_WAITING_CONTROLLER) != null) {
            wController = (WaitingRoomController) fm.findFragmentByTag(FragmentConfig.KEY_WAITING_CONTROLLER);
        } else {
            wController = new WaitingRoomController();
            fm.beginTransaction()
                    .add(wController, FragmentConfig.KEY_WAITING_CONTROLLER)
                    .commit();
        }


        session = new SessionManager(getApplicationContext());
        curQuiz = session.getActiveQuiz();

        JSONObject quizPOSTObj = buildAnswersForPOST();

        wController.POSTQuiz("cmpsci4020", "arh5w6",
                "dfc3013974f5a5dcee8381e42f246e60d764d67d8e422d15e703fdbc4acba630",
                quizPOSTObj);
        session.clearActiveQuiz();
    }

    @Override
    public void postInfo() {

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
}
