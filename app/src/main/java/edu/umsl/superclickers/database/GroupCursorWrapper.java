package edu.umsl.superclickers.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import edu.umsl.superclickers.userdata.Group;

/**
 * Created by Austin on 3/22/2017.
 *
 */

class GroupCursorWrapper extends CursorWrapper {

    GroupCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    Group getUser() {
        String guid = getString(getColumnIndex(GroupSchema.KEY_GID));
        String name = getString(getColumnIndex(UserSchema.KEY_NAME));
        Group group = new Group(name, guid);
        return group;
    }
}
