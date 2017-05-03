package edu.umsl.superclickers.activity.home;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.helper.QuizHolder;
import edu.umsl.superclickers.activity.login.LoginActivity;
import edu.umsl.superclickers.activity.quiz.QuizActivityUser;
import edu.umsl.superclickers.activity.quiz.QuizService;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.QuizListItem;
import edu.umsl.superclickers.userdata.Course;
import edu.umsl.superclickers.userdata.Group;
import edu.umsl.superclickers.userdata.User;

/**
 * Created by Austin on 2/4/2017.
 *
 */

public class HomeActivity extends AppCompatActivity implements
        HomeController.HomeListener,
        GroupController.GroupListener,
        HomeViewFragment.HomeViewListener {

    private final String TAG = HomeActivity.class.getSimpleName();

    private String userId = "arh5w6";
    private String quizID;
    private String courseId;
    private Group group;

    private ArrayList<QuizListItem> quizzes;
    private ArrayList<Course> courses;
    private ArrayList<String> courseIds;

    private SessionManager session;
    private HomeController hController;
    private GroupController gController;
    private HomeViewFragment hViewFragment;

    // BroadcastReceiver for QuizService
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            handleQuizTimer(intent);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // session manager
        session = new SessionManager(getApplicationContext());

        User user = null;

        user = session.getCurrentUser();
        courses = session.getEnrolledCourses();


        FragmentManager fm = getFragmentManager();


        if (fm.findFragmentByTag(FragmentConfig.KEY_HOME_VIEW) != null) {
            hViewFragment = (HomeViewFragment) fm.findFragmentByTag(FragmentConfig.KEY_HOME_VIEW);
        } else {
            hViewFragment = new HomeViewFragment();
            fm.beginTransaction()
                    .replace(R.id.home_frame, hViewFragment, FragmentConfig.KEY_HOME_VIEW)
                    .commit();
        }



        if (fm.findFragmentByTag(FragmentConfig.KEY_HOME_CONTROLLER) != null) {
            hController = (HomeController) fm.findFragmentByTag(FragmentConfig.KEY_HOME_CONTROLLER);
        } else {
            hController = new HomeController();
            fm.beginTransaction()
                    .add(hController, FragmentConfig.KEY_HOME_CONTROLLER)
                    .commit();
            // only request groups if hController DNE
            if(user != null) {
                hController.getQuizzesFor(user.getUserId());
            } else {
                hController.getQuizzesFor("arh5w6");
            }
        }

        if (fm.findFragmentByTag(FragmentConfig.KEY_GROUP_CONTROLLER) != null) {
            gController = (GroupController) fm.findFragmentByTag(FragmentConfig.KEY_GROUP_CONTROLLER);
        } else {
            gController = new GroupController();
            fm.beginTransaction()
                    .add(gController, FragmentConfig.KEY_GROUP_CONTROLLER)
                    .commit();
            // only request groups if gController DNE
            if(user != null) {
                gController.getGroupForUser(user.getUserId(), courses.get(0).getCourseId());
            }
        }


    }

    @Override
    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public void setQuizzes(ArrayList<QuizListItem> quizzes, ArrayList<String> courseIds) {
        this.quizzes = quizzes;
        this.courseIds = courseIds;

        setActiveQuiz(0);
        hViewFragment.setQuizAdapter(quizzes);
        // @TODO display running quizzes
    }

    @Override
    public void setActiveQuiz(int pos) {
        quizID = quizzes.get(pos).get_id();
        courseId = courseIds.get(pos);
        Log.d(TAG, "Selected quiz: " + quizzes.get(pos).getDescription());
    }

    public void stopQuiz() {
        // @TODO POST quiz for grading
        Toast.makeText(getApplicationContext(), "Quiz Submitted", Toast.LENGTH_LONG).show();
        stopService(new Intent(getBaseContext(), QuizService.class));
        session.clearActiveQuiz();
    }

    void handleQuizTimer(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 4);

            if (millisUntilFinished < 2000) {
                stopQuiz();
            }
        }
    }



    @Override
    public void startQuiz() {
        Intent i = new Intent(HomeActivity.this, QuizActivityUser.class);
        i.putExtra("QUIZ_ID", quizID);
        i.putExtra("USER_ID", userId);
        i.putExtra("COURSE_ID", courseId);
        i.putExtra("GROUP_ID", group.getGroupId());
        startActivity(i);
    }

    @Override
    public void goToGroups() {
        Intent in = new Intent(HomeActivity.this, GroupActivity.class);
        startActivity(in);
    }
    // logout user,
    @Override
    public void logoutUser() {
        session.setLogin(false);
        session.clearDatabase();
        // Go to login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
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
        Log.d(TAG, "Home Activity destoyed");
        super.onDestroy();
    }
}
