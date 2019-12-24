package com.hilal.todoapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.hilal.todoapp.db.TodoDbContract.*;


public class TodoAppDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "todoApp.db";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");


    public TodoAppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public Long addTodo(String description, Date dueDate) {
        ContentValues values = new ContentValues();
        values.put(TodoEntry.COLUMN_NAME_DESCRIPTION, description);
        values.put(TodoEntry.COLUMN_NAME_DUE_DATE, dateToString(dueDate));
        values.put(TodoEntry.COLUMN_NAME_PASSED, 0);
        values.put(TodoEntry.COLUMN_NAME_IS_DONE, 0);

        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TodoEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public void setPassed(Long id) {
        String setPassedQuery = "update " + TodoEntry.TABLE_NAME + " set " + TodoEntry.COLUMN_NAME_PASSED + "= 1 where " + TodoEntry._ID + " = " + id;
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(setPassedQuery);
        db.close();
    }


    public Todo select(Long id) {
        String selectQuery = "select * from " + TodoEntry.TABLE_NAME + " where " + TodoEntry._ID + " = " + id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        Todo todo = new Todo();
        if (cursor.getCount() > 0) {
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(TodoEntry.COLUMN_NAME_DESCRIPTION));
            String dueDate = cursor.getString(cursor.getColumnIndexOrThrow(TodoEntry.COLUMN_NAME_DUE_DATE));

            todo.setId(id);
            todo.setDesc(desc);
            todo.setDueDateStr(dueDate);
            todo.setDueDate(stringToDate(dueDate));
        }
        cursor.close();
        db.close();

        return todo;

    }

    public List<Todo> selectAllNotDone() {
        String selectQuery = "select * from " + TodoEntry.TABLE_NAME + " where " + TodoEntry.COLUMN_NAME_PASSED + " = 0 and " + TodoEntry.COLUMN_NAME_IS_DONE + " = 0";

        return selectList(selectQuery);
    }


    public List<Todo> selectAllDone() {
        String selectQuery = "select * from " + TodoEntry.TABLE_NAME + " where " + TodoEntry.COLUMN_NAME_PASSED + " = 1 or " + TodoEntry.COLUMN_NAME_IS_DONE + " = 1";

        return selectList(selectQuery);
    }

    private List<Todo> selectList(String selectQuery) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        List<Todo> todos = new ArrayList<>();
        while (cursor.moveToNext()) {
            Long id = cursor.getLong(cursor.getColumnIndexOrThrow(TodoEntry._ID));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(TodoEntry.COLUMN_NAME_DESCRIPTION));
            String dueDate = cursor.getString(cursor.getColumnIndexOrThrow(TodoEntry.COLUMN_NAME_DUE_DATE));
            long isDone = cursor.getLong(cursor.getColumnIndexOrThrow(TodoEntry.COLUMN_NAME_IS_DONE));

            Todo todo = new Todo();
            todo.setId(id);
            todo.setDesc(desc);
            todo.setDueDateStr(dueDate);
            todo.setDueDate(stringToDate(dueDate));
            todo.setDone(isDone == 1);
            todos.add(todo);
        }

        cursor.close();
        db.close();
        return todos;
    }


    public String dateToString(Date date) {
        return dateFormat.format(date);
    }

    public Date stringToDate(String strDate) {
        try {
            return dateFormat.parse(strDate);
        } catch (ParseException e) {
            return null;
        }
    }

    public void setCheckStatus(Long id, boolean isChecked) {
        int checked = isChecked ? 1 : 0;

        String setPassedQuery = "update " + TodoEntry.TABLE_NAME + " set " + TodoEntry.COLUMN_NAME_IS_DONE + "= " + checked + " where " + TodoEntry._ID + " = " + id;
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL(setPassedQuery);
        db.close();
    }


}
