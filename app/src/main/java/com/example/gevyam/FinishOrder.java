package com.example.gevyam;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;


public class FinishOrder extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner FC;
    EditText getCard;
    String keyIDWork;
    String keyIDComp;
    Long keyIDMeal;
    boolean exist;
    ArrayList food;

    ArrayList<String> compsName;
    int spinPos;
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ArrayAdapter<String> adp;

    String[] columns;
    int col1;

    ContentValues cv;

    Intent si;
    Intent gi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);

        gi = getIntent();
        food = gi.getStringArrayListExtra("food");

        getCard = findViewById(R.id.getCard);
        FC = findViewById(R.id.chooseFC);

        compsName = new ArrayList<>();
        compsName.add("Choose a Food Company");
        columns = new String[]{Company.NAME};
        hlp = new HelperDB(this);
        db = hlp.getReadableDatabase();
        crsr = db.query(Company.TABLE_COMPANIES, columns, Company.ACTIVE + "=?", new String[]{"0"}, null, null, null, null);
        col1 = crsr.getColumnIndex(Company.NAME);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            compsName.add(crsr.getString(col1));
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adp = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, compsName);
        FC.setAdapter(adp);
        FC.setOnItemSelectedListener(this);

        exist = false;


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void submit(View view) {
        if (spinPos == 0)
            Toast.makeText(this, "Please Choose A Food Company", Toast.LENGTH_LONG).show();
        else {
            keyIDWork = getCard.getText().toString();
            columns = new String[]{Worker.KEY_ID};
            hlp = new HelperDB(this);
            db = hlp.getReadableDatabase();
            crsr = db.query(Worker.TABLE_WORKERS, columns, Worker.ACTIVE + "=?", new String[]{"0"}, null, null, null, null);
            col1 = crsr.getColumnIndex(Worker.KEY_ID);

            crsr.moveToFirst();
            while (!crsr.isAfterLast() && !exist) {
                if (keyIDWork.equals(crsr.getString(col1))) {
                    exist = true;
                }
                crsr.moveToNext();
            }
            crsr.close();
            db.close();

            if (!exist)
                Toast.makeText(this, "Card Number Does Not Match Any Active Worker", Toast.LENGTH_LONG).show();
            else {
                cv = new ContentValues();
                cv.put(Meal.APPETIZER, food.get(0).toString());
                cv.put(Meal.MAINDISH, food.get(1).toString());
                cv.put(Meal.SIDE, food.get(2).toString());
                cv.put(Meal.DESSERT, food.get(3).toString());
                cv.put(Meal.DRINK, food.get(4).toString());
                db = hlp.getWritableDatabase();
                keyIDMeal = db.insert(Meal.TABLE_MEALS, null, cv);
                db.close();
                cv.clear();


                cv = new ContentValues();
                cv.put(Order.WORKER, keyIDWork);
                cv.put(Order.COMPANY, keyIDComp);
                cv.put(Order.MEAL, String.valueOf(keyIDMeal));
                cv.put(Order.DATE, String.valueOf(java.time.LocalDate.now()));
                cv.put(Order.TIME, String.valueOf(java.time.LocalTime.now()).substring(0, 8));
                db = hlp.getWritableDatabase();
                db.insert(Order.TABLE_ORDERS, null, cv);
                db.close();
                Toast.makeText(this, "Order Made Successfully!", Toast.LENGTH_LONG).show();
                si = new Intent(this, MainActivity.class);
                startActivity(si);


            }
        }


    }

    /**
     * On Click method of the back button. Takes the user back to the infoHub Activity.
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinPos = i;
        if (i != 0) {
            keyIDComp = String.valueOf(i);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}