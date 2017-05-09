package edu.umsl.superclickers.activity.quiz.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.app.FragmentConfig;

/**
 * Created by Austin on 4/19/2017.
 */

public class QuizViewUser extends QuizView {

    private final String TAG = QuizViewUser.class.getSimpleName();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        try {
            qController = (QuizView.QuizController) getActivity();
            quizGET = qController.getQuizGET();

            quizGET.setController(this);
            if (!resume || curQuiz == null) {
                getToken();
            }
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
            qController.resetQuizActivity();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        numQuestionsView = (TextView) view.findViewById(R.id.num_questions);
        progressView = (TextView) view.findViewById(R.id.quiz_progress);
        quizTimeView = (TextView) view.findViewById(R.id.quiz_timer_text);
        questionView = (TextView) view.findViewById(R.id.question_text_view);
        //horDottedProgress = (HorDottedProgress) view.findViewById(R.id.progress_quiz_dots);

        currQuestion();
        return view;
    }

    @Override
    public void loadAnswerFragment(boolean isNewQuestion) {
        questionView.setText(curQuestion.getQuestion());
        qController.setQuizIndex(curQuiz.getqNum());
        progressView.setText(Integer.toString(curQuiz.getqNum() + 1));
        // create instance of the answer fragment
        updateGUITimer(minutesLeft, secondsLeft);
        AnswerViewUser answerFrag = new AnswerViewUser();
        // load answer fragment into answerSection of QuizActivityUser
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.answer_segment, answerFrag, FragmentConfig.KEY_ANSWER_VIEW_USER);
        ft.commit();
    }


}
