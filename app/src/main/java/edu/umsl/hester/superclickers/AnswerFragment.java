package edu.umsl.hester.superclickers;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by stin on 1/31/17.
 */

public class AnswerFragment extends Fragment {


    public interface AnswerListener{
        int getTest();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer, container, false);



        return view;
    }


    int getTest() { return 1234; }
}
