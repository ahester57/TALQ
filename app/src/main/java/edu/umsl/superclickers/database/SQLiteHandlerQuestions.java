package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import edu.umsl.superclickers.database.schema.QuestionSchema;
import edu.umsl.superclickers.database.schema.TableSchema;
import edu.umsl.superclickers.quizdata.Question;

/**
 * Created by Austin on 4/20/2017.
 *
 */

public class SQLiteHandlerQuestions extends SQLiteOpenHelper {


    private final String TAG = getClass().getSimpleName();

    private static final String DB_NAME = "tbl-quiz";
    private static final int DB_VERSION = 1;

    private Context context;
    private static SQLiteHandlerQuestions sPersistence;

    public static SQLiteHandlerQuestions sharedInstance(Context context) {
        if (sPersistence == null) {
            sPersistence = new SQLiteHandlerQuestions(context);
        }
        return sPersistence;
    }

    public SQLiteHandlerQuestions(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    private String createQuestionTable() {

        return "CREATE TABLE IF NOT EXISTS " + TableSchema.TABLE_QUESTION + "("
                + QuestionSchema.KEY_ID + " INTEGER PRIMARY KEY, "
                + QuestionSchema.KEY_SESSION_ID + " TEXT, "
                + QuestionSchema.KEY_QUID + " TEXT, "
                + QuestionSchema.KEY_TITLE + " TEXT, "
                + QuestionSchema.KEY_TEXT + " TEXT, "
                + QuestionSchema.KEY_POINTS_POSS + " INTEGER"+ ")";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(createQuestionTable());
            Log.d(TAG, "Database question tables created");
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't create question table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_QUESTION);
            onCreate(db);
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't create question table");
        }
    }

    // add new question to database
    public void addQuestion(Question question) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(createQuestionTable());
            ContentValues values = QuestionCursorWrapper.createQuestionValues(question);

            // inserting row
            long id = db.insert(TableSchema.TABLE_QUESTION, null, values);
            db.close();
            Log.d(TAG, "New question inserted into sqlite: " + id + question.toString());
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't add question");
        }
    }

    public ArrayList<Question> getQuestions(String sessionId) {
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_QUESTION +
                " WHERE " + QuestionSchema.KEY_SESSION_ID + " = \"" + sessionId + "\"";
        ArrayList<Question> questions = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            QuestionCursorWrapper qCursor = new QuestionCursorWrapper(cursor, context);

            questions = qCursor.getQuestions();

            cursor.close();
            db.close();
            Log.d(TAG, "Fectching question from Sqlite: " + questions.toString());
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't get questions");
        }
        return questions;
    }

    public void removeQuestion(String questionId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TableSchema.TABLE_QUESTION +
                    " WHERE " + QuestionSchema.KEY_QUID + "=\"" + questionId + "\";");
            db.close();
            Log.d(TAG, "deleted question");
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't delete question");
        }
    }

    public void removeAllQuestions() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("DELETE FROM " + TableSchema.TABLE_QUESTION + ";");
            db.close();
            Log.d(TAG, "deleted questions");
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't get questions");
        }
    }
}
