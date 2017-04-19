package edu.umsl.superclickers.activity.quiz;


import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.home.HomeActivity;
import edu.umsl.superclickers.quizdata.Answer;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.Quiz;
import edu.umsl.superclickers.userdata.User;


public class QuizActivityUser extends AppCompatActivity implements
        View.OnClickListener,
        AnswerFragmentUser.AnswerListener,
        QuizGET.QuizGETController {

    private User user;
    private String userID;
    private String quizID;
    private String courseID;
    private String token;

    private Quiz curQuiz;
    private Question curQuestion;

    private Button submit;
    private TextView questionView;
    private QuizGET quizGET;

    @Override
    public void onClick(View view) {
        Intent quizIntent = new Intent(QuizActivityUser.this, HomeActivity.class);
        Toast.makeText(getApplicationContext(), "Quiz Submitted", Toast.LENGTH_LONG).show();
        startActivity(quizIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        questionView = (TextView) findViewById(R.id.question_text_view);
        submit = (Button) findViewById(R.id.submit_button);
        submit.setOnClickListener(this);
        Intent intent = getIntent();
        quizID = intent.getStringExtra("QUIZ_ID");
        userID = intent.getStringExtra("USER_ID");
        courseID = intent.getStringExtra("COURSE_ID");

        quizGET = new QuizGET();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .add(quizGET, "QUIZ_GET")
                .commit();

        // Load quiz
        quizGET.getToken(quizID);

        // @TODO save quiz to SQLite

        currQuestion();
    }


    @Override
    public void setQuiz(Quiz quiz) {
        curQuiz = quiz;
        currQuestion();
    }

    @Override
    public void setToken(String token) {
        this.token = token;
        quizGET.getQuiz(userID, courseID, quizID, token);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("QUIZ", curQuiz);

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
