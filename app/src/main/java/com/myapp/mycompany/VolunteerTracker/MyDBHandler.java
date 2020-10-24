package net.empoweringtechnology.volunteertracker;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "VolunteerData.db";
    public static final String TABLE_HOURS = "VolunteerActHours";
    public static final String COLUMN_ROWID = "_id";
    public static final String COLUMN_ORG = "Organization";
    public static final String COLUMN_ACTIVITY = "Activity";
    public static final String COLUMN_DATE = "Volunteer_Date";
    public static final String COLUMN_HOURS = "Hours";
    public static final String COLUMN_ORG_HOURS = "OrgHours";
    public static final String COLUMN_TOTAL_HOURS = "totalHours";

    public MyDBHandler(Context context, String
            name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String table_query = " CREATE TABLE " + TABLE_HOURS + "(" +
                COLUMN_ROWID + " integer PRIMARY KEY autoincrement," +
                COLUMN_ORG + " TEXT, " +
                COLUMN_ACTIVITY + " TEXT, " +
                COLUMN_DATE + " DATE, " +
                COLUMN_HOURS + " TEXT " +
                ");";
        db.execSQL(table_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOURS);
        onCreate(db);
    }

    /* SQLite database call to save activity data */
    public void SaveHours(Hours hours) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORG, hours.get_org());
        values.put(COLUMN_ACTIVITY, hours.get_activity());
        values.put(COLUMN_DATE, hours.get_date());
        values.put(COLUMN_HOURS, hours.get_hours());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_HOURS, null, values);
        db.close();
    }


    /* SQLite database call to update activity data.  */
    public void UpdateHours(Hours hours, int idVal) {
        String update_query = "UPDATE " + TABLE_HOURS + " SET  " + COLUMN_ORG + " = '" + hours.get_org() + "'," +
                COLUMN_ACTIVITY + " = '" + hours.get_activity() + "'," +
                COLUMN_DATE + " = '" + hours.get_date() + "'," +
                COLUMN_HOURS + " = '" + hours.get_hours() + "'" +

                " WHERE " + COLUMN_ROWID + " =" + idVal;
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(update_query);
            db.close();
        } catch (Exception ex) {
            Log.e("Error", "Error" + ex);

        }
    }

    /* SQLite database call to get activity data for home page */
    public Cursor FetchVoluteerInfo() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor mCursor = db.query(TABLE_HOURS, new String[]{COLUMN_ROWID,
                        COLUMN_ORG, COLUMN_ACTIVITY, COLUMN_DATE, COLUMN_HOURS},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /* SQLite database call to get report data */
    public Cursor FetchReportData() {
        SQLiteDatabase db = getWritableDatabase();

        String report_query = "SELECT  ROWID AS _id, " + COLUMN_ORG + ", sum(" + COLUMN_HOURS + ") AS " + COLUMN_ORG_HOURS + " FROM " + TABLE_HOURS + " WHERE " + COLUMN_HOURS + "> 0 " + " GROUP BY " + COLUMN_ORG + " Order BY " + COLUMN_ORG_HOURS + " DESC";

        Cursor cursor = db.rawQuery(report_query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /* SQLite database call to get total hours of volunteering data */
    public Cursor getTotalHours() {
        SQLiteDatabase db = getWritableDatabase();

        String report_query = "SELECT sum(" + COLUMN_HOURS + ") AS " + COLUMN_TOTAL_HOURS + " FROM " + TABLE_HOURS;

        Cursor cursor = db.rawQuery(report_query, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    /* SQLite database call to delete an acitivty*/
    public void DeleteItem(int id) {
        String delete_query = "DELETE FROM " + TABLE_HOURS + " WHERE " + COLUMN_ROWID + " =" + id;
        try {
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(delete_query);
            db.close();
        } catch (Exception ex) {
            Log.e("Error", "Error" + ex);

        }
    }
}