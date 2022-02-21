package com.example.gevyam;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import java.util.ArrayList;


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


    String[] showWork;
    String[] sortWork;
    String[] showComp;
    String[] sortComp;
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

    String name1;
    String name2;
    String ID;
    String keyID;
    String workCompany;

    String FCname;
    String FCtax;
    String FCmain;
    String FCsecondary;

    Intent si;
    int mode;


    int col1, col2, col3, col4, col5, col6;
    String active;
    String tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_info);


        add = findViewById(R.id.add);
        add.setBackgroundColor(getResources().getColor(R.color.worker));
        add.setText("Add Worker");
        add.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_person_add_alt_1_24 ,0);


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
     * On Click method of the back button. Takes the user back to the infoHub Activity.
     *
     * @param view
     */
    public void back(View view) {
        finish();
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
            add.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_person_add_alt_1_24 ,0);
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
            add.setCompoundDrawablesWithIntrinsicBounds(0, 0,R.drawable.ic_baseline_add_business_24 ,0);
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


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }

    public void add(View view) {
        si = new Intent(this,GetInfo.class);
        si.putExtra("mode",mode);
        startActivity(si);
    }
}