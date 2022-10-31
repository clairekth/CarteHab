package com.example.cartehab.models;

import android.util.Log;

import com.example.cartehab.outils.FabriqueNumero;

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
        id = h.getId() + "PI" + FabriqueNumero.getInstance().getNumeroPiece();
    }

    public Piece(Habitation h, String name, String i){
        habitation = h;
        nom = name;
        id = i;
    }

    public String getId(){
        return id;
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

    public boolean pieceEstOK() {
        if (murEst == null || !murEst.murEstOK()){
            return false;
        }
        if (murNord == null || !murEst.murEstOK()) {
            return false;
        }
        if (murOuest == null || !murOuest.murEstOK()){
            return false;
        }
        if (murSud == null || !murSud.murEstOK()) {
            return false;
        }

        return murSud.murADesPortes() || murOuest.murADesPortes() || murEst.murADesPortes() || murNord.murADesPortes(); //Au moins un des murs à une porte
    }


    public String erreurs(){
        boolean portesPresentes = false;
        StringBuilder sb = new StringBuilder();
        if (murOuest == null){
            sb.append("Il manque le mur Ouest.\n");
        } else {
            if (!murOuest.murEstOK()){
                for (Porte p : murOuest.getListePortes()){
                    if (p.porteEstOK() == 1){
                        sb.append("Au moins un des portes du mur Ouest n'a pas de pièces suivantes.\n");
                    } else if (p.porteEstOK() == 2){
                        sb.append("Le mur Est de " + p.getPieceSuivante().getNom() + "n'a pas de portes. " + this.nom + " et " +p.getPieceSuivante().getNom() + " ne ont donc pas reliées.\n");
                    } else if (p.porteEstOK() == 3){
                        sb.append("Aucune portes du mur Est de " + p.getPieceSuivante().getNom() + " ne relie " + this.nom + "\n");
                    }
                }
            }
            if (murOuest.murADesPortes()){
                portesPresentes = true;
            }
        }
        if (murEst == null){
            sb.append("Il manque le mur Est.\n");
        } else {
            if (!murEst.murEstOK()){
                for (Porte p : murEst.getListePortes()){
                    if (p.porteEstOK() == 1){
                        sb.append("Au moins un des portes du mur Est n'a pas de pièces suivantes.\n");
                    } else if (p.porteEstOK() == 2){
                        sb.append("Le mur Ouest de " + p.getPieceSuivante().getNom() + " n'a pas de portes. " + this.nom + " et " +p.getPieceSuivante().getNom() + " ne ont donc pas reliées.\n");
                    } else if (p.porteEstOK() == 3){
                        sb.append("Aucune portes du mur Ouest de " + p.getPieceSuivante().getNom() + " ne relie " + this.nom +"\n");
                    }
                }
            }
            if (murEst.murADesPortes()){
                portesPresentes = true;
            }
        }
        if (murNord == null){
            sb.append("Il manque le mur Nord.\n");
        }else {
            if (!murNord.murEstOK()){
                for (Porte p : murNord.getListePortes()){
                    if (p.porteEstOK() == 1){
                        sb.append("Au moins un des portes du mur Nord n'a pas de pièces suivantes.\n");
                    } else if (p.porteEstOK() == 2){
                        sb.append("Le mur Sud de " + p.getPieceSuivante().getNom() + " n'a pas de portes. " + this.nom + " et " +p.getPieceSuivante().getNom() + " ne ont donc pas reliées.\n");
                    } else if (p.porteEstOK() == 3){
                        sb.append("Aucune portes du mur Sud de " + p.getPieceSuivante().getNom() + " ne relie " + this.nom + "\n");
                    }
                }              }
            if (murNord.murADesPortes()){
                portesPresentes = true;
            }
        }
        if (murSud == null){
            sb.append("Il manque le mur Sud.\n");
        } else {
            if (!murSud.murEstOK()){
                for (Porte p : murSud.getListePortes()){
                    if (p.porteEstOK() == 1){
                        sb.append("Au moins un des portes du mur Sud n'a pas de pièces suivantes.\n");
                    } else if (p.porteEstOK() == 2){
                        sb.append("Le mur Nord de " + p.getPieceSuivante().getNom() + " n'a pas de portes. " + this.nom + " et " +p.getPieceSuivante().getNom() + " ne ont donc pas reliées.\n");
                    } else if (p.porteEstOK() == 3){
                        sb.append("Aucune portes du mur Nord de " + p.getPieceSuivante().getNom() + " ne relie " + this.nom +"\n");
                    }
                }
            }
            if (murSud.murADesPortes()){
                portesPresentes = true;
            }
        }

        if (!portesPresentes){
            sb.append("Il n'y a pas de portes dans la pièce.\n");
        }

        return sb.toString();
    }



    @Override
    public String toString() {
        return "Piece{" +
                ", nom='" + nom + '\'' +
                ", murNord=" + murNord +
                ",\n murSud=" + murSud +
                ",\n murEst=" + murEst +
                ",\n murOuest=" + murOuest +
                "}\n";
    }
}
