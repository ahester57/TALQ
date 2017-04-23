package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import edu.umsl.superclickers.database.schema.AnswerSchema;
import edu.umsl.superclickers.database.schema.TableSchema;
import edu.umsl.superclickers.quizdata.Answer;

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

        ContentValues values = AnswerCursorWrapper.createQuestionValues(answer);

        // inserting row
        long id = db.insert(TableSchema.TABLE_ANSWER ,null, values);
        db.close();

        Log.d(TAG, "New answer inserted into sqlite: " + id + answer.toString());
    }

    public ArrayList<Answer> getAnswers(String questionId) {
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_ANSWER +
                " WHERE " + AnswerSchema.KEY_QUESTION_ID + " = \"" + questionId + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        AnswerCursorWrapper aCursor = new AnswerCursorWrapper(cursor);

        ArrayList<Answer> answers = aCursor.getAnswers();

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

    public void removeAllAnswers() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TableSchema.TABLE_ANSWER + ";");
        db.close();
    }
}
