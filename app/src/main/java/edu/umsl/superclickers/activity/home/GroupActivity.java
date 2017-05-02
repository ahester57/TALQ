package edu.umsl.superclickers.activity.home;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        View.OnClickListener, GroupController.GroupListener {

    private final String TAG = GroupActivity.class.getSimpleName();

    private Button btnCreate, btnJoin;
    private EditText editGroupName;

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

        btnCreate = (Button) findViewById(R.id.create_group_button);
        btnJoin = (Button) findViewById(R.id.join_group_button);
        editGroupName = (EditText) findViewById(R.id.group_name_edit_text);

        session = new SessionManager(getApplicationContext());
        user = session.getCurrentUser();
        course = session.getEnrolledCourses().get(0);

        gController = new GroupController();
        // load fragment ... switch to recycler view
        gViewFragment = new GroupViewFragment();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.group_frame, gViewFragment);
        ft.add(gController, FragmentConfig.KEY_GROUP_CONTROLLER);
        ft.commit();



        btnCreate.setOnClickListener(this);
        btnJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_group_button:
                if (!editGroupName.getText().toString().trim().equals("")) {
                    gController.createGroup(user.get_id(), editGroupName.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Enter a group name, I say!",
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.join_group_button:
                gController.getGroupFor(user.getUserId(), course.getCourseId());
                break;
        }
    }


    @Override
    public void setGroup(Group group) {
        gViewFragment.setGroup(group);
    }
}
