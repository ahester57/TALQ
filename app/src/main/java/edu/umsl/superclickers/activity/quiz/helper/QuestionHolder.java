package edu.umsl.superclickers.activity.quiz.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Locale;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.quizdata.Question;

/**
 * Created by stin on 5/4/17.
 */

public class QuestionHolder extends RecyclerView.ViewHolder {

    private final String TAG = QuestionHolder.class.getSimpleName();
    private TextView tQuestionNumber;
    private TextView tQuestionText;
    private TextView tQuestionAnswers;
    private TextView tCheckMark;
    private TextView tXMark;
    private WeakReference<QuestionHolderListener> mListener;

    public interface QuestionHolderListener {
        void setQuiz(int pos);
    }


    public QuestionHolder(View itemView, QuestionHolderListener listener) {
        super(itemView);
        this.mListener = new WeakReference<>(listener);
        tQuestionNumber = (TextView) itemView.findViewById(R.id.question_number);
        tQuestionText = (TextView) itemView.findViewById(R.id.question_review_text);
        tQuestionAnswers = (TextView) itemView.findViewById(R.id.selected_answers);
        tCheckMark = (TextView) itemView.findViewById(R.id.check_mark);
        tXMark = (TextView) itemView.findViewById(R.id.x_mark);
    }

    public void bindQuestion(Question question, int numQuestions, int qNum) {
        tQuestionNumber.setText(String.format(Locale.getDefault(),
                "%d/%d", qNum+1, numQuestions));
        tQuestionText.setText(question.getText());
//        tQuestionAnswers.setText(question.getCourseId()); ////////
//        tXMark.setText(String.valueOf(question.getTimedLength()));
//        tXMark.setText(String.valueOf(question.getTimedLength()));
    }

}
