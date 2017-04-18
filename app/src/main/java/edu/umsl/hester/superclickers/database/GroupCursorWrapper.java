package edu.umsl.hester.superclickers.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import edu.umsl.hester.superclickers.userdata.Group;

/**
 * Created by Austin on 3/22/2017.
 */

public class GroupCursorWrapper extends CursorWrapper {

    public GroupCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Group getUser() {
        String guid = getString(getColumnIndex(GroupSchema.KEY_GID));
        String name = getString(getColumnIndex(UserSchema.KEY_NAME));
        Group group = new Group(name, guid);
        return group;
    }
}
