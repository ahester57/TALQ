package edu.umsl.hester.superclickers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by stin on 1/31/17.
 */

public class AnswerFragment extends Fragment {

    private AnswerListener activity;

    // QuizActivity must implement these methods
    // This will be our way of communicating to the main activity from fragments
    public interface AnswerListener{
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
        Button buttonA = (Button) view.findViewById(R.id.buttonA);
        Button buttonB = (Button) view.findViewById(R.id.buttonB);
        Button buttonC = (Button) view.findViewById(R.id.buttonC);
        Button buttonD = (Button) view.findViewById(R.id.buttonD);


        // Set the button texts from whatever the getA,B,C,D() methods in QuizActivity
        // Eventually, we'll move the potential answers somewhere else
        buttonA.setText(activity.getA());
        buttonB.setText(activity.getB());
        buttonC.setText(activity.getC());
        buttonD.setText(activity.getD());

        return view;
    }



}
