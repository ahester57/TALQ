package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import edu.umsl.superclickers.quizdata.Quiz;
import edu.umsl.superclickers.userdata.User;

/**
 * Created by Austin on 4/18/2017.
 */

public class SQLiteHandlerQuizzes extends SQLiteOpenHelper {

    private final String TAG = getClass().getSimpleName();

    private static final String DB_NAME = "tbl-quiz";
    private static final int DB_VERSION = 1;

    private static SQLiteHandlerQuizzes sPersistence;

    public static SQLiteHandlerQuizzes sharedInstance(Context context) {
        if (sPersistence == null) {
            sPersistence = new SQLiteHandlerQuizzes(context);
        }
        return sPersistence;
    }

    public SQLiteHandlerQuizzes(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
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

        // @TODO resume quiz
        //db.query();
        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_QUIZ);
        db.execSQL(createQuizTable());




        ContentValues values = new ContentValues();
        values.put(QuizSchema.KEY_SESSION_ID, quiz.getSessionId());
        values.put(QuizSchema.KEY_QID, quiz.get_id());
        values.put(QuizSchema.KEY_DESC, quiz.getDescription());
        values.put(QuizSchema.KEY_TEXT, quiz.getText());
        values.put(QuizSchema.KEY_AVAIL_DATE, quiz.getAvailableDate());
        values.put(QuizSchema.KEY_EXPIRY_DATE, quiz.getExpiryDate());
        values.put(QuizSchema.KEY_TIMED, quiz.getTimed());
        values.put(QuizSchema.KEY_LENGTH, quiz.getTimedLength());
        // inserting row
        long id = db.insert(TableSchema.TABLE_QUIZ ,null, values);
        db.close();

        Log.d(TAG, "New quiz inserted into sqlite: " + id + quiz.toString());
    }

    public void resumeQuiz() {

    }
}
