package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        db.execSQL(createQuizTable());

        Log.d(TAG, "Database quiz tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_QUIZ);
        onCreate(db);
    }

    // add new quiz to database
    public void addQuiz(Quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(createQuizTable());

        // @TODO if quiz with same Id exists don't crash but ignore

        ContentValues values = QuizCursorWrapper.createQuizValues(quiz);
        // inserting row
        long id = db.insert(TableSchema.TABLE_QUIZ ,null, values);
        db.close();

        Log.d(TAG, "New quiz inserted into sqlite: " + id + quiz.toString());
    }

    public Quiz getQuiz(String quizId) {
        Quiz quiz = null;
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_QUIZ +
                " WHERE _id = \"" + quizId + "\"";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        QuizCursorWrapper qCursor = new QuizCursorWrapper(cursor, context);
        quiz = qCursor.getQuiz();

        Log.d(TAG, "Fectching quiz from Sqlite: " + quiz.toString());
        cursor.close();
        db.close();

        return quiz;
    }

    public void removeQuiz(String quizId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TableSchema.TABLE_QUIZ +
                " WHERE " + QuizSchema.KEY_QID + "=\"" + quizId + "\";");
        db.close();
        Log.d(TAG, "Removed quiz from Sqlite: " + quizId);
    }

    public void removeAllQuizzes() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TableSchema.TABLE_QUIZ + ";");
        db.close();
        Log.d(TAG, "Removed all quizzes from Sqlite.");
    }

}
