package edu.umsl.hester.superclickers.activity.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umsl.hester.superclickers.R;

/**
 * Created by Austin on 2/7/2017.
 *
 */

public class GroupFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer, container, false);

        return view;
    }
}
