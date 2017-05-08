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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.helper.QuizHolder;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.QuizListItem;
import edu.umsl.superclickers.userdata.Course;

/**
 * Created by Austin on 5/7/2017.
 */

public class QuizPickViewFragment extends Fragment {


    private List<QuizListItem> mQuizzes;

    private SessionManager session;
    private RecyclerView qRecyclerView;
    private QuizPickListener hvListener;

    interface QuizPickListener {
        void startQuiz();
        List<QuizListItem> getQuizzes();
        void setActiveQuiz(int pos);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        session = new SessionManager(getActivity());
        hvListener = (QuizPickListener) getActivity();
        mQuizzes = hvListener.getQuizzes();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_list, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_alt);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        qRecyclerView = (RecyclerView) view.findViewById(R.id.quiz_list_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration divider = new DividerItemDecoration(qRecyclerView.getContext(),
                layoutManager.getOrientation());
        qRecyclerView.setLayoutManager(layoutManager);
        qRecyclerView.addItemDecoration(divider);
        qRecyclerView.setAdapter(new QuizAdapter(mQuizzes));

        Button startQuiz = (Button) view.findViewById(R.id.button_start_quiz);
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hvListener.startQuiz();
            }
        });



        return view;
    }


    void setQuizAdapter(List<QuizListItem> quizzes) {
        this.mQuizzes = quizzes;
        qRecyclerView.setAdapter(new QuizAdapter(quizzes));
    }

    private class QuizAdapter extends RecyclerView.Adapter<QuizHolder> {
        private final String TAG = QuizAdapter.class.getSimpleName();

        private int selectedPos = 0;
        private List<QuizListItem> mQuizzes;

        QuizAdapter(List<QuizListItem> quizzes) {
            mQuizzes = quizzes;
        }

        @Override
        public QuizHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.quiz_recycler_item, parent, false);
            return new QuizHolder(view);
        }

        public void setQuiz(int pos) {
            hvListener.setActiveQuiz(pos);
        }

        @Override
        public void onBindViewHolder(QuizHolder holder, final int position) {
            if (mQuizzes != null) {
                try {
                    // for highlighting
                    if (selectedPos == position) {
                        int color = getResources().getColor(android.R.color.holo_green_dark);
                        holder.itemView.setBackgroundColor(color);
                    } else {
                        int color = getResources().getColor(android.R.color.tertiary_text_dark);
                        holder.itemView.setBackgroundColor(color);
                    }
                    QuizListItem quiz = mQuizzes.get(position);
                    Course course = session.getCourseById(quiz.getCourseId());
                    holder.bindQuiz(quiz, course);
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
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
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
