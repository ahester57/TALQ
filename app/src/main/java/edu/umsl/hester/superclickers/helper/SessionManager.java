package edu.umsl.hester.superclickers.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;

import edu.umsl.hester.superclickers.app.LoginConfig;


public class SessionManager {

    private static String TAG = SessionManager.class.getSimpleName();

    SharedPreferences pref;

    Context _context;

    int PRIVATE_MODE = 0;

    //file name
    private static final String PREF_NAME = "AndroidQuizLogin";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
    }

    // @TODO disconnect (logout) if no interent connection

    public void setLogin(boolean isLoggedIn) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.apply();

        Log.d(TAG, "User login session changed");
    }

    public boolean isLoggedIn() {
        Thread test = new Thread(new Runnable() {
            @Override
            public void run() {
                InetAddress inet = null;
                try {
                    inet = InetAddress.getByName(LoginConfig.URL_LOGIN);

                } catch (IOException e) {
                    //setLogin(false);
                }
            }
        });
        //test.start();




        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}
