package edu.umsl.superclickers.userdata;

import org.json.JSONException;
import org.json.JSONObject;

import edu.umsl.superclickers.database.schema.CourseSchema;

/**
 * Created by Austin on 4/23/2017.
 */

public class Course {

    private String _id;
    private String courseId;
    private String extendedID;
    private String name;
    private String semester;
    private String instructor;

    public Course(String id, String courseId, String extendedID, String name,
                  String semester, String instructor) {
        this._id = id;
        this.courseId = courseId;
        this.extendedID = extendedID;
        this.name = name;
        this.semester = semester;
        this.instructor = instructor;
    }

    public Course(JSONObject cObj) throws JSONException {
        try {

            String id = cObj.getString(CourseSchema.KEY_UID);
            String courseId = cObj.getString(CourseSchema.KEY_COURSE_ID);
            String extendedID = cObj.getString(CourseSchema.KEY_EXTENDED_ID);
            String name = cObj.getString(CourseSchema.KEY_NAME);
            String semester = cObj.getString(CourseSchema.KEY_SEMESTER);
            String instructor = cObj.getString(CourseSchema.KEY_INSTRUCTOR);

            this._id = id;
            this.courseId = courseId;
            this.extendedID = extendedID;
            this.name = name;
            this.semester = semester;
            this.instructor = instructor;

        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException(e.getMessage());
        }
    }

    public String get_id() {
        return _id;
    }

    public String getCourseId() {
        return courseId;
    }

    public String getExtendedID() {
        return extendedID;
    }

    public String getName() {
        return name;
    }

    public String getSemester() {
        return semester;
    }

    public String getInstructor() {
        return instructor;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseId='" + courseId + '\'' +
                ", extendedID='" + extendedID + '\'' +
                ", name='" + name + '\'' +
                ", semester='" + semester + '\'' +
                ", instructor='" + instructor + '\'' +
                '}';
    }
}
