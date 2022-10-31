package com.example.cartehab.outils;

import android.util.Log;

import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Mur;

public class Globals {
    private static final Globals instance = new Globals();
    private Habitation data;
    private Mur mData;
    private Globals(){}
    public static Globals getInstance() {
        return instance;
    }
    public void setDataHabitation(Habitation data){
        this.data = data;
    }
    public Habitation getDataHabitation(){
        return data;
    }

    public void setmData(Mur m){
        Log.i("GLOBALS", "SET");
        mData = m;
    }
    public Mur getmData(){
        Log.i("GLOBALS", "GET");

        return mData;
    }

}
