package com.example.cartehab.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Mur;
import com.example.cartehab.models.Piece;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DialogChooseRoomNext{
    protected Activity activity;
    protected Habitation habitation;
    protected Mur mur;
    protected NameRoomNextListener listener;

    public interface NameRoomNextListener {
        void nameRoomNext(String fullName);
    }

    public DialogChooseRoomNext(Activity a, Habitation hab, Mur m, DialogChooseRoomNext.NameRoomNextListener lis) {
        activity = a;
        habitation = hab;
        mur = m;
        listener = lis;
    }

    public void showAlertDialog()  {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);

        builder.setTitle("Selectionner la pièce suivante");
        final String[] pieces;
        Log.i("Dialog", habitation.toString());
        if (habitation.nomPieceExisteDeja(mur.getPiece().getNom())){ //On check si la pièce où on est actuellement est déjà dans la liste
            pieces = new String[habitation.getListePieces().size()]; //Si oui -> on est dans la modification, il faut l'enlever de la sélection possible
        } else {
            pieces = new String[habitation.getListePieces().size() + 1];//Si non -> on est dans la création; elle n'est donc pas encore dans la liste
        }
        int i =0;
        for (Piece p : habitation.getListePieces().values()){
            if (!(p.getId().equals(mur.getPiece().getId()))){
                pieces[i] = p.getNom();
                i++;
            }
        }

        pieces[i] = "Pièce non créée";


        int checkedItem = 0;
        final Set<String> selectedItems = new HashSet<String>();
        selectedItems.add(pieces[checkedItem]);

        builder.setSingleChoiceItems(pieces, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do Something...
                selectedItems.clear();
                selectedItems.add(pieces[which]);
            }
        });


        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(selectedItems.isEmpty()) {
                    return;
                }
                String piece = selectedItems.iterator().next();
                dialog.dismiss();

                if (listener != null){
                    listener.nameRoomNext(piece);
                }

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}