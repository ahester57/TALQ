package edu.umsl.superclickers.database;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import java.util.ArrayList;

import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.Quiz;

/**
 * Created by Austin on 3/21/2017.
 */

public class QuizCursorWrapper extends CursorWrapper {

    private Context context;
    private SQLiteHandlerQuestions qdb;

    public QuizCursorWrapper(Cursor cursor, Context context) {
        super(cursor);
        this.context = context;
        this.qdb = SQLiteHandlerQuestions.sharedInstance(context);
    }

    public Quiz getQuiz() {
        Quiz quiz = null;

        moveToFirst();
        if (getCount() > 0) {
            String sessionId = getString(1);
            String _id = getString(2);
            String desc = getString(3);
            String text = getString(4);
            String avail = getString(5);
            String expiry = getString(6);
            boolean timed = getInt(7) > 0;
            int length = getInt(8);

            ArrayList<Question> questions = qdb.getQuestions(sessionId);
            quiz = new Quiz(_id, desc, text, avail, expiry, questions, sessionId, timed, length);
            // @TODO get add questions
        }
        return quiz;
    }


}
