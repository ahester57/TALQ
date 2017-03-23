package edu.umsl.hester.superclickers.activity.login;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import edu.umsl.hester.superclickers.app.SessionManager;
import edu.umsl.hester.superclickers.database.SQLiteHandler;

/**
 * Created by Austin on 3/21/2017.
 */

public class LoginController extends Fragment {


    private Context lContext;

    private ProgressDialog pDialog; //////
    private SessionManager session;
    private SQLiteHandler db;

    public void setContext(Context context) {
        lContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // @TODO put str reqs here
    }



    ///////////////////
}
