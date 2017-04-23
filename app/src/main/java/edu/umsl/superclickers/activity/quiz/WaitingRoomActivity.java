package edu.umsl.superclickers.activity.quiz;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;

/**
 * Created by Austin on 4/22/2017.
 */

public class WaitingRoomActivity extends Activity {

    private String userID;
    private String quizID;

    private WaitingRoomFragment wFragment;
    private SessionManager session;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        FragmentManager fm = getFragmentManager();
        // Check if quizGET exists
        if (fm.findFragmentByTag(FragmentConfig.KEY_WAITING_ROOM) != null) {
            wFragment = (WaitingRoomFragment) fm.findFragmentByTag(FragmentConfig.KEY_WAITING_ROOM);
        } else {
            wFragment = new WaitingRoomFragment();
            fm.beginTransaction()
                    .add(wFragment, FragmentConfig.KEY_WAITING_ROOM)
                    .commit();
        }

        session = new SessionManager(getApplicationContext());
    }
}
