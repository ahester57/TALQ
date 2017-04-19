package edu.umsl.superclickers.activity.quiz;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.app.Service;

import java.util.ArrayList;
import java.util.Locale;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.quizdata.Answer;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.Quiz;

/**
 * Created by Austin on 4/19/2017.
 */

public class QuizUserFragment extends Fragment implements
        AnswerFragmentUser.AnswerListener,
        QuizGET.QuizGETController {


    private String userID;
    private String quizID;
    private String courseID;
    private String token;
    private boolean onGoingQuiz = false;

    private Quiz curQuiz;
    private Question curQuestion;

    private Button submit;
    private TextView questionView;
    private TextView quizTimeView;
    private QuizGET quizGET;

    private QuizController qController;

    interface QuizController {
        void submitQuiz(Quiz quiz);
        QuizGET getQuizGET();
        void startQuizTimer();
    }

    public void setQuizInfo(String quizID, String userID, String courseID) {
        this.quizID = quizID;
        this.userID = userID;
        this.courseID = courseID;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        qController = (QuizController) getActivity();
        quizGET = qController.getQuizGET();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);

        quizTimeView = (TextView) view.findViewById(R.id.quiz_timer_text);
        questionView = (TextView) view.findViewById(R.id.question_text_view);
        submit = (Button) view.findViewById(R.id.submit_button);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qController.submitQuiz(curQuiz);
            }
        });



            quizGET.setController(this);
            quizGET.getToken(quizID);



        currQuestion();
        return view;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
        quizGET.getQuiz(userID, courseID, quizID, token);
    }

    @Override
    public void setQuiz(Quiz quiz) {
        curQuiz = quiz;
        currQuestion();


        startTimer();
    }

    public int getQuizTime() {
        return curQuiz.getTimedLength();
    }


    public void updateGUITimer(int minutesLeft, int secondsLeft) {
        String time = String.format(Locale.getDefault(), "%02d", minutesLeft) +
                String.format(Locale.getDefault(), ":%02d", secondsLeft);
        if (minutesLeft > curQuiz.getTimedLength() / 2) {
            quizTimeView.setTextColor(Color.GREEN);
        } else if (minutesLeft > curQuiz.getTimedLength() / 2 / 2) {
            quizTimeView.setTextColor(Color.YELLOW);
        } else {
            quizTimeView.setTextColor(Color.RED);
        }

        quizTimeView.setText(time);
    }

    void startTimer() {
        qController.startQuizTimer();
    }

    @Override
    public Question getQuestion() {
        return curQuestion;
    }

    @Override
    public void currQuestion() {
        if(curQuiz == null) {
            setBSQuiz();
        }
        curQuestion = curQuiz.getQuestion();
        questionView.setText(curQuestion.getQuestion());

        AnswerFragmentUser answerFragment = new AnswerFragmentUser();
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.answer_segment, answerFragment);
        ft.commit();
    }

    @Override
    public void nextQuestion() {
        if (curQuiz == null) {
            setBSQuiz();
        }
        curQuestion = curQuiz.getNextQuestion();
        questionView.setText(curQuestion.getQuestion());

        // create instance of the answer fragment
        AnswerFragmentUser answerFrag = new AnswerFragmentUser();
        // load answer fragment into answerSection of QuizActivityUser
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.answer_segment, answerFrag);
        ft.commit();
    }

    @Override
    public void prevQuestion() {
        if(curQuiz == null) {
            setBSQuiz();
        }
        curQuestion = curQuiz.getPrevQuestion();
        questionView.setText(curQuestion.getQuestion());

        AnswerFragmentUser answerFragment = new AnswerFragmentUser();
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.answer_segment, answerFragment);
        ft.commit();
    }

    public void setBSQuiz() {
        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("A", "1", 0));
        answers.add(new Answer("B", "2", 1));
        answers.add(new Answer("C", "3", 2));
        answers.add(new Answer("D", "4", 3));
        Question question = new Question("id", "title", "What is log_10 1000", 22, answers);
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);
        setQuiz(new Quiz("ddd", "description", "what is log_10 1000?", "now", "never",
                questions, 0));
    }
}
