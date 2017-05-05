package edu.umsl.superclickers.activity.quiz;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
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
 * Created by stin on 1/31/17.
 *
 */

public class AnswerViewUser extends AnswerView {
    private static final String TAG = AnswerViewUser.class.getSimpleName();
    

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        session = new SessionManager(getActivity());
        aListener = (AnswerView.AnswerListener) getFragmentManager()
                .findFragmentByTag(FragmentConfig.KEY_QUIZ_VIEW_USER);
        curQuestion = aListener.getQuestion();

        // selected answers now store "prevProgress" as allocatedPoints
        getSelectedAnswers();


        Log.d(TAG, "Answer view created.");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_user, container, false);

        A = (Button) view.findViewById(R.id.A_button);
        B = (Button) view.findViewById(R.id.B_button);
        C = (Button) view.findViewById(R.id.C_button);
        D = (Button) view.findViewById(R.id.D_button);
        aP = (SeekBarText) view.findViewById(R.id.A_points);
        bP = (SeekBarText) view.findViewById(R.id.B_points);
        cP = (SeekBarText) view.findViewById(R.id.C_points);
        dP = (SeekBarText) view.findViewById(R.id.D_points);
        pointsView = (TextView) view.findViewById(R.id.question_points);
        pointsView.setText(String.valueOf(curQuestion.getPointsPossible()));

        setSeekBarListeners();
        setAnswerText();

        return view;
    }



}
