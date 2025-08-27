package com.nga26.example.ngawangeduapp1.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME ="ngawangGame.db";
    public static final String TABLE_NAME = "users";
    public static final String ID_COL = "_id";
    public static final String USERNAME_COL = "username";
    public static final String DURATION_COL = "duration";
    public static final String LEVEL_COL = "level";//3 levels: 1, 2, 3
    public static final String DATE_COL = "date";
    public static final String SCORE_COL = "score"; //higher is better

    public DBHelper(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "create table " + TABLE_NAME
                + "("
                + ID_COL + " integer primary key autoincrement,"
                + USERNAME_COL + " text not null,"
                +DURATION_COL + " int default 0, "
                + LEVEL_COL + " text default '1',"
                + DATE_COL + " timestamp default CURRENT_TIMESTAMP,"
                + SCORE_COL + " int default 0"
                + ")";
        db.execSQL(create_table);
    }

    @Override
    //onUpgrade; automatically run
    //another method is onDowngrade, use same function to empty the table.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " +  TABLE_NAME);
        onCreate(db);
    }

    public void insertPlayer(String usr, int duration, String level, int score){
        //getwriteabledatabase i required for inset, update or delete
        //the DB is actually created when writableDatabase is called
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME_COL, usr);
        values.put(SCORE_COL, score);
        values.put(DURATION_COL, duration);
        values.put(LEVEL_COL, level);
        db.insert(TABLE_NAME, null, values);
    }

    public Cursor getAllPlayers(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sqlStr = "select * from " + TABLE_NAME
                + " order by "
                + LEVEL_COL + " ASC, "
                + SCORE_COL + " DESC, "
                + DURATION_COL + " ASC, "
                + USERNAME_COL + " ASC";
        return db.rawQuery(sqlStr, null);
    }
}

