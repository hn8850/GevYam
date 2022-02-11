package com.example.gevyam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

public class infoHub extends AppCompatActivity {
    Intent si;
    Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_hub);
        sw = findViewById(R.id.sw2);

    }



    public void go(View view) {

        if (sw.isChecked()) {
            si = new Intent(this, GetInfo.class);
        } else {
            si = new Intent(this, ShowInfo.class);
        }
        startActivity(si);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.add("Help");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mainhome) {
            si = new Intent(this, MainActivity.class);
            startActivity(si);
        } else if (id == R.id.setting) {

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
        else if (id == R.id.creds) {
            si = new Intent(this, credits.class);
            startActivity(si);
        }
        else
        {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setMessage(R.string.stepByStep);
            adb.setTitle("Need help?");
            adb.setIcon(R.drawable.smiley);
            adb.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog ad = adb.create();
            ad.show();

        }

        return true;
    }

}