package com.hilal.todoapp.db;

import android.provider.BaseColumns;


public final class TodoDbContract {

    private TodoDbContract() {

    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TodoEntry.TABLE_NAME + " (" +
                    TodoEntry._ID + " INTEGER PRIMARY KEY," +
                    TodoEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    TodoEntry.COLUMN_NAME_DUE_DATE + " TEXT," +
                    TodoEntry.COLUMN_NAME_PASSED + " INTEGER," +
                    TodoEntry.COLUMN_NAME_IS_DONE + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TodoEntry.TABLE_NAME;


    public static class TodoEntry implements BaseColumns {
        public static final String TABLE_NAME              = "todos";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_DUE_DATE    = "due_date";
        public static final String COLUMN_NAME_PASSED    = "passed";
        public static final String COLUMN_NAME_IS_DONE    = "is_done";


    }
}
