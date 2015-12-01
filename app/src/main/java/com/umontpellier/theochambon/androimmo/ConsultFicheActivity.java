package com.umontpellier.theochambon.androimmo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ConsultFicheActivity extends AppCompatActivity implements OnMapReadyCallback {
    boolean exportButton = false;
    double lat = 0;
    double lon = 0;
    String img1;
    String img2;
    String img3;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    Bitmap bitmap1;
    Bitmap bitmap2;
    Bitmap bitmap3;
    PhotoViewAttacher mAttacher;
    private ScrollView mScrollView;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_fiche);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.home);
        imageView1 = (ImageView) findViewById(R.id.photo1);
        imageView2 = (ImageView) findViewById(R.id.photo2);
        imageView3 = (ImageView) findViewById(R.id.photo3);

        initialiseMap();
        remplirFiche();
        activeFullScreen();
        new remplirFicheTask().execute();


        }

    public void remplirFiche(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        BddOpenHelper bdd = new BddOpenHelper(this);

        HashMap<String, String> contenu = bdd.getContenuFiche(id);

        if(!contenu.isEmpty()){

            TextView tv = (TextView) findViewById(R.id.NomFiche);
            tv.setText(contenu.get("nom"));

            tv = (TextView) findViewById(R.id.surfaceEdit);
            tv.setText(contenu.get("surface"));

            tv = (TextView) findViewById(R.id.nbpiecesEdit);
            tv.setText(contenu.get("nbpieces"));

            tv = (TextView) findViewById(R.id.nbchambreEdit);
            tv.setText(contenu.get("nbchambres"));

            tv = (TextView) findViewById(R.id.nbSDBEdit);
            tv.setText(contenu.get("nbsdb"));

            tv = (TextView) findViewById(R.id.nbWCEdit);
            tv.setText(contenu.get("nbwc"));

            tv = (TextView) findViewById(R.id.nbBalconEdit);
            tv.setText(contenu.get("nbbalcon"));

            tv = (TextView) findViewById(R.id.etagesEdit);
            tv.setText(contenu.get("etages"));

            tv = (TextView) findViewById(R.id.adrEdit);
            tv.setText(contenu.get("adr"));

            tv = (TextView) findViewById(R.id.villeEdit);
            tv.setText(contenu.get("ville"));

            tv = (TextView) findViewById(R.id.expoEdit);
            tv.setText(contenu.get("expo"));

            tv = (TextView) findViewById(R.id.taxeEdit);
            tv.setText(contenu.get("taxe"));

            tv = (TextView) findViewById(R.id.coproEdit);
            tv.setText(contenu.get("copro"));

            tv = (TextView) findViewById(R.id.notesEdit);
            tv.setText(contenu.get("notes"));

            lat = Double.parseDouble(contenu.get("lat"));
            lon = Double.parseDouble(contenu.get("lon"));
            img1 = contenu.get("img1");
            img2 = contenu.get("img2");
            img3 = contenu.get("img3");


        }
    }


    public void editFiche(View v){
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

    private void activeFullScreen(){
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        final RelativeLayout full = (RelativeLayout) findViewById(R.id.fullLayout);
        final ImageView fullImage = (ImageView) findViewById(R.id.fullview);
        final FloatingActionButton quit = (FloatingActionButton) findViewById(R.id.quitButton);

        mAttacher = new PhotoViewAttacher(fullImage);
        full.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                full.setVisibility(View.GONE);
                getSupportActionBar().show();
                fab.show();
                fab2.show();
            }});
        quit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                full.setVisibility(View.GONE);
                getSupportActionBar().show();
                fab.show();
                fab2.show();
            }});
        imageView1.setOnClickListener(new View.OnClickListener()
        {   @Override
            public void onClick (View v){
                getSupportActionBar().hide();
                full.setVisibility(View.VISIBLE);
                fullImage.setImageBitmap(bitmap1);
                fab.hide();
                fab2.hide();
            }});
        imageView2.setOnClickListener(new View.OnClickListener()
        {   @Override
            public void onClick (View v){
                getSupportActionBar().hide();
                full.setVisibility(View.VISIBLE);
                fullImage.setImageBitmap(bitmap2);
                fab.hide();
                fab2.hide();
            }});
        imageView3.setOnClickListener(new View.OnClickListener()
        {   @Override
            public void onClick (View v){
                getSupportActionBar().hide();
                full.setVisibility(View.VISIBLE);
                fullImage.setImageBitmap(bitmap3);
                fab.hide();
                fab2.hide();
            }});
    }

    private void initialiseMap(){
        WorkaroundMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap=((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        mScrollView=(ScrollView) findViewById(R.id.ScrollView01);((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).
                setListener(new WorkaroundMapFragment.OnTouchListener() {
                                @Override
                                public void onTouch () {
                                    mScrollView.requestDisallowInterceptTouchEvent(true);
                                }
                            });

        mapFragment.getMapAsync(this);

    }


    private class remplirFicheTask extends AsyncTask<Void, Void, Bitmap[]> {

        @Override
        protected Bitmap[] doInBackground(Void... params) {


            // Get the dimensions
            int targetW = 600;
            int targetH = 400;

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(img1, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            if(img1 != null)
                bitmap1 = BitmapFactory.decodeFile(img1, bmOptions);

            if(img2 != null)
                bitmap2 = BitmapFactory.decodeFile(img2, bmOptions);

            if(img3 != null)
                bitmap3 = BitmapFactory.decodeFile(img3, bmOptions);

            Bitmap[] ret = {bitmap1, bitmap2, bitmap3};

            return ret;

        }

        protected void onPostExecute(Bitmap[] result) {
            if(img1 != null)
            imageView1.setImageBitmap(result[0]);
            if(img2 != null)
            imageView2.setImageBitmap(result[1]);
            if(img3 != null)
            imageView3.setImageBitmap(result[2]);

        }
    }

    public void addExportButton(View v){
        final FloatingActionButton pdf = (FloatingActionButton) findViewById(R.id.fabExportPDF);
        final FloatingActionButton distant = (FloatingActionButton) findViewById(R.id.fabExportDistant);
        if(!exportButton){
            pdf.setVisibility(View.VISIBLE);
            distant.setVisibility(View.VISIBLE);
            exportButton = true;
        } else{
            pdf.setVisibility(View.GONE);
            distant.setVisibility(View.GONE);
            exportButton = false;
        }

    }

}
