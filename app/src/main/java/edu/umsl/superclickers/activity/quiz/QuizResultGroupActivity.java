package edu.umsl.superclickers.activity.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import edu.umsl.superclickers.app.SessionManager;

/**
 * Created by Austin on 5/2/2017.
 */

public class QuizResultGroupActivity extends AppCompatActivity {


    private String quizID;
    private String userID;
    private String courseID;
    private String groupID;

    private SessionManager session;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(getBaseContext());
        session.setDoneWithIndividualQuiz(false);

        Intent i = getIntent();
        quizID = i.getStringExtra("QUIZ_ID");
        courseID = i.getStringExtra("COURSE_ID");
        userID = i.getStringExtra("USER_ID");
        groupID = i.getStringExtra("GROUP_ID");

    }

    @Override
    protected void onDestroy() {
        session.clearActiveQuiz();
        super.onDestroy();
    }
}
