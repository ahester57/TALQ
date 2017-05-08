package edu.umsl.superclickers.activity.helper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.userdata.Course;

/**
 * Created by Austin on 5/7/2017.
 */

public class CourseHolder extends RecyclerView.ViewHolder {

    private final String TAG = CourseHolder.class.getSimpleName();
    private TextView tCourseName;
    private TextView tCourseID;
    private TextView tCourseInstructor;
    private TextView tCourseSemester;

    public CourseHolder(View itemView) {
        super(itemView);
        tCourseName = (TextView) itemView.findViewById(R.id.text_course_name);
        tCourseID = (TextView) itemView.findViewById(R.id.text_course_id);
        tCourseInstructor = (TextView) itemView.findViewById(R.id.text_course_instructor);
        tCourseSemester = (TextView) itemView.findViewById(R.id.text_course_semester);
    }

    public void bindCourse(Course course) {
        tCourseName.setText(course.getName());
        tCourseID.setText(course.getExtendedID());
        tCourseInstructor.setText(course.getInstructor()); ////////
        tCourseSemester.setText(course.getSemester());
    }
}
