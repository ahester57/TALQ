package edu.umsl.superclickers.activity.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umsl.superclickers.R;

/**
 * Created by Austin on 2/7/2017.
 *
 */

public class GroupViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_user, container, false);
        // @TODO
        // put recycler view here with groups and group members

        return view;
    }
}
