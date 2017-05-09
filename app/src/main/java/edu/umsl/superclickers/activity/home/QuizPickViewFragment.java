package edu.umsl.superclickers.activity.home;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.QuizActivityUser;
import edu.umsl.superclickers.activity.quiz.helper.QuizHolder;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.QuizListItem;
import edu.umsl.superclickers.userdata.Course;

/**
 * Created by Austin on 5/7/2017.
 */

public class QuizPickViewFragment extends Fragment {
    private final static String TAG = QuizPickViewFragment.class.getSimpleName();

    private List<QuizListItem> mQuizzes;

    private SessionManager session;
    private RecyclerView qRecyclerView;
    private QuizPickListener hvListener;

    interface QuizPickListener {
        void startQuiz(String token);
        String getActiveQuizTitle();
        List<QuizListItem> getQuizzes();
        void setActiveQuiz(int pos);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        session = new SessionManager(getActivity());
        hvListener = (QuizPickListener) getActivity();
        if (hvListener != null) {
            mQuizzes = hvListener.getQuizzes();
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz_list, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_alt);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        qRecyclerView = (RecyclerView) view.findViewById(R.id.quiz_list_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        qRecyclerView.setLayoutManager(layoutManager);

        qRecyclerView.setAdapter(new QuizAdapter(mQuizzes));

        Button startQuiz = (Button) view.findViewById(R.id.button_start_quiz);
        startQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (hvListener != null) {
                    if (session.isQuizRunning()) {
                        hvListener.startQuiz("ALL_GOOD");
                        Log.d(TAG, "Quiz resumed.");
                    } else {
                        String qName = hvListener.getActiveQuizTitle();
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.dialog_token_input, null);
                        final EditText tokenIn = (EditText) dialogView.findViewById(R.id.quiz_token_input);

                        new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_MinWidth)
                                .setTitle(Html.fromHtml("<h2>Enter token</h2>"))
                                .setMessage(Html.fromHtml("<h4>Enter the token for <i>" +
                                        qName + "</i>:</h4>"))
                                .setView(dialogView)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (tokenIn != null) {
                                            String token = tokenIn.getText().toString();
                                            hvListener.startQuiz(token);
                                            Log.d(TAG, "Quiz token input: " + token);
                                        }
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_menu_directions)
                                .show();
                    }
                }
            }
        });

        return view;
    }


    void setQuizAdapter(List<QuizListItem> quizzes) {
        this.mQuizzes = quizzes;
        if (qRecyclerView != null) {
            qRecyclerView.setAdapter(new QuizAdapter(quizzes));
        }
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
            if (hvListener != null) {
                hvListener.setActiveQuiz(pos);
            }
        }

        @Override
        public void onBindViewHolder(QuizHolder holder, final int position) {
            if (mQuizzes != null) {
                try {
                    // for highlighting
                    if (selectedPos == position) {
                        holder.itemView.setBackground(getResources().getDrawable(R.drawable.line));
                        holder.itemView.setPadding(8, 8, 8, 8);
                    } else {
                        holder.itemView.setBackgroundResource(0);
                        holder.itemView.setPadding(8, 8, 8, 8);
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
