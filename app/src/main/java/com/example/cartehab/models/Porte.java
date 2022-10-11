package com.example.cartehab.models;

import android.graphics.Rect;

public class Porte {
    protected String id;
    protected Rect rectangle;
    protected Mur mur;

    public Porte(Mur m, Rect rect){
        mur = m;
        rectangle = rect;
    }

}
