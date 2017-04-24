package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;

import edu.umsl.superclickers.database.schema.CourseSchema;
import edu.umsl.superclickers.userdata.Course;

/**
 * Created by Austin on 4/24/2017.
 */

class CourseCursorWrapper extends CursorWrapper {

    CourseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    Course getCourse() {
        Course course = null;

        moveToFirst();
        if (getCount() > 0) {
            String _id = getString(getColumnIndex(CourseSchema.KEY_UID));
            String courseId = getString(getColumnIndex(CourseSchema.KEY_COURSE_ID));
            String extID = getString(getColumnIndex(CourseSchema.KEY_EXTENDED_ID));
            String name = getString(getColumnIndex(CourseSchema.KEY_NAME));
            String semester = getString(getColumnIndex(CourseSchema.KEY_SEMESTER));
            String instructor = getString(getColumnIndex(CourseSchema.KEY_INSTRUCTOR));
            course = new Course(_id, courseId, extID, name, semester, instructor);
        }
        return course;
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
