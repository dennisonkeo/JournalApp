package com.example.computer.journalapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by computer on 6/30/2018.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "Journal";
    private static int DB_VKR = 1;
    private static String DB_TABLE = "Task";
    private static String DB_COLUMN1 = "TITLE";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VKR);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "create table " + DB_TABLE + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, TITLE TEXT);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = String.format("DELETE  TABLE IF EXISTS %s",DB_TABLE);
        db.execSQL(query);

        onCreate(db);

    }

    public  void insertNewTask(String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN1,title);


        db.insertWithOnConflict(DB_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteTask(String title)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE,DB_COLUMN1+ " = ?", new String[]{title});
        db.close();
    }

    public ArrayList<String> getTaskList()
    {
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DB_TABLE, null);
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(DB_TABLE, new String[]{DB_COLUMN1},null,null,null,null,null);

        while(cursor.moveToNext())
        {
            int index = cursor.getColumnIndex(DB_COLUMN1);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();

        return taskList;
    }
}
