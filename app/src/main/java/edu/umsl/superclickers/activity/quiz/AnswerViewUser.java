package edu.umsl.superclickers.activity.quiz;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.quizdata.Question;

/**
 * Created by stin on 1/31/17.
 *
 */

public class AnswerViewUser extends Fragment implements View.OnClickListener{

    private AnswerListener aListener;
    private Button A, B, C, D, buttonPrevious, buttonNext;
    private SeekBar aP, bP, cP, dP;
    private int[] prevProgress;
    private TextView pointsView;
    private Question curQuestion;



    interface AnswerListener {
        Question getQuestion();
        void nextQuestion();
        void currQuestion();
        void prevQuestion();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prevProgress = new int[4];
        aListener = (AnswerListener) getFragmentManager()
                .findFragmentByTag(FragmentConfig.KEY_QUIZ_VIEW_USER);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_user, container, false);



        pointsView = (TextView) view.findViewById(R.id.question_points);

        A = (Button) view.findViewById(R.id.A_button);
        B = (Button) view.findViewById(R.id.B_button);
        C = (Button) view.findViewById(R.id.C_button);
        D = (Button) view.findViewById(R.id.D_button);

        aP = (SeekBar) view.findViewById(R.id.A_points);
        bP = (SeekBar) view.findViewById(R.id.B_points);
        cP = (SeekBar) view.findViewById(R.id.C_points);
        dP = (SeekBar) view.findViewById(R.id.D_points);

        buttonNext = (Button) view.findViewById(R.id.next_question_button);
        buttonPrevious = (Button) view.findViewById(R.id.prev_question_button);

        setSeekBarListeners();


        curQuestion = aListener.getQuestion();
        pointsView.setText(curQuestion.getPointsPossible());
        setAnswerText();

        buttonPrevious.setOnClickListener(this);
        buttonNext.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.next_question_button:
                aListener.nextQuestion();
                break;
            case R.id.prev_question_button:
                aListener.prevQuestion();
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

    private void setSeekBarListeners() {
        aP.setMax(4);
        bP.setMax(4);
        cP.setMax(4);
        dP.setMax(4);

        aP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    int p = Integer.valueOf(curQuestion.getPointsPossible());
                    int pr = prevProgress[0] - progress;
                    if (p + pr < 0) {
                        aP.setProgress(0);
                        aP.setMax(4);
                        aP.setProgress(prevProgress[0]);
                    }
                    else {
                        prevProgress[0] = progress;
                        curQuestion.setPointsPossible(p + pr);
                        pointsView.setText(curQuestion.getPointsPossible());
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        bP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    int p = Integer.valueOf(curQuestion.getPointsPossible());
                    int pr = prevProgress[1] - progress;
                    if (p + pr < 0) {
                        bP.setProgress(0);
                        bP.setMax(4);
                        bP.setProgress(prevProgress[1]);
                    }
                    else {
                        prevProgress[1] = progress;
                        curQuestion.setPointsPossible(p + pr);
                        pointsView.setText(curQuestion.getPointsPossible());
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        cP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    int p = Integer.valueOf(curQuestion.getPointsPossible());
                    int pr = prevProgress[2] - progress;
                    if (p + pr < 0) {
                        cP.setProgress(0);
                        cP.setMax(4);
                        cP.setProgress(prevProgress[2]);
                    }
                    else {
                        prevProgress[2] = progress;
                        curQuestion.setPointsPossible(p + pr);
                        pointsView.setText(curQuestion.getPointsPossible());
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        dP.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    int p = Integer.valueOf(curQuestion.getPointsPossible());
                    int pr = prevProgress[3] - progress;
                    if (p + pr < 0) {
                        dP.setProgress(0);
                        dP.setMax(4);
                        dP.setProgress(prevProgress[3]);
                    }
                    else {
                        prevProgress[3] = progress;
                        curQuestion.setPointsPossible(p + pr);
                        pointsView.setText(curQuestion.getPointsPossible());
                    }
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}
