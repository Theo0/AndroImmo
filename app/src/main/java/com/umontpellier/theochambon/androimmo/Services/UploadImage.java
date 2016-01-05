package com.umontpellier.theochambon.androimmo.Services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.umontpellier.theochambon.androimmo.Constants.Constants;
import com.umontpellier.theochambon.androimmo.HttpServer.ConnectServer;
import com.umontpellier.theochambon.androimmo.R;


public class UploadImage extends IntentService {

    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
    int idBar = 1;

    public UploadImage() {
        super("UploadImage");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getApplicationContext());
        mBuilder.setContentTitle("Envoi")
                .setContentText("Envoi de la fiche en cours")
                .setSmallIcon(R.drawable.upload);
        mBuilder.setProgress(0, 0, true);
        mNotifyManager.notify(idBar, mBuilder.build());


        if (intent != null) {
            final String img1 = intent.getStringExtra("img1");
            final String img2 = intent.getStringExtra("img2");
            final String img3 = intent.getStringExtra("img3");
            int im1 = 0;
            int im2 = 0;
            int im3 = 0;

            ConnectServer conn = new ConnectServer();
            conn.setUrl(Constants.serverImageURL);
            if (img1 != null) {
                im1 = conn.uploadImage(img1);
            }
            if (img2 != null) {
                im2 = conn.uploadImage(img2);
            }
            if (img3 != null) {
                im3 = conn.uploadImage(img3);
            }

            if (im1 != -1 && im2 != -1 && im3 != -1) {
                Log.w("DEBUG", "Ca a bien upload les images");
            }

            conn.close();


        }

        mBuilder.setProgress(0, 0, false);
        mBuilder.setContentTitle("Terminé")
                .setContentText("Votre fiche à bien été envoyé au serveur.")
                .setSmallIcon(R.drawable.upload);
        mNotifyManager.notify(idBar, mBuilder.build());
    }
}
