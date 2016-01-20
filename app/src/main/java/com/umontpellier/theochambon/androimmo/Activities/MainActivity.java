package com.umontpellier.theochambon.androimmo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.umontpellier.theochambon.androimmo.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.home);

        
    }

    public void launchNewFiche(View v){
        Intent i = new Intent(MainActivity.this, NewFicheActivity.class);
        startActivity(i);
    }

    public void launchListeFiche(View v){
        Intent i = new Intent(MainActivity.this, ListeFicheActivity.class);
        startActivity(i);
    }

    public void launchListeFicheDistant(View v){
        Intent i = new Intent(MainActivity.this, CritereActivity.class);
        startActivity(i);
    }


}
