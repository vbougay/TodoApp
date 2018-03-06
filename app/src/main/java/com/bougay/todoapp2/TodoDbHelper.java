package com.bougay.todoapp2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDbHelper extends SQLiteOpenHelper {

    public TodoDbHelper(Context context) {
        super(context, TodoDbContract.DB_NAME, null, TodoDbContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TodoDbContract.TaskEntry.TABLE + " ( " +
                TodoDbContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TodoDbContract.TaskEntry.COL_TASK_TITLE + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TodoDbContract.TaskEntry.TABLE);
        onCreate(db);
    }
}
