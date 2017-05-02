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
        gRecycler.setAdapter(new GroupAdapter(gGroups));
        gRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }


    @Override
    public void setGroup(Group group) {
        // @TODO list all groups
        gGroups.add(group);
        gRecycler.setAdapter(new GroupAdapter(gGroups));
    }

    class GroupAdapter extends RecyclerView.Adapter<GroupHolder> implements
            GroupHolder.GroupHolderListener {

        private List<Group> mGroups;


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
        public void onBindViewHolder(GroupHolder holder, int position) {
            if (mGroups != null) {
                try {
                    holder.bindGroup(mGroups.get(position));
                } catch (IndexOutOfBoundsException e) {
                    Log.e("WHOOPS", "idk");
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
