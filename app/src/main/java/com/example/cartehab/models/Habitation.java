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

    public Habitation(){
        listePieces = new HashMap<>();
        id = "HAB" + FabriqueNumero.getInstance().getNumeroHabitation();
        name = id;
    }

    public Habitation(String id){
        listePieces = new HashMap<>();
        this.id = id;
    }
    public void addPiece(Piece p){
        listePieces.put(p.getId(), p);
    }

    public void remove(Piece piece){
        Log.i("Hab", "av" + this.toString());
        listePieces.remove(piece.getId());
        Log.i("Hab","remove : " + this.toString());
    }

    public String getId(){
        return id;
    }
    public String getName(){
        return name;
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
