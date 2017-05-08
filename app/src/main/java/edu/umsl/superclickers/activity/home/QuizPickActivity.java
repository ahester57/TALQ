package edu.umsl.superclickers.activity.home;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.QuizActivityUser;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.QuizListItem;
import edu.umsl.superclickers.userdata.Course;

/**
 * Created by Austin on 5/7/2017.
 */

public class QuizPickActivity extends AppCompatActivity implements
        QuizPickViewFragment.QuizPickListener,
        QuizPickController.QuizPickDelegate {

    private final String TAG = HomeActivity.class.getSimpleName();

    private String quizID;
    private String userID;
    private String courseID;
    private String groupID;

    private QuizListItem curQuiz;
    private ArrayList<QuizListItem> quizzes;
    private ArrayList<String> courseIds;
    private ArrayList<Course> courses;

    private SessionManager session;
    private QuizPickController hController;
    private QuizPickViewFragment qViewFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_alt);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());

        Intent i = getIntent();
        if (i.getExtras() != null) {
            courseID = i.getStringExtra("COURSE_ID");
            userID = i.getStringExtra("USER_ID");
            groupID = i.getStringExtra("GROUP_ID");
        }


        FragmentManager fm = getFragmentManager();
        if (fm.findFragmentByTag(FragmentConfig.KEY_QUIZ_LIST_VIEW) != null) {
            qViewFragment = (QuizPickViewFragment) fm.findFragmentByTag(FragmentConfig.KEY_QUIZ_LIST_VIEW);
        } else {
            qViewFragment = new QuizPickViewFragment();
            fm.beginTransaction()
                    .replace(R.id.group_frame, qViewFragment, FragmentConfig.KEY_QUIZ_LIST_VIEW)
                    .commit();
        }

        if (fm.findFragmentByTag(FragmentConfig.KEY_HOME_CONTROLLER) != null) {
            hController = (QuizPickController) fm.findFragmentByTag(FragmentConfig.KEY_HOME_CONTROLLER);
        } else {
            hController = new QuizPickController();
            fm.beginTransaction()
                    .add(hController, FragmentConfig.KEY_HOME_CONTROLLER)
                    .commit();
            // only request quizzes if hController DNE
            if(userID != null && courseID != null) {
                hController.getQuizzesFor(userID, courseID);
            } else {
                hController.getQuizzesFor("arh5w6", "cmpsci4020");
            }
        }
        Log.d(TAG, "Quiz pick created.");
    }

    @Override
    public void startQuiz() {
        try {
            Intent i = new Intent(QuizPickActivity.this, QuizActivityUser.class);
            i.putExtra("QUIZ_ID", quizID);
            i.putExtra("USER_ID", userID);
            i.putExtra("COURSE_ID", courseID);
            i.putExtra("GROUP_ID", groupID);
            startActivity(i);
            finish();
        } catch (NullPointerException e) {
            Log.e(TAG, "Info not found, Restarting....");
        }
    }

    @Override
    public String getActiveQuizTitle() {
        if (curQuiz != null) {
            return curQuiz.getDescription();
        }
        return null;
    }

    @Override
    public void setQuizzes(ArrayList<QuizListItem> quizzes) {
        this.quizzes = quizzes;

        setActiveQuiz(0);
        qViewFragment.setQuizAdapter(quizzes);
        // @TODO display running quizzes
        // @TODO display full course name
    }

    @Override
    public void setActiveQuiz(int pos) {
        curQuiz = quizzes.get(pos);
        quizID = curQuiz.get_id();
        Log.d(TAG, "Selected quiz: " + curQuiz.getDescription());
    }


    @Override
    public List<QuizListItem> getQuizzes() {
        return quizzes;
    }
}
