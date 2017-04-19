package edu.umsl.superclickers.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import edu.umsl.superclickers.userdata.User;

/**
 * Created by Austin on 3/21/2017.
 */

public class UserCursorWrapper extends CursorWrapper{

    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String uid = getString(getColumnIndex(UserSchema.KEY_UID));
        String first = getString(getColumnIndex(UserSchema.KEY_FIRST));
        String last = getString(getColumnIndex(UserSchema.KEY_LAST));
        String userId = getString(getColumnIndex(UserSchema.KEY_USER_ID));
        String email = getString(getColumnIndex(UserSchema.KEY_EMAIL));
        User user = new User(first, last, userId, email, uid);
        return user;
    }

}
