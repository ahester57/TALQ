package edu.umsl.hester.superclickers.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Austin on 2/4/2017.
 * 
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // all static
    private static final int DB_VERSION = 1;

    // db name
    private static final String DB_NAME = "android_api";
    // table name
    private static final String TABLE_USER = "users";
    private static final String TABLE_GROUP = "groups";
    private static final String TABLE_USER_GROUPS = "user_groups";
    // table columns
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_GUID = "guid";
    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                    + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
                    + KEY_EMAIL + " TEXT UNIQUE, " + KEY_UID + " TEXT, "
                    + KEY_CREATED_AT + " TEXT" + ")";
        String CREATE_GROUP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUP + "("
                    + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
                    + KEY_GUID + " TEXT UNIQUE, " + KEY_CREATED_AT + " TEXT" + ")";
        String CREATE_RELATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_GROUPS
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_UID + " TEXT, "
                    + KEY_GUID + " TEXT, " + KEY_CREATED_AT + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE);
        db.execSQL(CREATE_GROUP_TABLE);
        db.execSQL(CREATE_RELATION_TABLE);

        Log.d(TAG, "Database tables created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_GROUPS);
        // drop old tables and...
        // create them again
        onCreate(db);
    }

    // add new user to database
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_LOGIN_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
                + KEY_EMAIL + " TEXT UNIQUE, " + KEY_UID + " TEXT, "
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_UID, uid);
        values.put(KEY_CREATED_AT, created_at);

        // inserting row
        long id = db.insert(TABLE_USER ,null, values);
        db.close();

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    // ad new group to database
    public void addGroup(String name, String guid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();



        String CREATE_GROUP_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_GROUP + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_NAME + " TEXT, "
                + KEY_GUID + " TEXT UNIQUE, " + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_GROUP_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_GUID, guid);
        values.put(KEY_CREATED_AT, created_at);

        //insert row
        long id = db.insert(TABLE_GROUP, null, values);
        db.close();

        Log.d(TAG, "New group inserted into sqlite: " + id);
    }

    // add user to a group
    public void addUserToGroup(String uid, String guid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        String CREATE_RELATION_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_GROUPS
                + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_UID + " TEXT, "
                + KEY_GUID + " TEXT, " + KEY_CREATED_AT + " TEXT" + ")";

        db.execSQL(CREATE_RELATION_TABLE);
        ContentValues values = new ContentValues();
        values.put(KEY_UID, uid);
        values.put(KEY_GUID, guid);
        values.put(KEY_CREATED_AT, created_at);

        //insert row
        long id = db.insert(TABLE_USER_GROUPS, null, values);
        db.close();

        Log.d(TAG, "User " + uid + " added to group " + guid);

    }

    /// get user data
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("name", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("uid", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();

        Log.d(TAG, "Fectching user from Sqlite: " + user.toString());

        return user;
    }


    public String getGroupId(String name) {
        // @TODO
        return name;
    }

    public String[] getAllGroups() {
        // @TODO
        return new String[] {"abra", "kadabra", "alakazam"};
    }

    private void deleteUsersByEmail(String... emails) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, "WHERE email = ", emails);
        db.close();

        Log.d(TAG, "Deleted " + Arrays.toString(emails) + "from database" + TABLE_USER);
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete all users
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all users from database" + TABLE_USER);
    }


}
