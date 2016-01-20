package com.umontpellier.theochambon.androimmo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.umontpellier.theochambon.androimmo.Adapters.ContenuListAdapter;
import com.umontpellier.theochambon.androimmo.Adapters.ContenuListe;
import com.umontpellier.theochambon.androimmo.Managers.BddOpenHelper;
import com.umontpellier.theochambon.androimmo.R;
import com.umontpellier.theochambon.androimmo.Util.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class ListeFicheActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    List<ContenuListe> noms;
    ContenuListAdapter recAdapter;
    RecyclerView listView;
    BddOpenHelper bdd = new BddOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_fiche);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        noms = bdd.getNoms();
        listView = (RecyclerView) findViewById(R.id.listeBiens);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recAdapter = new ContenuListAdapter(this.getLayoutInflater(), noms);
        listView.setAdapter(recAdapter);

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_liste_fiche, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return true;
    }


    //Recherche dynamique
    @Override
    public boolean onQueryTextChange(String query) {
        noms = bdd.getNoms();
        final List<ContenuListe> filteredModelList = filter(noms, query);
        recAdapter.animateTo(filteredModelList);
        listView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<ContenuListe> filter(List<ContenuListe> models, String query) {
        query = query.toLowerCase();
        final List<ContenuListe> filteredModelList = new ArrayList<>();
        for (ContenuListe model : models) {
            final String text = model.getVille().toLowerCase();
            final String text2 = model.getNom().toLowerCase();
            if (text.contains(query) || text2.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    public void select(View v){
        Intent i = new Intent(ListeFicheActivity.this, ConsultFicheActivity.class);
        TextView tv = (TextView) v.findViewById(R.id.tvID);
        String id = tv.getText().toString();
        i.putExtra("id", id);
        i.putExtra("dist", "false");
        startActivity(i);
    }


}
