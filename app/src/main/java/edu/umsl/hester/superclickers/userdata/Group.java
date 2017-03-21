package edu.umsl.hester.superclickers.userdata;

import java.util.ArrayList;


public class Group{

    private String groupName;
    private int groupId;
    private ArrayList<User> users;


    Group(String groupName, int groupId) {
        this.groupName = groupName;
        this.groupId = groupId;

        users = new ArrayList<>();
        users.ensureCapacity(4);
    }

    // add a User to the group
    // returns success / failure
    boolean add(User user) {
        if (users.size() <= 5) {
            users.add(user);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupName='" + groupName + '\'' +
                ", groupId=" + groupId +
                ", users=" + users +
                '}';
    }
}
