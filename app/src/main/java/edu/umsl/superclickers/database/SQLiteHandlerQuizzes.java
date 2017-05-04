package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.umsl.superclickers.database.schema.QuizSchema;
import edu.umsl.superclickers.database.schema.TableSchema;
import edu.umsl.superclickers.quizdata.Quiz;

/**
 * Created by Austin on 4/18/2017.
 */

public class SQLiteHandlerQuizzes extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();

    private static final String DB_NAME = "tbl-quiz";
    private static final int DB_VERSION = 1;

    private Context context;
    private static SQLiteHandlerQuizzes sPersistence;

    public static SQLiteHandlerQuizzes sharedInstance(Context context) {
        if (sPersistence == null) {
            sPersistence = new SQLiteHandlerQuizzes(context);
        }
        return sPersistence;
    }

    public SQLiteHandlerQuizzes(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    private String createQuizTable() {

        return "CREATE TABLE IF NOT EXISTS " + TableSchema.TABLE_QUIZ + "("
                + QuizSchema.KEY_ID + " INTEGER PRIMARY KEY, "
                + QuizSchema.KEY_SESSION_ID + " TEXT, "
                + QuizSchema.KEY_QID + " TEXT UNIQUE, "
                + QuizSchema.KEY_DESC + " TEXT, "
                + QuizSchema.KEY_TEXT + " TEXT, "
                + QuizSchema.KEY_AVAIL_DATE + " TEXT, "
                + QuizSchema.KEY_EXPIRY_DATE + " TEXT, "
                + QuizSchema.KEY_TIMED + " TEXT, "
                + QuizSchema.KEY_LENGTH + " TEXT"+ ")";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(createQuizTable());
            Log.d(TAG, "Database quiz tables created");
        } catch (SQLiteException e) {
            Log.d(TAG, "Couldn't create quiz table.");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_QUIZ);
            onCreate(db);
        } catch (SQLiteException e) {
            Log.d(TAG, "Couldn't create quiz table.");
        }
    }

    // add new quiz to database
    public void addQuiz(Quiz quiz) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(createQuizTable());

            // @TODO if quiz with same Id exists don't crash but ignore

            ContentValues values = QuizCursorWrapper.createQuizValues(quiz);
            // inserting row
            long id = db.insert(TableSchema.TABLE_QUIZ, null, values);
            db.close();
            Log.d(TAG, "New quiz inserted into sqlite: " + values.getAsString(QuizSchema.KEY_DESC));
        } catch (SQLiteException e) {
            Log.d(TAG, "Couldn't add quiz.");
        }
    }

    public Quiz getQuiz(String quizId) {
        Quiz quiz = null;
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_QUIZ +
                " WHERE _id = \"" + quizId + "\"";

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            QuizCursorWrapper qCursor = new QuizCursorWrapper(cursor, context);
            quiz = qCursor.getQuizzes().get(0);

            if (quiz != null) {
                Log.d(TAG, "Fectching quiz from Sqlite: " + quiz.toString());
            } else {
                Log.e(TAG, "Error fetching quiz from SQlite.");
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Log.d(TAG, "Couldn't get quiz.");
        }
        return quiz;
    }

    public List<Quiz> getQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_QUIZ + ";";

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            QuizCursorWrapper qCursor = new QuizCursorWrapper(cursor, context);
            quizzes = qCursor.getQuizzes();

            if (quizzes != null) {
                Log.d(TAG, "Fectching quizzes from Sqlite: " + quizzes.toString());
            } else {
                Log.e(TAG, "Error fetching quizzes from SQlite.");
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
            Log.d(TAG, "Couldn't get quizzes.");
        }
        return quizzes;
    }

    public void removeQuiz(String quizId) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TableSchema.TABLE_QUIZ +
                    " WHERE " + QuizSchema.KEY_QID + "=\"" + quizId + "\";");
            db.close();
            Log.d(TAG, "Removed quiz from Sqlite: " + quizId);
        } catch (SQLiteException e) {
            Log.d(TAG, "Couldn't remove quiz.");
        }
    }

    public void removeAllQuizzes() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DELETE FROM " + TableSchema.TABLE_QUIZ + ";");
            db.close();
            Log.d(TAG, "Removed all quizzes from Sqlite.");
        } catch (SQLiteException e) {
            Log.d(TAG, "Couldn't remove quizzes.");
        }
    }

}
