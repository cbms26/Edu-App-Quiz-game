package com.nga26.example.ngawangeduapp1.model;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.nga26.example.ngawangeduapp1.helper.DBHelper;

import java.util.ArrayList;

public class User {
    private String username;
    private String duration;
    private String level;
    private String date;
    private String score;
    //constructor


    public User(String username, String duration, String level, String date, String score) {
        this.username = username;
        this.duration = duration;
        this.level = level;
        this.date = date;
        this.score = score;
    }
    //getters

    public String getUsername() {
        return username;
    }

    public String getDuration() {
        return duration;
    }

    public String getLevel() {
        return level;
    }

    public String getDate() {
        return date;
    }

    public String getScore() {
        return score;
    }
    @SuppressLint("Range")
    public static ArrayList<User> loadUsers(DBHelper dbHelper){
        ArrayList<User> users = new ArrayList<>();
        if (dbHelper != null){
            //get all user records
            Cursor cursor = dbHelper.getAllPlayers();
            if (cursor != null && cursor.getCount() > 0){
                cursor.moveToFirst();
                do{
                    //get user name
                    String username =
                            cursor.getString(cursor.getColumnIndex(DBHelper.USERNAME_COL));
                    //get duration
                    String durationStr =
                            cursor.getString(cursor.getColumnIndex(DBHelper.DURATION_COL));
                    //get level
                    String level =
                            cursor.getString(cursor.getColumnIndex(DBHelper.LEVEL_COL));
                    //get score
                    String scoreStr =
                            cursor.getString(cursor.getColumnIndex(DBHelper.SCORE_COL));
                    String dateStr =
                            cursor.getString(cursor.getColumnIndex(DBHelper.DATE_COL));
                    //add new user into the array users
                    users.add(new User(username, durationStr, level, dateStr, scoreStr));
                }while (cursor.moveToNext());
            }
        }
        return users;
    }
}
