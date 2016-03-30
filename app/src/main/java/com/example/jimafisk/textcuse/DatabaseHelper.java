package com.example.jimafisk.textcuse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jimafisk on 3/20/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TextHer.db";

    public static final String TABLE_CONTACTS = "Contacts";
    public static final String CONTACTS_ID = "ID";
    public static final String CONTACTS_NAME = "Name";
    public static final String CONTACTS_PHONE = "Phone";

    public static final String TABLE_SAVED_EXCUSES = "Saved_excuses";
    public static final String SAVED_EXCUSES_ID = "ID";
    public static final String SAVED_EXCUSES_EXCUSE = "Excuse";

    public static final String TABLE_RESPONSES = "Responses";
    public static final String RESPONSES_COL_ID = "ID";
    public static final String RESPONSES_COL_LOCATION = "Location";
    public static final String RESPONSES_COL_CONTACT_ID = "Contact_ID";
    public static final String RESPONSES_COL_TIME = "Time";
    public static final String RESPONSES_COL_EXCUSE = "Excuse";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + " (" + CONTACTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CONTACTS_NAME + " TEXT, " + CONTACTS_PHONE + " TEXT)");
        db.execSQL("create table " + TABLE_SAVED_EXCUSES + " (" + SAVED_EXCUSES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SAVED_EXCUSES_EXCUSE + " TEXT)");
        //db.execSQL("create table " + TABLE_RESPONSES + " (" + RESPONSES_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + RESPONSES_COL_LOCATION + " TEXT, " + RESPONSES_COL_CONTACT + " TEXT, " + RESPONSES_COL_TIME + " DATETIME, " + RESPONSES_COL_EXCUSE + " TEXT)");
        db.execSQL("create table " + TABLE_RESPONSES + " (" + RESPONSES_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + RESPONSES_COL_LOCATION + " TEXT, " + RESPONSES_COL_CONTACT_ID + " INTEGER, " + RESPONSES_COL_TIME + " TEXT, " + RESPONSES_COL_EXCUSE + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLES IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLES IF EXISTS " + TABLE_SAVED_EXCUSES);
        db.execSQL("DROP TABLES IF EXISTS " + TABLE_RESPONSES);
        onCreate(db);
    }

    //public boolean insertResponse(String Location, String Contact, Date Time, String Excuse) {
    public boolean insertResponse(String Location, Integer Contact, String Time, String Excuse) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RESPONSES_COL_LOCATION, Location);
        contentValues.put(RESPONSES_COL_CONTACT_ID, Contact);
        //final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSS");
        //contentValues.put(RESPONSES_COL_TIME, parser.format(Time));
        contentValues.put(RESPONSES_COL_TIME, Time);
        contentValues.put(RESPONSES_COL_EXCUSE, Excuse);
        long result = db.insert(TABLE_RESPONSES, null, contentValues);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean insertContact(String Name, String Phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_NAME, Name);
        contentValues.put(CONTACTS_PHONE, Phone);
        long result = db.insert(TABLE_CONTACTS, null, contentValues);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }
    public boolean removeContacts(List<Integer> contactDeleteIDs) {
        SQLiteDatabase db = this.getWritableDatabase();
        //ContentValues contentValues = new ContentValues();
        Integer numRowsDeleted = 0;
        for (int i = 0; i < contactDeleteIDs.size(); i++) {
            //contentValues.put(CONTACTS_ID, (Integer) contactDeleteIDs.get(i));
            String currentID = String.valueOf(contactDeleteIDs.get(i));
            numRowsDeleted += db.delete(TABLE_CONTACTS, "ID = ?", new String[] {currentID});
        }
        if (numRowsDeleted > 0) {
            return true;
        } else {
            return false;
        }

    }
    public Cursor getContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select " + CONTACTS_NAME + ", " + CONTACTS_ID + " from " + TABLE_CONTACTS, null);
        return result;
    }

    public Cursor getLastLocation() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select " + RESPONSES_COL_LOCATION + " from " + TABLE_RESPONSES + " order by " + RESPONSES_COL_ID + " DESC LIMIT 1", null);
        return result;
    }
    public Cursor getLastMeetTime() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select " + RESPONSES_COL_TIME + " from " + TABLE_RESPONSES + " order by " + RESPONSES_COL_ID + " DESC LIMIT 1", null);
        return result;
    }

}
