package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;

import edu.umsl.superclickers.quizdata.Answer;
import edu.umsl.superclickers.quizdata.Question;

/**
 * Created by Austin on 4/22/2017.
 *
 */

class QuestionCursorWrapper extends CursorWrapper {

    private SQLiteHandlerAnswers adb;

    QuestionCursorWrapper(Cursor cursor, Context context) {
        super(cursor);
        this.adb = SQLiteHandlerAnswers.sharedInstance(context);
    }

    ArrayList<Question> getQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        moveToFirst();
        if (getCount() > 0) {
            do {
                String _id = getString(2);
                String title = getString(3);
                String text = getString(4);
                int points = getInt(5);
                ArrayList<Answer> answers = adb.getAnswers(_id);
                questions.add(new Question(_id, title, text, points, answers));
                // @TODO get add answers
            } while (moveToNext());
        }
        return questions;
    }

    static ContentValues createQuestionValues(Question question) {
        ContentValues values = new ContentValues();
        values.put(QuestionSchema.KEY_SESSION_ID, question.getSessionId());
        values.put(QuestionSchema.KEY_QUID, question.get_id());
        values.put(QuestionSchema.KEY_TITLE, question.getTitle());
        values.put(QuestionSchema.KEY_TEXT, question.getText());
        values.put(QuestionSchema.KEY_POINTS_POSS, question.getPointsPossible());

        return values;
    }
}
