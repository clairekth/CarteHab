package com.example.cartehab.models;

import android.os.Build;
import android.util.Log;

import com.example.cartehab.outils.FabriqueNumero;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Habitation implements Serializable {
    protected String id;
    protected ArrayList<Piece> listePieces;
    protected String name;
    protected int lastNumPiece;

    public Habitation(){
        listePieces = new ArrayList<>();
        id = "HAB" + FabriqueNumero.getInstance().getNumeroHabitation();
        name = id;
    }

    public void setLastNumPiece(int i){
        lastNumPiece = i;
    }
    public int getLastNumPiece(){
        return lastNumPiece;
    }
    public void addPiece(Piece p){
        listePieces.add(p);
    }

    public void remove(Piece piece){
        listePieces.remove(piece);
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public Piece getPiece(String nom){
        for (Piece p : listePieces){
            if (p.getNom().equals(nom)){
                return p;
            }
        }
        return null;
    }

    public ArrayList<Piece> getListePieces(){
        return listePieces;
    }
    @Override
    public String toString() {
        return "Habitation{" +
                "id=" + id +
                "listePieces=" + listePieces +
                '}';
    }


    public boolean nomPieceExisteDeja(String nom){
        for (Piece p : listePieces){
            if (p.getNom().equalsIgnoreCase(nom)){
                return true;
            }
        }
        return false;
    }

    public void setName(String name){
        this.name = name;
    }
}
