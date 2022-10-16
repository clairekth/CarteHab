package com.example.cartehab.models;

import android.graphics.Bitmap;

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

    public Piece getPiece(){
        return piece;
    }

    public String toString(){
        return orientation + listePortes.size();
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

}
