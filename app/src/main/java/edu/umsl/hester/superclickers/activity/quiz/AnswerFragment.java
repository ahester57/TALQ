package edu.umsl.hester.superclickers.activity.quiz;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import edu.umsl.hester.superclickers.R;
import edu.umsl.hester.superclickers.quizdata.Question;

/**
 * Created by stin on 1/31/17.
 *
 */

public class AnswerFragment extends Fragment implements View.OnClickListener{

    private AnswerListener activity;
    private Button A, B, C, D, buttonPrevious, buttonNext;
    private SeekBar aP, bP, cP, dP;
    private Question curQuestion;

    interface AnswerListener {
        Question getQuestion();
        void nextQuestion();
        void currQuestion();
        void prevQuestion();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_user, container, false);

        activity = (AnswerListener) getActivity();

        A = (Button) view.findViewById(R.id.A_button);
        B = (Button) view.findViewById(R.id.B_button);
        C = (Button) view.findViewById(R.id.C_button);
        D = (Button) view.findViewById(R.id.D_button);
        buttonNext = (Button) view.findViewById(R.id.next_question_button);
        buttonPrevious = (Button) view.findViewById(R.id.prev_question_button);

        aP = (SeekBar) view.findViewById(R.id.A_points);
        bP = (SeekBar) view.findViewById(R.id.B_points);
        cP = (SeekBar) view.findViewById(R.id.C_points);
        dP = (SeekBar) view.findViewById(R.id.D_points);

        aP.setMax(4);
        bP.setMax(4);
        cP.setMax(4);
        dP.setMax(4);

        aP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        curQuestion = activity.getQuestion();
        setAnswerText();

        buttonPrevious.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.next_question_button:
                activity.nextQuestion();
                break;
            case R.id.prev_question_button:
                activity.prevQuestion();
                break;
            default:
                break;
        }
    }

    private void setAnswerText() {
        A.setText(curQuestion.getA().toString());
        B.setText(curQuestion.getB().toString());
        C.setText(curQuestion.getC().toString());
        D.setText(curQuestion.getD().toString());
    }
}
