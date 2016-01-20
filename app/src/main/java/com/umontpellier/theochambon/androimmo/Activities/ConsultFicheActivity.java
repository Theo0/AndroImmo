package com.umontpellier.theochambon.androimmo.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.umontpellier.theochambon.androimmo.Constants.Constants;
import com.umontpellier.theochambon.androimmo.HttpServer.ConnectServer;
import com.umontpellier.theochambon.androimmo.Managers.BddOpenHelper;
import com.umontpellier.theochambon.androimmo.Managers.ShakeEventManager;
import com.umontpellier.theochambon.androimmo.R;
import com.umontpellier.theochambon.androimmo.Services.UploadImage;
import com.umontpellier.theochambon.androimmo.Util.WorkaroundMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ConsultFicheActivity extends AppCompatActivity implements OnMapReadyCallback, ShakeEventManager.ShakeListener {
    double lat = 0;
    double lon = 0;
    String img1;
    String img2;
    String img3;
    String id = "0";
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    Bitmap bitmap1;
    Bitmap bitmap2;
    Bitmap bitmap3;
    PhotoViewAttacher mAttacher;
    FloatingActionButton fab;
    FloatingActionButton fab2;
    FloatingActionButton fabPDF;
    HashMap<String, String> contenu;
    private ScrollView mScrollView;
    private ShakeEventManager sd;
    private boolean isShaken = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_fiche);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sd = new ShakeEventManager();
        sd.setListener(this);
        sd.init(this);
        imageView1 = (ImageView) findViewById(R.id.photo1);
        imageView2 = (ImageView) findViewById(R.id.photo2);
        imageView3 = (ImageView) findViewById(R.id.photo3);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fabPDF = (FloatingActionButton) findViewById(R.id.fabExportPDF);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        initialiseMap();

        //On gère le traitement à faire et l'affichage selon si c'est une fiche locale ou distante
        if (getIntent().getStringExtra("dist").equals("true")) {
            fab.hide();
            fab2.hide();
            remplirFicheDistante();
        } else {
            fabPDF.hide();
            remplirFiche();
            new remplirFicheTask().execute();
        }
        activeFullScreen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sd.register();

    }

    @Override
    protected void onPause() {
        super.onPause();
        sd.deregister();
    }

    public void remplirFiche() {

        BddOpenHelper bdd = new BddOpenHelper(this);

        contenu = bdd.getContenuFiche(id);

        if (!contenu.isEmpty()) {
            remplirTextViews();
        }
    }

    //affecte le contenu récupéré depuis la BDD aux champs du layout
    public void remplirTextViews() {
        TextView tv = (TextView) findViewById(R.id.NomFiche);
        tv.setText(contenu.get("NOM"));

        tv = (TextView) findViewById(R.id.surfaceEdit);
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

        if (contenu.get("LAT") == null) {
            lat = 0;
        } else {
            lat = Double.parseDouble(contenu.get("LAT"));
        }
        if (contenu.get("LON") == null) {
            lon = 0;
        } else {
            lon = Double.parseDouble(contenu.get("LON"));
        }

        //On récupère le chemin des photos
        img1 = contenu.get("IMG1");
        img2 = contenu.get("IMG2");
        img3 = contenu.get("IMG3");


        //Si ce sont des images distantes, on utilise la librairie Picasso pour les charger depuis une URL
        if (getIntent().getStringExtra("dist").equals("true")) {
            if (img1 != null && !img1.equals("")) {
                Picasso.with(this)
                        .load(img1)
                        .resize(2000, 2000)
                        .centerCrop()
                        .into(imageView1);
            }
            if (img2 != null && !img2.equals("")) {
                Picasso.with(this)
                        .load(img2)
                        .resize(2000, 2000)
                        .centerCrop()
                        .into(imageView2);
            }
            if (img3 != null && !img3.equals("")) {
                Picasso.with(this)
                        .load(img3)
                        .resize(2000, 2000)
                        .centerCrop()
                        .into(imageView3);
            }
        }
        initialiseMap();

    }

    public void remplirFicheDistante() {
        new getDonneesFicheDistantTask().execute();
    }


    //Méthode correspondant à l'action du bouton "Editer", pour lancer une nouvelle activité
    public void editFiche(View v) {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        Intent i = new Intent(ConsultFicheActivity.this, EditFicheActivity.class);
        i.putExtra("id", id);
        startActivity(i);
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
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lon))
                .title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));
    }


    //Gestion du full screen pour les images
    private void activeFullScreen() {
        final RelativeLayout full = (RelativeLayout) findViewById(R.id.fullLayout);
        final ImageView fullImage = (ImageView) findViewById(R.id.fullview);
        final FloatingActionButton quit = (FloatingActionButton) findViewById(R.id.quitButton);

        mAttacher = new PhotoViewAttacher(fullImage);
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                full.setVisibility(View.GONE);
                getSupportActionBar().show();
                if (!getIntent().getStringExtra("dist").equals("true")) {
                    fab.show();
                    fab2.show();
                } else {
                    fabPDF.show();
                }
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                full.setVisibility(View.GONE);
                getSupportActionBar().show();
                if (!getIntent().getStringExtra("dist").equals("true")) {
                    fab.show();
                    fab2.show();
                } else {
                    fabPDF.show();
                }
            }
        });
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().hide();
                full.setVisibility(View.VISIBLE);
                if (getIntent().getStringExtra("dist").equals("true"))
                    Picasso.with(ConsultFicheActivity.this).load(img1).resize(2000, 2000).centerCrop().into(fullImage);
                else
                    fullImage.setImageBitmap(bitmap1);
                fab.hide();
                fab2.hide();
                fabPDF.hide();
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().hide();
                full.setVisibility(View.VISIBLE);
                if (getIntent().getStringExtra("dist").equals("true"))
                    Picasso.with(ConsultFicheActivity.this).load(img1).resize(2000, 2000).centerCrop().into(fullImage);
                else
                    fullImage.setImageBitmap(bitmap2);
                fab.hide();
                fab2.hide();
                fabPDF.hide();
            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportActionBar().hide();
                full.setVisibility(View.VISIBLE);
                if (getIntent().getStringExtra("dist").equals("true"))
                    Picasso.with(ConsultFicheActivity.this).load(img3).resize(2000, 2000).centerCrop().into(fullImage);
                else
                    fullImage.setImageBitmap(bitmap3);
                fab.hide();
                fab2.hide();
            }
        });
    }

    private void initialiseMap() {
        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        GoogleMap mMap = ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        mScrollView = (ScrollView) findViewById(R.id.ScrollView01);
        ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).
                setListener(new WorkaroundMapFragment.OnTouchListener() {
                    @Override
                    public void onTouch() {
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                });

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onShake() {
        if (getIntent().getStringExtra("dist").equals("true") && !isShaken) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Voici le lien vers la fiche du bien immobilier : " + Constants.serverURL + "getPDF?id=" + id);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.partage)));
            isShaken = true;
        }
        if (!isShaken) isShaken = true;
    }

    //Encodage des données d'une fiche en JSON
    public JSONObject ficheToJSON() throws JSONException {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, String> entry : contenu.entrySet()) {
            if (!entry.getKey().equals("IMG1") && !entry.getKey().equals("IMG2") && !entry.getKey().equals("IMG3"))
                json.put(entry.getKey(), entry.getValue());
        }
        String[] imgname;
        if (img1 != null) {
            imgname = img1.split("/");
            json.put("IMG1", Constants.uploadDirectory + imgname[9]);
        }
        if (img2 != null) {
            imgname = img2.split("/");
            json.put("IMG2", Constants.uploadDirectory + imgname[9]);
        }
        if (img3 != null) {
            imgname = img3.split("/");
            json.put("IMG3", Constants.uploadDirectory + imgname[9]);
        }
        return json;
    }

    //Méthode correspondant au bouton d'export
    public void exportDistant(View v) throws JSONException {
        JSONObject toSend = ficheToJSON();
        Toast toast = Toast.makeText(getApplicationContext(), "Envoi de la fiche...", Toast.LENGTH_SHORT);
        toast.show();
        new envoiJSONTask().execute(toSend);

    }

    //Méthode correspondant au bouton d'export
    public void exportPDF(View w) {
        Uri webpage = Uri.parse(Constants.serverURL + "getPDF?id=" + id);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    //Récupération et décodage du fichier JSON représentant une fiche distante
    private class getDonneesFicheDistantTask extends AsyncTask<Void, Void, HashMap<String, String>> {

        @Override
        protected HashMap<String, String> doInBackground(Void... params) {
            ConnectServer conn = new ConnectServer();
            conn.setUrl(Constants.serverURL + "getFiche.php?id=" + id);
            contenu = new HashMap<>();
            JSONArray json = conn.getResponseFromURL();
            try {
                if (json != null && json.length() > 0) {
                    JSONObject js = json.getJSONObject(0);
                    Iterator<?> keyset = js.keys();
                    while (keyset.hasNext()) {
                        String key = (String) keyset.next();
                        String value = js.getString(key);
                        if (value == "null") value = "";
                        contenu.put(key, value);
                    }
                } else {
                    contenu.put("NOM", "FICHE INTROUVABLE");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return contenu;
        }

        @Override
        protected void onPostExecute(HashMap<String, String> contenu) {
            if (!contenu.isEmpty()) {
                remplirTextViews();
            }
        }
    }

    //Charger les photos se fait dans une AsyncTask pour ne pas ralentir l'UI
    private class remplirFicheTask extends AsyncTask<Void, Void, Bitmap[]> {

        @Override
        protected Bitmap[] doInBackground(Void... params) {

            // Get the dimensions
            int targetW = 1000;
            int targetH = 1000;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(img1, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            if (img1 != null)
                bitmap1 = BitmapFactory.decodeFile(img1, bmOptions);

            if (img2 != null)
                bitmap2 = BitmapFactory.decodeFile(img2, bmOptions);

            if (img3 != null)
                bitmap3 = BitmapFactory.decodeFile(img3, bmOptions);

            Bitmap[] ret = {bitmap1, bitmap2, bitmap3};

            return ret;

        }

        protected void onPostExecute(Bitmap[] result) {
            if (img1 != null)
                imageView1.setImageBitmap(result[0]);
            if (img2 != null)
                imageView2.setImageBitmap(result[1]);
            if (img3 != null)
                imageView3.setImageBitmap(result[2]);

        }
    }

    //Envoi d'une fiche vers le serveur distant à l'aide d'un AsyncTask et d'un IntentService
    protected class envoiJSONTask extends AsyncTask<JSONObject, Void, Integer> {

        @Override
        protected Integer doInBackground(JSONObject... params) {
            Intent i = new Intent(ConsultFicheActivity.this, UploadImage.class);
            JSONObject json = params[0];
            ConnectServer conn = new ConnectServer();
            conn.setUrl(Constants.serverURL + "setFiche.php");
            int env = conn.sendJSONtoURL(json);

            conn.setUrl(Constants.serverImageURL);
            if (img1 != null) {
                i.putExtra("img1", img1);
            }
            if (img2 != null) {
                i.putExtra("img2", img2);
            }
            if (img3 != null) {
                i.putExtra("img3", img3);
            }

            conn.close();
            startService(i);
            return env;
        }

        @Override
        protected void onPostExecute(Integer param) {
            Toast toast = Toast.makeText(getApplicationContext(), "Enregistrement réussi", Toast.LENGTH_SHORT);
            if (param == 1)
                toast.show();
            else {
                toast = Toast.makeText(getApplicationContext(), "Enregistrement échoué", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
