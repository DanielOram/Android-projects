package com.danieloram.android.things_for_daniel_todo_app.db;

/**
 * Created by Daniel Oram on 30/08/2015.
 */
import android.provider.BaseColumns;


public class TaskContract {
    public static final String DB_NAME = "com.danieloram.android.things_for_daniel_todo_app.db.tasks";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tasks";

    public class Columns {
        public static final String TASK = "task";
        public static final String _ID = BaseColumns._ID;
    }
}
