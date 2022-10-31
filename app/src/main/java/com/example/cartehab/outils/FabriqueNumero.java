package com.example.cartehab.outils;

import android.util.Log;

public class FabriqueNumero {

    private static final FabriqueNumero instance = new FabriqueNumero();

    private int cptHabitation;

    private int cptPiece;

    private FabriqueNumero() {
    }


    public static FabriqueNumero getInstance() {
        return instance;
    }

    public int getNumeroHabitation() {
        cptHabitation++;
        return cptHabitation ;
    }


    public int getNumeroPiece() {
        cptPiece++;
        return cptPiece ;
    }

    public void resetCompteurPiece(){
        cptPiece = 0;
    }

    public int getNumeroHabitationSansIncre(){
        return cptHabitation;
    }

    public int getNumeroPieceSansIncre(){
        return cptPiece;
    }

    public void setCptHabitation(int i ){
        cptHabitation = i;
    }

    public void setCptPiece(int i ){
        cptPiece = i;

    }
}
