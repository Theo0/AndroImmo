package com.umontpellier.theochambon.androimmo.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.umontpellier.theochambon.androimmo.Managers.BddOpenHelper;
import com.umontpellier.theochambon.androimmo.Managers.ShakeEventManager;
import com.umontpellier.theochambon.androimmo.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewFicheActivity extends AppCompatActivity implements ShakeEventManager.ShakeListener {

    private Location lastKnownLocation;
    private double lat = 0;
    private double lon = 0;
    static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView imageView1;
    private ImageView imageView2;
    private ImageView imageView3;
    private String mCurrentPhotoPath1;
    private String mCurrentPhotoPath2;
    private String mCurrentPhotoPath3;
    private int currentPhoto;
    private ShakeEventManager sd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fiche);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sd = new ShakeEventManager();
        sd.setListener(this);
        sd.init(this);
        this.imageView1 = (ImageView) this.findViewById(R.id.image1);
        this.imageView2 = (ImageView) this.findViewById(R.id.image2);
        this.imageView3 = (ImageView) this.findViewById(R.id.image3);
        Button photoButton1 = (Button) this.findViewById(R.id.boutonPhoto1);
        Button photoButton2 = (Button) this.findViewById(R.id.boutonPhoto2);
        Button photoButton3 = (Button) this.findViewById(R.id.boutonPhoto3);
        photoButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentPhoto = 1;
                dispatchTakePictureIntent(currentPhoto);
            }
        });
        photoButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentPhoto = 2;
                dispatchTakePictureIntent(currentPhoto);
            }
        });
        photoButton3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentPhoto = 3;
                dispatchTakePictureIntent(currentPhoto);
            }
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic(currentPhoto);
        }
    }

    private void setPic(int id) {
        // Get the dimensions of the View
        int targetW = 400;
        int targetH = 400;
        String mCurrentPhotoPath = mCurrentPhotoPath1;
        ImageView imageView = (ImageView) this.findViewById(R.id.image1);
        switch (id){
            case 1:
                targetW = imageView1.getWidth();
                targetH = imageView1.getHeight();
                mCurrentPhotoPath = mCurrentPhotoPath1;
                break;
            case 2:
                targetW = imageView2.getWidth();
                targetH = imageView2.getHeight();
                mCurrentPhotoPath = mCurrentPhotoPath2;
                imageView = (ImageView) this.findViewById(R.id.image2);
                break;
            case 3:
                targetW = imageView3.getWidth();
                targetH = imageView3.getHeight();
                mCurrentPhotoPath = mCurrentPhotoPath3;
                imageView = (ImageView) this.findViewById(R.id.image3);
                break;
        }

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;



        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    private void dispatchTakePictureIntent(int id) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(id);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast toast = Toast.makeText(getApplicationContext(), "Erreur Photo", Toast.LENGTH_LONG);
                toast.show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }

    }


    private File createImageFile(int id) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents

        switch (id) {
            case 1:
                mCurrentPhotoPath1 = image.getAbsolutePath();
                break;
            case 2:
                mCurrentPhotoPath2 = image.getAbsolutePath();
                break;
            case 3:
                mCurrentPhotoPath3 = image.getAbsolutePath();
                break;
        }

        return image;
    }

    public void sauveFiche(View v) {


        EditText nom = (EditText) findViewById(R.id.nomEdit);
        if (nom.getText().toString().matches("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Veuillez renseigner au moins le nom de la fiche !", Toast.LENGTH_LONG);
            toast.show();
        } else {
            EditText surface = (EditText) findViewById(R.id.surfaceEdit);
            if (surface.getText().toString().matches("")) {
                surface.setText("0");
            }
            EditText nbPieces = (EditText) findViewById(R.id.nbpiecesEdit);
            if (nbPieces.getText().toString().matches("")) {
                nbPieces.setText("0");
            }
            EditText nbChambres = (EditText) findViewById(R.id.nbchambreEdit);
            if (nbChambres.getText().toString().matches("")) {
                nbChambres.setText("0");
            }
            EditText nbsdb = (EditText) findViewById(R.id.nbSDBEdit);
            if (nbsdb.getText().toString().matches("")) {
                nbsdb.setText("0");
            }
            EditText nbWC = (EditText) findViewById(R.id.nbWCEdit);
            if (nbWC.getText().toString().matches("")) {
                nbWC.setText("0");
            }
            EditText nbbalcon = (EditText) findViewById(R.id.nbBalconEdit);
            if (nbbalcon.getText().toString().matches("")) {
                nbbalcon.setText("0");
            }
            EditText etage = (EditText) findViewById(R.id.etagesEdit);
            if (etage.getText().toString().matches("")) {
                etage.setText("0");
            }
            EditText adr = (EditText) findViewById(R.id.adrEdit);
            EditText ville = (EditText) findViewById(R.id.villeEdit);
            EditText expo = (EditText) findViewById(R.id.expoEdit);
            EditText taxe = (EditText) findViewById(R.id.taxeEdit);
            if (taxe.getText().toString().matches("")) {
                taxe.setText("0");
            }
            EditText copro = (EditText) findViewById(R.id.coproEdit);
            if (copro.getText().toString().matches("")) {
                copro.setText("0");
            }
            EditText prix = (EditText) findViewById(R.id.prixEdit);
            if (prix.getText().toString().matches("")) {
                prix.setText("0");
            }
            EditText notes = (EditText) findViewById(R.id.notesEdit);


            BddOpenHelper bdd = new BddOpenHelper(this);
            long er = bdd.insert(nom.getText().toString(), Integer.parseInt(surface.getText().toString()), Integer.parseInt(nbPieces.getText().toString()), Integer.parseInt(nbChambres.getText().toString()), Integer.parseInt(nbsdb.getText().toString()), Integer.parseInt(nbWC.getText().toString()), Integer.parseInt(nbbalcon.getText().toString()), Integer.parseInt(etage.getText().toString()), adr.getText().toString(), ville.getText().toString(), expo.getText().toString(), Integer.parseInt(taxe.getText().toString()), Integer.parseInt(copro.getText().toString()), Integer.parseInt(prix.getText().toString()), notes.getText().toString(), lat, lon, mCurrentPhotoPath1, mCurrentPhotoPath2, mCurrentPhotoPath3);

            if (er == -1) {
                Toast toast = Toast.makeText(getApplicationContext(), "Erreur lors de la création de la fiche (erreur BDD)", Toast.LENGTH_LONG);
                toast.show();
            }

            Intent i = new Intent(NewFicheActivity.this, MainActivity.class);
            startActivity(i);
        }
    }

    public void locate(View v) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;
        lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if(lastKnownLocation != null){
        lat = lastKnownLocation.getLatitude();
        lon = lastKnownLocation.getLongitude();
        }
        else{
            lat = 0;
            lon = 0;
        }
        Toast toast = Toast.makeText(getApplicationContext(), "Bien localisé", Toast.LENGTH_SHORT);
        toast.show();


    }

    /*
     */

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
    public void onShake() {
        locate(null);
    }
}
