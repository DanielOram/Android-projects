package com.aut.android.danielorammicro_bloggingapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aut.android.danielorammicro_bloggingapp.StatusContract;

/**
 * Created by cwc8902 on 13/08/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context){
        super(context,StatusContract.DB_NAME,null,StatusContract.DB_VERSION);
    }

    //called only once to create the database first time
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("create table %s (%s int primary key, %s int, %s text, %s text)",
                StatusContract.TABLE, StatusContract.Columns._ID,
                StatusContract.Columns.CREATED_AT,StatusContract.Columns.USER,
                StatusContract.Columns.TEXT);
        Log.d("DbHelper", "SQL: " + sql);
        db.execSQL(sql);
    }

    //Called every time oldVersion != newVersion
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + StatusContract.TABLE);
        this.onCreate(db);
    }
}
