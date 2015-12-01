package com.umontpellier.theochambon.androimmo;

/**
 * Created by theo on 10/11/2015.
 */
public class ContenuListe {
    private String nom, ville, id, surface, pieces;



    public ContenuListe(String n, String v, String id, String p, String s){
        this.nom = n;
        this.ville = v;
        this.id = id;
        this.surface = s;
        this.pieces = p;


    }

    public String getNom() {
        return nom;
    }

    public String getVille() {
        return ville;
    }

    public String getId() {
        return id;
    }

    public String getSurface() {
        return surface;
    }

    public String getPieces() {
        return pieces;
    }

}
