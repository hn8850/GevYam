package com.example.gevyam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GevYam.db";
    private static final int DATABASE_VERSION = 2;
    String strCreate, strDelete;


    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        strCreate="CREATE TABLE "+Worker.TABLE_WORKERS;
        strCreate+=" ("+Worker.KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+Worker.FIRST_NAME+" TEXT,";
        strCreate+=" "+Worker.LAST_NAME+" TEXT,";
        strCreate+=" "+Worker.ID+" TEXT,";
        strCreate+=" "+Worker.COMPANY_NAME+" TEXT,";
        strCreate+=" "+Worker.PHONE_NUMBER+" TEXT,";
        strCreate+=" "+Worker.ACTIVE+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        strDelete="DROP TABLE IF EXISTS "+Worker.TABLE_WORKERS;
        db.execSQL(strDelete);
        onCreate(db);

    }
}
