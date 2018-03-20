package com.example.libin.fyp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "course.db";
    private static final String TABLE_COURSE = "course";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_COURSERNAME = "name";

    private static final String COLUMN_PLACE = "place";
    private static final String COLUMN_STARTTIME = "startTime";
    private static final String COLUMN_ENDTIME = "endTime";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_COURSE + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DAY + " DAY OF WEEK, " +
                COLUMN_COURSERNAME + " TEXT, " +
                COLUMN_PLACE + " TEXT, " +
                COLUMN_STARTTIME + " TEXT, " +
                COLUMN_ENDTIME + " TEXI" + ")";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_COURSE);
        onCreate(db);
    }

    // add a new row to the database
    public void addSession(Session session) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_COURSERNAME, session.getName());
        values.put(COLUMN_DAY, session.getDay());
        values.put(COLUMN_PLACE, session.getPlace());
        values.put(COLUMN_STARTTIME, session.getStartTime());
        values.put(COLUMN_ENDTIME, session.getEndTime());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_COURSE, null, values);
        db.close();
    }

    // delete a course from database
    public void deleteCourse(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_COURSE + " WHERE " + COLUMN_ID + "=\"" + id + "\";");
    }

    // print out the database as a string
//    public String databaseToString() {
//        String dbString = "";
//        SQLiteDatabase db = getWritableDatabase();
//        String query = " SELECT * " + "FROM " + TABLE_COURSE + ";";
////        String query = "DESCRIBE " + TABLE_COURSE + ";";
//
//        //cursor point to a location in your results
//        Cursor c = db.rawQuery(query, null);
//        //move to the first row in your results
//        c.moveToFirst();
//        while (!c.isAfterLast()) {
//            if (c.getString(c.getColumnIndex("name")) != null) {
//                dbString += c.getString(c.getColumnIndex("name"));
//                dbString += "\n";
//            }
//        }
//        db.close();
//        return dbString;
//    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_COURSE, null);
        return res;
    }


    public void updateData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        db.update(TABLE_COURSE, contentValues, "_id = ?", new String[]{id});
    }
}
