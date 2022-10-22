package com.example.cartehab.models;

import android.graphics.Rect;

import java.io.Serializable;

public class Porte implements Serializable {
    protected int left, top, right, bottom;
    protected Mur mur;
    protected Piece pieceActuelle;
    protected Piece pieceSuivante;

    public Porte(Mur m, int l, int t, int r, int b){
        mur = m;
        left = l;
        top = t;
        right = r;
        bottom = b;
        pieceActuelle = m.getPiece();
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
                ", pieceActuelle=" + pieceActuelle +
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

    public Piece getPieceActuelle(){
        return pieceActuelle;
    }
}
