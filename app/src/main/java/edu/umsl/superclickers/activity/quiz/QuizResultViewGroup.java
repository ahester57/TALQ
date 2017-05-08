package edu.umsl.superclickers.activity.quiz;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.helper.QuestionHolder;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.Question;

/**
 * Created by stin on 5/4/17.
 */

public class QuizResultViewGroup extends Fragment {

    private final static String TAG = QuizResultViewGroup.class.getSimpleName();

    private List<Question> mQuestions;

    private RecyclerView uRecyclerView;
    private RecyclerView gRecyclerView;
    private ResultListener rListener;
    private SessionManager session;

    private int viewIsScrolling = 1;
    private boolean userIsTouched = false;
    private boolean groupIsTouched = false;

    interface ResultListener {
        void finishQuiz();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        rListener = (ResultListener) getActivity();
        session = new SessionManager(getActivity());
        mQuestions = session.getActiveQuiz().getQuestions();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        Button finish = (Button) view.findViewById(R.id.button_finish_group_quiz);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rListener.finishQuiz();
            }
        });

        uRecyclerView = (RecyclerView) view.findViewById(R.id.user_result_recycler);
        gRecyclerView = (RecyclerView) view.findViewById(R.id.group_result_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        LinearLayoutManager glayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration divider = new DividerItemDecoration(uRecyclerView.getContext(),
                layoutManager.getOrientation());
        DividerItemDecoration gdivider = new DividerItemDecoration(gRecyclerView.getContext(),
                glayoutManager.getOrientation());
        uRecyclerView.setLayoutManager(layoutManager);
        uRecyclerView.addItemDecoration(divider);
        gRecyclerView.setLayoutManager(glayoutManager);
        gRecyclerView.addItemDecoration(gdivider);

        SelfScrollListener userOnScroll = new SelfScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (userIsTouched) {
                    if (viewIsScrolling == -1) {
                        viewIsScrolling = 0;
                    }
                    if (viewIsScrolling == 0) {
                        gRecyclerView.scrollBy(dx, dy);
                    }
                }
            }
        };
        SelfScrollListener groupOnScroll = new SelfScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (groupIsTouched) {
                    if (viewIsScrolling == -1) {
                        viewIsScrolling = 1;
                    }
                    if (viewIsScrolling == 1) {
                        uRecyclerView.scrollBy(dx, dy);
                    }
                }
            }
        };

        uRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                userIsTouched = true;
                return false;
            }
        });
        gRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                groupIsTouched = true;
                return false;
            }
        });

        uRecyclerView.addOnScrollListener(userOnScroll);
        gRecyclerView.addOnScrollListener(groupOnScroll);
        uRecyclerView.setScrollX(0);
        gRecyclerView.setScrollX(0);
        setQuestionAdapter();
        return view;
    }


    void setQuestionAdapter() {
        if (mQuestions != null) {
            uRecyclerView.setAdapter(new QuestionAdapter(mQuestions));
            gRecyclerView.setAdapter(new QuestionAdapter(mQuestions));
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
                    if (selectedPos == position) {
                        holder.itemView.setBackgroundColor(Color.GREEN);
                    } else {
                        holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                    }
//                    holder.bindQuestion(mQuestions.get(position), mQuestions.size(),
//                            position,
//                            session.getSelectedAnswersFor(mQuestions.get(position).get_id()));
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

    private class SelfScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                viewIsScrolling = -1;
            }
        }
    }

}
