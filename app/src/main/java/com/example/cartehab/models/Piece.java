package com.example.cartehab.models;

import java.io.Serializable;

public class Piece implements Serializable {
    protected String id;
    protected Habitation habitation;
    protected Mur murNord;
    protected Mur murSud;
    protected Mur murEst;
    protected Mur murOuest;

    public Piece(Habitation h){
        habitation = h;
    }

    public void setMurNord(Mur mur){
        murNord = mur;
    }

    public Mur getMurNord(){
        return murNord;
    }
}
