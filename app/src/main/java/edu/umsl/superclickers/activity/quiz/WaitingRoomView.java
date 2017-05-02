package edu.umsl.superclickers.activity.quiz;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.umsl.superclickers.R;

/**
 * Created by Austin on 4/22/2017.
 *
 */

public class WaitingRoomView extends Fragment {

    private final static String TAG = WaitingRoomView.class.getSimpleName();

    private TextView textQuizInfo;

    private String quizInfo = "none";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waiting_room, container, false);

        textQuizInfo = (TextView) view.findViewById(R.id.text_quiz_info);
        textQuizInfo.setText(quizInfo);


        return view;
    }

    public void setTextQuizInfo(String quizInfo) {
        textQuizInfo.setText(quizInfo);
    }

    void parseQuizResponse(String quizResponse) {


        this.quizInfo = quizResponse;
    }
}
