package com.example.gevyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * @author : Harel Navon harelnavon2710@gmail.com
 * @version : 1.1
 * @since : 21.2.2022
 * The main Activity of the app. Serves as the hub for the user to access all features in the app.
 */

public class MainActivity extends AppCompatActivity {
    Intent si;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Sends the user over to the ShowInfo Activity.
     *
     * @param view
     */
    public void infogo(View view) {
        si = new Intent(this, ShowInfo.class);
        startActivity(si);
    }

    /**
     * Sends the user over to the GetOrder Activity.
     *
     * @param view
     */
    public void orderGo(View view) {
        si = new Intent(this, GetOrder.class);
        startActivity(si);
    }

    /**
     * Sends the user over to the showOrder Activity.
     *
     * @param view
     */
    public void prevOrder(View view) {
        si = new Intent(this, showOrder.class);
        startActivity(si);
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