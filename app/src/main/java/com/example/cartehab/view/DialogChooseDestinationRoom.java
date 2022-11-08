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

public class DialogChooseDestinationRoom{
    protected Activity activity;
    protected Habitation habitation;
    protected Piece pActuelle;
    protected NameRoomNextListener listener;

    public interface NameRoomNextListener {
        void nameRoomNext(String fullName);
    }

    public DialogChooseDestinationRoom(Activity a, Habitation hab, Piece p, DialogChooseDestinationRoom.NameRoomNextListener lis) {
        activity = a;
        habitation = hab;
        pActuelle = p;
        listener = lis;
    }

    public void showAlertDialog()  {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);

        builder.setTitle("Sélectionnez la pièce de destination : ");
        final String[] pieces = new String[habitation.getListePieces().size() -1];
        int i = 0;
        for (Piece p : habitation.getListePieces()){
            if (p != pActuelle){
                pieces[i] = p.getNom();
                i++;
            }
        }

        int checkedItem = 0;
        final Set<String> selectedItems = new HashSet<String>();
        selectedItems.add(pieces[checkedItem]);

        builder.setSingleChoiceItems(pieces, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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