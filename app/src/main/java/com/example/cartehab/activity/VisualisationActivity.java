package com.example.cartehab.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Mur;
import com.example.cartehab.models.Piece;
import com.example.cartehab.models.Porte;
import com.example.cartehab.outils.Globals;
import com.example.cartehab.outils.SaveManager;
import com.example.cartehab.view.DialogChooseDestinationRoom;
import com.example.cartehab.view.DialogChooseRoomNext;
import com.example.cartehab.view.GraphDialog;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class VisualisationActivity extends AppCompatActivity {
    protected Piece p;
    protected ArrayList<String> listeHabitation;
    protected TextView roomName;
    protected Habitation h;
    protected ArrayList<Button> listeButtonPorte;
    protected ImageView wall;
    protected ConstraintLayout layout;
    protected TextView orientation;
    private String orientationMur;
    protected Button droit;
    protected Button gauche;
    private String[] ptCardinaux = {"Nord", "Est", "Sud", "Ouest"};
    private int i = 0;
    protected ImageButton goToButton;
    protected boolean gpsActif;
    protected Piece pieceFinale;
    protected TextView instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualisation);

        Intent i = getIntent();
        listeHabitation = (ArrayList<String>) i.getSerializableExtra("Liste");
        roomName = findViewById(R.id.room_name);
        listeButtonPorte = new ArrayList<>();
        wall = findViewById(R.id.wall);
        layout = findViewById(R.id.layout);
        orientation = findViewById(R.id.orientation);
        instructions = findViewById(R.id.instructions_gps);
        instructions.setZ(1);
        orientationMur = "Nord";
        gpsActif = false;

        AlertDialog d = alertOpenHabitation();
        d.show();

        droit = findViewById(R.id.button_droit);
        droit.setOnClickListener(view -> {
            goDroite();
        });

        gauche = findViewById(R.id.button_gauche);
        gauche.setOnClickListener(view -> {
            goGauche();
        });

        goToButton = findViewById(R.id.go_to_button);
        goToButton.setOnClickListener(view -> {
            DialogChooseDestinationRoom.NameRoomNextListener listener = new DialogChooseDestinationRoom.NameRoomNextListener() {
                @Override
                public void nameRoomNext(String fullName) {
                    pieceFinale = h.getPiece(fullName);
                    gpsActif = true;
                    gpsInstruction();
                }
            };
            final DialogChooseDestinationRoom dialog = new DialogChooseDestinationRoom(VisualisationActivity.this, h,p,listener);
            dialog.showAlertDialog();
        });

        ImageButton information = findViewById(R.id.information_button);
        information.setOnClickListener(view -> {
            AlertDialog.Builder bd = new AlertDialog.Builder(this);
            switch (orientationMur) {
                case "Nord" :
                    if (p.getMurNord() != null){
                        bd.setMessage("Heure de la prise de la photo : " + p.getMurNord().getHeurePhoto().toString() +"\n" +
                                "Temps : " + p.getMurNord().getTemps());
                    } else {
                        bd.setMessage("Pas d'informations.");
                    }
                    break;
                case "Ouest":
                    if (p.getMurOuest() != null){
                        bd.setMessage("Heure de la prise de la photo : " + p.getMurOuest().getHeurePhoto().toString() +"\n" +
                                "Temps : " + p.getMurOuest().getTemps());
                    } else {
                        bd.setMessage("Pas d'informations.");
                    }
                    break;
                case "Est":
                    if (p.getMurEst() != null){
                        bd.setMessage("Heure de la prise de la photo : " + p.getMurEst().getHeurePhoto().toString() +"\n" +
                                "Temps : " + p.getMurEst().getTemps());
                    } else {
                        bd.setMessage("Pas d'informations.");
                    }
                    break;
                case "Sud":
                    if (p.getMurSud() != null){
                        bd.setMessage("Heure de la prise de la photo : " + p.getMurSud().getHeurePhoto().toString() +"\n" +
                                "Temps : " + p.getMurSud().getTemps());
                    } else {
                        bd.setMessage("Pas d'informations.");
                    }
                    break;
            }
            AlertDialog alert = bd.create();
            alert.setTitle("Informations sur la photo");
            alert.show();
        });


        Button graph = findViewById(R.id.graph);
        graph.setOnClickListener(view -> {
            GraphDialog g = new GraphDialog(this,h.nomPieceToGraph());
            g.show();
        });
    }



    protected void goDroite(){
        i = (i+1)%4;
        orientationMur = ptCardinaux[i];
        set3D();
        gpsInstruction();
    }

    protected void goGauche(){
        i = (i+3)%4; //i+3 modulo 4 <=> i-1
        orientationMur = ptCardinaux[i];
        set3D();
        gpsInstruction();
    }

    protected void gpsInstruction(){
        if (gpsActif){
            if (p == pieceFinale){
                Toast.makeText(VisualisationActivity.this, "Vous êtes arrivé(e) à destination !", Toast.LENGTH_LONG).show();
                gpsActif = false;
                instructions.setText("");
            } else {
                String instruction = h.indicationGPS(p,pieceFinale);
                instructions.setText(instruction);            }
        }
    }
    protected AlertDialog alertOpenHabitation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.choisissez_habitation_a_ouvrir));
        String[] noms = new String[listeHabitation.size()];
        noms = listeHabitation.toArray(noms);
        final String[] finalNoms = noms;
        builder.setSingleChoiceItems(noms,0, null).setPositiveButton(getResources().getString(R.string.valider), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int n = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                h = SaveManager.open(getApplicationContext(),finalNoms[n]);
                if (h.getListePieces().size() == 0){
                    Toast.makeText(VisualisationActivity.this,"Pas de pièces de créées.",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    p = h.getListePieces().get(0);
                    roomName.setText(p.getNom());
                    set3D();
                }

            }
        });

        builder.setNegativeButton(getResources().getString(R.string.annuler), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        return builder.create();
    }


    public void set3D(){
        if (orientationMur.equals("Nord")){
            orientation.setText(getResources().getString(R.string.nord));
            for (Button b : listeButtonPorte){
                layout.removeView(b);
            }
            listeButtonPorte.clear();

            if (p.getMurNord() != null){
                afficherMur(p.getMurNord());

            } else {
                wall.setImageBitmap(null);
            }
        } else if (orientationMur.equals("Est")){
            orientation.setText(getResources().getString(R.string.est));

            for (Button b : listeButtonPorte){
                layout.removeView(b);
            }
            listeButtonPorte.clear();

            if (p.getMurEst() != null){
                afficherMur(p.getMurEst());

            } else {
                wall.setImageBitmap(null);

            }
        } else if (orientationMur.equals("Sud")){
            orientation.setText(getResources().getString(R.string.sud));

            for (Button b : listeButtonPorte){
                layout.removeView(b);
            }
            listeButtonPorte.clear();
            if (p.getMurSud() != null){
                afficherMur(p.getMurSud());

            } else {
                wall.setImageBitmap(null);

            }
        } else {
            orientation.setText(getResources().getString(R.string.ouest));
            for (Button b : listeButtonPorte){
                layout.removeView(b);
            }
            listeButtonPorte.clear();
            if (p.getMurOuest() != null){
                afficherMur(p.getMurOuest());
            } else {
                wall.setImageBitmap(null);
            }
        }
    }

    public void afficherMur(Mur m){
        FileInputStream fis = null;
        try {
            fis = openFileInput(m.getId());
            Bitmap bp = BitmapFactory.decodeStream(fis);
            wall.setImageBitmap(bp);
            for (Porte porte : m.getListePortes()){
                Button b = new Button(this);
                b.setX(porte.getLeft());
                b.setY(porte.getTop());
                b.setHeight(porte.getBottom() - porte.getTop());
                b.setWidth(porte.getRight() - porte.getLeft());
                //Log.i("Porte", "NewRoom : " + p.toString());

                if (porte.getPieceSuivante() == null){
                    b.setText(getResources().getString(R.string.piece_suivante_non_creee));
                } else {
                    b.setText(porte.getPieceSuivante().getNom());
                }
                b.setBackgroundColor(Color.argb(60,50,156,123));
                b.setOnClickListener(viewB -> {
                    if (porte.getPieceSuivante() != null) {
                        p = porte.getPieceSuivante();
                        roomName.setText(p.getNom());
                        set3D();
                        gpsInstruction();
                    }
                });
                listeButtonPorte.add(b);
                layout.addView(b);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}