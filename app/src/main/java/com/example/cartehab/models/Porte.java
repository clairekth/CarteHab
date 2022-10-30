package com.example.cartehab.models;

import android.graphics.Rect;
import android.util.Log;

import java.io.Serializable;

public class Porte implements Serializable {
    protected int left, top, right, bottom;
    protected Mur mur;
    protected Piece pieceSuivante;

    public Porte(Mur m, int l, int t, int r, int b){
        mur = m;
        left = l;
        top = t;
        right = r;
        bottom = b;
        pieceSuivante = null;
    }

    public void setPieceSuivante(Piece p){
        pieceSuivante = p;
    }

    @Override
    public String toString() {
        return "Porte{" +
                ", left=" + left +
                ", top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", mur=" + mur +
                ", pieceSuivante=" + pieceSuivante +
                '}';
    }

    public Piece getPieceSuivante(){
        return pieceSuivante;
    }


    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public Mur getMur() {
        return mur;
    }

    public int porteEstOK(){
        Log.i("Dia", "O : " + this.getMur().getOrientation());
        if (pieceSuivante == null){
            return 1;
        } else {
            if (this.getMur().getOrientation().equals("N")){
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
                if (pieceSuivante.getMurEst().getListePortes().size() == 0) {
                    return 2;
                } else {
                    int liee = 3;
                    Piece p = this.getMur().getPiece();
                    for (Porte pS : pieceSuivante.getMurEst().getListePortes()) {
                        Log.i("Dia", pS.toString());
                        if (pS.pieceSuivante != null) {
                            if (pS.pieceSuivante.getId().equals(p.getId())) {
                                liee = 0;
                            }
                        }
                    }
                    return liee;
                }
            } else if (this.getMur().getOrientation().equals("E")){
                if (pieceSuivante.getMurOuest().getListePortes().size() == 0) {
                    return 2;
                } else {
                    int liee = 3;
                    Piece p = this.getMur().getPiece();
                    for (Porte pS : pieceSuivante.getMurOuest().getListePortes()) {
                        Log.i("Dia", pS.toString());
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

}
