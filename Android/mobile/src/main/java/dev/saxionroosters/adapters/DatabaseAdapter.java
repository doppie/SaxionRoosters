package dev.saxionroosters.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import dev.saxionroosters.extras.S;
import dev.saxionroosters.extras.Tools;
import dev.saxionroosters.model.RoosterNotification;

/**
 * Created by Doppie on 31-3-2016.
 */
public class DatabaseAdapter {

    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String DB_CREATE_NOTIFICATIONS = "CREATE TABLE " + TABLE_NOTIFICATIONS
            + " (" + S.NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + S.CONTENT_TITLE + " TEXT NOT NULL, "
            + S.CONTENT_SUBTITLE + " TEXT NOT NULL, "
            + S.SHOW_DATE + " TIMESTAMP);";

    public static final String DB_NAME = "saxionroosters_notifications_db";
    public static final int DB_VERSION = 1;
    private final Context c;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    //SINGLETON class because of multi threading. SQLite should have only one conn.
    //This conn. can be shared by multiple threads.
    private static DatabaseAdapter instance = null;

    public static DatabaseAdapter getInstance(Context c) {
        if(instance == null) {
            instance = new DatabaseAdapter(c);
        }
        return instance;
    }

    protected DatabaseAdapter(Context c) {
        //protected makes is unreachable for instances outside this class.
        this.c = c;
        dbHelper = new DatabaseHelper(c, this);
    }

    private void open() {
        db = dbHelper.getWritableDatabase();
    }

    private void close() {
        dbHelper.close();
    }

    public ArrayList<RoosterNotification> getNotifications() {
        open();
        ArrayList<RoosterNotification> notifications = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NOTIFICATIONS, null);
        c.moveToFirst();
        while(!c.isAfterLast()) {
            int id = c.getInt(c.getColumnIndex(S.NOTIFICATION_ID));
            String contentTitle = c.getString(c.getColumnIndex(S.CONTENT_TITLE));
            String contentSubtitle = c.getString(c.getColumnIndex(S.CONTENT_SUBTITLE));
            Date showDate = new Date(c.getLong(c.getColumnIndex(S.SHOW_DATE)));

            RoosterNotification notification = new RoosterNotification(id, contentTitle, contentSubtitle, showDate);
            notifications.add(notification);
        }

        close();
        return notifications;
    }

    public int addNotification(String title, String subtitle, Date showDate) throws SQLException {
        open();
        ContentValues values = new ContentValues();
        values.put(S.CONTENT_TITLE, title);
        values.put(S.CONTENT_SUBTITLE, subtitle);
        values.put(S.SHOW_DATE, showDate.getTime());

        int outcome =  (int) db.insertWithOnConflict(TABLE_NOTIFICATIONS, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        close();
        return outcome;
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context c, DatabaseAdapter adapter) {
            super(c, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_NOTIFICATIONS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Tools.log("Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
        }
    }

}
