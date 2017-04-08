package com.example.tcp.currencyconverter.api;

/**
 * Created by tcp on 4/7/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CURRENCY_TABLE_NAME = "currencies";
    public static final String CURRENCY_COLUMN_ID = "id";
    public static final String CURRENCY_COLUMN_NAME = "name";//char3
    public static final String CURRENCY_COLUMN_FNAME = "fName";//name
    public static final String CURRENCY_COLUMN_RATE = "rate";
    //private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table currencies " +
                        "(id integer primary key, name text, fName text, rate text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS currencies");
        onCreate(db);
    }

    public boolean insertCurrency (String name, String fName, String rate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("fName", fName);
        contentValues.put("rate", rate);
        db.insert("currencies", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select * from currencies where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CURRENCY_TABLE_NAME);
        return numRows;
    }

    public boolean updateCurrency (Integer id, String name, String fName, String rate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("fName", fName);
        contentValues.put("rate", rate);
        db.update("currencies", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteCurrency (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("currencies",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllCurrencies() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from currencies", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(CURRENCY_COLUMN_ID)));
            res.moveToNext();
        }
        return array_list;
    }
}