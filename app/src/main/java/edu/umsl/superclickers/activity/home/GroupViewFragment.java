package edu.umsl.superclickers.activity.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.activity.helper.GroupHolder;
import edu.umsl.superclickers.userdata.Group;

/**
 * Created by Austin on 2/7/2017.
 *
 */

public class GroupViewFragment extends Fragment implements GroupController.GroupListener {


    private RecyclerView gRecycler;
    private List<Group> gGroups = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);
        setRetainInstance(true);
        // @TODO
        // put recycler view here with groups and group members


        gRecycler = (RecyclerView) view.findViewById(R.id.group_list_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        gRecycler.setLayoutManager(layoutManager);
        gRecycler.setAdapter(new GroupAdapter(gGroups));
        return view;
    }


    @Override
    public void setGroup(Group group) {
        // checks if your group is trying again
        for (Group g : gGroups) {
            if (group.getGroupId().equals(g.getGroupId())) {
                return;
            }
        }
        gGroups.add(group);
        gRecycler.setAdapter(new GroupAdapter(gGroups));
    }

    private class GroupAdapter extends RecyclerView.Adapter<GroupHolder> implements
            GroupHolder.GroupHolderListener {

        private List<Group> mGroups;
        private int selectedPos = 0;

        public GroupAdapter(List<Group> groups) {
            mGroups = groups;
        }

        @Override
        public GroupHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.group_recycler_item, parent, false);
            GroupHolder gHolder = new GroupHolder(view, this);
            return gHolder;
        }

        @Override
        public void setGroup(int pos) {
            //
        }

        @Override
        public void onBindViewHolder(GroupHolder holder, final int position) {
            if (mGroups != null) {
                try {
                    if (selectedPos == position) {
                        holder.itemView.setBackground(getResources().getDrawable(R.drawable.line));
                        holder.itemView.setPadding(8, 8, 8, 8);
                    } else {
                        holder.itemView.setBackgroundResource(0);
                        holder.itemView.setPadding(8, 8, 8, 8);
                    }

                    holder.bindGroup(mGroups.get(position));
                } catch (IndexOutOfBoundsException e) {
                    Log.e("GROUP_RECYCLER", e.getMessage());
                }
            }
        }

        @Override
        public int getItemCount() {
            if (mGroups != null) {
                return mGroups.size();
            }
            return 0;
        }
    }
}
