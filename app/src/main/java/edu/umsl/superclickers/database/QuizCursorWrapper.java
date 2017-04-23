package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;

import edu.umsl.superclickers.database.schema.QuizSchema;
import edu.umsl.superclickers.quizdata.Question;
import edu.umsl.superclickers.quizdata.Quiz;

/**
 * Created by Austin on 3/21/2017.
 *
 */

class QuizCursorWrapper extends CursorWrapper {

    private SQLiteHandlerQuestions qdb;

    QuizCursorWrapper(Cursor cursor, Context context) {
        super(cursor);
        this.qdb = SQLiteHandlerQuestions.sharedInstance(context);
    }

    Quiz getQuiz() {
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
        }
        return quiz;
    }

    static ContentValues createQuizValues(Quiz quiz) {
        ContentValues values = new ContentValues();
        values.put(QuizSchema.KEY_SESSION_ID, quiz.getSessionId());
        values.put(QuizSchema.KEY_QID, quiz.get_id());
        values.put(QuizSchema.KEY_DESC, quiz.getDescription());
        values.put(QuizSchema.KEY_TEXT, quiz.getText());
        values.put(QuizSchema.KEY_AVAIL_DATE, quiz.getAvailableDate());
        values.put(QuizSchema.KEY_EXPIRY_DATE, quiz.getExpiryDate());
        values.put(QuizSchema.KEY_TIMED, quiz.getTimed());
        values.put(QuizSchema.KEY_LENGTH, quiz.getTimedLength());

        return values;
    }

}
