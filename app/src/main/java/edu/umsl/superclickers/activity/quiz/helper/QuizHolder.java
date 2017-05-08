package edu.umsl.superclickers.activity.quiz.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.quizdata.QuizListItem;
import edu.umsl.superclickers.userdata.Course;

/**
 * Created by Austin on 4/22/2017
 */

public class QuizHolder extends RecyclerView.ViewHolder {

    private final String TAG = QuizHolder.class.getSimpleName();
    private TextView tQuizName;
    private TextView tQuizText;
    private TextView tCourseName;
    private TextView tQuizTime;

    public QuizHolder(View itemView) {
        super(itemView);
        tQuizName = (TextView) itemView.findViewById(R.id.text_quiz_name);
        tQuizText = (TextView) itemView.findViewById(R.id.text_quiz_text);
        tCourseName = (TextView) itemView.findViewById(R.id.text_course_name);
        tQuizTime = (TextView) itemView.findViewById(R.id.text_quiz_time);
    }

    public void bindQuiz(QuizListItem quiz, Course course) {
        tQuizName.setText(quiz.getDescription());
        tQuizText.setText(quiz.getText());
        tCourseName.setText(course.getName()); ////////
        String quizTime = String.valueOf(quiz.getTimedLength()) + " min";
        tQuizTime.setText(quizTime);
    }


}
