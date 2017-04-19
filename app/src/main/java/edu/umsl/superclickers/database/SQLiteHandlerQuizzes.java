package edu.umsl.superclickers.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Austin on 4/18/2017.
 */

public class SQLiteHandlerQuizzes extends SQLiteOpenHelper {

    private static final String DB_NAME = "tbl-quiz";
    private static final int DB_VERSION = 1;

    public SQLiteHandlerQuizzes(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
