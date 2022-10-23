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
        final String[] pieces = new String[habitation.getListePieces().size() + 1];
        int i =0;
        for (String p : habitation.getListePieces().keySet()){
            if (! p.equals(mur.getPiece().getNom())){
                pieces[i] = p;
                i++;
            }
        }
        pieces[i] = "Pièce non créée";


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