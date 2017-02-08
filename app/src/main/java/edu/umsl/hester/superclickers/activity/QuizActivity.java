package edu.umsl.hester.superclickers.activity;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.umsl.hester.superclickers.R;
import edu.umsl.hester.superclickers.app.Question;
import edu.umsl.hester.superclickers.app.Quiz;
import edu.umsl.hester.superclickers.app.User;


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



        // Load quiz
        if (savedInstanceState != null) {
            curQuiz = new Quiz();
        } else {
            curQuiz = new Quiz();
        }

        nextQuestion();
    }

    // put button listeners here
    @Override
    public void onClick(View view) {
        // handle button clicks
        switch (view.getId()) {
            case (R.id.testButton):
                curQuiz = new Quiz();
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
