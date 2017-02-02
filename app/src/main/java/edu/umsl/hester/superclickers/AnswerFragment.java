package edu.umsl.hester.superclickers;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by stin on 1/31/17.
 */

public class AnswerFragment extends Fragment implements View.OnClickListener{

    private AnswerListener activity;

    Button buttonA, buttonB, buttonC, buttonD;

    // QuizActivity must implement these methods
    // This will be our way of communicating to the main activity from fragments
    public interface AnswerListener{
        String getAnswer();
        String getA();
        String getB();
        String getC();
        String getD();
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


        // Set the button texts from whatever the getA,B,C,D() methods in QuizActivity
        // Eventually, we'll move the potential answers somewhere else
        setButtonText();

        buttonA.setOnClickListener(this);
        buttonB.setOnClickListener(this);
        buttonC.setOnClickListener(this);
        buttonD.setOnClickListener(this);

        return view;
    }


    // made button listening easier
    @Override
    public void onClick(View view) {
        // handle button clicks
        switch(view.getId()) {
            case R.id.buttonA:

                break;
            case R.id.buttonB:

                break;
            case R.id.buttonC:
                buttonA.setEnabled(false);
                buttonB.setEnabled(false);
                buttonC.setEnabled(false);
                buttonD.setEnabled(false);
                view.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                break;
            case R.id.buttonD:

                break;
        }
    }

    private void setButtonText() {
        buttonA.setText(activity.getA());
        buttonB.setText(activity.getB());
        buttonC.setText(activity.getC());
        buttonD.setText(activity.getD());
    }



}
