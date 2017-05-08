package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;

import edu.umsl.superclickers.database.schema.CourseSchema;
import edu.umsl.superclickers.userdata.Course;

/**
 * Created by Austin on 4/24/2017.
 */

class CourseCursorWrapper extends CursorWrapper {

    CourseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<>();

        moveToFirst();
        if (getCount() > 0) {
            do {
                String _id = getString(getColumnIndex(CourseSchema.KEY_UID));
                String courseId = getString(getColumnIndex(CourseSchema.KEY_COURSE_ID));
                String extID = getString(getColumnIndex(CourseSchema.KEY_EXTENDED_ID));
                String name = getString(getColumnIndex(CourseSchema.KEY_NAME));
                String semester = getString(getColumnIndex(CourseSchema.KEY_SEMESTER));
                String instructor = getString(getColumnIndex(CourseSchema.KEY_INSTRUCTOR));
                courses.add(new Course(_id, courseId, extID, name, semester, instructor));
            } while (moveToNext());
        }
        return courses;
    }

    static ContentValues createCourseValues(Course course) {
        ContentValues values = new ContentValues();
        values.put(CourseSchema.KEY_UID, course.get_id());
        values.put(CourseSchema.KEY_COURSE_ID, course.getCourseId());
        values.put(CourseSchema.KEY_EXTENDED_ID, course.getExtendedID());
        values.put(CourseSchema.KEY_NAME, course.getName());
        values.put(CourseSchema.KEY_SEMESTER, course.getSemester());
        values.put(CourseSchema.KEY_INSTRUCTOR, course.getInstructor());


        return values;
    }
}
