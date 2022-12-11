package com.example.cartehab.models;

import java.io.Serializable;
/**
 * La classe Porte représente une porte sur un mur.
 * @author Claire Kurth
 */
public class Porte implements Serializable {
    /**
     * Coordoonées (x,y) du point haut gauche (left,top) et bas droit (right,bottom) d'un rectangle
     * correspondant aux dimensions de la porte.
     */
    protected int left, top, right, bottom;
    /**
     * Le mur contenant la porte.
     */
    protected Mur mur;
    /**
     * La pièce vers laquelle mène la porte.
     */
    protected Piece pieceSuivante;

    /**
     * Constructeur.
     * @param m Le mur contenant la porte.
     * @param l point x haut gauche.
     * @param t point y haut gauche.
     * @param r point x bas droite.
     * @param b point y bas droite.
     */
    public Porte(Mur m, int l, int t, int r, int b){
        mur = m;
        left = l;
        top = t;
        right = r;
        bottom = b;
        pieceSuivante = null;
    }

    /**
     * Cette méthode permet de set la pièce vers laquelle mène la porte. Si il n'y a qu'une porte sur le mur opposé de
     * la pièce suivante, set automatique de la pièce suivante comme étant this.
     * @param p La pièce suivante.
     */
    public void setPieceSuivante(Piece p) {
        if (p == null){
            pieceSuivante = null;
        } else {
            pieceSuivante = p;

            /*Modifie automatiquement les portes du même mur que this de la pièce suivante si il n'y a qu'une porte*/
            if (p != null) {
                if (mur.getOrientation().equals("N")) {
                    if (p.getMurSud() != null && p.getMurSud().getListePortes().size() == 1) {
                        for (Porte po : p.getMurSud().getListePortes()) {
                            if (po.pieceSuivante == null) {
                                po.setPieceSuivante(this.mur.getPiece());
                                break;
                            }
                        }
                    }
                }
                if (mur.getOrientation().equals("S")) {
                    if (p.getMurNord() != null && p.getMurNord().getListePortes().size() == 1) {
                        for (Porte po : p.getMurNord().getListePortes()) {
                            if (po.pieceSuivante == null) {
                                po.setPieceSuivante(this.mur.getPiece());
                                break;
                            }
                        }
                    }
                }
                if (mur.getOrientation().equals("E")) {
                    if (p.getMurOuest() != null && p.getMurOuest().getListePortes().size() == 1) {
                        for (Porte po : p.getMurOuest().getListePortes()) {
                            if (po.pieceSuivante == null) {
                                po.setPieceSuivante(this.mur.getPiece());
                                break;
                            }
                        }
                    }
                }
                if (mur.getOrientation().equals("O")) {
                    if (p.getMurEst() != null && p.getMurEst().getListePortes().size() == 1) {
                        for (Porte po : p.getMurEst().getListePortes()) {
                            if (po.pieceSuivante == null) {
                                po.setPieceSuivante(this.mur.getPiece());
                                break;
                            }
                        }
                    }
                }
            }
        }

    }

    /**
     * Cette méthode permet d'afficher les attributs de la classe pour le débugg.
     * @return Un string avec les attributs de la classe.
     */
    @Override
    public String toString() {
        if (pieceSuivante == null){
            return "Porte{pieceSuivante=null";
        }
        return "Porte{" +
                "pieceSuivante=" + pieceSuivante.getNom() +
                "}\n";
    }

    /**
     * Cette méthode permet de récupérer la pièce suivante.
     * @return la pièce suivante.
     */
    public Piece getPieceSuivante(){
        return pieceSuivante;
    }

    /**
     * Cette méthode permet de récupérer le point x en haut à gauche.
     * @return le point x en haut à gauche.
     */
    public int getLeft() {
        return left;
    }
    /**
     * Cette méthode permet de récupérer le point y en haut à gauche.
     * @return le point y en haut à gauche.
     */
    public int getTop() {
        return top;
    }
    /**
     * Cette méthode permet de récupérer le point x en bas à droite.
     * @return le point x en bas à droite.
     */
    public int getRight() {
        return right;
    }
    /**
     * Cette méthode permet de récupérer le point y en bas à droite.
     * @return le point y en bas à droite.
     */
    public int getBottom() {
        return bottom;
    }

    /**
     * Cette méthode permet de récupérer le mur contenant la porte.
     * @return le mur contenant la porte.
     */
    public Mur getMur() {
        return mur;
    }

    /**
     * Cette méthode permet de vérifier que la porte a été construite correctement c'est à dire qu'elle contient bien
     * une pièce suivante, que la pièce suivante contient bien un mur opposé à celui de this et qu'il contient bien
     * une porte vers la pièce où this est située.
     * @return  1 si la pièce suivante n'a pas de mur, 2 si le mur opposé de la pièce suivante n'a pas de portes,
     * 3 si la pièce suivante n'a pas de portes vers la pièce contenant this, 4 si la pièce suivante n'a pas de mur opposé,
     * 0 si tout est ok.
     */
    public int porteEstOK(){
        if (pieceSuivante == null){
            return 1;
        } else {
            if (this.getMur().getOrientation().equals("N")){
                if (pieceSuivante.getMurSud() == null){
                    return 4;
                }
                if (pieceSuivante.getMurSud().getListePortes().size() == 0){
                    return 2;
                } else {
                    int liee = 3;
                    Piece p = this.getMur().getPiece();
                    for (Porte pS : pieceSuivante.getMurSud().getListePortes()){
                        if (pS.pieceSuivante != null) {
                            if (pS.pieceSuivante.getId().equals(p.getId())) {
                                liee = 0;
                            }
                        }
                    }
                    return liee;
                }
            } else if (this.getMur().getOrientation().equals("S")){
                if (pieceSuivante.getMurNord() == null){
                    return 4;
                }
                if (pieceSuivante.getMurNord().getListePortes().size() == 0){
                    return 2;
                } else {
                    int liee = 3;
                    Piece p = this.getMur().getPiece();
                    for (Porte pS : pieceSuivante.getMurNord().getListePortes()){
                        if (pS.pieceSuivante != null) {
                            if (pS.pieceSuivante.getId().equals(p.getId())) {
                                liee = 0;
                            }
                        }
                    }
                    return liee;
                }
            }else if (this.getMur().getOrientation().equals("O")) {
                if (pieceSuivante.getMurEst() == null){
                    return 4;
                }
                if (pieceSuivante.getMurEst().getListePortes().size() == 0) {
                    return 2;
                } else {
                    int liee = 3;
                    Piece p = this.getMur().getPiece();
                    for (Porte pS : pieceSuivante.getMurEst().getListePortes()) {
                        if (pS.pieceSuivante != null) {
                            if (pS.pieceSuivante.getId().equals(p.getId())) {
                                liee = 0;
                            }
                        }
                    }
                    return liee;
                }
            } else if (this.getMur().getOrientation().equals("E")){
                if (pieceSuivante.getMurOuest() == null){
                    return 4;
                }
                if (pieceSuivante.getMurOuest().getListePortes().size() == 0) {
                    return 2;
                } else {
                    int liee = 3;
                    Piece p = this.getMur().getPiece();
                    for (Porte pS : pieceSuivante.getMurOuest().getListePortes()) {
                        if (pS.pieceSuivante != null) {
                            if (pS.pieceSuivante.getId().equals(p.getId())) {
                                liee = 0;
                            }
                        }
                    }
                    return liee;
                }
            }
        }
        return 0;
    }

    /**
     * Cette méthode permet de savoir si la pièce suivante de this est la pièce passée en paramètre.
     * @param p La pièce que l'on veux vérifier.
     * @return true si la pièce suivante de this est p, false sinon.
     */
    public boolean porteVers(Piece p){
        return pieceSuivante == p;
    }
}
