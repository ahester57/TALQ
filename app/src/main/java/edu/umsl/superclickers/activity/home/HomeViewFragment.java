package edu.umsl.superclickers.activity.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.helper.CourseHolder;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.userdata.Course;
import edu.umsl.superclickers.userdata.User;

/**
 * Created by Austin on 5/2/2017.
 */

public class HomeViewFragment extends Fragment implements View.OnClickListener {

    private TextView textName;
    private TextView textEmail;

    private List<Course> courses;

    private SessionManager session;
    private RecyclerView qRecyclerView;
    private HomeViewListener hvListener;

    interface HomeViewListener {
        void logoutUser();
        void goToGroups();
        void goToQuizzes();
        void setActiveCourse(String courseId);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        session = new SessionManager(getActivity());
        hvListener = (HomeViewListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_alt);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        textName = (TextView) view.findViewById(R.id.name_text_view);
        textEmail = (TextView) view.findViewById(R.id.email_text_view);
        Button btnLogout = (Button) view.findViewById(R.id.logout_button);
        Button btnPlay = (Button) view.findViewById(R.id.play_button);
        Button btnCreateGroup = (Button) view.findViewById(R.id.groups_button);
        qRecyclerView = (RecyclerView) view.findViewById(R.id.course_list_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration divider = new DividerItemDecoration(qRecyclerView.getContext(),
                layoutManager.getOrientation());
        qRecyclerView.setLayoutManager(layoutManager);
        qRecyclerView.addItemDecoration(divider);
        qRecyclerView.setAdapter(new CourseAdapter(courses));

        if (!session.isLoggedIn()) {
            //logoutUser();
            btnCreateGroup.setEnabled(false);
        } else {
            // Fetch user info from sqlite
            User user = session.getCurrentUser();
            updateTextInfo(user);

        }

        btnLogout.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnCreateGroup.setOnClickListener(this);


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_alt, menu);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.logout_button:
                hvListener.logoutUser();
                break;
            case R.id.play_button:
                hvListener.goToQuizzes();
                break;
            case R.id.groups_button:
                hvListener.goToGroups();
                break;
        }
    }

    void updateTextInfo(User user) {
        textName.setText(user.getLast() +
                ", " + user.getFirst());
        textEmail.setText(user.getUserId());
    }

    void setCourseAdapter(List<Course> courses) {
        this.courses = courses;
        qRecyclerView.setAdapter(new CourseAdapter(courses));
    }


    private class CourseAdapter extends RecyclerView.Adapter<CourseHolder> {
        private final String TAG = CourseAdapter.class.getSimpleName();

        private int selectedPos = 0;
        private List<Course> mCourses;

        CourseAdapter(List<Course> courses) {
            mCourses = courses;
        }

        @Override
        public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.course_recycler_item, parent, false);
            return new CourseHolder(view);
        }

        public void goToQuizzes(String courseId) {
            hvListener.setActiveCourse(courseId);
        }

        @Override
        public void onBindViewHolder(CourseHolder holder, final int position) {
            if (mCourses != null) {
                try {
                    // for highlighting
                    if (selectedPos == position) {
                        int color = getResources().getColor(android.R.color.holo_green_dark);
                        holder.itemView.setBackgroundColor(color);
                    } else {
                        int color = getResources().getColor(android.R.color.tertiary_text_dark);
                        holder.itemView.setBackgroundColor(color);
                    }
                    final Course course = mCourses.get(position);
                    holder.bindCourse(course);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notifyItemChanged(selectedPos);
                            selectedPos = position;
                            notifyItemChanged(selectedPos);
                            goToQuizzes(course.getCourseId());
                        }
                    });
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }
            }
        }

        @Override
        public int getItemCount() {
            if (mCourses != null) {
                return mCourses.size();
            }
            return 0;
        }
    }

}
