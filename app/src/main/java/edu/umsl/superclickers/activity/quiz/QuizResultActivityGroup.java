package edu.umsl.superclickers.activity.quiz;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;

/**
 * Created by Austin on 5/2/2017.
 */

public class QuizResultActivityGroup extends AppCompatActivity implements
                QuizResultViewGroup.ResultListener {


    private String quizID;
    private String userID;
    private String courseID;
    private String groupID;

    private SessionManager session;
    private QuizResultViewGroup quizResultViewGroup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        session = new SessionManager(getBaseContext());
        session.setDoneWithIndividualQuiz(false);

        Intent i = getIntent();
        if (i.getExtras() != null) {
            quizID = i.getStringExtra("QUIZ_ID");
            courseID = i.getStringExtra("COURSE_ID");
            userID = i.getStringExtra("USER_ID");
            groupID = i.getStringExtra("GROUP_ID");
        }

        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentByTag(FragmentConfig.KEY_RESULT_VIEW_GROUP) != null) {
            quizResultViewGroup = (QuizResultViewGroup)
                    fm.findFragmentByTag(FragmentConfig.KEY_RESULT_VIEW_GROUP);
        } else {
            quizResultViewGroup = new QuizResultViewGroup();
            fm.beginTransaction()
                    .add(R.id.quiz_container, quizResultViewGroup,
                            FragmentConfig.KEY_RESULT_VIEW_GROUP)
                    .commit();
            // if quiz result view DNE
        }


    }

    @Override
    public void finishQuiz() {
        // @TODO

        finish();
    }

    @Override
    protected void onDestroy() {
        session.clearActiveQuiz();
        super.onDestroy();
    }
}
