package com.example.gevyam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "GevYam.db";
    private static final int DATABASE_VERSION = 8;
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
        strCreate+=" "+Worker.ACTIVE+" TEXT";
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+Company.TABLE_COMPANIES;
        strCreate+=" ("+Company.KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+Company.TAX+" TEXT,";
        strCreate+=" "+Company.NAME+" TEXT,";
        strCreate+=" "+Company.MAIN+" TEXT,";
        strCreate+=" "+Company.SECONDARY+" TEXT,";
        strCreate+=" "+Company.ACTIVE+" TEXT";
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+Order.TABLE_ORDERS;
        strCreate+=" ("+Order.KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+Order.WORKER+" TEXT,";
        strCreate+=" "+Order.COMPANY+" TEXT,";
        strCreate+=" "+Order.MEAL+" TEXT,";
        strCreate+=" "+Order.DATE+" TEXT,";
        strCreate+=" "+Order.TIME+" TEXT";
        strCreate+=");";
        db.execSQL(strCreate);

        strCreate="CREATE TABLE "+Meal.TABLE_MEALS;
        strCreate+=" ("+Meal.KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+Meal.APPETIZER+" TEXT,";
        strCreate+=" "+Meal.MAINDISH+" TEXT,";
        strCreate+=" "+Meal.SIDE+" TEXT,";
        strCreate+=" "+Meal.DESSERT+" TEXT,";
        strCreate+=" "+Meal.DRINK+" TEXT";
        strCreate+=");";
        db.execSQL(strCreate);




    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        strDelete="DROP TABLE IF EXISTS "+Worker.TABLE_WORKERS;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+Company.TABLE_COMPANIES;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+Meal.TABLE_MEALS;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+Order.TABLE_ORDERS;
        db.execSQL(strDelete);
        onCreate(db);

    }
}
