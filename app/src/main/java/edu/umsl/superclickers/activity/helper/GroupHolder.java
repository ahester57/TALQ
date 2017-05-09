package edu.umsl.superclickers.activity.helper;

/*
  Created by Austin on 5/1/2017.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.userdata.Group;

/**
 * Created by Austin on 4/22/2017
 */

public class GroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final String TAG = GroupHolder.class.getSimpleName();
    private TextView tGroupName;
    private TextView tGroupCourse;
    private TextView tGroupUsers;
    private WeakReference<GroupHolderListener> mListener;

    public interface GroupHolderListener {
        void setGroup(int pos);
    }


    public GroupHolder(View itemView, GroupHolderListener listener) {
        super(itemView);
        this.mListener = new WeakReference<>(listener);
        tGroupName = (TextView) itemView.findViewById(R.id.text_group_name);
        tGroupCourse = (TextView) itemView.findViewById(R.id.text_group_course);
        tGroupUsers = (TextView) itemView.findViewById(R.id.text_group_users);
        itemView.setOnClickListener(this);
    }

    public void bindGroup(Group group, String users) {
        tGroupName.setText(group.getGroupName());
        tGroupCourse.setText(group.getCourseIds().toString());
        tGroupUsers.setText(users); ////////
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Clicked: " + this + " @ pos: " + getAdapterPosition());
        mListener.get().setGroup(getAdapterPosition());
    }
}
