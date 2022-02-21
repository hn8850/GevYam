package com.example.gevyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Intent si;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void infogo(View view) {
        si = new Intent(this, ShowInfo.class);
        startActivity(si);
    }
    public void orderGo(View view) {
        si = new Intent(this,GetOrder.class);
        startActivity(si);
    }

    public void prevOrder(View view) {
        si = new Intent(this,showOrder.class);
        startActivity(si);
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



}