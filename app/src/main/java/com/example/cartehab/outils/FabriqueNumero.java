package com.example.cartehab.outils;

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
        return cptHabitation - 1;
    }


    public int getNumeroPiece() {
        cptPiece++;
        return cptPiece -1 ;
    }

    public void resetCompteurPiece(){
        cptPiece = 0;
    }
}
