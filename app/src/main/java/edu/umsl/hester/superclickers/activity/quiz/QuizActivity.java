package edu.umsl.hester.superclickers.activity.quiz;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import edu.umsl.hester.superclickers.R;
import edu.umsl.hester.superclickers.quizdata.Answer;
import edu.umsl.hester.superclickers.quizdata.Question;
import edu.umsl.hester.superclickers.quizdata.Quiz;
import edu.umsl.hester.superclickers.userdata.User;


public class QuizActivity extends AppCompatActivity implements View.OnClickListener, AnswerFragment.AnswerListener {

    private User user;

    private Quiz curQuiz;
    private Question curQuestion;

    private Button test;
    private TextView questionView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        // test buttons and whatnot
        test = (Button) findViewById(R.id.testButton);
        questionView = (TextView) findViewById(R.id.questionView);

        test.setOnClickListener(this);


        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("id", "A", "1", 0));
        answers.add(new Answer("id", "B", "2", 1));
        answers.add(new Answer("id", "C", "3", 2));
        answers.add(new Answer("id", "D", "4", 3));
        Question question = new Question("id", "title", "What is log_10 1000", 22, answers);
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);
        curQuiz = new Quiz("ddd", "description", "what is log_10 1000?", "now", "never",
                questions, 0);
        nextQuestion();
        
        // Load quiz


        nextQuestion();
    }

    // put button listeners here
    @Override
    public void onClick(View view) {
        // handle button clicks
        switch (view.getId()) {
            case (R.id.testButton):

                nextQuestion();

        }
    }


    // returns current question
    @Override
    public Question getQuestion() {
        return curQuestion;
    }

    @Override
    public void nextQuestion() {
        curQuestion = curQuiz.getNextQuestion();

        questionView.setText(curQuestion.getQuestion());

        // create instance of the answer fragment
        AnswerFragment answerFrag = new AnswerFragment();
        // load answer fragment into answerSection of QuizActivity
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.answerSection, answerFrag);
        ft.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("QUIZ", curQuiz);

    }
}
