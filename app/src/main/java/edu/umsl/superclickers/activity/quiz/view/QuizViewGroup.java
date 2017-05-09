package edu.umsl.superclickers.activity.quiz.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.helper.QuizGET;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.quizdata.SelectedAnswer;

/**
 * Created by Austin on 4/22/2017.
 *
 */

public class QuizViewGroup extends QuizView implements AnswerViewGroup.AnswerListener {

    private final String TAG = QuizViewGroup.class.getSimpleName();

    private AnswerViewGroup aViewFragment;
    private QuizViewGroup.QuizController qController;

    public interface QuizController {
        QuizGET getQuizGET();
        void startQuizTimer();
        void setQuizIndex(int qNum);
        int getQuizIndex();
        void setSelectedAnswers(ArrayList<SelectedAnswer> selectedAnswers);
        void resetQuizActivity();
        void setHasChosen(boolean flag);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        try {
            qController = (QuizViewGroup.QuizController) getActivity();
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
        View view = inflater.inflate(R.layout.fragment_quiz_group, container, false);
        numQuestionsView = (TextView) view.findViewById(R.id.num_questions_group);
        progressView = (TextView) view.findViewById(R.id.quiz_progress_group);
        quizTimeView = (TextView) view.findViewById(R.id.quiz_timer_text_group);
        questionView = (TextView) view.findViewById(R.id.question_text_view_group);
//        horDottedProgress = (HorDottedProgress) view.findViewById(R.id.progress_quiz_dots);
        qController = (QuizViewGroup.QuizController) getActivity();
        currQuestion();
        return view;
    }

    @Override
    public void setSelectedAnswers(ArrayList<SelectedAnswer> selectedAnswers) {
        qController.setSelectedAnswers(selectedAnswers);
    }

    @Override
    public void setHasChosen(boolean flag) {
        qController.setHasChosen(flag);
    }

    void disableButton(int index) {
        if (aViewFragment != null) {
            aViewFragment.disableButton(index);
        }
    }

    @Override
    public void loadAnswerFragment() {
        questionView.setText(curQuestion.getQuestion());
        qController.setQuizIndex(curQuiz.getqNum());
        progressView.setText(Integer.toString(curQuiz.getqNum() + 1));
        // create instance of the answer fragment
        updateGUITimer(minutesLeft, secondsLeft);
        aViewFragment = new AnswerViewGroup();
        // load answer fragment into answerSection of QuizActivityUser
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.answer_segment_group, aViewFragment, FragmentConfig.KEY_ANSWER_VIEW_GROUP);
        ft.commit();
    }

}

