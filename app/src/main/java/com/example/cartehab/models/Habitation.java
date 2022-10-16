package com.example.cartehab.models;

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

    @Override
    public String toString() {
        return "Habitation{" +
                "listePieces=" + listePieces +
                '}';
    }
}
