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

    public DialogChooseRoomNext(Activity a, Habitation hab, Mur m) {
        activity = a;
        habitation = hab;
        mur = m;
    }

    public void showAlertDialog()  {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);

        builder.setTitle("Selectionné la pièce suivante");
        final String[] pieces = new String[habitation.getListePieces().size() + 1];
        int i =0;
        for (Piece p : habitation.getListePieces()){
            if (! p.getNom().equals(mur.getPiece().getNom())){
                pieces[i] = p.getNom();
                i++;
            }
        }
        pieces[i] = "Pièce non créée";
        // Add a list
       // final String[] animals = {"Horse", "Cow", "Camel", "Sheep", "Goat"};

        int checkedItem = i; // Sheep
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

        //
        builder.setCancelable(true);

        // Create "Yes" button with OnClickListener.
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(selectedItems.isEmpty()) {
                    return;
                }
                String piece = selectedItems.iterator().next();

                // Close Dialog
                dialog.dismiss();
                // Do something, for example: Call a method of Activity...
                Toast.makeText(activity,"You select " + piece,
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Create "Cancel" button with OnClickListener.
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(activity,"You choose Cancel button",
                        Toast.LENGTH_SHORT).show();
                //  Cancel
                dialog.cancel();
            }
        });

        // Create AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }

}