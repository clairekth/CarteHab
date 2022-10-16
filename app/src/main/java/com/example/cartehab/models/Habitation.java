package com.example.cartehab.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Habitation implements Serializable {
    protected ArrayList<Piece> listePieces;

    public Habitation(){
        listePieces = new ArrayList<>();
    }

    public void addPiece(Piece p){
        listePieces.add(p);
    }

    public ArrayList<Piece> getListePieces(){
        return listePieces;
    }

    public Piece getPiece(String nom){
        Log.i("Hab", "__________");
        for (Piece p : listePieces){
            Log.i("Hab", p.getNom() + " - " + nom);
            if (p.getNom().equals(nom)){
                Log.i("Hab", "OUI");
                return p;
            } else {
                Log.i("Hab", "NON");

            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "Habitation{" +
                "listePieces=" + listePieces +
                '}';
    }
}
