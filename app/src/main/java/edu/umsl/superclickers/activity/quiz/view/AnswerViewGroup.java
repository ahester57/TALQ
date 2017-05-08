package edu.umsl.superclickers.activity.quiz.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.helper.SeekBarText;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.SelectedAnswer;

/**
 * Created by Austin on 4/22/2017.
 *
 */

public class AnswerViewGroup extends AnswerView {
    private static final String TAG = AnswerViewGroup.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        session = new SessionManager(getActivity());
        aListener = (AnswerView.AnswerListener) getFragmentManager()
                .findFragmentByTag(FragmentConfig.KEY_QUIZ_VIEW_GROUP);
        curQuestion = aListener.getQuestion();

        getSelectedAnswers();

        Log.d(TAG, "Answer view created.");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_group, container, false);

        A = (Button) view.findViewById(R.id.A_button_group);
        B = (Button) view.findViewById(R.id.B_button_group);
        C = (Button) view.findViewById(R.id.C_button_group);
        D = (Button) view.findViewById(R.id.D_button_group);
        aP = (SeekBarText) view.findViewById(R.id.A_points_group);
        bP = (SeekBarText) view.findViewById(R.id.B_points_group);
        cP = (SeekBarText) view.findViewById(R.id.C_points_group);
        dP = (SeekBarText) view.findViewById(R.id.D_points_group);


        pointsView = (TextView) view.findViewById(R.id.question_points_group);
        pointsView.setText(String.valueOf(curQuestion.getPointsPossible()));

        setSeekBarListeners();
        aP.setEnabled(false);
        bP.setEnabled(false);
        cP.setEnabled(false);
        dP.setEnabled(false);
        setAnswerText();
        A.setOnClickListener(answerClick);
        B.setOnClickListener(answerClick);
        C.setOnClickListener(answerClick);
        D.setOnClickListener(answerClick);
        A.setEnabled(true);

        return view;
    }

    View.OnClickListener answerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = -1;
            clearButtons();
            int color = getResources().getColor(android.R.color.holo_green_light);
            switch (v.getId()) {
                case R.id.A_button_group:
                    A.setBackgroundColor(color);
                    index = 0;
                    break;
                case R.id.B_button_group:
                    B.setBackgroundColor(color);
                    index = 1;
                    break;
                case R.id.C_button_group:
                    C.setBackgroundColor(color);
                    index = 2;
                    break;
                case R.id.D_button_group:
                    D.setBackgroundColor(color);
                    index = 3;
                    break;
            }
            if (index == -1) {
                return;
            }
            String questionId = curQuestion.get_id();
            selectedAnswers = new ArrayList<>();
            if (selectedAnswers.size() != 4) {
                Log.d(TAG, "New selected answers created.");
                selectedAnswers.add(new SelectedAnswer(curQuestion.getA().getValue(), 0, questionId));
                selectedAnswers.add(new SelectedAnswer(curQuestion.getB().getValue(), 0, questionId));
                selectedAnswers.add(new SelectedAnswer(curQuestion.getC().getValue(), 0, questionId));
                selectedAnswers.add(new SelectedAnswer(curQuestion.getD().getValue(), 0, questionId));
            }
            selectedAnswers.get(index).setAllocatedPoints(4);
            if (aListener != null) {
                aListener.setSelectedAnswers(selectedAnswers);
            }
            Log.d(TAG, "selected answer: " + selectedAnswers.toString());
        }
    };

    void clearButtons() {
        int color = getResources().getColor(android.R.color.background_light);
        A.setBackgroundColor(color);
        B.setBackgroundColor(color);
        C.setBackgroundColor(color);
        D.setBackgroundColor(color);
    }

    @Override
    void setSeekBarListeners() {
        aP.setMax(4);
        bP.setMax(4);
        cP.setMax(4);
        dP.setMax(4);
        aP.setProgress(selectedAnswers.get(0).getAllocatedPoints());
        bP.setProgress(selectedAnswers.get(1).getAllocatedPoints());
        cP.setProgress(selectedAnswers.get(2).getAllocatedPoints());
        dP.setProgress(selectedAnswers.get(3).getAllocatedPoints());
        int count = 0;
        for (SelectedAnswer a : selectedAnswers) {
            count += a.getAllocatedPoints();
        }
        int maxPoints = curQuestion.getMaxPoints();
        curQuestion.setPointsPossible(4 - count); // @TODO add getMaxPoints in Question
        pointsView.setText(String.valueOf(curQuestion.getPointsPossible()));
    }



}
