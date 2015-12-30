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

    /* GESTION DU MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem spinner_item) {
        // Handle action bar spinner_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = spinner_item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(spinner_item);
    }*/

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
