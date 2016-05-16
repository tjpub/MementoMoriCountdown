package com.gmail.jaaska.jaakko.calendarcountdown;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Handler for all SQLite activity.
 * Reads and writes into the database.
 *
 *
 * Created by jaakko on 15.5.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    // Constants
    public static final String DB_NAME = "calendarcountdown.db";
    public static final int DB_VERSION = 1;

    private static final String TBLCOUNTDOWN = "countdown";
    private static final String TBLEXCLUDEDDAYS = "excludeddays";

    private static final String COLCOUNTDOWNID = "countdownid";
    private static final String COLCDENDDATE = "cdenddate";
    private static final String COLCDEXCLUDEWEEKENDS = "cdexcludeweekends";
    private static final String COLCDLABEL = "cdlabel";
    private static final String COLCDWIDGET = "cdwidget";

    private static final String COLEXCLUDEDDAYSID = "excludeddaysid";
    private static final String COLEDFROMDATE = "edfromdate";
    private static final String COLEDTODATE = "edtodate";


    // Member variables
    private SQLiteDatabase db;



    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);


    }

    public void openDb() {
        Log.d(TAG, "openDb() - called");
        db = this.getWritableDatabase();
    }

    public void closeDb() {
        Log.d(TAG, "closeDb() - called");
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initSchema(db);
        Log.d(TAG, "onCreate() - done");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Constructs empty tables. This is called when DB is first created.
     */
    private void initSchema(SQLiteDatabase db) {

        // create countdown table
        String sql = "CREATE TABLE '"+TBLCOUNTDOWN+"' (" +
                "`"+COLCOUNTDOWNID+"` INTEGER," +
                "`"+COLCDENDDATE+"` INTEGER," +
                "`"+COLCDEXCLUDEWEEKENDS+"` INTEGER," +
                "`"+COLCDLABEL+"` TEXT," +
                "`"+COLCDWIDGET+"` INTEGER," +
                "PRIMARY KEY("+COLCOUNTDOWNID+")" +
                ")";
        db.execSQL(sql);

        // create excludeddays table
        sql = "CREATE TABLE `"+TBLEXCLUDEDDAYS+"` (" +
                "`"+COLEXCLUDEDDAYSID+"` INTEGER," +
                "`"+COLCOUNTDOWNID+"` INTEGER," +
                "`"+COLEDFROMDATE+"` INTEGER," +
                "`"+COLEDTODATE+"` INTEGER," +
                "PRIMARY KEY("+COLEXCLUDEDDAYSID+")" +
                ")";

        db.execSQL(sql);
        Log.d(TAG, "initSchema() - done");
    }

    /**
     * Reads and returns settings from database.
     * @return
     */
    public List<CountdownSettings> loadSettings() {
        ArrayList<CountdownSettings> ret = new ArrayList<>();

        String sql = "select * from "+TBLCOUNTDOWN;

        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();

        CountdownSettings countdown;
        while(!cur.isAfterLast()) {
            countdown = cursorToCountdown(cur);
            ret.add(countdown);

            // Load excluded date ranges for countdown
            loadExcludedDaysForCountdown(countdown);

            cur.moveToNext();
        }

        Log.d(TAG, "loadSettings() - loaded "+Integer.toString(ret.size())+" countdowns from DB");

        return ret;
    }

    /**
     * Loads and sets excluded date ranges for given countdown.
     * @param countdown
     */
    private void loadExcludedDaysForCountdown(CountdownSettings countdown) {
        String sql = "select * from "+TBLEXCLUDEDDAYS+
                " where "+COLCOUNTDOWNID+"="+Integer.toString(countdown.getDbId());

        Cursor cur = db.rawQuery(sql, null);
        cur.moveToFirst();
        ExcludedDays exclDays;
        while(!cur.isAfterLast()) {
            exclDays = cursorToExcludedDays(cur);
            exclDays.setSettings(countdown); // this is null before setting
            countdown.addExcludedDays(exclDays);
            cur.moveToNext();
        }
    }

    /**
     * Saves all settings given as parameter into DB.
     *
     * @param list
     */
    public void saveToDB(List<CountdownSettings> list) {
        String sql = "";

        // Iterate through all the settings.
        for(CountdownSettings settings : list) {
            // Check if entry for current CountDownSettings already exists in DB
            sql = "select * from "+TBLCOUNTDOWN+" where "+COLCOUNTDOWNID+"=" +Integer.toString(settings.getDbId());
            Cursor cur = db.rawQuery(sql, null);
            if(cur.getCount() > 0) {
                // It id already exist --> update existing
                Log.d(TAG, "saveToDB() - updating countdownid "+Integer.toString(settings.getDbId()));
                sql = "update "+TBLCOUNTDOWN+" set "+COLCDENDDATE+"="+Long.toString(settings.getEndDate())+","+
                        COLCDEXCLUDEWEEKENDS+"="+ (settings.isExcludeWeekends() ? "1" : "0") + ","+
                        COLCDLABEL+"='"+settings.getLabel()+"' where "+COLCOUNTDOWNID+"="+Integer.toString(settings.getDbId());
                db.execSQL(sql);
            }

            else {
                // It did not exist --> insert a new entry
                Log.d(TAG, "saveToDB() - inserting a new countdown entry");
                sql = "insert into "+TBLCOUNTDOWN+"("+COLCDENDDATE+","+COLCDEXCLUDEWEEKENDS+","+COLCDLABEL+") "+
                        "values("+Long.toString(settings.getEndDate())+","+(settings.isExcludeWeekends() ? "1" : "0")+","+
                        "'"+settings.getLabel()+"')";
                db.execSQL(sql);

                // Update settings with corresponding rowID.
                // (Used when inserting excluded ranges)
                Cursor rowIdCur = db.rawQuery("select last_insert_rowid();", null);
                rowIdCur.moveToFirst();
                settings.setDbId(rowIdCur.getInt(0));

            }

            // Save excluded date ranges.
            saveExcludedDaysOfCountdown(settings);
        }

    }

    private void saveExcludedDaysOfCountdown(CountdownSettings countdown) {

        // First clear all the existing excluded ranges for the countdown.
        String sql = "delete from "+TBLEXCLUDEDDAYS+
                " where "+COLCOUNTDOWNID+"="+Integer.toString(countdown.getDbId());
        db.execSQL(sql);

        for(ExcludedDays excludedDays : countdown.getExcludedDays()) {
            // Check if entry already exists.

            sql = "select * from "+TBLEXCLUDEDDAYS+" where "+COLEXCLUDEDDAYSID+"="+Integer.toString(excludedDays.getDbId());
            Cursor cur = db.rawQuery(sql, null);

            if(cur.getCount() > 0) {
                // Entry did already exist --> update it.
                Log.d(TAG, "saveExcludedDaysOfCountdown() - updating an excludeddays entry");
                sql = "update "+TBLEXCLUDEDDAYS+" set "+
                        COLEDFROMDATE+"="+Long.toString(excludedDays.getFromDate())+","+
                        COLEDTODATE+"="+Long.toString(excludedDays.getToDate())+
                        " where "+COLEXCLUDEDDAYSID+"="+Integer.toString(excludedDays.getDbId());
                db.execSQL(sql);

            }

            else {
                // Did not already exist in the database.
                // --> insert a new one.

                Log.d(TAG, "saveExcludedDaysOfCountdown() - inserting a new excludeddays entry");
                sql = "insert into " + TBLEXCLUDEDDAYS + "(" +
                        COLCOUNTDOWNID + "," +
                        COLEDFROMDATE + "," +
                        COLEDTODATE + ") values ("
                        + Integer.toString(countdown.getDbId()) + "," +
                        Long.toString(excludedDays.getFromDate()) + "," +
                        Long.toString(excludedDays.getToDate()) + ")";

                db.execSQL(sql);

                // Update excludedDays with corresponding rowID.
                Cursor exclRowIdCur = db.rawQuery("select last_insert_rowid();", null);
                exclRowIdCur.moveToFirst();
                excludedDays.setDbId(exclRowIdCur.getInt(0));
            }
        }
    }

    private CountdownSettings cursorToCountdown(Cursor cur) {
        CountdownSettings ret = new CountdownSettings();

        ret.setDbId(cur.getInt(0));
        ret.setEndDate(cur.getLong(1));
        ret.setExcludeWeekends(cur.getInt(2) == 1);
        ret.setLabel(cur.getString(3));

        return ret;
    }

    private ExcludedDays cursorToExcludedDays(Cursor cur) {
        ExcludedDays ret = new ExcludedDays();

        ret.setDbId(cur.getInt(0));
        ret.setFromDate(cur.getLong(2));
        ret.setToDate(cur.getLong(3));

        return ret;
    }
}
