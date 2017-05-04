package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import edu.umsl.superclickers.database.schema.AnswerSchema;
import edu.umsl.superclickers.database.schema.TableSchema;
import edu.umsl.superclickers.quizdata.Answer;
import edu.umsl.superclickers.quizdata.SelectedAnswer;

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

    private String createSelectedAnswerTable() {
        return "CREATE TABLE IF NOT EXISTS " + TableSchema.TABLE_SELECTED_ANSWER + "("
                + AnswerSchema.KEY_ID + " INTEGER PRIMARY KEY, "
                + AnswerSchema.KEY_QUESTION_ID + " TEXT, "
                + AnswerSchema.KEY_VALUE + " TEXT, "
                + AnswerSchema.KEY_ALLOCATED_POINTS + " INTEGER"+ ")";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(createAnswerTable());
            db.execSQL(createSelectedAnswerTable());
            Log.d(TAG, "Database answer tables created");
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't create answers table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_ANSWER);
            db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_SELECTED_ANSWER);
            onCreate(db);
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't create answers table");
        }
    }

    // add new answer to database
    public void addAnswer(Answer answer) {


        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(createAnswerTable());

            ContentValues values = AnswerCursorWrapper.createAnswerValues(answer);

            // inserting row
            long id = db.insert(TableSchema.TABLE_ANSWER ,null, values);
            db.close();

            Log.d(TAG, "New answer inserted into sqlite: " + id + answer.toString());
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't add answers");
        }
    }

    // add new selected answer to database
    public void addSelectedAnswer(SelectedAnswer answer) {


        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(createSelectedAnswerTable());

            ContentValues values = AnswerCursorWrapper.createSelectedAnswerValues(answer);

            // inserting row
            long id = db.insert(TableSchema.TABLE_SELECTED_ANSWER ,null, values);
            db.close();
            Log.d(TAG, "Selected answer inserted into sqlite: " + id + answer.toString());
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't add selected answers");
        }

    }

    public ArrayList<Answer> getAnswers(String questionId) {
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_ANSWER +
                " WHERE " + AnswerSchema.KEY_QUESTION_ID + " = \"" + questionId + "\"";

        ArrayList<Answer> answers = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            AnswerCursorWrapper aCursor = new AnswerCursorWrapper(cursor);

            answers = aCursor.getAnswers();

            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Log.d(TAG, "dang no  answers");
        }
        Log.d(TAG, "Fectching answers from Sqlite: " + answers.toString());
        return answers;
    }

    public ArrayList<SelectedAnswer> getSelectedAnswers(String questionId) {
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_SELECTED_ANSWER +
                " WHERE " + AnswerSchema.KEY_QUESTION_ID + " = \"" + questionId + "\"";

        ArrayList<SelectedAnswer> answers = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            AnswerCursorWrapper aCursor = new AnswerCursorWrapper(cursor);

            answers = aCursor.getSelectedAnswers();

            cursor.close();
            db.close();
            Log.d(TAG, "Fectching selected answers from Sqlite: " + answers.toString());
        } catch (SQLiteException e) {
            Log.d(TAG, "dang no selected answers");
        }
        return answers;
    }

    public void removeSelectedFromQuestion(String questionId) {


        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TableSchema.TABLE_SELECTED_ANSWER +
                    " WHERE " + AnswerSchema.KEY_QUESTION_ID + "=\"" + questionId + "\";");
            db.close();
        } catch (SQLiteException e) {
            Log.d(TAG, "dang no selected answers");
        }
    }

    public void removeAllAnswers() {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TableSchema.TABLE_ANSWER + ";");
            db.execSQL("DELETE FROM " + TableSchema.TABLE_SELECTED_ANSWER + ";");
            db.close();
        } catch (SQLiteException e) {
            Log.d(TAG, "dang no answers");
        }
    }
}
