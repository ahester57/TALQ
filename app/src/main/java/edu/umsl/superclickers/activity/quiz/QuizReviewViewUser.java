package edu.umsl.superclickers.activity.quiz;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.helper.QuestionHolder;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.Question;

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

        qRecyclerView = (RecyclerView) view.findViewById(R.id.review_quiz_recycler);
        qRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setQuestionAdapter();

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
            View view = inflater.inflate(R.layout.quiz_recycler_item, parent, false);
            return new QuestionHolder(view, this);
        }

        @Override
        public void setQuiz(int pos) {

            //hvListener.setActiveQuiz(pos);
        }

        @Override
        public void onBindViewHolder(QuestionHolder holder, final int position) {
            if (mQuestions != null) {
                try {
                    if (selectedPos == position) {
                        holder.itemView.setBackgroundColor(Color.GREEN);
                    } else {
                        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                    }
                    holder.bindQuiz(mQuestions.get(position));
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
            if (mQuestions != null) {
                return mQuestions.size();
            }
            return 0;
        }
    }
}
