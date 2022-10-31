package com.example.cartehab.models;

import android.os.Build;
import android.util.Log;

import com.example.cartehab.outils.FabriqueNumero;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Habitation implements Serializable {
    protected String id;
    protected HashMap<String, Piece> listePieces;
    protected String name;
    protected int lastNumPiece;

    public Habitation(){
        listePieces = new HashMap<>();
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
        listePieces.put(p.getId(), p);
    }

    public void remove(Piece piece){
        listePieces.remove(piece.getId());
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public Piece getPiece(String nom){
        for (Piece p : listePieces.values()){
            if (p.getNom().equals(nom)){
                return p;
            }
        }
        return null;
    }

    public HashMap<String, Piece> getListePieces(){
        return listePieces;
    }
    @Override
    public String toString() {
        return "Habitation{" +
                "id=" + id +
                "listePieces=" + listePieces +
                '}';
    }

    public ArrayList<Piece> hashmapToList(){
        ArrayList<Piece> pieces = new ArrayList<>();
        for (Piece p : listePieces.values()){
            pieces.add(p);
        }
        return pieces;
    }

    public boolean nomPieceExisteDeja(String nom){
        for (Piece p : listePieces.values()){
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
