package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import edu.umsl.superclickers.quizdata.Answer;
import edu.umsl.superclickers.quizdata.Question;

/**
 * Created by Austin on 4/20/2017.
 */

public class SQLiteHandlerAnswers extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();

    private static final String DB_NAME = "tbl-quiz";
    private static final int DB_VERSION = 1;

    private static SQLiteHandlerAnswers sPersistence;

    public static SQLiteHandlerAnswers sharedInstance(Context context) {
        if (sPersistence == null) {
            sPersistence = new SQLiteHandlerAnswers(context);
        }
        return sPersistence;
    }

    public SQLiteHandlerAnswers(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private String createAnswerTable() {

        return "CREATE TABLE IF NOT EXISTS " + TableSchema.TABLE_ANSWER + "("
                + AnswerSchema.KEY_ID + " INTEGER PRIMARY KEY, "
                + AnswerSchema.KEY_QUESTION_ID + " TEXT, "
                + AnswerSchema.KEY_VALUE + " TEXT, "
                + AnswerSchema.KEY_TEXT + " TEXT, "
                + AnswerSchema.KEY_SORT_ORDER + " INTEGER"+ ")";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createAnswerTable());

        Log.d(TAG, "Database answer tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_ANSWER);
        onCreate(db);
    }

    // add new answer to database
    public void addAnswer(Answer answer) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(createAnswerTable());

        ContentValues values = new ContentValues();
        values.put(AnswerSchema.KEY_QUESTION_ID, answer.getQuestionId());
        values.put(AnswerSchema.KEY_VALUE, answer.getValue());
        values.put(AnswerSchema.KEY_TEXT, answer.getText());
        values.put(AnswerSchema.KEY_SORT_ORDER, answer.getSortOrder());

        // inserting row
        long id = db.insert(TableSchema.TABLE_ANSWER ,null, values);
        db.close();

        Log.d(TAG, "New answer inserted into sqlite: " + id + answer.toString());
    }

    public ArrayList<Answer> getAnswers(String questionId) {
        ArrayList<Answer> answers = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_ANSWER +
                " WHERE " + AnswerSchema.KEY_QUESTION_ID + " = \"" + questionId + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                String _id = cursor.getString(1);
                String value = cursor.getString(2);
                String text = cursor.getString(3);
                int sort = cursor.getInt(4);
                answers.add(new Answer(value, text, sort, _id));
                // @TODO get add answers
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fectching question from Sqlite: " + answers.toString());
        return answers;
    }

    public void removeAnswersFromQuestion(String questionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TableSchema.TABLE_ANSWER +
                " WHERE " + AnswerSchema.KEY_QUESTION_ID + "=\"" + questionId + "\";");
        db.close();
    }
}
