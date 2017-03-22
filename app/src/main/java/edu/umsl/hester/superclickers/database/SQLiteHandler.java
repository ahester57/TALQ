package edu.umsl.hester.superclickers.database;

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
    
    // db name
    private static final String DB_NAME = "android_api";
    private static final int DB_VERSION = 1;

    public SQLiteHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private String createLoginTable() {
        return "CREATE TABLE IF NOT EXISTS " + TableSchema.TABLE_USER + "("
                + UserSchema.KEY_ID + " INTEGER PRIMARY KEY, "
                + UserSchema.KEY_NAME + " TEXT, "
                + UserSchema.KEY_EMAIL + " TEXT UNIQUE, "
                + UserSchema.KEY_UID + " TEXT, "
                + UserSchema.KEY_CREATED_AT + " TEXT" + ")";
    }

    private String createGroupTable() {
        return "CREATE TABLE IF NOT EXISTS " + TableSchema.TABLE_GROUP + "("
                + GroupSchema.KEY_ID + " INTEGER PRIMARY KEY, "
                + GroupSchema.KEY_NAME + " TEXT, "
                + GroupSchema.KEY_GUID + " TEXT UNIQUE, "
                + GroupSchema.KEY_CREATED_AT + " TEXT" + ")";
    }

    private String createRelationTable() {
        return "CREATE TABLE IF NOT EXISTS " + TableSchema.TABLE_USER_GROUPS
                + "(" + GroupSchema.KEY_ID + " INTEGER PRIMARY KEY, "
                + UserSchema.KEY_UID + " TEXT, "
                + GroupSchema.KEY_GUID + " TEXT, "
                + GroupSchema.KEY_CREATED_AT + " TEXT" + ")";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(createLoginTable());
        db.execSQL(createGroupTable());
        db.execSQL(createRelationTable());

        Log.d(TAG, "Database tables created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_GROUP);
        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_USER_GROUPS);
        // drop old tables and...
        // create them again
        onCreate(db);
    }

    // add new user to database
    public void addUser(String name, String email, String uid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(createLoginTable());

        ContentValues values = new ContentValues();
        values.put(UserSchema.KEY_NAME, name);
        values.put(UserSchema.KEY_EMAIL, email);
        values.put(UserSchema.KEY_UID, uid);
        values.put(UserSchema.KEY_CREATED_AT, created_at);

        // inserting row
        long id = db.insert(TableSchema.TABLE_USER ,null, values);
        db.close();

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    // ad new group to database
    public void addGroup(String name, String guid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(createGroupTable());

        ContentValues values = new ContentValues();
        values.put(GroupSchema.KEY_NAME, name);
        values.put(GroupSchema.KEY_GUID, guid);
        values.put(GroupSchema.KEY_CREATED_AT, created_at);

        //insert row
        long id = db.insert(TableSchema.TABLE_GROUP, null, values);
        db.close();

        Log.d(TAG, "New group inserted into sqlite: " + id);
    }

    // add user to a group
    public void addUserToGroup(String uid, String guid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(createRelationTable());

        ContentValues values = new ContentValues();
        values.put(UserSchema.KEY_UID, uid);
        values.put(GroupSchema.KEY_GUID, guid);
        values.put(GroupSchema.KEY_CREATED_AT, created_at);

        //insert row
        long id = db.insert(TableSchema.TABLE_USER_GROUPS, null, values);
        db.close();

        Log.d(TAG, "User " + uid + " added to group " + guid);

    }

    /// get user data
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<>();
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_USER;

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

    public boolean isUserInAGroup() {
        return false; // @TODO
    }

    public String[] getAllGroups() {
        // @TODO
        return new String[] {"abra", "kadabra", "alakazam"};
    }

    private void deleteUsersByEmail(String... emails) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TableSchema.TABLE_USER, "WHERE email = ", emails);
        db.close();

        Log.d(TAG, "Deleted " + Arrays.toString(emails) + "from database" + TableSchema.TABLE_USER);
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete all users
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        db.delete(TableSchema.TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all users from database" + TableSchema.TABLE_USER);
    }


}
