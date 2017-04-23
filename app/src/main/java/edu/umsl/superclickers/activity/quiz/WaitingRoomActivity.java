package edu.umsl.superclickers.activity.quiz;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;

/**
 * Created by Austin on 4/22/2017.
 */

public class WaitingRoomActivity extends AppCompatActivity {

    private String userID;
    private String quizID;

    private WaitingRoomView wFragment;
    private SessionManager session;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        FragmentManager fm = getFragmentManager();
        // Check if quizGET exists
        if (fm.findFragmentByTag(FragmentConfig.KEY_WAITING_ROOM) != null) {
            wFragment = (WaitingRoomView) fm.findFragmentByTag(FragmentConfig.KEY_WAITING_ROOM);
        } else {
            wFragment = new WaitingRoomView();
            fm.beginTransaction()
                    .add(wFragment, FragmentConfig.KEY_WAITING_ROOM)
                    .commit();
        }

        session = new SessionManager(getApplicationContext());
    }
}
