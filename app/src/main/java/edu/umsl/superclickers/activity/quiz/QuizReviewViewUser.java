package edu.umsl.superclickers.activity.quiz;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.helper.QuestionHolder;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.SelectedAnswer;

/**
 * Created by stin on 5/4/17.
 */

public class QuizReviewViewUser extends Fragment {

    private final static String TAG = QuizReviewViewUser.class.getSimpleName();

    private List<Question> mQuestions;

    private RecyclerView qRecyclerView;
    private ReviewListener rListener;
    private SessionManager session;

    interface ReviewListener {
        void submitQuiz();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        rListener = (ReviewListener) getActivity();
        session = new SessionManager(getActivity());
        mQuestions = session.getActiveQuiz().getQuestions();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_review, container, false);

        Button submit = (Button) view.findViewById(R.id.button_submit_quiz);
        qRecyclerView = (RecyclerView) view.findViewById(R.id.review_quiz_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        qRecyclerView.setLayoutManager(layoutManager);

        setQuestionAdapter();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rListener.submitQuiz();
            }
        });
        return view;
    }

    void setQuestionAdapter() {
        if (mQuestions != null) {
            qRecyclerView.setAdapter(new QuestionAdapter(mQuestions));
        }
    }


    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> implements
            QuestionHolder.QuestionHolderListener {

        private int selectedPos = 0;
        private List<Question> mQuestions;

        QuestionAdapter(List<Question> questions) {
            mQuestions = questions;
        }

        @Override
        public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.question_recycler_item, parent, false);
            return new QuestionHolder(view, this);
        }


        @Override
        public void onBindViewHolder(QuestionHolder holder, final int position) {
            if (mQuestions != null) {
                try {
                    // for getting selected answers
                    boolean answeredFully = false;
                    int pointCount = 0;
                    ArrayList<SelectedAnswer> selectedAnswers = new ArrayList<>();
                    for (SelectedAnswer sa : session.getSelectedAnswersFor(mQuestions.get(position).get_id())) {
                        if (sa.getAllocatedPoints() != 0) {
                            selectedAnswers.add(sa);
                            pointCount += sa.getAllocatedPoints();
                        }
                    }
                    // for checking fully answered question
                    if (pointCount >= 4) {
                        answeredFully = true;
                    }
                    //

                    // for highlighting selected view
                    if (selectedPos == position) {
                        // if selected
                        if (answeredFully) {
                            holder.itemView.setBackground(getResources().getDrawable(R.drawable.line));
                            holder.itemView.setPadding(8, 8, 8, 8);
                        } else {
                            holder.itemView.setBackground(getResources().getDrawable(R.drawable.line_black));
                            holder.itemView.setPadding(8, 8, 8, 8);
                        }
                    } else {
                        // not selected
                        holder.itemView.setBackgroundResource(0);
                        holder.itemView.setPadding(8, 8, 8, 8);
                    }

                    // bind question to view
                    holder.bindQuestion(mQuestions.get(position), mQuestions.size(),
                            position, selectedAnswers, answeredFully);
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            notifyItemChanged(selectedPos);
                            selectedPos = position;
                            notifyItemChanged(selectedPos);

                        }
                    });
                } catch (IndexOutOfBoundsException e) {
                    Log.e("WHOOPS", "idk");
                }
            }
        }

        @Override
        public int getItemCount() {
            if (mQuestions != null) {
                return mQuestions.size();
            }
            return 0;
        }
    }
}
