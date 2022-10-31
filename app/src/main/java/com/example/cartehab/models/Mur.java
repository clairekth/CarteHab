package com.example.cartehab.models;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Mur implements Serializable {
    protected String id;
    protected Piece piece;
    protected String orientation;
    protected ArrayList<Porte> listePortes;

    public Mur(Piece p, String o, String id){
        piece = p;
        orientation = o;
        listePortes = new ArrayList<>();
        this.id = id;
    }


    public void addPorte(Porte p){
        listePortes.add(p);
    }

    @Override
    public String toString() {
        return "Mur{" +
                "id='" + id + '\'' +
                ", piece=" + piece.getNom() +
                ", orientation='" + orientation + '\'' +
                ", listePortes" + listePortes +
                "}\n";
    }

    public Piece getPiece(){
        return piece;
    }

    public ArrayList<Porte> getListePortes(){
        return listePortes;
    }

    public String getId(){
        return id;
    }

    public String getOrientation(){
        return orientation;
    }

    public void deletePortes(){
        listePortes.clear();
    }

    public boolean murEstOK(){
        Log.i("PORTE",this.toString());
        for (Porte p : listePortes){
            if (p.porteEstOK() != 0){
                return false; //Une des portes a une erreur
            }
        }
        return true;
    }

    public boolean murADesPortes(){
        return listePortes.size() != 0;
    }


}
