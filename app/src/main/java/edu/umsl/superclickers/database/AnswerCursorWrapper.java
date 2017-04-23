package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;

import edu.umsl.superclickers.quizdata.Answer;

/**
 * Created by Austin on 4/22/2017.
 *
 */

class AnswerCursorWrapper extends CursorWrapper {

    AnswerCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    ArrayList<Answer> getAnswers() {
        ArrayList<Answer> answers = new ArrayList<>();
        moveToFirst();
        if (getCount() > 0) {
            do {
                String _id = getString(1);
                String value = getString(2);
                String text = getString(3);
                int sort = getInt(4);
                answers.add(new Answer(value, text, sort, _id));
            } while (moveToNext());
        }
        return answers;
    }

    static ContentValues createQuestionValues(Answer answer) {
        ContentValues values = new ContentValues();
        values.put(AnswerSchema.KEY_QUESTION_ID, answer.getQuestionId());
        values.put(AnswerSchema.KEY_VALUE, answer.getValue());
        values.put(AnswerSchema.KEY_TEXT, answer.getText());
        values.put(AnswerSchema.KEY_SORT_ORDER, answer.getSortOrder());

        return values;
    }
}
