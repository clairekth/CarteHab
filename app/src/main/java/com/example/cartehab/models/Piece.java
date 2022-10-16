package com.example.cartehab.models;

import java.io.Serializable;

public class Piece implements Serializable {
    protected String id;
    protected String nom;
    protected Habitation habitation;
    protected Mur murNord;
    protected Mur murSud;
    protected Mur murEst;
    protected Mur murOuest;

    public Piece(Habitation h){
        habitation = h;
    }

    public void setMur(Mur mur){
        if (mur.getOrientation().equals("N")){
            murNord = mur;
        } else if (mur.getOrientation().equals("S")){
            murSud = mur;
        } else if (mur.getOrientation().equals("E")){
            murEst = mur;
        } else {
            murOuest = mur;
        }
    }

    public void setNom(String nom){
        this.nom = nom;

    }
    public Mur getMurNord(){
        return murNord;
    }
    public Mur getMurSud() {
        return murSud;
    }

    public Mur getMurEst() {
        return murEst;
    }

    public Mur getMurOuest() {
        return murOuest;
    }

    public String getNom(){
        return nom;
    }

    @Override
    public String toString() {
        return "Piece{" +
                ", nom='" + nom + '\'' +
                ", murNord=" + murNord +
                ", murSud=" + murSud +
                ", murEst=" + murEst +
                ", murOuest=" + murOuest +
                '}';
    }
}
