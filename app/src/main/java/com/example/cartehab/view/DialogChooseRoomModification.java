package com.example.cartehab.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Mur;

import java.util.HashSet;
import java.util.Set;

public class DialogChooseRoomModification extends Dialog {
    protected Habitation habitation;
    protected NameRoomToModifyListener listener;
    protected Context context;
    public interface NameRoomToModifyListener {
        void nameRoomToModify(String fullName);
    }

    public DialogChooseRoomModification(Context c, Habitation hab, DialogChooseRoomModification.NameRoomToModifyListener lis) {
        super(c);
        habitation = hab;
        listener = lis;
        context = c;
    }

    public void showAlertDialog()  {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        builder.setTitle("Selectionné la pièce suivante");
        final String[] pieces = new String[habitation.getListePieces().size()];
        int i =0;
        for (String p : habitation.getListePieces().keySet()){
            pieces[i] = p;
            i++;

        }


        int checkedItem = 0; // Sheep
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
        builder.setCancelable(false);

        // Create "Yes" button with OnClickListener.
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(selectedItems.isEmpty()) {
                    return;
                }
                String piece = selectedItems.iterator().next();
                dialog.dismiss();

                if (listener != null){
                    listener.nameRoomToModify(piece);
                }

            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


}
