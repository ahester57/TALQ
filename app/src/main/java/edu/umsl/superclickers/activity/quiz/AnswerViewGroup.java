package edu.umsl.superclickers.activity.quiz;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.helper.SeekBarText;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.Question;
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
        setAnswerText();

        return view;
    }





}
