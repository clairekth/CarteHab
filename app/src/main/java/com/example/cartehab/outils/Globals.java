package com.example.cartehab.outils;


import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Mur;

/**
 * Cette classe permet de stocker en global l'habitation afin de la faire parvenir entre les activités peut importe sa taille.
 * Elle permet aussi de stocker en global un mur.
 * @author Claire Kurth
 */
public class Globals {
    /**
     * L'instance.
     */
    private static final Globals instance = new Globals();
    /**
     * L'habitation.
     */
    private Habitation data;
    /**
     * Le mur.
     */
    private Mur mData;

    /**
     * Constructeur.
     */
    private Globals(){}

    /**
     * Cette méthode permet de retourner l'instance.
     * @return l'instance.
     */
    public static Globals getInstance() {
        return instance;
    }

    /**
     * Cette méthode permet de set l'habitation stockée en global.
     * @param data l'habitation.
     */
    public void setDataHabitation(Habitation data){
        this.data = data;
    }

    /**
     * Cette méthode permet de récupérer l'habitation stockée en global.
     * @return l'habitation.
     */
    public Habitation getDataHabitation(){
        return data;
    }

    /**
     * Cette méthode permet de set le mur stocké en global
     * @param m le mur.
     */
    public void setmData(Mur m){
        mData = m;
    }

    /**
     * Cette méthode permet de récupérer le mur stocké en global.
     * @return le mur.
     */
    public Mur getmData(){
        return mData;
    }

}
