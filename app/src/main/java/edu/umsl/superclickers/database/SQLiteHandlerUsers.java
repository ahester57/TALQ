package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;

import edu.umsl.superclickers.userdata.User;

/**
 * Created by Austin on 2/4/2017.
 * 
 */

public class SQLiteHandlerUsers extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandlerUsers.class.getSimpleName();

    // db name
    private static final String DB_NAME = "tbl_users";
    private static final int DB_VERSION = 1;

    private static SQLiteHandlerUsers sPersistence;

    public static SQLiteHandlerUsers sharedInstance(Context context) {
        if (sPersistence == null) {
            sPersistence = new SQLiteHandlerUsers(context);
        }
        return sPersistence;
    }

    public SQLiteHandlerUsers(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private String createLoginTable() {

        return "CREATE TABLE IF NOT EXISTS " + TableSchema.TABLE_USER + "("
                + UserSchema.KEY_ID + " INTEGER PRIMARY KEY, "
                + UserSchema.KEY_FIRST + " TEXT, "
                + UserSchema.KEY_LAST + " TEXT, "
                + UserSchema.KEY_USER_ID + " TEXT, "
                + UserSchema.KEY_EMAIL + " TEXT UNIQUE, "
                + UserSchema.KEY_UID + " TEXT" + ")";
    }

    private String createGroupTable() {
        return "CREATE TABLE IF NOT EXISTS " + TableSchema.TABLE_GROUP + "("
                + GroupSchema.KEY_ID + " INTEGER PRIMARY KEY, "
                + GroupSchema.KEY_NAME + " TEXT, "
                + GroupSchema.KEY_GID + " TEXT UNIQUE, "
                + GroupSchema.KEY_CREATED_AT + " TEXT" + ")";
    }

    private String createRelationTable() {
        return "CREATE TABLE IF NOT EXISTS " + TableSchema.TABLE_USER_GROUPS
                + "(" + GroupSchema.KEY_ID + " INTEGER PRIMARY KEY, "
                + UserSchema.KEY_UID + " TEXT, "
                + GroupSchema.KEY_G_ID + " TEXT, "
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
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_USER);
        db.execSQL(createLoginTable());

        ContentValues values = UserCursorWrapper.createUserValues(user);

        // inserting row
        long id = db.insert(TableSchema.TABLE_USER ,null, values);
        db.close();

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    // ad new group to database
    public void addGroup(String name, String guid, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_GROUP);
        db.execSQL(createGroupTable());

        ContentValues values = new ContentValues();
        values.put(GroupSchema.KEY_NAME, name);
        values.put(GroupSchema.KEY_GID, guid);
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
        values.put(GroupSchema.KEY_GID, guid);
        values.put(GroupSchema.KEY_CREATED_AT, created_at);

        //insert row
        long id = db.insert(TableSchema.TABLE_USER_GROUPS, null, values);
        db.close();

        Log.d(TAG, "User " + uid + " added to group " + guid + ": " + id);

    }

    /// get user data
    public User getCurrentUser() { // change to return user object
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_USER;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        UserCursorWrapper uCursor = new UserCursorWrapper(cursor);

        User user = uCursor.getUser();

        cursor.close();
        db.close();

        Log.d(TAG, "Fectching user from Sqlite: " + user.toString());

        return user;
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete all users
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        db.delete(TableSchema.TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all users from database" + TableSchema.TABLE_USER);
    }

    private void deleteUsersByEmail(String... emails) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TableSchema.TABLE_USER, "WHERE email = ", emails);
        db.close();

        Log.d(TAG, "Deleted " + Arrays.toString(emails) + "from database" + TableSchema.TABLE_USER);
    }

}
