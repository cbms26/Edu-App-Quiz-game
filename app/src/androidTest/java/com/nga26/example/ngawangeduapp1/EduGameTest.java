package com.nga26.example.ngawangeduapp1;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.database.Cursor;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.nga26.example.ngawangeduapp1.helper.DBHelper;
import com.nga26.example.ngawangeduapp1.model.User;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EduGameTest {
    Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    @Test
    public void useAppContext() {

        assertEquals("com.nga26.example.ngawangeduapp", appContext.getPackageName());
        testDB();
        testQuestionImageManager();
    }

    private void testQuestionImageManager() {
        //test get answer
        //assertEquals("25", imageManager.getAnswer(4));
        //test count
        //assertEquals("6", "" + imageManager.count());
    }

    private void testDB() {
        //create an object of DBHelper
        DBHelper dbHelper = new DBHelper(appContext);
        //clear DB
        dbHelper.getWritableDatabase().execSQL("delete from " + DBHelper.TABLE_NAME);
        //inset one user
        dbHelper.insertPlayer("Tony", 100, "2", 75);
        //verify if we have one record in DB
        Cursor cursor = dbHelper.getAllPlayers();
        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            //a love to get all records
            do {
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
                //Verification
                assertEquals("Tony",username);
                assertEquals("100",durationStr);
                assertEquals("2",level);
                assertEquals("75", scoreStr);



            }while (cursor.moveToNext());
            //test loadUsers
            ArrayList<User> users = User.loadUsers(dbHelper);
            assertEquals("Tony", users.get(0).getUsername());
            assertEquals("75", users.get(0).getScore());
            assertEquals("100", users.get(0).getDuration());
            assertEquals("2", users.get(0).getLevel());
        }
    }
}