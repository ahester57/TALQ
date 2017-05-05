package edu.umsl.superclickers.activity.quiz.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.quizdata.Question;

/**
 * Created by stin on 5/4/17.
 */

public class QuestionHolder extends RecyclerView.ViewHolder {

    private final String TAG = QuestionHolder.class.getSimpleName();
    private TextView tQuizName;
    private TextView tQuizText;
    private TextView tCourseName;
    private TextView tQuizTime;
    private WeakReference<QuestionHolderListener> mListener;

    public interface QuestionHolderListener {
        void setQuiz(int pos);
    }


    public QuestionHolder(View itemView, QuestionHolderListener listener) {
        super(itemView);
        this.mListener = new WeakReference<>(listener);
        tQuizName = (TextView) itemView.findViewById(R.id.text_quiz_name);
        tQuizText = (TextView) itemView.findViewById(R.id.text_quiz_text);
        tCourseName = (TextView) itemView.findViewById(R.id.text_course_name);
        tQuizTime = (TextView) itemView.findViewById(R.id.text_quiz_time);
    }

    public void bindQuiz(Question quiz) {
//        tQuizName.setText(quiz.getDescription());
        tQuizText.setText(quiz.getText());
//        tCourseName.setText(quiz.getCourseId()); ////////
//        tQuizTime.setText(String.valueOf(quiz.getTimedLength()));
    }

}
