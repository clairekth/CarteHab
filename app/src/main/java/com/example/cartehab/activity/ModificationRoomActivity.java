package com.example.cartehab.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
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
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.cartehab.view.AdapterListRoom;
import com.example.cartehab.view.DialogNameCustom;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe représentant une activité permettant de modifier une pièce.
 * @author Claire Kurth
 */
public class ModificationRoomActivity extends AppCompatActivity implements SensorEventListener {
    /**
     * L'habitation entrain d'être construite.
     */
    protected Habitation hab;
    /**
     * La pièce entrain d'être modifier.
     */
    protected Piece piece;
    /**
     * Le mur affiché actuellement.
     */
    protected Mur m;
    /**
     * La liste des portes du mur affiché actuellement sous forme de bouton.
     */
    protected ArrayList<Button> listeButtonPorte;
    /**
     * Le degré actuel du magnétomètre.
     */
    protected float degree;
    /**
     * L'imageView contenant la photo du mur actuel.
     */
    protected ImageView wall;
    /**
     * Le layout contenant les divers éléments.
     */
    protected ConstraintLayout layout;
    /**
     * Le dernier temps d'update.
     */
    protected long lastUpdateTime = 0;
    /**
     * Le manageur des sensors.
     */
    protected SensorManager sensorManager;
    /**
     * TextView permettant d'afficher le nom de la pièce.
     */
    protected TextView roomName;
    /**
     * booléan permettant de savoir si le dialog a était fermé ou non.
     */
    private boolean dialogDismiss;
    /**
     * TextView permettant d'afficher l'orientation actuelle du téléphone.
     */
    protected TextView orientation;
    /**
     * Bouton permettant de modifier les portes.
     */
    protected Button modification;
    /**
     * ImageButton qui permet d'afficher dans une alert tous les problèmes de la pièce.
     */
    protected ImageButton attention;
    /**
     * Booléan permettant de savoir si l'utilisateur a demandé à ce que la pièce soit supprimé.
     */
    protected boolean pieceASuppr;

    /**
     * Champ contenant le capteur d'accélération.
     */
    protected Sensor sensorAccelerometer;

    /**
     * Champ contenant le capteur magnétique.
     */
    protected Sensor sensorMagnetic;

    /**
     * Champ contenant un tableau qui contient les données du capteur d'accélération.
     */
    private final float[] accelerometerData = new float[3];

    /**
     * Champ contenant un tableau qui contient les données du capteur magnétique.
     */
    private final float[] magnetometerData = new float[3];

    /**
     * Champ contenant un tableau qui contient la matrice de rotation.
     */
    private final float[] rotationMatrix = new float[9];

    /**
     * Champ contenant un tableau qui contient l'orientation des angles (-z, x , y).
     */
    private final float[] orientationAngles = new float[3];

    /**
     * Launcher attendant le résuldat de l'activité SelectDoorActivity déclenchée lorsqu'une photo a été prise.
     */
    final ActivityResultLauncher<Intent> launcherSelectDoor = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    hab= Globals.getInstance().getDataHabitation();
                    if (piece.pieceEstOK()){
                        attention.setVisibility(View.INVISIBLE);
                    }
                }
            });


    /**
     * Champ du launcher de résultat pour l'activité lorsque l'on a pris une photo.
     */
    final ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Bundle extras = null;
                if (result.getData() != null) {
                    extras = result.getData().getExtras();
                }
                if (extras != null) {
                    Bitmap bmp = (Bitmap) extras.get("data");
                    try {
                        FileOutputStream fos = null;
                        if (orientation().equals("Nord")){
                            m = new Mur(piece, "N",piece.getId()+"MUR_NORD" );
                            fos = openFileOutput(piece.getId()+"MUR_NORD", MODE_PRIVATE);
                        } else if (orientation().equals("Sud")){
                            m = new Mur(piece, "S",piece.getId()+"MUR_SUD" );
                            fos = openFileOutput(piece.getId()+"MUR_SUD", MODE_PRIVATE);
                        } else if (orientation().equals("Est")){
                            m = new Mur(piece, "E",piece.getId()+"MUR_EST" );
                            fos = openFileOutput(piece.getId()+"MUR_EST", MODE_PRIVATE);
                        } else {
                            m = new Mur(piece, "O",piece.getId()+"MUR_OUEST" );
                            fos = openFileOutput(piece.getId()+"MUR_OUEST", MODE_PRIVATE);
                        }
                        m.setHeurePhoto();
                        m.getMeteo(this);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();

                        SaveManager.getInstance().save(getApplicationContext(),hab);

                        Intent intent = new Intent(ModificationRoomActivity.this,SelectDoorActivity.class);
                        Globals.getInstance().setDataHabitation(hab);
                        Globals.getInstance().setmData(m);
                        launcherSelectDoor.launch(intent);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });


    /**
     * Méthode onCreate.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_room);
        pieceASuppr = false;
        hab = Globals.getInstance().getDataHabitation();
        roomName = findViewById(R.id.room_name);
        listeButtonPorte = new ArrayList<>();
        wall = findViewById(R.id.wall);
        layout = findViewById(R.id.layout);
        orientation = findViewById(R.id.orientation);
        modification = findViewById(R.id.modification_button);

        dialogDismiss = false;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(ModificationRoomActivity.this,sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(ModificationRoomActivity.this,sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);

        /*--AlertDialog qui permet de choisir la pièce que l'on veut modifier--*/
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        AdapterListRoom adapter = new AdapterListRoom(ModificationRoomActivity.this,hab.getListePieces());
        alert.setTitle(getResources().getString(R.string.choisissez_piece_a_modifier));
        alert.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.setCancelable(false);
        alert.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Piece p = adapter.getListePieces(which);
                setPiece(p);
                roomName.setText(p.getNom());
                dialogDismiss = true;
                dialog.dismiss();
            }
        });

        alert.setNegativeButton(getResources().getString(R.string.annuler), (dialog, which) -> {
            dialog.cancel();
            finish();
        });
        alert.show();
        /*----------------------------------------------------------*/

        Button prendrePhoto = (Button) findViewById(R.id.take_picture);
        prendrePhoto.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager())!=null) {
                launcher.launch(intent);
            }

        });

        modification.setOnClickListener(view -> {
            if (orientation().equals("Nord")){
                m = piece.getMurNord();
            } else if (orientation().equals("Sud")){
                m = piece.getMurSud();
            } else if (orientation().equals("Est")){
                m = piece.getMurEst();
            } else {
                m = piece.getMurOuest();
            }

            if (m == null) {
                Toast.makeText(ModificationRoomActivity.this, getResources().getString(R.string.impossible_modifier_mur), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ModificationRoomActivity.this,SelectDoorActivity.class);
                Globals.getInstance().setmData(m);
                Globals.getInstance().setDataHabitation(hab);
                launcherSelectDoor.launch(intent);
            }
        });

        attention = findViewById(R.id.button_attention);
        attention.setVisibility(View.INVISIBLE);



    }

    /**
     * Cette méthode permet de set la pièce actuelle après sa sélection par l'utilisateur.
     * @param p La pièce à set.
     */
    private void setPiece(Piece p){
        piece = p;
        if (!piece.pieceEstOK()) {
            attention.setVisibility(View.VISIBLE);
            attention.setOnClickListener(view -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(ModificationRoomActivity.this);
                builder.setMessage(piece.erreurs());
                builder.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog alertErrorRoom = builder.create();
                alertErrorRoom.show();
            });
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * Méthode qui change les données stockées de l'accéléromètre et magnétique en fonction des évènements.
     * Elle change aussi l'affichage en fonction des données récoltées.
     * @param event Evenement
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        /*Mise à jour des data de l'accéléromètre et du magnétomère*/
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerData, 0, accelerometerData.length);
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerData, 0, magnetometerData.length);
        }

        if (System.currentTimeMillis() - lastUpdateTime > 50) {
            SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerData, magnetometerData);
            SensorManager.getOrientation(rotationMatrix, orientationAngles);
            degree = (float) Math.toDegrees(-orientationAngles[0]);

            ImageView compass = findViewById(R.id.compass);
            compass.setRotation(degree);

            //Permet d'avoir un degree un peu plus stable
            degree = (float) Math.toDegrees((orientationAngles[0] + Math.PI*2) % (Math.PI*2));
            if (dialogDismiss){
                set3D();
            }
            lastUpdateTime = System.currentTimeMillis();
        }

    }


    /**
     * Cette méthode permet de récupérer l'orientation actuelle du téléphone.
     * @return l'orientation actuelle du téléphone.
     */
    public String orientation(){
        if (degree < 90) {
            return "Nord";
        } else if (degree >=90 && degree < 180) {
            return "Est";
        } else if (degree >=180 && degree < 270){
            return "Sud";
        }
        return "Ouest";
    }

    /**
     * Cette méthode permet de set le visuel lié à l'orientation du téléphone.
     */
    public void set3D(){
        if (orientation().equals("Nord")){
            orientation.setText(getResources().getString(R.string.nord));

            for (Button b : listeButtonPorte){
                layout.removeView(b);
            }
            listeButtonPorte.clear();

            if (piece.getMurNord() != null){
                afficherMur(piece.getMurNord());
                modification.setEnabled(true);
            } else {
                wall.setImageBitmap(null);
                modification.setEnabled(false);

            }
        } else if (orientation().equals("Est")){
            orientation.setText(getResources().getString(R.string.est));

            for (Button b : listeButtonPorte){
                layout.removeView(b);
            }
            listeButtonPorte.clear();

            if (piece.getMurEst() != null){
                afficherMur(piece.getMurEst());
                modification.setEnabled(true);

            } else {
                wall.setImageBitmap(null);
                modification.setEnabled(false);

            }
        } else if (orientation().equals("Sud")){
            orientation.setText(getResources().getString(R.string.sud));

            for (Button b : listeButtonPorte){
                layout.removeView(b);
            }
            listeButtonPorte.clear();
            if (piece.getMurSud() != null){
                afficherMur(piece.getMurSud());
                modification.setEnabled(true);


            } else {
                wall.setImageBitmap(null);
                modification.setEnabled(false);

            }
        } else {
            orientation.setText(getResources().getString(R.string.ouest));

            for (Button b : listeButtonPorte){
                layout.removeView(b);
            }
            listeButtonPorte.clear();
            if (piece.getMurOuest() != null){
                afficherMur(piece.getMurOuest());
                modification.setEnabled(true);

            } else {
                wall.setImageBitmap(null);
                modification.setEnabled(false);

            }
        }
    }

    /**
     * Cette méthode permet d'afficher la photo et les portes du mur passé en paramètre.
     * @param m le mur a affiché.
     */
    public void afficherMur(Mur m){
        FileInputStream fis = null;
        try {
            fis = openFileInput(m.getId());
            Bitmap bp = BitmapFactory.decodeStream(fis);
            wall.setImageBitmap(bp);
            for (Porte p : m.getListePortes()){
                Button b = new Button(this);
                b.setX(p.getLeft());
                b.setY(p.getTop());
                b.setHeight(p.getBottom() - p.getTop());
                b.setWidth(p.getRight() - p.getLeft());

                if (p.getPieceSuivante() == null){
                    b.setText(getResources().getString(R.string.piece_suivante_non_creee));
                } else {
                    b.setText(p.getPieceSuivante().getNom());
                }
                b.setBackgroundColor(Color.argb(60,50,156,123));
                listeButtonPorte.add(b);
                layout.addView(b);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode de création du menu.
     * @param menu Le menu.
     * @return true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_modification_room,menu);
        return true;
    }

    /**
     * Méthode qui gère les actions des items du menu.
     * @param item Les items du menu.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modifier_nom:
                DialogNameCustom.FullNameListener listener = new DialogNameCustom.FullNameListener() {
                    @Override
                    public void fullNameEntered(String fullName) {
                        piece.setNom(fullName);
                        roomName.setText(piece.getNom());
                    }
                };
                final DialogNameCustom dialog = new DialogNameCustom(this, listener,hab);
                dialog.show();
                return true;
            case R.id.delete_piece:
                piece.suppressionSiEstUnePieceSuivante();
                hab.remove(piece);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Méthode onBackPressed. Save dans l'habitation en global & désenregistrement des sensors.
     */
    @Override
    public void onBackPressed(){
        sensorManager.unregisterListener(ModificationRoomActivity.this);
        Globals.getInstance().setDataHabitation(hab);
        Intent data = new Intent();
        setResult(RESULT_OK, data);

        super.onBackPressed();
    }

    /**
     * Méthode onPause.
     */
    @Override
    protected void onPause(){
        sensorManager.unregisterListener(ModificationRoomActivity.this);
        super.onPause();
    }

    /**
     * Méthode onResume.
     */
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(ModificationRoomActivity.this,sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(ModificationRoomActivity.this,sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Méthode finish.
     */
    @Override
    public void finish() {
        sensorManager.unregisterListener(ModificationRoomActivity.this);
        Globals.getInstance().setDataHabitation(hab);
        Intent data = new Intent();
        setResult(RESULT_OK, data);

        super.finish();
    }

}