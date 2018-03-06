package com.bougay.todoapp2;

import android.provider.BaseColumns;

public class TodoDbContract {
    public static final String DB_NAME = "com.bougay.todoapp.db";
    public static final int DB_VERSION = 1;

    public class TaskEntry implements BaseColumns {
        public static final String TABLE = "items";

        public static final String COL_TASK_TITLE = "title";
    }
}
