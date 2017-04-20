package edu.umsl.superclickers.activity.home;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.umsl.superclickers.database.SQLiteHandlerUsers;
import edu.umsl.superclickers.userdata.User;

/**
 * Created by Austin on 3/21/2017.
 */

public class UserModel {

    private User mUser;
    private List<String> mFriendNames;
    private List<String> mFriendEmails;
    private SQLiteHandlerUsers mSQLHandler;

    public UserModel(Context context) {
        mSQLHandler = SQLiteHandlerUsers.sharedInstance(context);
    }

    public UserModel(JSONObject userJSON, Context context) {
        mSQLHandler = SQLiteHandlerUsers.sharedInstance(context);

        try {
            String first = userJSON.getString("first");
            String last = userJSON.getString("last");
            String ssoId = userJSON.getString("userId");
            String email = userJSON.getString("email");
            String _id = userJSON.getString("_id");
            mUser = new User(first, last, ssoId, email, _id);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        mSQLHandler.addUser(mUser);
    }

    public UserModel(User user, Context context) {
        mSQLHandler = SQLiteHandlerUsers.sharedInstance(context);
        mUser = user;
        //mSQLHandler.addFriends(newFriends);
    }

    public User getUser() {
        return mUser;
    }

    public List<String> getFriendNames() {
        if (mFriendNames == null) {
            //mFriendNames = mSQLHandler.getFriendNames();
        }
        return mFriendNames;
    }


}
