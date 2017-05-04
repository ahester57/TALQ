package edu.umsl.superclickers.activity.quiz;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umsl.superclickers.R;

/**
 * Created by stin on 5/4/17.
 */

public class QuizReviewViewUser extends Fragment {

    private ReviewListener rListener;

    interface ReviewListener {
        void submitQuiz();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rListener = (ReviewListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_review, container, false);


        return view;
    }
}
