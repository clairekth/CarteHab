package com.example.cartehab.models;

import android.graphics.Rect;

import java.io.Serializable;

public class Porte implements Serializable {
    protected String id;
    protected int left, top, right, bottom;
    protected Mur mur;
    protected Piece piece1;
    protected Piece piece2;

    public Porte(Mur m, int l, int t, int r, int b){
        mur = m;
        left = l;
        top = t;
        right = r;
        bottom = b;
    }

    public String getId() {
        return id;
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
}
