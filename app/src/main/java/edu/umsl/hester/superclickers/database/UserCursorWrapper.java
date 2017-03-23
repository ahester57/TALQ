package edu.umsl.hester.superclickers.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import edu.umsl.hester.superclickers.userdata.User;

/**
 * Created by Austin on 3/21/2017.
 */

public class UserCursorWrapper extends CursorWrapper{

    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String uid = getString(getColumnIndex(UserSchema.KEY_UID));
        String name = getString(getColumnIndex(UserSchema.KEY_NAME));
        String email = getString(getColumnIndex(UserSchema.KEY_EMAIL));
        User user = new User(name, email, uid);
        return user;
    }

}
