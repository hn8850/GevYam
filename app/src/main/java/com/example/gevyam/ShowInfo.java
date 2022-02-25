package com.example.gevyam;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * @author : Harel Navon harelnavon2710@gmail.com
 * @version : 1.1
 * @since : 21.2.2022
 * In this Activity, the user can view all of the information in the Workers and Companies tables from
 * the database.
 * The user can also filter and sort by specific parameters for ease of use, and access each row of
 * information and edit it.
 */

public class ShowInfo extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    Switch sw;
    Button add;
    ActionBar aBar;
    ColorDrawable cd;

    TextView workTv, compTv;
    TextView info;

    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ListView lv;
    ArrayList<String> tbl;
    ArrayAdapter adp;

    String[] showWork, sortWork, showComp, sortComp;

    ArrayAdapter showADP;
    ArrayAdapter sortADP;
    Spinner showSP;
    Spinner sortSP;
    int showPos;
    int sortPos;

    String[] columns;
    String selection;
    String[] selectionArgs;
    String orderBy;

    String[] filterShow;

    String keyID, name1, name2, workCompany, ID;
    String FCname, FCtax, FCmain, FCsecondary;

    Intent si;
    int mode;

    int col1, col2, col3, col4, col5, col6;
    String active;
    String tmp;

    /**
     * Sets up all of the Widgets in the Activity.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);


        add = findViewById(R.id.add);
        add.setBackgroundColor(getResources().getColor(R.color.worker));
        add.setText("Add Worker");
        add.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_person_add_alt_1_24, 0);


        info = findViewById(R.id.info);
        info.setText("Card Number, First Name, Last Name, ID, Company, Status");
        sw = findViewById(R.id.sw3);
        sw.setOnCheckedChangeListener(this);
        compTv = findViewById(R.id.tv8);
        workTv = findViewById(R.id.tv2);
        compTv.setTypeface(null, Typeface.NORMAL);
        compTv.setTextColor(Color.BLACK);
        compTv.setTextSize(15);
        workTv.setTextSize(20);
        workTv.setTypeface(null, Typeface.BOLD);
        workTv.setTextColor(Color.BLACK);

        aBar = getSupportActionBar();
        cd = new ColorDrawable(getResources().getColor(R.color.worker));
        aBar.setBackgroundDrawable(cd);

        lv = findViewById(R.id.lv);
        lv.setOnItemClickListener(this);

        filterShow = new String[]{"0", "1", null};


        showWork = new String[]{"Active Workers", "Inactive Workers", "All Workers"};
        sortWork = new String[]{"Card Number - Rising", "Card Number - Descending", "Last Name  A-->Z", "Last Name  Z-->A", "Company  A-->Z", "Company  Z-->A"};
        showComp = new String[]{"Active Companies", "Inactive Companies", "All Companies"};
        sortComp = new String[]{"Company Number - Rising", "Company Number - Descending", "Name  A-->Z", "Name  Z-->A"};
        showSP = findViewById(R.id.showSP);
        sortSP = findViewById(R.id.sortSP);
        showADP = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, showWork);
        showSP.setAdapter(showADP);
        sortADP = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, sortWork);
        sortSP.setAdapter(sortADP);


        showSP.setOnItemSelectedListener(this);
        sortSP.setOnItemSelectedListener(this);
        hlp = new HelperDB(this);
        mode = 0;
        showPos = 0;
        sortPos = 0;
    }

    /**
     * Since this Activity is returned to after editing/submitting new information,
     * this Method is in order to re-read, sort and filter the information based on state of the
     * Activity before it was left, and based on the new/changed information in the database.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (sortPos == 0 && mode == 0) sortWorker(true, Worker.KEY_ID);
        else if (sortPos == 1 && mode == 0) sortWorker(false, Worker.KEY_ID);
        else if (sortPos == 2 && mode == 0) sortWorker(true, Worker.LAST_NAME);
        else if (sortPos == 3 && mode == 0) sortWorker(false, Worker.LAST_NAME);
        else if (sortPos == 4 && mode == 0) sortWorker(true, Worker.COMPANY_NAME);
        else if (sortPos == 5 && mode == 0) sortWorker(false, Worker.COMPANY_NAME);
        else if (sortPos == 0 && mode == 1) sortCompany(true, Company.KEY_ID);
        else if (sortPos == 1 && mode == 1) sortCompany(false, Company.KEY_ID);
        else if (sortPos == 2 && mode == 1) sortCompany(true, Company.NAME);
        else if (sortPos == 3 && mode == 1) sortCompany(false, Company.NAME);

    }

    /**
     * Sorts the information read from the Companies table in the database,
     * based on what the user has selected in the Spinner Widget's.
     *
     * @param order    : Determines if the order of the information presented to the user will be
     *                 rising or descending, based on their choice in the SortSP Spinner Widget.
     * @param category : Determines what category will the information presented to the user  be
     *                 sorted by, based on their choice in the SortSP Spinner Widget.
     */
    public void sortCompany(boolean order, String category) {
        columns = new String[]{Company.KEY_ID, Company.NAME, Company.TAX, Company.MAIN, Company.SECONDARY, Company.ACTIVE};
        if (showPos != 2) {
            selection = Company.ACTIVE + "=?";
            selectionArgs = new String[]{filterShow[showPos]};
        } else {
            selection = null;
            selectionArgs = null;
        }

        if (order) orderBy = category;
        else orderBy = category + " DESC";

        db = hlp.getReadableDatabase();
        tbl = new ArrayList<>();
        crsr = db.query(Company.TABLE_COMPANIES, columns, selection, selectionArgs, null, null, orderBy, null);
        col1 = crsr.getColumnIndex(Company.KEY_ID);
        col2 = crsr.getColumnIndex(Company.NAME);
        col3 = crsr.getColumnIndex(Company.TAX);
        col4 = crsr.getColumnIndex(Company.MAIN);
        col5 = crsr.getColumnIndex(Company.SECONDARY);
        col6 = crsr.getColumnIndex(Worker.ACTIVE);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            keyID = crsr.getString(col1);
            FCname = crsr.getString(col2);
            FCtax = crsr.getString(col3);
            FCmain = crsr.getString(col4);
            FCsecondary = crsr.getString(col5);

            if (crsr.getInt(col6) == 0) active = "Active";
            else active = "Not Active";


            tmp = "" + keyID + " : " + FCname + " - " + FCtax + " , " + FCmain + " , " + FCsecondary + " - " + active;
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
     * Sorts the information read from the Workers table in the database,
     * based on what the user has selected in the Spinner Widget's.
     *
     * @param order    : Determines if the order of the information presented to the user will be
     *                 rising or descending, based on their choice in the SortSP Spinner Widget.
     * @param category : Determines what category will the information presented to the user  be
     *                 sorted by, based on their choice in the SortSP Spinner Widget.
     */
    public void sortWorker(boolean order, String category) {
        columns = new String[]{Worker.KEY_ID, Worker.LAST_NAME, Worker.FIRST_NAME, Worker.ID, Worker.COMPANY_NAME, Worker.ACTIVE};
        if (showPos != 2) {
            selection = Company.ACTIVE + "=?";
            selectionArgs = new String[]{filterShow[showPos]};
        } else {
            selection = null;
            selectionArgs = null;
        }

        if (order) orderBy = category;
        else orderBy = category + " DESC";


        db = hlp.getReadableDatabase();
        tbl = new ArrayList<>();
        crsr = db.query(Worker.TABLE_WORKERS, columns, selection, selectionArgs, null, null, orderBy, null);
        col1 = crsr.getColumnIndex(Worker.KEY_ID);
        col2 = crsr.getColumnIndex(Worker.LAST_NAME);
        col3 = crsr.getColumnIndex(Worker.FIRST_NAME);
        col4 = crsr.getColumnIndex(Worker.ID);
        col5 = crsr.getColumnIndex(Worker.COMPANY_NAME);
        col6 = crsr.getColumnIndex(Worker.ACTIVE);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            keyID = crsr.getString(col1);
            name1 = crsr.getString(col2);
            name2 = crsr.getString(col3);
            ID = crsr.getString(col4);
            workCompany = crsr.getString(col5);

            if (crsr.getInt(col6) == 0) active = "Active";
            else active = "Not Active";


            tmp = "" + keyID + " : " + name2 + " " + name1 + " - " + ID + " - " + workCompany + " - " + active;
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
     * Sends the user over to the GetInfo Activity (which in it, the user can input new
     * companies or workers).
     *
     * @param view
     */
    public void add(View view) {
        si = new Intent(this, GetInfo.class);
        si.putExtra("mode", mode);
        startActivity(si);
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
            cd = new ColorDrawable(getResources().getColor(R.color.worker));
            aBar.setBackgroundDrawable(cd);
            add.setBackgroundColor(getResources().getColor(R.color.worker));
            add.setText("Add Worker");
            add.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_person_add_alt_1_24, 0);
            compTv.setTypeface(null, Typeface.NORMAL);
            compTv.setTextColor(Color.BLACK);
            compTv.setTextSize(15);
            workTv.setTextSize(20);
            workTv.setTypeface(null, Typeface.BOLD);
            workTv.setTextColor(Color.BLACK);
            mode = 0;
            showADP = new ArrayAdapter<>(this,
                    R.layout.support_simple_spinner_dropdown_item, showWork);
            showSP.setAdapter(showADP);
            sortADP = new ArrayAdapter<>(this,
                    R.layout.support_simple_spinner_dropdown_item, sortWork);
            sortSP.setAdapter(sortADP);
            info.setText("Card Number, First Name, Last Name, ID, Company, Status");
            sortWorker(true, Worker.KEY_ID);

        } else {
            cd = new ColorDrawable(getResources().getColor(R.color.company));
            aBar.setBackgroundDrawable(cd);
            add.setBackgroundColor(getResources().getColor(R.color.company));
            add.setText("Add Food Company");
            add.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_add_business_24, 0);
            workTv.setTypeface(null, Typeface.NORMAL);
            workTv.setTextColor(Color.BLACK);
            workTv.setTextSize(15);
            compTv.setTextSize(20);
            compTv.setTypeface(null, Typeface.BOLD);
            workTv.setTextColor(Color.BLACK);
            showADP = new ArrayAdapter<>(this,
                    R.layout.support_simple_spinner_dropdown_item, showComp);
            showSP.setAdapter(showADP);
            sortADP = new ArrayAdapter<>(this,
                    R.layout.support_simple_spinner_dropdown_item, sortComp);
            sortSP.setAdapter(sortADP);
            mode = 1;
            info.setText("Company Number, Company Name, Tax Number, Main Phone, Secondary Phone, Status");
            sortCompany(true, Company.KEY_ID);

        }


    }

    /**
     * The listener for items selected in the ShowSP and SortSP Spinner Widget's.
     * Reads, filters and sorts the information from the desired table (based on the mode variable).
     * Each possible combination of the 2 Spinner Widget's choices and mode determines which table
     * will be read, and how will the information from it will be presented.
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView == showSP) {
            showPos = i;
        } else {
            sortPos = i;
        }

        if (sortPos == 0 && mode == 0) sortWorker(true, Worker.KEY_ID);
        else if (sortPos == 1 && mode == 0) sortWorker(false, Worker.KEY_ID);
        else if (sortPos == 2 && mode == 0) sortWorker(true, Worker.LAST_NAME);
        else if (sortPos == 3 && mode == 0) sortWorker(false, Worker.LAST_NAME);
        else if (sortPos == 4 && mode == 0) sortWorker(true, Worker.COMPANY_NAME);
        else if (sortPos == 5 && mode == 0) sortWorker(false, Worker.COMPANY_NAME);
        else if (sortPos == 0 && mode == 1) sortCompany(true, Company.KEY_ID);
        else if (sortPos == 1 && mode == 1) sortCompany(false, Company.KEY_ID);
        else if (sortPos == 2 && mode == 1) sortCompany(true, Company.NAME);
        else if (sortPos == 3 && mode == 1) sortCompany(false, Company.NAME);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * The listener for the ListView in which the information is presented.
     * When the user clicks a cell in the list, they will be sent over to the specific Activity,
     * in which they can edit the information they have clicked.
     *
     * @param adapterView
     * @param view
     * @param i
     * @param l
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        si = new Intent(this, specific.class);
        tmp = lv.getItemAtPosition(i).toString();
        int dotsIndex = 0;
        for (int j = 0; j < tmp.length(); j++) {
            if (tmp.charAt(j) == ':') {
                dotsIndex = j;
                break;
            }

        }
        keyID = tmp.substring(0, dotsIndex - 1);

        si.putExtra("keyID", Integer.parseInt(keyID));
        si.putExtra("mode", mode);
        startActivity(si);
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