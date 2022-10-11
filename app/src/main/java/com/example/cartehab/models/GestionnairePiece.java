package com.example.cartehab.models;

import java.util.ArrayList;

public class GestionnairePiece {
    protected ArrayList<Piece> listePieces;
    protected Habitation habitation;

    public GestionnairePiece(Habitation hab){
        habitation = hab;
    }
}
