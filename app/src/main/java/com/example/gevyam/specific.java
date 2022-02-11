package com.example.gevyam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class specific extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    Switch sw;
    TextView title;
    ImageView pic;

    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    EditText et5;

    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tVactive, tVNotActive;

    SQLiteDatabase db;
    HelperDB hlp;
    ContentValues cv;
    Cursor crsr;


    Intent si, gi;
    int mode;
    int pos;
    int counter;
    Button edit;
    Button clear;

    int col1,col2, col3, col4, col5, col6, col7;
    String[] oldData;

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
        clear = findViewById(R.id.clear);

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


        gi = getIntent();
        pos = gi.getIntExtra("pos", -1);
        mode = gi.getIntExtra("mode", -1);
        hlp = new HelperDB(this);
        if (mode == 0) workerMode();
        else if (mode == 1) companyMode();
        else {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setMessage("An error has occurred.");
            adb.setTitle("Oh No!");
            adb.setIcon(R.drawable.frowny);
            adb.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            AlertDialog ad = adb.create();
            ad.show();
        }


    }

    /**
     * Sets all of the Views according to the information needed for a new worker.
     */
    public void workerMode() {
        tv1.setText("First Name");
        tv2.setText("Last Name");
        tv3.setText("ID ");
        tv4.setText("Company");
        tv5.setText("Phone Number");
        et5.setVisibility(View.VISIBLE);



        db = hlp.getWritableDatabase();
        crsr = db.query(Worker.TABLE_WORKERS, null, null, null, null, null, null);
        col1 = crsr.getColumnIndex(Worker.KEY_ID);
        col2 = crsr.getColumnIndex(Worker.FIRST_NAME);
        col3 = crsr.getColumnIndex(Worker.LAST_NAME);
        col4 = crsr.getColumnIndex(Worker.ID);
        col5 = crsr.getColumnIndex(Worker.COMPANY_NAME);
        col6 = crsr.getColumnIndex(Worker.PHONE_NUMBER);
        col7 = crsr.getColumnIndex(Worker.ACTIVE);


        crsr.moveToPosition(pos);

        title.setText("Here is some information about : " + crsr.getString(col2) + " " + crsr.getString(col3));
        pic.setImageResource(R.drawable.worker);

        et1.setText(crsr.getString(col2));
        et2.setText(crsr.getString(col3));
        et3.setText(crsr.getString(col4));
        et4.setText(crsr.getString(col5));
        et5.setText(crsr.getString(col6));
        if (crsr.getInt(col7) == 0) {
            sw.setChecked(false);
            tVactive.setTextColor(Color.GREEN);
            tVactive.setTextSize(20);
            tVactive.setTypeface(null, Typeface.BOLD);
        } else {
            sw.setChecked(true);
            tVNotActive.setTextColor(Color.RED);
            tVNotActive.setTextSize(20);
            tVNotActive.setTypeface(null, Typeface.BOLD);

        }


        crsr.close();
        db.close();


    }

    /**
     * Sets all of the Views according to the information needed for a new company.
     */
    public void companyMode() {
        tv1.setText("Company Name");
        tv2.setText("Company ID");
        tv3.setText("Main Phone");
        tv4.setText("Secondary Phone");
        tv5.setText("");
        et5.setVisibility(View.INVISIBLE);
        pic.setImageResource(R.drawable.radio1);


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
     * Disables all of the EditText's on screen, until the edit button if pressed
     */
    public void edit_change(boolean change) {
        et1.setEnabled(change);
        et2.setEnabled(change);
        et3.setEnabled(change);
        et4.setEnabled(change);
        et5.setEnabled(change);
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
     * On click method of the Clear button, uses the clear method.
     *
     * @param view
     */
    public void clrButton(View view) {
        clear();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (!b){
            tVactive.setTextColor(Color.GREEN);
            tVactive.setTextSize(20);
            tVactive.setTypeface(null, Typeface.BOLD);

            tVNotActive.setTextColor(Color.BLACK);
            tVNotActive.setTextSize(15);
            tVNotActive.setTypeface(null, Typeface.NORMAL);

        }
        else{
            tVNotActive.setTextColor(Color.RED);
            tVNotActive.setTextSize(20);
            tVNotActive.setTypeface(null, Typeface.BOLD);

            tVactive.setTextColor(Color.BLACK);
            tVactive.setTextSize(15);
            tVactive.setTypeface(null, Typeface.NORMAL);
        }

    }

    public void edit(View view) {
        counter++;
        if (counter % 2 == 1) {
            clear.setTextColor(Color.BLACK);
            edit.setBackgroundColor(Color.rgb(98, 0, 238));
            edit.setText("Submit");
            edit_change(true);
            sw.setClickable(true);
            if (mode == 0) {
                oldData = new String[6];
                oldData[0] = et1.getText().toString();
                oldData[1] = et2.getText().toString();
                oldData[2] = et3.getText().toString();
                oldData[3] = et4.getText().toString();
                oldData[4] = et5.getText().toString();
                if (!sw.isChecked()) oldData[5] = "0";
                else oldData[5] = "1";
                et1.setInputType(InputType.TYPE_CLASS_TEXT);
                et2.setInputType(InputType.TYPE_CLASS_TEXT);
                et3.setInputType(InputType.TYPE_CLASS_NUMBER);
                et4.setInputType(InputType.TYPE_CLASS_TEXT);
                et5.setInputType(InputType.TYPE_CLASS_PHONE);
                et5.setVisibility(View.VISIBLE);
                et5.setEnabled(true);


            } else {
                oldData = new String[5];
                oldData[0] = et1.getText().toString();
                oldData[1] = et2.getText().toString();
                oldData[2] = et3.getText().toString();
                oldData[3] = et4.getText().toString();
                if (!sw.isChecked()) oldData[4] = "0";
                else oldData[4] = "1";
            }

        } else {
            clear.setTextColor(Color.WHITE);
            edit.setBackgroundColor(Color.rgb(179, 173, 173));
            edit.setText("Edit");
            edit_change(false);
            cv = new ContentValues();

            if (mode == 0) {

                cv.put(Worker.FIRST_NAME, et1.getText().toString());
                cv.put(Worker.LAST_NAME, et2.getText().toString());
                cv.put(Worker.ID, et3.getText().toString());
                cv.put(Worker.COMPANY_NAME, et4.getText().toString());
                cv.put(Worker.PHONE_NUMBER, et5.getText().toString());
                if (!sw.isChecked()) cv.put(Worker.ACTIVE, 0);
                else cv.put(Worker.ACTIVE, 1);
                db = hlp.getWritableDatabase();


                db.update(Worker.TABLE_WORKERS, cv, Worker.FIRST_NAME + "=?", new String[]{oldData[0]});
                db.update(Worker.TABLE_WORKERS, cv, Worker.LAST_NAME + "=?", new String[]{oldData[1]});
                db.update(Worker.TABLE_WORKERS, cv, Worker.ID + "=?", new String[]{oldData[2]});
                db.update(Worker.TABLE_WORKERS, cv, Worker.COMPANY_NAME + "=?", new String[]{oldData[3]});
                db.update(Worker.TABLE_WORKERS, cv, Worker.PHONE_NUMBER + "=?", new String[]{oldData[4]});
                db.update(Worker.TABLE_WORKERS, cv, Worker.ACTIVE + "=?", new String[]{oldData[5]});

            }
            else{

            }
            db.close();


        }

    }
}
