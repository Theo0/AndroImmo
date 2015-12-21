package com.umontpellier.theochambon.androimmo.HttpServer;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
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

    public int uploadImage(String path) {
        int serverResponseCode;
        int retour = -1;

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        DataOutputStream dos = null;
        try {
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            File sourceFile = new File(path);
            if (!sourceFile.isFile()) {
                return -1;
            } else {
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                connect = (HttpURLConnection) url.openConnection();
                connect.setDoInput(true); // Allow Inputs
                connect.setDoOutput(true); // Allow Outputs
                connect.setUseCaches(false); // Don't use a Cached Copy
                connect.setRequestMethod("POST");
                connect.setRequestProperty("Connection", "Keep-Alive");
                connect.setRequestProperty("ENCTYPE", "multipart/form-data");
                connect.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connect.setRequestProperty("uploaded_file", path);

                dos = new DataOutputStream(connect.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name='uploaded_file'; filename='" + path + "'" + lineEnd);

                dos.writeBytes(lineEnd);

                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connect.getResponseCode();
                String serverResponseMessage = connect.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    retour = 0;
                    fileInputStream.close();
                    dos.flush();
                    dos.close();
                } else {
                    retour = serverResponseCode;
                }


                BufferedReader br = new BufferedReader(new InputStreamReader(this.connect.getInputStream()));
                String aux;
                StringBuilder builder = new StringBuilder();
                while ((aux = br.readLine()) != null) {
                    builder.append(aux);
                }

                Log.w("DEBUG : ", builder.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return retour;
    }

    public void close() {
        if (this.connect != null) {
            this.connect.disconnect();
        }

    }

}
