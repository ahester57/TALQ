package edu.umsl.hester.superclickers;

import android.content.Intent;
import android.os.AsyncTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;



/**
 * Created by Austin on 2/2/2017.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button login;
    private Button send;
    private EditText userName;
    private TextView serverText;

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
        serverText = (TextView) findViewById(R.id.serverText);

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


                login.setEnabled(false);
                //user = new User(userName.getText().toString(), userName.getText().toString(), 85);

                break;
            case R.id.buttonSend:
//                Thread thread = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Connect();
//                    }
//                });
//                thread.start();


                Intent quizIntent = new Intent(LoginActivity.this, QuizActivity.class);
                quizIntent.putExtra("USER_NAME", userName.getText().toString());
                startActivity(quizIntent);
                break;
        }
    }


    public void Connect() {
        try {
            Socket socket = new Socket("192.168.1.125", 39909);
            PrintWriter pw = new PrintWriter(socket.getOutputStream());
            pw.println(userName.getText().toString());
        } catch (Exception ioe) {
            Log.e("Connect", ioe.getMessage());
        }
    }

    /*
        Handles responsed from ConnectAsyncTask
         While change to enums once fully working
     */
    private Handler getmHandler() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        // server message received
                        serverText.setText(ConnectAsyncTask.serverMessage);
                        break;
                    case 11:
                        serverText.setText(R.string.connecting);
                        break;
                    case 67:
                        // After server is done sending data
                        //serverText.setText("Server closed");
                        login.setEnabled(true);
                        break;
                    case 69:
                        // after sent name
                        userName.setText(R.string.pwd);
                        break;
                    case 87:
                        serverText.setText("........");
                        break;
                    default:
                        serverText.setText(msg.toString());
                }
            }
        };
        return mHandler;
    }


}
