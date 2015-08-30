package com.danieloram.android.things_for_daniel_todo_app.db;

/**
 * Created by Daniel Oram on 30/08/2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//Helper class for opening the database
public class TaskDBHelper extends SQLiteOpenHelper{

    public TaskDBHelper(Context context){
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = String.format("CREATE TABLE %s (" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "%s TEXT)",
                TaskContract.TABLE,
                TaskContract.Columns.TASK);
        Log.d("TaskDBHelper","Query to form table: "+ sqlQuery);
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
        onCreate(db);
    }
}
