package edu.umsl.superclickers.activity.home;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.app.FragmentConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.userdata.Course;
import edu.umsl.superclickers.userdata.Group;
import edu.umsl.superclickers.userdata.User;

/**
 * Created by Austin on 2/7/2017.
 *
 */

public class GroupActivity extends AppCompatActivity implements
        GroupController.GroupListener {

    private final String TAG = GroupActivity.class.getSimpleName();

    private SessionManager session;
    private GroupController gController;
    private GroupViewFragment gViewFragment;

    private User user;
    private Course course;

    // @TODO if user is in a group, then show groups + members

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_alt);
        setSupportActionBar(toolbar);

        session = new SessionManager(getApplicationContext());
        user = session.getCurrentUser();
        course = session.getEnrolledCourses().get(0);

        FragmentManager fm = getFragmentManager();

        if (fm.findFragmentByTag(FragmentConfig.KEY_GROUP_VIEW) != null) {
            gViewFragment = (GroupViewFragment) fm.findFragmentByTag(FragmentConfig.KEY_GROUP_VIEW);
        } else {
            gViewFragment = new GroupViewFragment();
            fm.beginTransaction()
                    .replace(R.id.group_frame, gViewFragment, FragmentConfig.KEY_GROUP_VIEW)
                    .commit();
        }
        if (fm.findFragmentByTag(FragmentConfig.KEY_GROUP_CONTROLLER) != null) {
            gController = (GroupController) fm.findFragmentByTag(FragmentConfig.KEY_GROUP_CONTROLLER);
        } else {
            gController = new GroupController();
            fm.beginTransaction()
                    .add(gController, FragmentConfig.KEY_GROUP_CONTROLLER)
                    .commit();
            // only request groups if gController DNE
            gController.getGroupForUser(user.getUserId(), course.getCourseId());
            gController.getGroupForCourse(course.get_id());
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alt, menu);
        return true;
    }

    @Override
    public void setGroup(Group group) {
        gViewFragment.setGroup(group);

    }
}
