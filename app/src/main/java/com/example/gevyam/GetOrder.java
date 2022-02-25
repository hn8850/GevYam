package com.example.gevyam;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * @author : Harel Navon harelnavon2710@gmail.com
 * @version : 1.1
 * @since : 21.2.2022
 * In this Activity, the user begins to fill out their order details, by submitting what they
 * would like to eat.
 */

public class GetOrder extends AppCompatActivity {

    EditText et1, et2, et3, et4, et5;
    TextView tv1, tv2, tv3, tv4, tv5;

    String appetizer, mainDish, sideDish, dessert, drink;
    ArrayList<String> food;

    Intent si;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_order);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);
        clear();

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv4);
        tv3 = findViewById(R.id.tv5);
        tv4 = findViewById(R.id.tv6);
        tv5 = findViewById(R.id.tv7);

    }


    /**
     * The On Click Method for the submit button. Receives the information submitted by the user,
     * checks if its valid, and then updates the databases accordingly.
     *
     * @param view
     */
    public void next(View view) {

        appetizer = et1.getText().toString();
        mainDish = et2.getText().toString();
        sideDish = et3.getText().toString();
        dessert = et4.getText().toString();
        drink = et5.getText().toString();

        food = new ArrayList<>();
        food.add(appetizer);
        food.add(mainDish);
        food.add(sideDish);
        food.add(dessert);
        food.add(drink);
        si = new Intent(this, FinishOrder.class);
        si.putStringArrayListExtra("food", food);
        startActivity(si);


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