package edu.umsl.superclickers.activity.quiz.helper;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Austin on 4/19/2017.
 */

public class QuizService extends Service {
    private final static String TAG = QuizService.class.getSimpleName();

    private int quizTime = 20;
    private String quizId;
    private CountDownTimer quizTimer = null;

    public static final String COUNTDOWN_BR = "edu.umsl.superclickers.app.countdown_br";
    private Intent qI = new Intent(COUNTDOWN_BR);

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getExtras() != null) {
            quizTime = intent.getIntExtra("QUIZ_TIME", 10);
            quizId = intent.getStringExtra("QUIZ_ID");
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Starting quiz timer.");

        quizTimer = new CountDownTimer((quizTime * 60 * 1000) + 2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "Countdown seconds remaining: " + millisUntilFinished / 1000);
                qI.putExtra("countdown", millisUntilFinished);
                sendBroadcast(qI);
            }

            @Override
            public void onFinish() {
                Log.d(TAG, "Quiz timer finished.");
                //session.clearDatabase();
                stopSelf();
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
        return null;
    }
}
