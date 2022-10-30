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
                ", piece=" + piece +
                ", orientation='" + orientation + '\'' +
                ", listePortes" + listePortes.size() +
                '}';
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
        for (Porte p : listePortes){
            if (!p.porteEstOK()){
                return false; //Une des portes n'a pas de pièces suivantes
            }
        }
        return true;
    }

    public boolean murADesPortes(){
        return listePortes.size() != 0;
    }

}
