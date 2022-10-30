package com.example.cartehab.view;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DialogNameCustom extends Dialog {

    public interface FullNameListener {
        void fullNameEntered(String fullName);
    }

    protected EditText textInput;
    protected Button buttonOk;
    protected Context context;
    protected DialogNameCustom.FullNameListener listener;
    protected Habitation hab;
    protected ArrayList<String> nomsH;

    public DialogNameCustom(@NonNull Context context, DialogNameCustom.FullNameListener listener, Habitation h) {
        super(context);
        this.context = context;
        this.listener = listener;
        this.hab = h;
    }

    public DialogNameCustom(@NonNull Context context, DialogNameCustom.FullNameListener listener, ArrayList<String> noms) {
        super(context);
        this.context = context;
        this.listener = listener;
        nomsH = noms;
        hab = null;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_name_custom);
        textInput = findViewById(R.id.text_input);
        buttonOk = findViewById(R.id.ok_button);
        buttonOk.setOnClickListener(view -> {
            String fullName = this.textInput.getText().toString();
            if (hab != null) {
                if (fullName == null || fullName.isEmpty()) {
                    Toast.makeText(this.context, "Entrez le nom d'une pièce", Toast.LENGTH_LONG).show();
                    return;
                }
                if (hab.nomPieceExisteDeja(fullName)) {
                    Toast.makeText(this.context, "Ce nom de pièce existe déjà.", Toast.LENGTH_LONG).show();
                    return;
                }
            } else {

                for (String n : nomsH){
                    if (n.equalsIgnoreCase(fullName)){
                        Toast.makeText(this.context, "Ce nom d'habitation existe déjà.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            this.dismiss(); // Close Dialog

            if(this.listener!= null)  {
                this.listener.fullNameEntered(fullName);
            }

        });
    }
}