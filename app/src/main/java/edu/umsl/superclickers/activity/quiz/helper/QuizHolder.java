package edu.umsl.superclickers.activity.quiz.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.quizdata.QuizListItem;

/**
 * Created by Austin on 4/22/2017
 */

public class QuizHolder extends RecyclerView.ViewHolder {

    private final String TAG = QuizHolder.class.getSimpleName();
    private TextView tQuizName;
    private TextView tQuizText;
    private TextView tCourseName;
    private TextView tQuizTime;
    private WeakReference<QuizHolderListener> mListener;

    public interface QuizHolderListener {
        void setQuiz(int pos);
    }


    public QuizHolder(View itemView, QuizHolderListener listener) {
        super(itemView);
        this.mListener = new WeakReference<>(listener);
        tQuizName = (TextView) itemView.findViewById(R.id.text_quiz_name);
        tQuizText = (TextView) itemView.findViewById(R.id.text_quiz_text);
        tCourseName = (TextView) itemView.findViewById(R.id.text_course_name);
        tQuizTime = (TextView) itemView.findViewById(R.id.text_quiz_time);
    }

    public void bindQuiz(QuizListItem quiz) {
        tQuizName.setText(quiz.getDescription());
        tQuizText.setText(quiz.getText());
        tCourseName.setText(quiz.getCourseId()); ////////
        String quizTime = String.valueOf(quiz.getTimedLength()) + " min";
        tQuizTime.setText(quizTime);
    }


}
