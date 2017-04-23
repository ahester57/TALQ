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
        db.execSQL(createQuestionTable());

        Log.d(TAG, "Database question tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_QUESTION);
        onCreate(db);
    }

    // add new question to database
    public void addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(createQuestionTable());

        ContentValues values = new ContentValues();
        values.put(QuestionSchema.KEY_SESSION_ID, question.getSessionId());
        values.put(QuestionSchema.KEY_QUID, question.get_id());
        values.put(QuestionSchema.KEY_TITLE, question.getTitle());
        values.put(QuestionSchema.KEY_TEXT, question.getText());
        values.put(QuestionSchema.KEY_POINTS_POSS, question.getPointsPossible());

        // inserting row
        long id = db.insert(TableSchema.TABLE_QUESTION ,null, values);
        db.close();

        Log.d(TAG, "New question inserted into sqlite: " + id + question.toString());
    }

    public ArrayList<Question> getQuestions(String sessionId) {
        ArrayList<Question> questions = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_QUESTION +
                " WHERE " + QuestionSchema.KEY_SESSION_ID + " = \"" + sessionId + "\"";

        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteHandlerAnswers adb = SQLiteHandlerAnswers.sharedInstance(context);
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                String _id = cursor.getString(2);
                String title = cursor.getString(3);
                String text = cursor.getString(4);
                int points = cursor.getInt(5);
                ArrayList<Answer> answers = adb.getAnswers(_id);
                questions.add(new Question(_id, title, text, points, answers));
                // @TODO get add answers
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Fectching question from Sqlite: " + questions.toString());
        return questions;
    }

    public void removeQuestion(String questionId) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TableSchema.TABLE_QUESTION +
                " WHERE " + QuestionSchema.KEY_QUID + "=\"" + questionId + "\";");
        db.close();
    }

    public void removeAllQuestions() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + TableSchema.TABLE_QUESTION + ";");
        db.close();
    }
}