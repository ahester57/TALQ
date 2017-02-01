package edu.umsl.hester.superclickers;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity implements AnswerFragment.AnswerListener{

    private Question curQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        // test buttons and whatnot
        final Button test = (Button) findViewById(R.id.testButton);
        final TextView questionView = (TextView) findViewById(R.id.questionView);


        curQuestion = new Question();

        questionView.setText(curQuestion.getQuestion());


        test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                test.setEnabled(false);
            }
        });



        // create instance of the answer fragment
        AnswerFragment answerFrag = new AnswerFragment();

        // load answer fragment into answerSection of QuizActivity
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.answerSection, answerFrag);
        ft.commit();

    }


    // Eventually, the following methods (located elsewhere, this is for concept) will
    // return the potential answers to the given question
    @Override
    public String getA() {
        return curQuestion.getA();
    }

    @Override
    public String getB() {
        return curQuestion.getB();
    }

    @Override
    public String getC() {
        return curQuestion.getC();
    }

    @Override
    public String getD() {
        return curQuestion.getD();
    }

    @Override
    public String getAnswer() {
        return curQuestion.getAnswer();
    }
}
