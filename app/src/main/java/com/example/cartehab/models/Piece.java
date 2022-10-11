package com.example.cartehab.models;

public class Piece {
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
}
