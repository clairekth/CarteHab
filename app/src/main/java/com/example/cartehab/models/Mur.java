package com.example.cartehab.models;

import android.graphics.Bitmap;

public class Mur {
    protected String id;
    protected Bitmap photo;
    protected Piece piece;
    protected String orientation;

    public Mur(Piece p, Bitmap ph, String o){
        piece = p;
        photo = ph;
        orientation = o;
    }

}
