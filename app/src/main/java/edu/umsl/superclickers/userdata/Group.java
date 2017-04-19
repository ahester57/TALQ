package edu.umsl.superclickers.userdata;

import java.util.ArrayList;
import java.util.List;


public class Group{

    private String groupName;
    private String groupId;
    private ArrayList<User> users;


    public Group(String groupName, String groupId) {
        this.groupName = groupName;
        this.groupId = groupId;

        users = new ArrayList<>();
        users.ensureCapacity(4);
    }

    public void setUsers(List<User> users) {
        this.users = (ArrayList<User>) users;
    }

    // add a User to the group
    // returns success / failure
    public boolean add(User user) {
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
                "id='" + groupId + '\'' +
                ", name='" + groupName +  '\'' +
                ", users=" + '[' + users + ']' +
                '}';
    }
}
