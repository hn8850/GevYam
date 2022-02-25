package com.example.gevyam;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.util.ArrayList;

/**
 * @author : Harel Navon harelnavon2710@gmail.com
 * @version : 1.1
 * @since : 21.2.2022
 * In this Activity, the user completes their food order, by choosing a food company and entering
 * their worker card number.
 */

public class FinishOrder extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner FC;
    EditText getCard;
    String keyIDWork;
    String keyIDComp;
    ArrayList<Object> compIDs;
    Long keyIDMeal;
    boolean exist;
    ArrayList<String> food;
    ArrayList<String> compsName;
    int spinPos;
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ArrayAdapter<String> adp;
    String[] columns;
    int col1, col2;
    ContentValues cv;
    Intent si;
    Intent gi;


    /**
     * Sets up the necessary information to complete a food order. Sets up a Spinner Widget with
     * all of the active Food Companies.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);

        gi = getIntent();
        food = gi.getStringArrayListExtra("food");
        getCard = findViewById(R.id.getCard);
        FC = findViewById(R.id.chooseFC);
        compsName = new ArrayList<>();
        compIDs = new ArrayList<>();
        compsName.add("Choose a Food Company");
        columns = new String[]{Company.NAME, Company.KEY_ID};
        hlp = new HelperDB(this);
        db = hlp.getReadableDatabase();
        crsr = db.query(Company.TABLE_COMPANIES, columns, Company.ACTIVE + "=?", new String[]{"0"}, null, null, null, null);
        col1 = crsr.getColumnIndex(Company.NAME);
        col2 = crsr.getColumnIndex(Company.KEY_ID);
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            compsName.add(crsr.getString(col1));
            compIDs.add(crsr.getString(col2));
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

    /**
     * The On-Click method of the SUBMIT Button Widget.
     * Checks if the user has selected a Food Company and has entered a card number of an active worker.
     * If so, completes the order and inserts new rows into the Orders and Meals tables.
     *
     * @param view
     */
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
                cv.put(Meal.APPETIZER, food.get(0));
                cv.put(Meal.MAINDISH, food.get(1));
                cv.put(Meal.SIDE, food.get(2));
                cv.put(Meal.DESSERT, food.get(3));
                cv.put(Meal.DRINK, food.get(4));
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
     * The onItemSelected Listener for the food companies Spinner Widget.
     * Sets the keyIDcomp variable to the keyID of the selected food company.
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spinPos = i;
        if (i != 0) {
            keyIDComp = compIDs.get(i - 1).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * On Click method of the back button. Takes the user back to the Main Activity.
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * Creates the General Options Menu (menu.xml) for this Activity.
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    /**
     * Sends the user over to the Activity chosen in the Options Menu.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mainhome) {
            si = new Intent(this, MainActivity.class);
        } else if (id == R.id.order) {
            si = new Intent(this, GetOrder.class);
        } else if (id == R.id.infoOrder) {
            si = new Intent(this, showOrder.class);
        } else if (id == R.id.setting) {
            si = new Intent(this, ShowInfo.class);
        } else {
            si = new Intent(this, credits.class);
        }
        startActivity(si);
        return true;
    }
}