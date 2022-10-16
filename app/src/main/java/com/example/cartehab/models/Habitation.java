package com.example.cartehab.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Habitation implements Serializable {
    protected HashMap<String, Piece> listePieces;

    public Habitation(){
        listePieces = new HashMap<>();
    }

    public void addPiece(Piece p){
        listePieces.put(p.getNom(), p);
    }


    public Piece getPiece(String nom){
        if (nom.equals("Pièce non créée")){
            return null;
        }
        return listePieces.get(nom);
    }

    public HashMap<String, Piece> getListePieces(){
        return listePieces;
    }
    @Override
    public String toString() {
        return "Habitation{" +
                "listePieces=" + listePieces +
                '}';
    }
}
