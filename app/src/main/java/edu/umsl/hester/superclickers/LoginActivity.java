package edu.umsl.hester.superclickers;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



/**
 * Created by Austin on 2/2/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button login;
    private Button send;
    private EditText userName;

    private AsyncTask asdf;
    private Handler mHandler;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = (Button) findViewById(R.id.buttonLogin);
        send = (Button) findViewById(R.id.buttonSend);
        userName = (EditText) findViewById(R.id.editName);

        login.setOnClickListener(this);
        send.setOnClickListener(this);


    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.buttonLogin:
                /*
                    Login button attempts to connect to server found in InstantMessager
                    app. Run Server2.java before attempting to connect
                 */
                asdf = new ConnectAsyncTask(getmHandler()).execute(userName.getText().toString());

                //login.setEnabled(false);
                //user = new User(userName.getText().toString(), userName.getText().toString(), 85);

                break;
            case R.id.buttonSend:



                Intent quizIntent = new Intent(LoginActivity.this, QuizActivity.class);
                quizIntent.putExtra("USER_NAME", userName.getText().toString());
                startActivity(quizIntent);
                break;
        }
    }

    private Handler getmHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        userName.setText(ConnectAsyncTask.serverMessage);
                        break;
                    case 11:
                        userName.setText("could not connect");
                        break;
                    case 67:
                        userName.setText("..");
                        break;
                    case 69:
                        userName.setText("....");
                        break;
                    case 87:
                        userName.setText("........");
                        break;
                    default:
                        userName.setText(msg.toString());
                }
            }
        };
        return mHandler;
    }


}
