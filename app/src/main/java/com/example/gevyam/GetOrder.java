package com.example.gevyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GetOrder extends AppCompatActivity {
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

    SQLiteDatabase db;
    HelperDB hlp;
    ContentValues cv;

    String appetizer;
    String mainDish;
    String sideDish;
    String dessert;
    String drink;

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

        //check_meal()
        food = new ArrayList<>();
        food.add(appetizer);
        food.add(mainDish);
        food.add(sideDish);
        food.add(dessert);
        food.add(drink);
        si = new Intent(this,FinishOrder.class);
        si.putStringArrayListExtra("food",food);
        startActivity(si);// finish next activity, then return and finish here;

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
}