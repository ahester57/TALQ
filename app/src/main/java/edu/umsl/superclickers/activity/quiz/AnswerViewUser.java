package edu.umsl.superclickers.activity.quiz;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.SelectedAnswer;

/**
 * Created by stin on 1/31/17.
 *
 */

public class AnswerViewUser extends Fragment implements View.OnClickListener {
    private static final String TAG = AnswerViewUser.class.getSimpleName();

    private Button A;
    private Button B;
    private Button C;
    private Button D;
    private SeekBar aP, bP, cP, dP;
    private TextView pointsView;

    private Question curQuestion;
    private ArrayList<SelectedAnswer> selectedAnswers;

    private SessionManager session;
    private AnswerListener aListener;

    interface AnswerListener {
        Question getQuestion();
        void nextQuestion();
        void currQuestion();
        void prevQuestion();
        void setSelectedAnswers(ArrayList<SelectedAnswer> selectedAnswers);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        session = new SessionManager(getActivity());
        aListener = (AnswerListener) getFragmentManager()
                .findFragmentByTag(FragmentConfig.KEY_QUIZ_VIEW_USER);
        curQuestion = aListener.getQuestion();
        String questionId = curQuestion.get_id();

        // selected answers now store "prevProgress" as allocatedPoints
        selectedAnswers = new ArrayList<>();
        selectedAnswers = session.getSelectedAnswersFor(questionId);
        if (selectedAnswers.size() != 4) {
            Log.d(TAG, "New selected answers created.");
            selectedAnswers.add(new SelectedAnswer(curQuestion.getA().getValue(), 0, questionId));
            selectedAnswers.add(new SelectedAnswer(curQuestion.getB().getValue(), 0, questionId));
            selectedAnswers.add(new SelectedAnswer(curQuestion.getC().getValue(), 0, questionId));
            selectedAnswers.add(new SelectedAnswer(curQuestion.getD().getValue(), 0, questionId));
        }

        Log.d(TAG, "Answer view created.");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_user, container, false);

        A = (Button) view.findViewById(R.id.A_button);
        B = (Button) view.findViewById(R.id.B_button);
        C = (Button) view.findViewById(R.id.C_button);
        D = (Button) view.findViewById(R.id.D_button);
        aP = (SeekBar) view.findViewById(R.id.A_points);
        bP = (SeekBar) view.findViewById(R.id.B_points);
        cP = (SeekBar) view.findViewById(R.id.C_points);
        dP = (SeekBar) view.findViewById(R.id.D_points);

//        Button buttonNext = (Button) view.findViewById(R.id.next_question_button);
//        Button buttonPrevious = (Button) view.findViewById(R.id.prev_question_button);
//        buttonPrevious.setOnClickListener(this);
//        buttonNext.setOnClickListener(this);

        pointsView = (TextView) view.findViewById(R.id.question_points);
        pointsView.setText(String.valueOf(curQuestion.getPointsPossible()));

        setSeekBarListeners();
        setAnswerText();

        return view;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.action_next_question:
                Log.d("ASDFAKLSDJFLAKSJDLFKA", "asdkfja;klsdjf;aksjd;flk");
                session.setSelectedAnswersFor(selectedAnswers);
                aListener.nextQuestion();
                break;
            case R.id.action_prev_question:
                session.setSelectedAnswersFor(selectedAnswers);
                aListener.prevQuestion();
                break;
            default:
                break;
        }
    }

    private void setAnswerText() {
        A.setText(curQuestion.getA().toString());
        B.setText(curQuestion.getB().toString());
        C.setText(curQuestion.getC().toString());
        D.setText(curQuestion.getD().toString());
    }

    private void setSeekBarListeners() {
        aP.setMax(4);
        bP.setMax(4);
        cP.setMax(4);
        dP.setMax(4);
        aP.setOnSeekBarChangeListener(seekListener);
        bP.setOnSeekBarChangeListener(seekListener);
        cP.setOnSeekBarChangeListener(seekListener);
        dP.setOnSeekBarChangeListener(seekListener);
        aP.setProgress(selectedAnswers.get(0).getAllocatedPoints());
        bP.setProgress(selectedAnswers.get(1).getAllocatedPoints());
        cP.setProgress(selectedAnswers.get(2).getAllocatedPoints());
        dP.setProgress(selectedAnswers.get(3).getAllocatedPoints());
        int count = 0;
        for (SelectedAnswer a : selectedAnswers) {
            count += a.getAllocatedPoints();
        }
        curQuestion.setPointsPossible(4 - count);
        pointsView.setText(String.valueOf(curQuestion.getPointsPossible()));
    }

    private SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int index = -1;
            switch (seekBar.getId()) {
                case R.id.A_points:
                    index = 0;
                    break;
                case R.id.B_points:
                    index = 1;
                    break;
                case R.id.C_points:
                    index = 2;
                    break;
                case R.id.D_points:
                    index = 3;
                    break;
            }
            if (index == -1) {
                return;
            }
            int p = curQuestion.getPointsPossible();
            int pr = selectedAnswers.get(index).getAllocatedPoints() - progress;
            if (p + pr < 0) {
                seekBar.setProgress(selectedAnswers.get(index).getAllocatedPoints());
            } else {
                selectedAnswers.get(index).setAllocatedPoints(progress);
                curQuestion.setPointsPossible(p + pr);
                pointsView.setText(String.valueOf(curQuestion.getPointsPossible()));
            }
            aListener.setSelectedAnswers(selectedAnswers);
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };
}
