package edu.umsl.hester.superclickers;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizActivity extends AppCompatActivity implements AnswerFragment.AnswerListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        // test buttons and whatnot
        Button test = (Button) findViewById(R.id.testButton);
        final TextView asdf = (TextView) findViewById(R.id.asdf);

        test.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                asdf.setText(R.string.testButton);
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
        return "Aasdfa";
    }

    @Override
    public String getB() {
        return "B";
    }

    @Override
    public String getC() {
        return "C";
    }

    @Override
    public String getD() {
        return "D";
    }
}
