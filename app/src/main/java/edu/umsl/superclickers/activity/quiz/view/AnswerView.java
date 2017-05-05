package edu.umsl.superclickers.activity.quiz.view;

import android.app.Fragment;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.quiz.helper.SeekBarText;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.SelectedAnswer;

/**
 * Created by Austin on 5/4/2017.
 *
 */

public abstract class AnswerView extends Fragment {

    private final static String TAG = AnswerView.class.getSimpleName();

    Button A;
    Button B;
    Button C;
    Button D;
    SeekBarText aP, bP, cP, dP;
    TextView pointsView;

    Question curQuestion;
    ArrayList<SelectedAnswer> selectedAnswers;

    SessionManager session;
    AnswerViewGroup.AnswerListener aListener;

    interface AnswerListener {
        Question getQuestion();
        void setSelectedAnswers(ArrayList<SelectedAnswer> selectedAnswers);
    }

    void setAnswerText() {
        A.setText(curQuestion.getA().toString());
        B.setText(curQuestion.getB().toString());
        C.setText(curQuestion.getC().toString());
        D.setText(curQuestion.getD().toString());
    }

    void setSeekBarListeners() {
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
        int maxPoints = curQuestion.getMaxPoints();
        curQuestion.setPointsPossible(4 - count); // @TODO add getMaxPoints in Question
        pointsView.setText(String.valueOf(curQuestion.getPointsPossible()));
    }

    SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
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
                case R.id.A_points_group:
                    index = 0;
                    break;
                case R.id.B_points_group:
                    index = 1;
                    break;
                case R.id.C_points_group:
                    index = 2;
                    break;
                case R.id.D_points_group:
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
            if (aListener != null) {
                aListener.setSelectedAnswers(selectedAnswers);
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    void getSelectedAnswers() {
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
    }
}
