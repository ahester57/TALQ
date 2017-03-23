package edu.umsl.hester.superclickers.activity.quiz;


import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;
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


public class QuizActivity extends AppCompatActivity implements
        View.OnClickListener, AnswerFragment.AnswerListener,
        QuizGET.QuizGETController {

    private User user;
    private String quizID;
    private Quiz curQuiz;
    private Question curQuestion;

    private Button test;
    private TextView questionView;

    private QuizGET quizGET;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        questionView = (TextView) findViewById(R.id.questionView);


        Intent intent = getIntent();
        quizID = intent.getStringExtra("QUIZ_ID");


        quizGET = new QuizGET();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(quizGET, "QUIZ_GET")
                .commit();

        // Load quiz
        //quizGET.getQuiz("8e21fdc6-2a2a-4023-9a32-6313b3e142b1");    //danger quiz
        //quizGET.getQuiz("0784ae31-8aa4-43c1-9dca-df33a2d5053e");  //superhero quiz
        quizGET.getQuiz(quizID);



        nextQuestion();
    }

    // put button listeners here
    @Override
    public void onClick(View view) {
        // handle button clicks
        switch (view.getId()) {


        }
    }

    @Override
    public void setQuiz(Quiz quiz) {
        curQuiz = quiz;
        nextQuestion();
    }

    public void setBSQuiz() {
        ArrayList<Answer> answers = new ArrayList<>();
        answers.add(new Answer("id", "A", "1", 0));
        answers.add(new Answer("id", "B", "2", 1));
        answers.add(new Answer("id", "C", "3", 2));
        answers.add(new Answer("id", "D", "4", 3));
        Question question = new Question("id", "title", "What is log_10 1000", 22, answers);
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(question);
        setQuiz(new Quiz("ddd", "description", "what is log_10 1000?", "now", "never",
                questions, 0));
    }

    // returns current question
    @Override
    public Question getQuestion() {
        return curQuestion;
    }

    @Override
    public void nextQuestion() {
        if (curQuiz == null) {
            setBSQuiz();
        }
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
