package edu.umsl.hester.superclickers;

import android.os.Bundle;
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
    private EditText userName;

    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        login = (Button) findViewById(R.id.buttonLogin);
        userName = (EditText) findViewById(R.id.editName);

        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.buttonLogin:
                user = new User(userName.getText().toString(), userName.getText().toString(), 85);

                break;
        }
    }
}
