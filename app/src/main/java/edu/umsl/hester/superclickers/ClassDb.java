package edu.umsl.hester.superclickers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Austin on 2/1/2017.
 */

class ClassDb {

    static final String KEY_ROWID = "_id";
    static final String KEY_NAME = "name";
    static final String KEY_STUID = "student_id";

    private static final String DB_NAME = "class_db";
    private static final String DB_TABLE = "students";
    private static final int DB_VERSION = 1;

    private DbHelper dbHelper;
    private final Context ourContext;
    private SQLiteDatabase ourDatabase;

    private static class DbHelper extends SQLiteOpenHelper {

        DbHelper (Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DB_TABLE + " (" + KEY_ROWID +
                    " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME +
                    " TEXT NOT NULL, " + KEY_STUID + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }

    ClassDb(Context c) {
        ourContext = c;
    }

    ClassDb open() {
        dbHelper = new DbHelper(ourContext);
        ourDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    void close() {
        dbHelper.close();
    }

    // add user to database
    long createEntry(String name, String stuID) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, name);
        cv.put(KEY_STUID, stuID);
        return ourDatabase.insert(DB_TABLE, null, cv);
    }
}
