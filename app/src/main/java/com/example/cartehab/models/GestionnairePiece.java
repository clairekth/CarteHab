package com.example.cartehab.models;

import java.io.Serializable;
import java.util.ArrayList;

public class GestionnairePiece implements Serializable {
    protected ArrayList<Piece> listePieces;
    protected Habitation habitation;

    public GestionnairePiece(Habitation hab){
        habitation = hab;
    }
}
