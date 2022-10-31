package com.example.cartehab.outils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import com.example.cartehab.models.Habitation;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveManager {

    private static final SaveManager instance = new SaveManager();
    private SaveManager() {}
    public static SaveManager getInstance() {
        return instance;
    }

    public static void save(Context context, Habitation hab){
        FileOutputStream fos = null;
        ObjectOutputStream o = null;
        try {
            fos = context.openFileOutput(hab.getName()+".data", MODE_PRIVATE);
            o = new ObjectOutputStream(fos);
            hab.setLastNumPiece(FabriqueNumero.getInstance().getNumeroPieceSansIncre());
            o.writeObject(hab);
            o.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Habitation open(Context context, String name){
        Habitation hab = null;
        try {
            FileInputStream fis =  context.openFileInput(name + ".data");
            ObjectInputStream o = new ObjectInputStream(fis);
            hab = (Habitation) o.readObject();
            FabriqueNumero.getInstance().setCptPiece(hab.getLastNumPiece());
            o.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return hab;
    }


}
