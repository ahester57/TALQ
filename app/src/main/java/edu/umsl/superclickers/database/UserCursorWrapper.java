package edu.umsl.superclickers.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorWrapper;

import edu.umsl.superclickers.quizdata.Quiz;
import edu.umsl.superclickers.userdata.User;

/**
 * Created by Austin on 3/21/2017.
 */

class UserCursorWrapper extends CursorWrapper{

    UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    User getUser() {
        User user = null;

        moveToFirst();
        if (getCount() > 0) {
            String uid = getString(getColumnIndex(UserSchema.KEY_UID));
            String first = getString(getColumnIndex(UserSchema.KEY_FIRST));
            String last = getString(getColumnIndex(UserSchema.KEY_LAST));
            String userId = getString(getColumnIndex(UserSchema.KEY_USER_ID));
            String email = getString(getColumnIndex(UserSchema.KEY_EMAIL));
            user = new User(first, last, userId, email, uid);
        }
        return user;
    }

    static ContentValues createUserValues(User user) {
        ContentValues values = new ContentValues();
        values.put(UserSchema.KEY_FIRST, user.getFirst());
        values.put(UserSchema.KEY_LAST, user.getLast());
        values.put(UserSchema.KEY_USER_ID, user.getUserId());
        values.put(UserSchema.KEY_EMAIL, user.getEmail());
        values.put(UserSchema.KEY_UID, user.get_id());

        return values;
    }

}
