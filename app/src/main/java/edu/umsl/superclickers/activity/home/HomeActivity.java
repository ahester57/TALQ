package edu.umsl.superclickers.activity.home;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
        View.OnClickListener, HomeController.HomeListener {

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
    private RecyclerView qRecyclerView;

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

        TextView textName = (TextView) findViewById(R.id.name_text_view);
        TextView textEmail = (TextView) findViewById(R.id.email_text_view);
        Button btnLogout = (Button) findViewById(R.id.logout_button);
        Button btnPlay = (Button) findViewById(R.id.play_button);
        Button btnCreateGroup = (Button) findViewById(R.id.groups_button);
        qRecyclerView = (RecyclerView) findViewById(R.id.quiz_list_recycler);
        qRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // session manager
        session = new SessionManager(getApplicationContext());

        User user = null;
        if (!session.isLoggedIn()) {
            //logoutUser();
            btnCreateGroup.setEnabled(false);
        } else {
            // Fetch user info from sqlite
            user = session.getCurrentUser();

            textName.setText(user.getLast() +
                    ", " + user.getFirst());
            textEmail.setText(user.getUserId());
        }

        courses = session.getEnrolledCourses();

        hController = new HomeController();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(hController, FragmentConfig.KEY_HOME_CONTROLLER)
                .commit();

        if(user != null) {
            hController.getQuizzesFor(user.getUserId());
            hController.getGroupFor(user.getUserId(), courses.get(0).getCourseId());
        } else {
            hController.getQuizzesFor("arh5w6");
            hController.getGroupFor("arh5w6", courses.get(0).getCourseId());
        }



        // @TODO download group info and put it SQLite

        btnLogout.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnCreateGroup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.logout_button:
                logoutUser();
                break;
            case R.id.play_button:
                Intent i = new Intent(HomeActivity.this, QuizActivityUser.class);
                i.putExtra("QUIZ_ID", quizID);
                i.putExtra("USER_ID", userId);
                i.putExtra("COURSE_ID", courseId);
                startActivity(i);
                //finish();
                break;
            case R.id.groups_button:
                Intent in = new Intent(HomeActivity.this, GroupActivity.class);
                startActivity(in);
                break;
        }
    }

    @Override
    public void setGroup(Group group) {
        this.group = group;
        session.setGroupId(group.getGroupId());
    }

    @Override
    public void setQuizzes(ArrayList<QuizListItem> quizzes, ArrayList<String> courseIds) {
        this.quizzes = quizzes;
        this.courseIds = courseIds;

        setActiveQuiz(0);
        qRecyclerView.setAdapter(new QuizAdapter(quizzes));
        // @TODO display running quizzes
    }

    void setActiveQuiz(int pos) {
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

    // logout user,
    private void logoutUser() {
        session.setLogin(false);
        session.clearDatabase();
        // Go to login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    class QuizAdapter extends RecyclerView.Adapter<QuizHolder> implements
            QuizHolder.QuizHolderListener {

        private List<QuizListItem> mQuizzes;
        private List<Course> mCourses;

        public QuizAdapter(List<QuizListItem> quizzes) {
            mQuizzes = quizzes;
        }

        @Override
        public QuizHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater.inflate(R.layout.quiz_recycler_item, parent, false);
            QuizHolder qHolder = new QuizHolder(view, this);
            return qHolder;
        }

        @Override
        public void setQuiz(int pos) {
            setActiveQuiz(pos);
        }

        @Override
        public void onBindViewHolder(QuizHolder holder, int position) {
            if (mQuizzes != null) {
                try {
                    holder.bindQuiz(mQuizzes.get(position));
                } catch (IndexOutOfBoundsException e) {
                    Log.e("WHOOPS", "idk");
                }
            }
        }

        @Override
        public int getItemCount() {
            if (mQuizzes != null) {
                return mQuizzes.size();
            }
            return 0;
        }
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
