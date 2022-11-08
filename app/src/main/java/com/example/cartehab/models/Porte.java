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

    @Override
    public String toString() {
        if (pieceSuivante == null){
            return "Porte{pieceSuivante=null";
        }
        return "Porte{" +
                "pieceSuivante=" + pieceSuivante.getNom() +
                "}\n";
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

    public boolean porteVers(Piece p){
        return pieceSuivante == p;
    }
}
