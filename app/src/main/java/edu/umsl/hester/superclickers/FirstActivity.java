package edu.umsl.hester.superclickers;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FirstActivity extends AppCompatActivity implements AnswerFragment.AnswerListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


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

        // load answer fragment into answerSection of FirstActivity
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.answerSection, answerFrag);
        ft.commit();

    }

    @Override
    public int getTest() {
        return 2;
    }
}
