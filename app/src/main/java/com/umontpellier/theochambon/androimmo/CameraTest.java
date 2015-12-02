package com.umontpellier.theochambon.androimmo;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.umontpellier.theochambon.androimmo.Constants.Constants;
import com.umontpellier.theochambon.androimmo.HttpServer.ConnectServer;
import com.umontpellier.theochambon.androimmo.Managers.ShakeEventManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CameraTest extends AppCompatActivity implements  ShakeEventManager.ShakeListener {


    private ShakeEventManager sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sd = new ShakeEventManager();
        sd.setListener(this);
        sd.init(this);
        new RetrieveData().execute();

    }

    @Override
    public void onShake() {
        Toast.makeText(this, "SHAKED", Toast.LENGTH_SHORT).show();
    }


    class RetrieveData extends AsyncTask<Void, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(Void... params) {
            ConnectServer conn = new ConnectServer();

            conn.setUrl(Constants.serverURL + "listeFiches.php");
            JSONArray json = conn.getResponseFromURL();

            return json;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            try {
                if (json != null) {

                    Log.w("taille JSON : ", json.length() + "");
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject js = json.getJSONObject(i);
                        Log.w("JSON", js.getString("NOM"));

                    }
                } else
                    Log.w("debug", "JSON VIDE !!!!!");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
