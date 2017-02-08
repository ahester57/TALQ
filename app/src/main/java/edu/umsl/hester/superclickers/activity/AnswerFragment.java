package edu.umsl.hester.superclickers.activity;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.umsl.hester.superclickers.R;
import edu.umsl.hester.superclickers.app.Question;

/**
 * Created by stin on 1/31/17.
 */

public class AnswerFragment extends Fragment implements View.OnClickListener{

    private AnswerListener activity;

    Button buttonA, buttonB, buttonC, buttonD, buttonNext;
    Question curQuestion;

    // QuizActivity must implement these methods
    // This will be our way of communicating to the main activity from fragments
    public interface AnswerListener{
        Question getQuestion();
        void nextQuestion();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer, container, false);

        activity = (AnswerListener) getActivity();

        // Initialize buttons from fragment_answer
        // Fragments are awesome and we should use them
        buttonA = (Button) view.findViewById(R.id.buttonA);
        buttonB = (Button) view.findViewById(R.id.buttonB);
        buttonC = (Button) view.findViewById(R.id.buttonC);
        buttonD = (Button) view.findViewById(R.id.buttonD);
        buttonNext = (Button) view.findViewById(R.id.buttonNext);

        // Set the button texts from whatever the getA,B,C,D() methods in QuizActivity
        // Eventually, we'll move the potential answers somewhere else
        curQuestion = activity.getQuestion();
        setButtonText();

        buttonA.setOnClickListener(this);
        buttonB.setOnClickListener(this);
        buttonC.setOnClickListener(this);
        buttonD.setOnClickListener(this);

        buttonNext.setOnClickListener(this);
        buttonNext.setEnabled(false);

        // @TODO add knowing which button is right directly

        return view;
    }


    // made button listening easier
    @Override
    public void onClick(View view) {
        // handle button clicks


        switch(view.getId()){
            case R.id.buttonNext:
                //this.getFragmentManager().beginTransaction().remove(this).commit();
                // loads new question
                activity.nextQuestion();
                break;
            default:
                Button but = (Button) view;
                // check if button contains the right answer or not
                if (checkAnswer(but)) {
                    setSuccess(but);
                } else {
                    setFailure(but);
                }
                buttonNext.setEnabled(true);
        }


    }

    // whether button contains the right answer
    private boolean checkAnswer(Button but) {
        return (curQuestion.check(but.getText().toString()));
    }

    // makes correct choice green
    private void setSuccess(Button but) {
        disableButtons();
        but.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
    }

    // makes wrong choice red
    private void setFailure(Button but) {
        disableButtons();
        but.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
    }

    // set buttons to choices
    private void setButtonText() {
        buttonA.setText(curQuestion.getA());
        buttonB.setText(curQuestion.getB());
        buttonC.setText(curQuestion.getC());
        buttonD.setText(curQuestion.getD());
    }

    //disable buttons
    private void disableButtons() {
        buttonA.setEnabled(false);
        buttonA.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.DST);
        buttonB.setEnabled(false);
        buttonB.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.DST);
        buttonC.setEnabled(false);
        buttonC.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.DST);
        buttonD.setEnabled(false);
        buttonD.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.DST);
    }


}
