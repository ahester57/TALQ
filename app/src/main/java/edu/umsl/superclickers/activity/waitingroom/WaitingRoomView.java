package edu.umsl.superclickers.activity.waitingroom;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.umsl.superclickers.R;

/**
 * Created by Austin on 4/22/2017.
 *
 */

public class WaitingRoomView extends Fragment {

    private final static String TAG = WaitingRoomView.class.getSimpleName();

    private TextView textQuizInfo;
    private TextView textGroupStatus;


    private String quizInfo = "none";
    private String groupStatus = "";

    private WaitRoomListener wrListener;

    interface WaitRoomListener {
        void startGroupQuiz();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        wrListener = (WaitRoomListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_waiting_room, container, false);



        Button startGroupQuiz = (Button) view.findViewById(R.id.button_submit_quiz);
        textQuizInfo = (TextView) view.findViewById(R.id.text_quiz_info);
        textGroupStatus = (TextView) view.findViewById(R.id.text_group_status);
        textQuizInfo.setText(quizInfo);
        textGroupStatus.setText(groupStatus);

        startGroupQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wrListener.startGroupQuiz();
            }
        });


        return view;
    }

    public void setTextQuizInfo(String quizInfo) {
        this.quizInfo = quizInfo;
        textQuizInfo.setText(quizInfo);
    }

    public void setTextGroupStatus(String groupStatus) {
        this.groupStatus = groupStatus;
        textGroupStatus.setText(groupStatus);
    }

}
