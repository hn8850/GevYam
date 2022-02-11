package com.example.gevyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;


public class ShowInfo extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener {
    Switch sw;

    TextView showWork, ShowComp;

    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ListView lv;
    ArrayList<String> tbl;
    ArrayAdapter adp;


    String name1;
    String name2;

    Intent si;
    int mode;

    String ID;
    int col2, col3, col4, col7;
    String active;
    String tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);
        sw = findViewById(R.id.sw3);
        sw.setOnCheckedChangeListener(this);
        ShowComp = findViewById(R.id.tv8);
        showWork = findViewById(R.id.tv2);
        ShowComp.setTypeface(null, Typeface.NORMAL);
        ShowComp.setTextColor(Color.BLACK);
        ShowComp.setTextSize(15);
        showWork.setTextSize(20);
        showWork.setTypeface(null, Typeface.BOLD);
        showWork.setTextColor(Color.rgb(98, 0, 238));

        lv = findViewById(R.id.lv);
        lv.setOnItemClickListener(this);


        hlp = new HelperDB(this);
        workerMode();
        mode = 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mode == 0) workerMode();
        else companyMode();
    }

    /**
     * On Click method of the back button. Takes the user back to the infoHub Activity.
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }


    /**
     * Sets all of the Views according to the information needed for a new worker.
     */
    public void workerMode() {
        db = hlp.getWritableDatabase();
        tbl = new ArrayList<>();
        crsr = db.query(Worker.TABLE_WORKERS, null, null, null, null, null, null);
        col2 = crsr.getColumnIndex(Worker.LAST_NAME);
        col3 = crsr.getColumnIndex(Worker.FIRST_NAME);
        col4 = crsr.getColumnIndex(Worker.ID);

        col7 = crsr.getColumnIndex(Worker.ACTIVE);


        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            name1 = crsr.getString(col2);
            name2 = crsr.getString(col3);
            ID = crsr.getString(col4);

            if (crsr.getInt(col7) == 0) active = "Active";
            else active = "Not Active";


            tmp = "" + name1 + ", " + name2 + ", " + ID + ", " + active;
            tbl.add(tmp);
            crsr.moveToNext();

        }

        crsr.close();
        db.close();
        adp = new ArrayAdapter<>(
                this, R.layout.support_simple_spinner_dropdown_item, tbl);
        lv.setAdapter(adp);


    }

    /**
     * Sets all of the Views according to the information needed for a new company.
     */
    public void companyMode() {


    }


    /**
     * Listener for toggles of the switch, and shifts the activity layout between worker and company modes.
     *
     * @param compoundButton The switch
     * @param b              Is the switch toggled on or not?
     */

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b) {
            ShowComp.setTypeface(null, Typeface.NORMAL);
            ShowComp.setTextColor(Color.BLACK);
            ShowComp.setTextSize(15);
            showWork.setTextSize(20);
            showWork.setTypeface(null, Typeface.BOLD);
            showWork.setTextColor(Color.rgb(98, 0, 238));
            workerMode();
            mode = 0;
        } else {
            showWork.setTypeface(null, Typeface.NORMAL);
            showWork.setTextColor(Color.BLACK);
            showWork.setTextSize(15);
            ShowComp.setTextSize(20);
            ShowComp.setTypeface(null, Typeface.BOLD);
            ShowComp.setTextColor(Color.rgb(98, 0, 238));
            companyMode();
            mode = 1;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mainhome) {
            si = new Intent(this, MainActivity.class);
            startActivity(si);
        } else if (id == R.id.setting) {
            si = new Intent(this, infoHub.class);
            startActivity(si);
        }
        /*
        else if (id == R.id.order) {
            si = new Intent(this, GetOrder.class);
            startActivity(si);
        }
        else if (id == R.id.infoOrder) {
            si = new Intent(this, ShowOrder.class);
            startActivity(si);
        }

         */
        else {
            si = new Intent(this, credits.class);
            startActivity(si);
        }

        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        si = new Intent(this, specific.class);
        si.putExtra("pos", i);
        si.putExtra("mode", mode);
        startActivity(si);


    }
}