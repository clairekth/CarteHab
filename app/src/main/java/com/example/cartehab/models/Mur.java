package com.example.cartehab.models;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

public class Mur implements Serializable {
    protected String id;
    protected String photo;
    protected Piece piece;
    protected String orientation;
    protected ArrayList<Porte> listePortes;

    public Mur(Piece p, String ph, String o){
        piece = p;
        photo = ph;
        orientation = o;
        listePortes = new ArrayList<>();
    }

    public String getPhoto(){
        return photo;
    }

    public void addPorte(Porte p){
        listePortes.add(p);
    }

    public String toString(){
        return orientation + listePortes.size();
    }

}
