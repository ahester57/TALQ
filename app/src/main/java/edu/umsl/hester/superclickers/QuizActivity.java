package edu.umsl.hester.superclickers;


import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener, AnswerFragment.AnswerListener{

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


        curQuestion = new Question();

        questionView.setText(curQuestion.getQuestion());


        test.setOnClickListener(this);



        // create instance of the answer fragment
        AnswerFragment answerFrag = new AnswerFragment();

        // load answer fragment into answerSection of QuizActivity
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.answerSection, answerFrag);
        ft.commit();

    }

    // put button listeners here
    @Override
    public void onClick(View view) {
        // handle button clicks
        switch(view.getId()) {
            case(R.id.testButton):
                boolean flag = true;
                try {
                    ClassDb entry = new ClassDb(QuizActivity.this);
                    entry.open();
                    entry.createEntry("Farmer", "3494903");
                    entry.close();
                } catch (Exception e) {
                    flag = false;
                } finally {
                    if (flag) {
                        Dialog d = new Dialog(this);
                        d.setTitle("Success");
                        TextView tv = new TextView(this);
                        tv.setText("User added");
                        d.setContentView(tv);
                        d.show();
                    } else {
                        Dialog d = new Dialog(this);
                        d.setTitle("Fail");
                        TextView tv = new TextView(this);
                        tv.setText("User not added");
                        d.setContentView(tv);
                        d.show();
                    }
                }
                break;
        }
    }


    // returns current question
    @Override
    public Question getQuestion() {
        return curQuestion;
    }

    @Override
    public void nextQuestion() {
        curQuestion = new Question();
        questionView.setText(curQuestion.getQuestion());
        AnswerFragment answerFrag = new AnswerFragment();

        // load answer fragment into answerSection of QuizActivity
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.answerSection, answerFrag);
        ft.commit();
    }
}
