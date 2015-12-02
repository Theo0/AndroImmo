package com.umontpellier.theochambon.androimmo.Managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.umontpellier.theochambon.androimmo.Adapters.ContenuListe;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by theo on 21/10/2015.
 */

public class BddOpenHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "FICHES";
    private static final String DATABASE_NAME = "IMMO";
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY, NOM TEXT, SURFACE INTEGER, NBPIECES INTEGER, NBCHAMBRES INTEGER, NBSDB INTEGER, NBWC INTEGER, NBBALCON INTEGER, ETAGES INTEGER, ADR TEXT, VILLE TEXT, EXPO TEXT, TAXE INTEGER, COPRO INTEGER, NOTES TEXT, LAT INTEGER, LON INTEGER, IMG1 TEXT, IMG2 TEXT, IMG3 TEXT)";

    public BddOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public long insert(String nom, int surface, int nbpieces, int nbchambres, int nbsdb, int nbwc, int nbbalcon, int etage, String adr, String ville, String expo, int taxe, int copro, String notes, double lat, double lon, String img1, String img2, String img3){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("NOM", nom);
        values.put("SURFACE", surface);
        values.put("NBPIECES", nbpieces);
        values.put("NBCHAMBRES", nbchambres);
        values.put("NBSDB", nbsdb);
        values.put("NBWC", nbwc);
        values.put("NBBALCON", nbbalcon);
        values.put("ETAGES", etage);
        values.put("ADR", adr);
        values.put("VILLE", ville);
        values.put("EXPO", expo);
        values.put("TAXE", taxe);
        values.put("COPRO", copro);
        values.put("NOTES", notes);
        values.put("LAT", lat);
        values.put("LON", lon);
        values.put("IMG1", img1);
        values.put("IMG2", img2);
        values.put("IMG3", img3);

        long er = db.insert(TABLE_NAME, null, values);
        db.close();
        return er;
    }

    public long update(String nom, int surface, int nbpieces, int nbchambres, int nbsdb, int nbwc, int nbbalcon, int etage, String adr, String ville, String expo, int taxe, int copro, String notes, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("NOM", nom);
        values.put("SURFACE", surface);
        values.put("NBPIECES", nbpieces);
        values.put("NBCHAMBRES", nbchambres);
        values.put("NBSDB", nbsdb);
        values.put("NBWC", nbwc);
        values.put("NBBALCON", nbbalcon);
        values.put("ETAGES", etage);
        values.put("ADR", adr);
        values.put("VILLE", ville);
        values.put("EXPO", expo);
        values.put("TAXE", taxe);
        values.put("COPRO", copro);
        values.put("NOTES", notes);

        long er = db.update(TABLE_NAME, values, "ID =" + id, null);
        db.close();
        return er;

    }

    public ArrayList<ContenuListe> getNoms(){
        ArrayList<ContenuListe> noms = new ArrayList<>();

        String query = "SELECT ID, NOM, VILLE, NBPIECES, SURFACE FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                noms.add(new ContenuListe(cursor.getString(1), cursor.getString(2), cursor.getString(0), cursor.getString(3), cursor.getString(4)));
            } while(cursor.moveToNext());
        }

        cursor.close();
        return noms;

    }

    public HashMap<String, String> getContenuFiche(String id){
        HashMap<String, String> contenu = new HashMap<String, String>();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE ID =" + id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            contenu.put("NOM", cursor.getString(1));
            contenu.put("SURFACE", cursor.getString(2));
            contenu.put("NBPIECES", cursor.getString(3));
            contenu.put("NBCHAMBRES", cursor.getString(4));
            contenu.put("NBSDB", cursor.getString(5));
            contenu.put("NBWC", cursor.getString(6));
            contenu.put("NBBALCON", cursor.getString(7));
            contenu.put("ETAGES", cursor.getString(8));
            contenu.put("ADR", cursor.getString(9));
            contenu.put("VILLE", cursor.getString(10));
            contenu.put("EXPO", cursor.getString(11));
            contenu.put("TAXE", cursor.getString(12));
            contenu.put("COPRO", cursor.getString(13));
            contenu.put("NOTES", cursor.getString(14));
            contenu.put("LAT", cursor.getString(15));
            contenu.put("LON", cursor.getString(16));
            contenu.put("IMG1", cursor.getString(17));
            contenu.put("IMG2", cursor.getString(18));
            contenu.put("IMG3", cursor.getString(19));

        }

        cursor.close();

        return contenu;
    }

}
