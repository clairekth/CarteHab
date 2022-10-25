package com.example.cartehab.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Mur;

import java.util.HashSet;
import java.util.Set;

public class DialogChooseRoomModification extends Dialog {
    protected Habitation habitation;
    protected NameRoomToModifyListener listener;
    protected Context context;
    protected Dialog dialog;

    public interface NameRoomToModifyListener {
        void nameRoomToModify(String fullName);
    }

    public DialogChooseRoomModification(Context c, Habitation hab, DialogChooseRoomModification.NameRoomToModifyListener lis) {
        super(c);
        habitation = hab;
        listener = lis;
        context = c;

        setContentView(R.layout.dialog_recycler_view);
        /*RecyclerView recyclerRoom = (RecyclerView) findViewById(R.id.recycler_view);
        AdapterListRoom adapter = new AdapterListRoom(habitation.hashmapToList());
        recyclerRoom.setAdapter(adapter);
        recyclerRoom.setLayoutManager(new LinearLayoutManager(c));*/

        dialog = new Dialog(c);
    }

    public void showAlertDialog()  {
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this.context);

        builder.setTitle("Selectionner la pièce suivante");
        final String[] pieces = new String[habitation.getListePieces().size()];
        int i =0;
        for (String p : habitation.getListePieces().keySet()){
            pieces[i] = p;
            i++;
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
                    listener.nameRoomToModify(piece);
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();*/
        dialog.show();
    }


}
