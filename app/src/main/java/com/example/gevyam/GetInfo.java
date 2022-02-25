package com.example.gevyam;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author : Harel Navon harelnavon2710@gmail.com
 * @version : 1.1
 * @since : 21.2.2022
 * In this Activity, the user can add new information to the Worker and Company databases.
 */


public class GetInfo extends AppCompatActivity {
    TextView title;
    ImageView pic;
    ActionBar aBar;
    ColorDrawable cd;

    EditText et1, et2, et3, et4, et5;
    TextView tv1, tv2, tv3, tv4, tv5;

    SQLiteDatabase db;
    HelperDB hlp;
    ContentValues cv;

    Long keyID;
    String ID, name1, name2, company, phone;
    String FCname, FCtax, FCmain, FCsecondary;

    Intent gi, si;
    int mode;
    AlertDialog.Builder adb;

    /**
     * Sets up all of the Widgets in the Activity, according to the value for the mode variable, which
     * determines if the user has chosen to add a new company , or a new worker.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_info);
        title = findViewById(R.id.Title);
        title.setText(R.string.SetTitle);
        pic = findViewById(R.id.pic);

        aBar = getSupportActionBar();
        gi = getIntent();
        mode = gi.getIntExtra("mode", 0);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv4);
        tv3 = findViewById(R.id.tv5);
        tv4 = findViewById(R.id.tv6);
        tv5 = findViewById(R.id.tv7);

        hlp = new HelperDB(this);
        if (mode == 0) workerMode();
        else companyMode();
    }

    /**
     * The On Click Method for the submit button. Receives the information submitted by the user,
     * checks if its valid, and then updates the databases accordingly.
     *
     * @param view
     */
    public void submit(View view) {
        cv = new ContentValues();
        db = hlp.getWritableDatabase();

        if (mode == 0) {
            name1 = et1.getText().toString();
            name2 = et2.getText().toString();
            ID = et3.getText().toString();
            company = et4.getText().toString();
            phone = et5.getText().toString();

            if (check_worker()) {
                cv.put(Worker.FIRST_NAME, name1);
                cv.put(Worker.LAST_NAME, name2);
                cv.put(Worker.ID, ID);
                cv.put(Worker.COMPANY_NAME, company);
                cv.put(Worker.PHONE_NUMBER, phone);
                cv.put(Worker.ACTIVE, "0");

                keyID = db.insert(Worker.TABLE_WORKERS, null, cv);
                db.close();

                adb = new AlertDialog.Builder(this);
                adb.setTitle("Attention!");
                adb.setIcon(R.drawable.exclamation_mark_red);
                adb.setMessage("Your Worker Card Number is " + keyID);
                adb.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog ad = adb.create();
                ad.show();

                Toast.makeText(this, "Worker added successfully!", Toast.LENGTH_SHORT).show();
                clear();
            }
        } else {
            FCname = et1.getText().toString();
            FCtax = et2.getText().toString();
            FCmain = et3.getText().toString();
            FCsecondary = et4.getText().toString();

            if (check_FC()) {
                cv.put(Company.NAME, FCname);
                cv.put(Company.TAX, FCtax);
                cv.put(Company.MAIN, FCmain);
                cv.put(Company.SECONDARY, FCsecondary);
                cv.put(Company.ACTIVE, "0");

                db = hlp.getWritableDatabase();
                db.insert(Company.TABLE_COMPANIES, null, cv);
                db.close();
                Toast.makeText(this, "Food Company added successfully!", Toast.LENGTH_SHORT).show();
                clear();
            }
        }

    }

    /**
     * Checks is the info submitted by the user in the worker section is valid
     *
     * @return True or False accordingly.
     */
    public boolean check_worker() {
        if (name1.matches("") || name2.matches("")) {
            Toast.makeText(this, "Please enter a valid name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!valid_id(ID)) {
            Toast.makeText(this, "Please enter a valid ID number!", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (company.matches("")) {
            Toast.makeText(this, "Please enter a valid company name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.text.TextUtils.isDigitsOnly(phone) || (phone.length() != 10 && phone.length() != 9)) {
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
        if (FCname.matches("")) {
            Toast.makeText(this, "Please enter a valid Food Company name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.text.TextUtils.isDigitsOnly(FCtax)) {
            Toast.makeText(this, "Please enter a valid tax number!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.text.TextUtils.isDigitsOnly(FCmain) || (FCmain.length() != 10 && FCmain.length() != 9)) {
            Toast.makeText(this, "Please enter a valid phone number!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.text.TextUtils.isDigitsOnly(FCsecondary) || (FCsecondary.length() != 10 && FCsecondary.length() != 9)) {
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
     * Sets all of the Views according to the information needed for a new worker.
     */
    public void workerMode() {
        tv1.setText("First Name");
        tv2.setText("Last Name");
        tv3.setText("ID ");
        tv4.setText("Company");
        tv5.setText("Phone Number");
        cd = new ColorDrawable(getResources().getColor(R.color.worker));
        aBar.setBackgroundDrawable(cd);

        et1.setInputType(InputType.TYPE_CLASS_TEXT);
        et2.setInputType(InputType.TYPE_CLASS_TEXT);
        et3.setInputType(InputType.TYPE_CLASS_NUMBER);
        et4.setInputType(InputType.TYPE_CLASS_TEXT);
        et5.setInputType(InputType.TYPE_CLASS_PHONE);
        et5.setVisibility(View.VISIBLE);
        et5.setEnabled(true);
        clear();
        pic.setImageResource(R.drawable.worker);
    }

    /**
     * Sets all of the Views according to the information needed for a new company.
     */
    public void companyMode() {
        tv1.setText("Company Name");
        tv2.setText("Tax Number");
        tv3.setText("Main Phone");
        tv4.setText("Secondary Phone");
        tv5.setText("");
        cd = new ColorDrawable(getResources().getColor(R.color.company));
        aBar.setBackgroundDrawable(cd);

        et1.setInputType(InputType.TYPE_CLASS_TEXT);
        et2.setInputType(InputType.TYPE_CLASS_NUMBER);
        et3.setInputType(InputType.TYPE_CLASS_PHONE);
        et4.setInputType(InputType.TYPE_CLASS_PHONE);
        et5.setEnabled(false);
        et5.setVisibility(View.INVISIBLE);
        clear();
        pic.setImageResource(R.drawable.radio1);

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
     * On Click method of the back button. Takes the user back to the infoHub Activity.
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    /**
     * Clears all of the Text from the EditText's.
     */
    public void clear() {
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et5.setText("");
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