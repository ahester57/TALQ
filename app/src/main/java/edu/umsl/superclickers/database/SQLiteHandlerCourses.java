package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import edu.umsl.superclickers.database.schema.CourseSchema;
import edu.umsl.superclickers.database.schema.TableSchema;
import edu.umsl.superclickers.userdata.Course;

/**
 * Created by Austin on 4/24/2017.
 */

public class SQLiteHandlerCourses extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandlerCourses.class.getSimpleName();

    // db name
    private static final String DB_NAME = "tbl_users";
    private static final int DB_VERSION = 1;

    private static SQLiteHandlerCourses sPersistence;

    public static SQLiteHandlerCourses sharedInstance(Context context) {
        if (sPersistence == null) {
            sPersistence = new SQLiteHandlerCourses(context);
        }
        return sPersistence;
    }

    public SQLiteHandlerCourses(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private String createCourseTable() {
        return "CREATE TABLE IF NOT EXISTS " + TableSchema.TABLE_COURSE + "("
                + CourseSchema.KEY_ID + " INTEGER PRIMARY KEY, "
                + CourseSchema.KEY_UID + " TEXT UNIQUE, "
                + CourseSchema.KEY_COURSE_ID + " TEXT, "
                + CourseSchema.KEY_EXTENDED_ID + " TEXT, "
                + CourseSchema.KEY_NAME + " TEXT, "
                + CourseSchema.KEY_SEMESTER + " TEXT, "
                + CourseSchema.KEY_INSTRUCTOR + " TEXT" + ")";
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(createCourseTable());

            Log.d(TAG, "Course tables created");
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't create course table");
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TableSchema.TABLE_COURSE);
            // drop old tables and...
            // create them again
            onCreate(db);
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't create course table");
        }
    }


    // add new course to database
    public void addCourse(Course course) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL(createCourseTable());
            ContentValues values = CourseCursorWrapper.createCourseValues(course);
            // inserting row
            long id = db.insert(TableSchema.TABLE_COURSE, null, values);
            db.close();
            Log.d(TAG, "New course inserted into sqlite: " + values.getAsString(CourseSchema.KEY_NAME));
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't add course");
        }
    }


    /// get course data
    public ArrayList<Course> getCurrentCourses() { // change to return user object
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_COURSE;
        ArrayList<Course> courses = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            CourseCursorWrapper uCursor = new CourseCursorWrapper(cursor);

            courses = uCursor.getCourses();

            cursor.close();
            db.close();
            Log.d(TAG, "Fectching courses from Sqlite: " + courses.toString());
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't get courses");
        }
        return courses;
    }

    /// get course data
    public Course getCourseById(String courseId) { // change to return user object
        String selectQuery = "SELECT * FROM " + TableSchema.TABLE_COURSE +
                    " WHERE " + CourseSchema.KEY_COURSE_ID + " = \"" + courseId + "\";";

        Course course = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            CourseCursorWrapper uCursor = new CourseCursorWrapper(cursor);
            course = uCursor.getCourses().get(0);

            cursor.close();
            db.close();
            Log.d(TAG, "Fectching course from Sqlite: " + course.toString());
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't get courses");
        }
        return course;
    }

    public void deleteAllCourses() {

        // delete all courses
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TableSchema.TABLE_COURSE, null, null);
            db.close();
            Log.d(TAG, "Deleted all course from database" + TableSchema.TABLE_COURSE);
        } catch (SQLiteException e) {
            Log.d(TAG, "couldn't delete courses");
        }
    }


}
