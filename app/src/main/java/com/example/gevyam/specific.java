package com.example.gevyam;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author : Harel Navon harelnavon2710@gmail.com
 * @version : 1.1
 * @since : 21.2.2022
 * In this Activity, the user can view and edit information about the food company/worker they have chosen.
 */

public class specific extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    Switch sw;
    TextView title;
    ImageView pic;
    ActionBar aBar;
    ColorDrawable cd;

    EditText et1, et2, et3, et4, et5;
    TextView tv1, tv2, tv3, tv4, tv5;
    TextView tVactive, tVNotActive;

    SQLiteDatabase db;
    HelperDB hlp;
    ContentValues cv;
    Cursor crsr;
    int keyID;

    int mode;
    int counter;
    Button edit;
    Button cancel;
    TextView clear;
    SpannableString content;

    int col1, col2, col3, col4, col5, col6, col7;
    String[] oldData;
    boolean cont;
    Intent si;
    Intent gi;

    /**
     * Sets up all of the Widgets in the Activity, according to the value for the mode variable, which
     * determines if the user has chosen to view a food company , or a worker.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific);
        counter = 0;
        sw = findViewById(R.id.sw4);
        sw.setOnCheckedChangeListener(this);
        sw.setClickable(false);
        title = findViewById(R.id.Title);
        pic = findViewById(R.id.pic);
        edit = findViewById(R.id.edit2);
        clear = findViewById(R.id.cleartext);
        content = new SpannableString("Clear all");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        cancel = findViewById(R.id.cancel);
        cancel.setVisibility(View.INVISIBLE);
        cancel.setClickable(false);


        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        edit_change(false);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv4);
        tv3 = findViewById(R.id.tv5);
        tv4 = findViewById(R.id.tv6);
        tv5 = findViewById(R.id.tv7);
        tVactive = findViewById(R.id.tv11);
        tVNotActive = findViewById(R.id.tv12);

        tVactive.setTextSize(15);
        tVNotActive.setTextSize(15);

        aBar = getSupportActionBar();

        gi = getIntent();
        keyID = gi.getIntExtra("keyID", 0);
        mode = gi.getIntExtra("mode", -1);
        hlp = new HelperDB(this);
        if (mode == 0) workerMode();
        else companyMode();
    }

    /**
     * Sets all of the Views according to the information of the worker that was pressed.
     */
    public void workerMode() {
        tv1.setText("First Name");
        tv2.setText("Last Name");
        tv3.setText("ID ");
        tv4.setText("Company");
        tv5.setText("Phone Number");
        et5.setVisibility(View.VISIBLE);
        et1.setInputType(InputType.TYPE_CLASS_TEXT);
        et2.setInputType(InputType.TYPE_CLASS_TEXT);
        et3.setInputType(InputType.TYPE_CLASS_NUMBER);
        et4.setInputType(InputType.TYPE_CLASS_TEXT);
        et5.setInputType(InputType.TYPE_CLASS_PHONE);
        et5.setVisibility(View.VISIBLE);

        cd = new ColorDrawable(getResources().getColor(R.color.worker));
        aBar.setBackgroundDrawable(cd);

        db = hlp.getWritableDatabase();
        crsr = db.query(Worker.TABLE_WORKERS, null, null, null, null, null, null);
        col1 = crsr.getColumnIndex(Worker.KEY_ID);
        col2 = crsr.getColumnIndex(Worker.FIRST_NAME);
        col3 = crsr.getColumnIndex(Worker.LAST_NAME);
        col4 = crsr.getColumnIndex(Worker.ID);
        col5 = crsr.getColumnIndex(Worker.COMPANY_NAME);
        col6 = crsr.getColumnIndex(Worker.PHONE_NUMBER);
        col7 = crsr.getColumnIndex(Worker.ACTIVE);

        crsr.moveToPosition(keyID - 1);

        title.setText("Here is some information about : " + crsr.getString(col2) + " " + crsr.getString(col3));
        pic.setImageResource(R.drawable.worker);

        oldData = new String[]{crsr.getString(col2), crsr.getString(col3), crsr.getString(col4), crsr.getString(col5), crsr.getString(col6), ""};
        et1.setText(oldData[0]);
        et2.setText(oldData[1]);
        et3.setText(oldData[2]);
        et4.setText(oldData[3]);
        et5.setText(oldData[4]);
        if (crsr.getInt(col7) == 0) {
            sw.setChecked(false);
            tVactive.setTextColor(Color.GREEN);
            tVactive.setTextSize(20);
            tVactive.setTypeface(null, Typeface.BOLD);
            oldData[5] = "0";
        } else {
            sw.setChecked(true);
            tVNotActive.setTextColor(Color.RED);
            tVNotActive.setTextSize(20);
            tVNotActive.setTypeface(null, Typeface.BOLD);
            oldData[5] = "1";

        }

        crsr.close();
        db.close();

    }

    /**
     * Sets all of the Views according to the information of the food company that was pressed.
     */
    public void companyMode() {
        tv1.setText("Company Name");
        tv2.setText("Company ID");
        tv3.setText("Main Phone");
        tv4.setText("Secondary Phone");
        tv5.setText("");
        et5.setVisibility(View.INVISIBLE);
        pic.setImageResource(R.drawable.radio1);
        et1.setInputType(InputType.TYPE_CLASS_TEXT);
        et2.setInputType(InputType.TYPE_CLASS_NUMBER);
        et3.setInputType(InputType.TYPE_CLASS_PHONE);
        et4.setInputType(InputType.TYPE_CLASS_PHONE);
        et5.setEnabled(false);
        et5.setVisibility(View.INVISIBLE);

        cd = new ColorDrawable(getResources().getColor(R.color.company));
        aBar.setBackgroundDrawable(cd);

        db = hlp.getWritableDatabase();
        crsr = db.query(Company.TABLE_COMPANIES, null, null, null, null, null, null);
        col1 = crsr.getColumnIndex(Company.KEY_ID);
        col2 = crsr.getColumnIndex(Company.NAME);
        col3 = crsr.getColumnIndex(Company.TAX);
        col4 = crsr.getColumnIndex(Company.MAIN);
        col5 = crsr.getColumnIndex(Company.SECONDARY);
        col7 = crsr.getColumnIndex(Company.ACTIVE);


        crsr.moveToPosition(keyID - 1);

        title.setText("Here is some information about : " + crsr.getString(col2));


        oldData = new String[]{crsr.getString(col2), crsr.getString(col3), crsr.getString(col4), crsr.getString(col5), ""};
        et1.setText(oldData[0]);
        et2.setText(oldData[1]);
        et3.setText(oldData[2]);
        et4.setText(oldData[3]);
        if (crsr.getInt(col7) == 0) {
            sw.setChecked(false);
            tVactive.setTextColor(Color.GREEN);
            tVactive.setTextSize(20);
            tVactive.setTypeface(null, Typeface.BOLD);
            oldData[4] = "0";
        } else {
            sw.setChecked(true);
            tVNotActive.setTextColor(Color.RED);
            tVNotActive.setTextSize(20);
            tVNotActive.setTypeface(null, Typeface.BOLD);
            oldData[4] = "1";

        }

        crsr.close();
        db.close();
    }

    /**
     * Enables editing on all of the visible EditText's. When pressed once, this button becomes
     * a submit button, which when pressed, returns back to an edit button, disables editing on
     * all of the visible EditText's , and updates database according to the information the
     * user has edited.
     *
     * @param view
     */
    public void edit(View view) {
        counter++;
        if (counter % 2 == 1) {
            clear.setTextColor(Color.BLACK);
            edit.setText("Submit");
            edit_change(true);
            sw.setClickable(true);
            clear.setText(content);
            cancel.setText("Cancel");
            cancel.setClickable(true);
            cancel.setVisibility(View.VISIBLE);
            cancel.setBackgroundColor(Color.rgb(98, 0, 238));


        } else {
            cont = false;
            if (mode == 0) {
                if (check_worker()) cont = true;
            } else {
                if (check_FC()) cont = true;
            }

            if (cont) {
                clear.setTextColor(Color.WHITE);
                clear.setText("");
                edit.setText("Edit");
                sw.setClickable(false);
                edit_change(false);
                cancel.setBackgroundColor(Color.rgb(179, 173, 173));
                cancel.setClickable(false);

                cv = new ContentValues();
                db = hlp.getWritableDatabase();

                if (mode == 0) {
                    cv.put(Worker.FIRST_NAME, et1.getText().toString());
                    cv.put(Worker.LAST_NAME, et2.getText().toString());
                    cv.put(Worker.ID, et3.getText().toString());
                    cv.put(Worker.COMPANY_NAME, et4.getText().toString());
                    cv.put(Worker.PHONE_NUMBER, et5.getText().toString());
                    if (!sw.isChecked()) cv.put(Worker.ACTIVE, 0);
                    else cv.put(Worker.ACTIVE, 1);
                    db.update(Worker.TABLE_WORKERS, cv, Worker.KEY_ID + "=?", new String[]{String.valueOf(keyID)});
                } else {
                    cv.put(Company.NAME, et1.getText().toString());
                    cv.put(Company.TAX, et2.getText().toString());
                    cv.put(Company.MAIN, et3.getText().toString());
                    cv.put(Company.SECONDARY, et4.getText().toString());
                    if (!sw.isChecked()) cv.put(Worker.ACTIVE, 0);
                    else cv.put(Worker.ACTIVE, 1);
                    db.update(Company.TABLE_COMPANIES, cv, Company.KEY_ID + "=?", new String[]{String.valueOf(keyID)});
                }
                db.close();
                finish();
            } else {
                counter--;
            }

        }
    }


    /**
     * Checks is the info submitted by the user in the worker section is valid
     *
     * @return True or False accordingly.
     */
    public boolean check_worker() {
        if (et1.getText().toString().matches("") || et2.getText().toString().matches("")) {
            Toast.makeText(this, "Please enter a valid name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!valid_id(et3.getText().toString())) {
            Toast.makeText(this, "Please enter a valid ID number!", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (et4.getText().toString().matches("")) {
            Toast.makeText(this, "Please enter a valid company name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.text.TextUtils.isDigitsOnly(et5.getText().toString()) || (et5.getText().toString().length() != 10 && et5.getText().toString().length() != 9)) {
            Toast.makeText(this, "Please enter a valid phone number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Checks is the info submitted by the user in the company section is valid
     *
     * @return True or False accordingly.
     */
    public boolean check_FC() {
        if (et1.getText().toString().matches("")) {
            Toast.makeText(this, "Please enter a valid Food Company name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.text.TextUtils.isDigitsOnly(et2.getText().toString())) {
            Toast.makeText(this, "Please enter a valid tax number!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.text.TextUtils.isDigitsOnly(et3.getText().toString()) || (et3.getText().toString().length() != 10 && et3.getText().toString().length() != 9)) {
            Toast.makeText(this, "Please enter a valid phone number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.text.TextUtils.isDigitsOnly(et4.getText().toString()) || (et4.getText().toString().length() != 10 && et4.getText().toString().length() != 9)) {
            Toast.makeText(this, "Please enter a valid phone number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Checks if the given ID is valid according to Israeli standards.
     *
     * @param str The string of the given ID.
     * @return True or false according to the validity of the ID.
     */
    public static boolean valid_id(String str) {
        if (str.length() > 9) return false;
        int x;
        int sum = 0;
        int len = 9 - str.length();
        for (int i = 0; i < len; i++) {
            str = "0" + str;
        }
        for (int i = 0; i < str.length(); i++) {
            try {
                x = Integer.parseInt(str.substring(i, i + 1));
            } catch (Exception e) {
                return false;
            }
            if (i % 2 == 1) x = x * 2;
            if (x > 9) x = x % 10 + x / 10;
            sum += x;
        }
        return sum % 10 == 0;
    }


    /**
     * Only available when the SUBMIT Button is visible. Disables all of the visible EditText's.
     * Reverts the Text on the EditText's back to the what it was before pressing the EDIT Button.
     *
     * @param view
     */
    public void cancel(View view) {
        edit_change(false);
        et1.setText(oldData[0]);
        et2.setText(oldData[1]);
        et3.setText(oldData[2]);
        et4.setText(oldData[3]);
        if (mode == 0) {
            et5.setText(oldData[4]);
            sw.setChecked(!oldData[5].equals("0"));
        } else {
            sw.setChecked(!oldData[4].equals("0"));
        }
        sw.setClickable(false);
        cancel.setBackgroundColor(Color.rgb(179, 173, 173));
        cancel.setClickable(false);
        clear.setTextColor(Color.WHITE);
        clear.setText("");
        edit.setText("Edit");
        counter = 0;
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
            tVactive.setTextColor(Color.GREEN);
            tVactive.setTextSize(20);
            tVactive.setTypeface(null, Typeface.BOLD);

            tVNotActive.setTextColor(Color.BLACK);
            tVNotActive.setTextSize(15);
            tVNotActive.setTypeface(null, Typeface.NORMAL);

        } else {
            tVNotActive.setTextColor(Color.RED);
            tVNotActive.setTextSize(20);
            tVNotActive.setTypeface(null, Typeface.BOLD);

            tVactive.setTextColor(Color.BLACK);
            tVactive.setTextSize(15);
            tVactive.setTypeface(null, Typeface.NORMAL);
        }
    }

    /**
     * Clears all of the Text from the EditText's.
     */
    public void clear() {
        if (et1.isEnabled()) {
            et1.setText("");
            et2.setText("");
            et3.setText("");
            et4.setText("");
            et5.setText("");
        }
    }

    /**
     * Disables/Enables all of the EditText's on screen.
     */
    public void edit_change(boolean change) {
        et1.setEnabled(change);
        et2.setEnabled(change);
        et3.setEnabled(change);
        et4.setEnabled(change);
        et5.setEnabled(change);
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
     * On click method of the Clear button, uses the clear method.
     *
     * @param view
     */
    public void clrButton(View view) {
        clear();
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
