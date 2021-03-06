package com.umontpellier.theochambon.androimmo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.umontpellier.theochambon.androimmo.Managers.BddOpenHelper;
import com.umontpellier.theochambon.androimmo.R;

import java.util.HashMap;

public class EditFicheActivity extends AppCompatActivity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_fiche);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        remplirFiche();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }


    //Récupération du contenu de la fiche et affichage sur l'UI
    public void remplirFiche(){


        BddOpenHelper bdd = new BddOpenHelper(this);

        HashMap<String, String> contenu = bdd.getContenuFiche(id);


        if(!contenu.isEmpty()){

            EditText tv2 = (EditText) findViewById(R.id.nomEdit);
            tv2.setText(contenu.get("NOM"));


            TextView tv = (TextView) findViewById(R.id.surfaceEdit);
            tv.setText(contenu.get("SURFACE"));

            tv = (TextView) findViewById(R.id.nbpiecesEdit);
            tv.setText(contenu.get("NBPIECES"));

            tv = (TextView) findViewById(R.id.nbchambreEdit);
            tv.setText(contenu.get("NBCHAMBRES"));

            tv = (TextView) findViewById(R.id.nbSDBEdit);
            tv.setText(contenu.get("NBSDB"));

            tv = (TextView) findViewById(R.id.nbWCEdit);
            tv.setText(contenu.get("NBWC"));

            tv = (TextView) findViewById(R.id.nbBalconEdit);
            tv.setText(contenu.get("NBBALCON"));

            tv = (TextView) findViewById(R.id.etagesEdit);
            tv.setText(contenu.get("ETAGES"));

            tv = (TextView) findViewById(R.id.adrEdit);
            tv.setText(contenu.get("ADR"));

            tv = (TextView) findViewById(R.id.villeEdit);
            tv.setText(contenu.get("VILLE"));

            tv = (TextView) findViewById(R.id.expoEdit);
            tv.setText(contenu.get("EXPO"));

            tv = (TextView) findViewById(R.id.taxeEdit);
            tv.setText(contenu.get("TAXE"));

            tv = (TextView) findViewById(R.id.coproEdit);
            tv.setText(contenu.get("COPRO"));

            tv = (TextView) findViewById(R.id.prixEdit);
            tv.setText(contenu.get("PRIX"));

            tv = (TextView) findViewById(R.id.notesEdit);
            tv.setText(contenu.get("NOTES"));
        }

    }


    //Mise à jour des données de la fiche dans la BDD
    public void sauveFiche(View v){
        EditText nom = (EditText)findViewById(R.id.nomEdit);
        if(nom.getText().toString().matches("")){
            Toast toast = Toast.makeText(getApplicationContext(), "Veuillez renseigner au moins le nom de la fiche !", Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            EditText surface = (EditText)findViewById(R.id.surfaceEdit);
            if(surface.getText().toString().matches("")){surface.setText("0");}
            EditText nbPieces = (EditText)findViewById(R.id.nbpiecesEdit);
            if(nbPieces.getText().toString().matches("")){nbPieces.setText("0");}
            EditText nbChambres = (EditText)findViewById(R.id.nbchambreEdit);
            if(nbChambres.getText().toString().matches("")){nbChambres.setText("0");}
            EditText nbsdb = (EditText)findViewById(R.id.nbSDBEdit);
            if(nbsdb.getText().toString().matches("")){nbsdb.setText("0");}
            EditText nbWC =(EditText)findViewById(R.id.nbWCEdit);
            if(nbWC.getText().toString().matches("")){nbWC.setText("0");}
            EditText nbbalcon = (EditText)findViewById(R.id.nbBalconEdit);
            if(nbbalcon.getText().toString().matches("")){nbbalcon.setText("0");}
            EditText etage = (EditText)findViewById(R.id.etagesEdit);
            if(etage.getText().toString().matches("")){etage.setText("0");}
            EditText adr = (EditText)findViewById(R.id.adrEdit);
            EditText ville = (EditText)findViewById(R.id.villeEdit);
            EditText expo = (EditText)findViewById(R.id.expoEdit);
            EditText taxe = (EditText)findViewById(R.id.taxeEdit);
            if(taxe.getText().toString().matches("")){taxe.setText("0");}
            EditText copro = (EditText)findViewById(R.id.coproEdit);
            if (copro.getText().toString().matches("")) {
                copro.setText("0");
            }
            EditText prix = (EditText) findViewById(R.id.prixEdit);
            if(copro.getText().toString().matches("")){copro.setText("0");}
            EditText notes = (EditText)findViewById(R.id.notesEdit);


            BddOpenHelper bdd = new BddOpenHelper(this);
            long er = bdd.update(nom.getText().toString(), Integer.parseInt(surface.getText().toString()), Integer.parseInt(nbPieces.getText().toString()), Integer.parseInt(nbChambres.getText().toString()), Integer.parseInt(nbsdb.getText().toString()), Integer.parseInt(nbWC.getText().toString()), Integer.parseInt(nbbalcon.getText().toString()), Integer.parseInt(etage.getText().toString()), adr.getText().toString(), ville.getText().toString(), expo.getText().toString(), Integer.parseInt(taxe.getText().toString()), Integer.parseInt(copro.getText().toString()), Integer.parseInt(prix.getText().toString()), notes.getText().toString(), id);

            if(er == -1){
                Toast toast = Toast.makeText(getApplicationContext(), "Erreur lors de la MAJ de la fiche (erreur BDD)", Toast.LENGTH_LONG);
                toast.show();
            }

            Intent i = new Intent(EditFicheActivity.this, MainActivity.class );
            startActivity(i);
        }
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
}
