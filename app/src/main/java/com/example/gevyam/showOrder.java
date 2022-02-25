package com.example.gevyam;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author : Harel Navon harelnavon2710@gmail.com
 * @version : 1.1
 * @since : 21.2.2022
 * In this Activity, the user can view all of the previous orders that were made.
 */

public class showOrder extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    TextView order;
    ActionBar aBar;
    ColorDrawable cd;


    TextView tv1, tv2, tv3, tv4, tv5, tv6,tv7;
    TextView tvAPP, tvMAIN, tvSIDE, tvDES, tvDRI, tvTIME,tvDATE;
    String[] mealAll;

    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ListView lv;
    ArrayList<String> tbl;
    ArrayAdapter<String> adp;
    ArrayAdapter<String> sortADP;
    Spinner sortSP;
    int sortPos;
    String[] sort;

    String keyID, workerID, companyID, mealID;
    ArrayList<String> workers, companies, meals, times,dates;
    String date;

    String[] columns;
    String orderBy;
    int col1, col2, col3, col4, col5, col6;
    String tmp;

    Intent si;

    /**
     * Sets up all of the Widgets in the Activity.
     * Collects and stores the workers and companies names,as well as every main dish from the
     * Workers, Companies and Meals tables in the database.
     * Then the SortOrder function is called, and the information about all previous orders is
     * displayed.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order);

        aBar = getSupportActionBar();
        cd = new ColorDrawable(getResources().getColor(R.color.order));
        aBar.setBackgroundDrawable(cd);


        order = findViewById(R.id.textView);

        tv1 = findViewById(R.id.textView4);
        tv2 = findViewById(R.id.textView5);
        tv3 = findViewById(R.id.textView6);
        tv4 = findViewById(R.id.textView7);
        tv5 = findViewById(R.id.textView8);
        tv6 = findViewById(R.id.textView3);
        tv7 = findViewById(R.id.textView15);

        tvAPP = findViewById(R.id.textView9);
        tvMAIN = findViewById(R.id.textView10);
        tvSIDE = findViewById(R.id.textView11);
        tvDES = findViewById(R.id.textView12);
        tvDRI = findViewById(R.id.textView13);
        tvTIME = findViewById(R.id.textView14);
        tvDATE = findViewById(R.id.textView16);

        sortSP = findViewById(R.id.sortSP);
        lv = findViewById(R.id.lv);
        lv.setOnItemClickListener(this);
        sort = new String[]{"Date", "Worker", "Food Company"};
        sortADP = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, sort);
        sortSP.setAdapter(sortADP);
        sortSP.setOnItemSelectedListener(this);
        hlp = new HelperDB(this);
        sortPos = 0;
        db = hlp.getReadableDatabase();

        // Collecting Worker Names;
        columns = new String[]{Worker.LAST_NAME, Worker.FIRST_NAME,Worker.ID};
        crsr = db.query(Worker.TABLE_WORKERS, columns, null, null, null, null, null, null);
        col1 = crsr.getColumnIndex(Worker.LAST_NAME);
        col2 = crsr.getColumnIndex(Worker.FIRST_NAME);
        col3 = crsr.getColumnIndex(Worker.ID);
        crsr.moveToFirst();
        workers = new ArrayList<>();
        while (!crsr.isAfterLast()) {
            workers.add(crsr.getString(col2) + " " + crsr.getString(col1) + " , " + crsr.getString(col3));
            crsr.moveToNext();
        }

        // Collecting Food Company Names;
        columns = new String[]{Company.NAME, Company.ACTIVE};
        crsr = db.query(Company.TABLE_COMPANIES, columns, null, null, null, null, null, null);
        col1 = crsr.getColumnIndex(Company.NAME);
        col2 = crsr.getColumnIndex(Company.ACTIVE);
        crsr.moveToFirst();
        companies = new ArrayList<>();
        while (!crsr.isAfterLast()) {
            companies.add(crsr.getString(col1));
            crsr.moveToNext();
        }

        // Collecting Main Dishes;
        columns = new String[]{Meal.MAINDISH};


        crsr = db.query(Meal.TABLE_MEALS, columns, null, null, null, null, null, null);
        col1 = crsr.getColumnIndex(Meal.MAINDISH);
        crsr.moveToFirst();
        meals = new ArrayList<>();
        while (!crsr.isAfterLast()) {
            meals.add(crsr.getString(col1));
            crsr.moveToNext();
        }

        db.close();
        crsr.close();
        sortOrder(Order.KEY_ID);
    }

    /**
     * Sorts the information read from the Orders table in the database,
     * based on what the user has selected in the Spinner Widget.
     *
     * @param category : Determines what category will the information presented to the user  be
     *                 sorted by, based on their choice in the SortSP Spinner Widget.
     */
    public void sortOrder(String category) {
        times = new ArrayList<>();
        dates = new ArrayList<>();
        orderBy = category;

        db = hlp.getReadableDatabase();
        tbl = new ArrayList<>();
        crsr = db.query(Order.TABLE_ORDERS, null, null, null, null, null, orderBy, null);
        col1 = crsr.getColumnIndex(Order.KEY_ID);
        col2 = crsr.getColumnIndex(Order.WORKER);
        col3 = crsr.getColumnIndex(Order.COMPANY);
        col4 = crsr.getColumnIndex(Order.MEAL);
        col5 = crsr.getColumnIndex(Order.DATE);
        col6 = crsr.getColumnIndex(Order.TIME);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            keyID = crsr.getString(col1);
            workerID = crsr.getString(col2);
            companyID = crsr.getString(col3);
            mealID = crsr.getString(col4);
            dates.add(crsr.getString(col5));
            times.add(crsr.getString(col6));

            tmp = "" + keyID + " : " + workers.get(Integer.parseInt(workerID) - 1) + " , " + companies.get(Integer.parseInt(companyID) - 1) + " , " + meals.get(Integer.parseInt(mealID) - 1);
            tbl.add(tmp);
            crsr.moveToNext();

        }
        crsr.close();
        db.close();
        adp = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, tbl);
        lv.setAdapter(adp);
    }


    /**
     * The listener for items selected in the SortSP Spinner Widget.
     * Reads, filters and sorts the information from the Orders table.
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        sortPos = i;
        if (sortPos == 0) sortOrder(Order.KEY_ID);
        else if (sortPos == 1) sortOrder(Order.WORKER);
        else if (sortPos == 2) sortOrder(Order.COMPANY);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * The listener for the ListView in which the information is presented.
     * When the user clicks a cell in the list, more information about that order will be presented
     * in the bottom of the screen.
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        tmp = lv.getItemAtPosition(i).toString();
        int dotsIndex = 0;
        for (int j = 0; j < tmp.length(); j++) {
            if (tmp.charAt(j) == ':') {
                dotsIndex = j;
                break;
            }

        }
        keyID = tmp.substring(0, dotsIndex - 1);
        if (tv1.getVisibility() == View.INVISIBLE) {
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            tv3.setVisibility(View.VISIBLE);
            tv4.setVisibility(View.VISIBLE);
            tv5.setVisibility(View.VISIBLE);
            tv6.setVisibility(View.VISIBLE);
            tv7.setVisibility(View.VISIBLE);
        }

        order.setText("Order Number " + keyID);
        mealAll = new String[5];
        db = hlp.getReadableDatabase();
        crsr = db.query(Order.TABLE_ORDERS, new String[]{Order.MEAL}, Order.KEY_ID + "=?", new String[]{keyID}, null, null, null, null);
        col1 = crsr.getColumnIndex(Order.MEAL);
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            mealID = crsr.getString(col1);
            crsr.moveToNext();
        }
        crsr.close();

        crsr = db.query(Meal.TABLE_MEALS, null, Meal.KEY_ID + "=?", new String[]{mealID}, null, null, null);
        col1 = crsr.getColumnIndex(Meal.APPETIZER);
        col2 = crsr.getColumnIndex(Meal.MAINDISH);
        col3 = crsr.getColumnIndex(Meal.SIDE);
        col4 = crsr.getColumnIndex(Meal.DESSERT);
        col5 = crsr.getColumnIndex(Meal.DRINK);
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            mealAll[0] = crsr.getString(col1);
            mealAll[1] = crsr.getString(col2);
            mealAll[2] = crsr.getString(col3);
            mealAll[3] = crsr.getString(col4);
            mealAll[4] = crsr.getString(col5);
            crsr.moveToNext();
        }
        crsr.close();
        db.close();

        for (int j = 0; j < 5; j++) {
            if (mealAll[j].isEmpty()) mealAll[j] = "NONE";
        }

        tvAPP.setText(mealAll[0]);
        tvMAIN.setText(mealAll[1]);
        tvSIDE.setText(mealAll[2]);
        tvDES.setText(mealAll[3]);
        tvDRI.setText(mealAll[4]);
        tvTIME.setText(times.get(i));
        tvDATE.setText(dates.get(i));

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