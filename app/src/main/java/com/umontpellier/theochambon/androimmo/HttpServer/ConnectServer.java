package com.umontpellier.theochambon.androimmo.HttpServer;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by theo on 01/12/2015.
 */
public class ConnectServer {

    private URL url;
    private HttpURLConnection connect;

    public ConnectServer() {

    }

    public void setUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public JSONArray getResponseFromURL() {
        JSONArray json = null;
        try {
            this.connect = (HttpURLConnection) this.url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(this.connect.getInputStream()));
            String aux;
            StringBuilder builder = new StringBuilder();
            while ((aux = br.readLine()) != null) {
                builder.append(aux);
            }

            json = new JSONArray(builder.toString());
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


    public int sendJSONtoURL(JSONObject json) {
        StringBuilder builder = null;
        try {
            this.connect = (HttpURLConnection) url.openConnection();
            this.connect.setDoOutput(true);
            this.connect.connect();
            OutputStreamWriter wr = new OutputStreamWriter(this.connect.getOutputStream());
            wr.write("json=" + json.toString());
            Log.w("JSON =====", json.toString());
            wr.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader(this.connect.getInputStream()));
            String aux;
            builder = new StringBuilder();
            while ((aux = br.readLine()) != null) {
                builder.append(aux);
            }
            Log.w("JSON renvoyé : ", builder.toString());
            wr.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (builder.toString().equals("OK"))
            return 1;
        else
            return 0;
    }

    public void close() {
        if (this.connect != null) {
            this.connect.disconnect();
        }

    }

}
