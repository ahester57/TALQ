package edu.umsl.superclickers.activity.home;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.login.LoginActivity;
import edu.umsl.superclickers.activity.quiz.QuizActivityUser;
import edu.umsl.superclickers.activity.quiz.helper.QuizService;
import edu.umsl.superclickers.activity.waitingroom.WaitingRoomActivity;
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

        // @TODO add AlarmManager for submitting quiz
        // session manager
        session = new SessionManager(getApplicationContext());

        User user;
        user = session.getCurrentUser();
        userId = user.getUserId();
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
            // only request quizzes if hController DNE
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
                gController.getGroupForUser(userId, courses.get(0).getCourseId());
            }
        }
    }

    void updateInformation() {
        try {
            User user = session.getCurrentUser();
            userId = user.getUserId();
            courses = session.getEnrolledCourses();
            if (gController != null) {
                gController.getGroupForUser(userId, courses.get(0).getCourseId());
            } else {
                onRestart();
            }
        } catch (NullPointerException e) {
            onRestart();
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
        // @TODO display full course name
    }

    @Override
    public void setActiveQuiz(int pos) {
        quizID = quizzes.get(pos).get_id();
        courseId = courseIds.get(pos);
        Log.d(TAG, "Selected quiz: " + quizzes.get(pos).getDescription());
    }


    @Override
    public void startQuiz() {
        try {
            Intent i = new Intent(HomeActivity.this, QuizActivityUser.class);
            i.putExtra("QUIZ_ID", quizID);
            i.putExtra("USER_ID", userId);
            i.putExtra("COURSE_ID", courseId);
            i.putExtra("GROUP_ID", group.getGroupId());
            startActivity(i);
        } catch (NullPointerException e) {
            Log.e(TAG, "Info not found, Restarting....");
            updateInformation();
        }
    }

    public void stopQuiz() {
        // @TODO POST quiz for grading
        stopService(new Intent(getBaseContext(), QuizService.class));

        Intent quizIntent = new Intent(HomeActivity.this, WaitingRoomActivity.class);
        quizIntent.putExtra("QUIZ_ID", quizID);
        quizIntent.putExtra("COURSE_ID", courseId);
        quizIntent.putExtra("USER_ID", userId);
        quizIntent.putExtra("GROUP_ID", group.getGroupId());
        setResult(Activity.RESULT_OK);
        startActivity(quizIntent);
    }

    @Override
    public void goToGroups() {
        Intent in = new Intent(HomeActivity.this, GroupActivity.class);
        startActivity(in);
    }

    void handleQuizTimer(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 4);

            if (millisUntilFinished < 2000) {
                stopQuiz();
            }
        }
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
        Log.d(TAG, "Home Activity destoyed");
        super.onDestroy();
    }
}
