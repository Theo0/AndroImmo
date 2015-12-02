package com.umontpellier.theochambon.androimmo.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import com.umontpellier.theochambon.androimmo.Constants.Constants;
import com.umontpellier.theochambon.androimmo.HttpServer.ConnectServer;
import com.umontpellier.theochambon.androimmo.R;
import com.umontpellier.theochambon.androimmo.Util.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListeFicheDistantesActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    ContenuListAdapter recAdapter;
    RecyclerView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    List<ContenuListe> noms;
    List<ContenuListe> nomsCopie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_fiche_distantes);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nomsCopie = new ArrayList<>();

        //ON CHECK LA CONNEXION INTERNET
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(R.string.noConnectTitle);
            alertDialogBuilder.setMessage(R.string.noConnectBody).setCancelable(false).setPositiveButton("Retour au Menu", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    ListeFicheDistantesActivity.this.finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }


        new RetrieveData().execute();
        listView = (RecyclerView) findViewById(R.id.listeBiensDistants);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new RetrieveData().execute();
            }
        });


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

    @Override
    public boolean onQueryTextChange(String query) {
        copieNoms(this.nomsCopie, noms);//On recharge l'intégralité des noms
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

    public void select(View v) {
        Intent i = new Intent(ListeFicheDistantesActivity.this, ConsultFicheActivity.class);
        TextView tv = (TextView) v.findViewById(R.id.tvID);
        String id = tv.getText().toString();
        i.putExtra("id", id);
        i.putExtra("dist", "true");
        startActivity(i);
    }

    public void adaptRecyclerView(List<ContenuListe> noms) {
        this.noms = noms; //On le garde en variable globale car on en a besoin pour la fonction de recherche
        copieNoms(noms, nomsCopie);
        recAdapter = new ContenuListAdapter(this.getLayoutInflater(), this.noms);
        listView.setAdapter(recAdapter);
    }

    public void copieNoms(List<ContenuListe> listeACopier, List<ContenuListe> listeRec) {
        if (listeRec != null && !listeRec.isEmpty()) listeRec.clear();
        for (ContenuListe l : listeACopier) {
            listeRec.add(l);
        }
    }


    class RetrieveData extends AsyncTask<Void, Void, List<ContenuListe>> {

        ProgressDialog progDailog;

        @Override
        protected void onPreExecute() {
            progDailog = new ProgressDialog(ListeFicheDistantesActivity.this);
            progDailog.setMessage("Chargement des fiches...");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected List<ContenuListe> doInBackground(Void... params) {
            ConnectServer conn = new ConnectServer();
            conn.setUrl(Constants.serverURL + "listeFiches.php");
            JSONArray json = conn.getResponseFromURL();
            JSONObject js;
            List<ContenuListe> listeNoms = new ArrayList<>();
            if (json != null) {
                try {
                    for (int i = 0; i < json.length(); i++) {
                        js = json.getJSONObject(i);
                        if (js.getString("VILLE") == "null") js.put("VILLE", "");
                        if (js.getString("SURFACE") == "null") js.put("SURFACE", "");
                        if (js.getString("NBPIECES") == "null") js.put("NBPIECES", "");
                        listeNoms.add(new ContenuListe(js.getString("NOM"), js.getString("VILLE"), js.getString("ID"), js.getString("SURFACE"), js.getString("NBPIECES")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            conn.close();
            return listeNoms;
        }

        @Override
        protected void onPostExecute(List<ContenuListe> noms) {
            progDailog.dismiss();
            mSwipeRefreshLayout.setRefreshing(false);
            adaptRecyclerView(noms);
        }
    }
}



