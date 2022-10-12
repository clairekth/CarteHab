package com.example.cartehab.models;

import android.graphics.Rect;

import java.io.Serializable;

public class Porte implements Serializable {
    protected String id;
    protected int left, top, right, bottom;
    protected Mur mur;

    public Porte(Mur m, int l, int t, int r, int b){
        mur = m;
        left = l;
        top = t;
        right = r;
        bottom = b;
    }

}
