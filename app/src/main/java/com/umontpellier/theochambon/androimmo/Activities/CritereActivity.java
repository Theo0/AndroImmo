package com.umontpellier.theochambon.androimmo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.umontpellier.theochambon.androimmo.R;


//Ecran représentant le critère de séléction des fiches distantes
public class CritereActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_critere);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar spinner_item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Récupération des critères de recherche
    public void recherche(View v) {
        EditText prixMin = (EditText) findViewById(R.id.PrixEditMin);
        if (prixMin.getText().toString().matches("")) {
            prixMin.setText("0");
        }
        EditText prixMax = (EditText) findViewById(R.id.PrixEditMax);
        if (prixMax.getText().toString().matches("")) {
            prixMax.setText("0");
        }

        EditText tailleMin = (EditText) findViewById(R.id.TailleEditMin);
        if (tailleMin.getText().toString().matches("")) {
            tailleMin.setText("0");
        }
        EditText tailleMax = (EditText) findViewById(R.id.TailleEditMax);
        if (tailleMax.getText().toString().matches("")) {
            tailleMax.setText("0");
        }

        EditText ville = (EditText) findViewById(R.id.VilleEdit);
        if (ville.getText().toString().matches("")) {
            ville.setText("0");
        }

        EditText piecesMin = (EditText) findViewById(R.id.PiecesEditMin);
        if (piecesMin.getText().toString().matches("")) {
            piecesMin.setText("0");
        }
        EditText piecesMax = (EditText) findViewById(R.id.PiecesEditMax);
        if (piecesMax.getText().toString().matches("")) {
            piecesMax.setText("0");
        }

        Intent i = new Intent(CritereActivity.this, ListeFicheDistantesActivity.class);
        i.putExtra("prixMin", prixMin.getText().toString());
        i.putExtra("prixMax", prixMax.getText().toString());
        i.putExtra("ville", ville.getText().toString());
        i.putExtra("tailleMin", tailleMin.getText().toString());
        i.putExtra("tailleMax", tailleMax.getText().toString());
        i.putExtra("piecesMin", piecesMin.getText().toString());
        i.putExtra("piecesMax", piecesMax.getText().toString());
        startActivity(i);

    }

}
