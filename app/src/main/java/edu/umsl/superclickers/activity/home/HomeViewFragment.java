package edu.umsl.superclickers.activity.home;

import android.app.Fragment;
import android.content.Intent;
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
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.helper.QuizHolder;
import edu.umsl.superclickers.activity.quiz.QuizActivityUser;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.QuizListItem;
import edu.umsl.superclickers.userdata.Course;
import edu.umsl.superclickers.userdata.User;

/**
 * Created by Austin on 5/2/2017.
 */

public class HomeViewFragment extends Fragment implements View.OnClickListener {

    private TextView textName;
    private TextView textEmail;

    private List<QuizListItem> quizzes;

    private SessionManager session;
    private RecyclerView qRecyclerView;
    private HomeViewListener hvListener;

    interface HomeViewListener {
        void logoutUser();
        void startQuiz();
        void goToGroups();
        void setActiveQuiz(int pos);
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
        qRecyclerView = (RecyclerView) view.findViewById(R.id.quiz_list_recycler);
        qRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        qRecyclerView.setAdapter(new QuizAdapter(quizzes));

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
                hvListener.startQuiz();
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

    void setQuizAdapter(List<QuizListItem> quizzes) {
        this.quizzes = quizzes;
        qRecyclerView.setAdapter(new QuizAdapter(quizzes));
    }


    class QuizAdapter extends RecyclerView.Adapter<QuizHolder> implements
            QuizHolder.QuizHolderListener {

        private int selectedPos = 0;
        private List<QuizListItem> mQuizzes;
        private List<Course> mCourses;

        public QuizAdapter(List<QuizListItem> quizzes) {
            mQuizzes = quizzes;
        }

        @Override
        public QuizHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.quiz_recycler_item, parent, false);
            QuizHolder qHolder = new QuizHolder(view, this);
            return qHolder;
        }

        @Override
        public void setQuiz(int pos) {
            hvListener.setActiveQuiz(pos);
        }

        @Override
        public void onBindViewHolder(QuizHolder holder, final int position) {
            if (mQuizzes != null) {
                try {
                    if (selectedPos == position) {
                        holder.itemView.setBackgroundColor(Color.GREEN);
                    } else {
                        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                    }
                    holder.bindQuiz(mQuizzes.get(position));
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notifyItemChanged(selectedPos);
                            selectedPos = position;
                            notifyItemChanged(selectedPos);
                            setQuiz(position);
                        }
                    });
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

}
