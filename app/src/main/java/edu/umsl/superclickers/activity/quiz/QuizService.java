package edu.umsl.superclickers.activity.quiz;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Austin on 4/19/2017.
 */

public class QuizService extends Service {

    final String TAG = getClass().getSimpleName();

    private int quizTime = 20;
    CountDownTimer quizTimer = null;

    public static final String COUNTDOWN_BR = "edu.umsl.superclickers.quiz.countdown_br";
    Intent qI = new Intent(COUNTDOWN_BR);


    void setQuizTime(int time) {
        quizTime = time;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() != null) {
            quizTime = intent.getIntExtra("QUIZ_TIME", 20);
        }

        return START_STICKY;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Starting quiz timer.");



        quizTimer = new CountDownTimer(quizTime * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                Log.d(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                qI.putExtra("countdown", millisUntilFinished);
                sendBroadcast(qI);

            }

            @Override
            public void onFinish() {
                Log.d(TAG, "Quiz timer finished.");
            }
        };

        quizTimer.start();

    }

    @Override
    public void onDestroy() {
        quizTimer.cancel();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        quizTime = intent.getIntExtra("QUIZ_NAME", 20);

        return null;
    }
}
