package com.example.cartehab.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Mur;
import com.example.cartehab.models.Piece;
import com.example.cartehab.models.Porte;
import com.example.cartehab.outils.Globals;
import com.example.cartehab.view.DialogNameCustom;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
/**
 * Classe représentant une activité permettant la création d'une pièce.
 * @author Claire Kurth
 */
public class NewRoomActivity extends AppCompatActivity implements SensorEventListener {
    /**
     * La pièce entrain d'être créer actuellement.
     */
    protected Piece p;
    /**
     * Le mur actuellement affiché.
     */
    protected Mur m;
    /**
     * L'habitation contenant la pièce.
     */
    protected Habitation h;
    /**
     * La liste des portes du mur affiché actuellement sous la forme de boutons.
     */
    protected ArrayList<Button> listeButtonPorte;
    /**
     * Le degré actuel du magnétomètre.
     */
    protected float degree;
    /**
     * ImageView permettant d'afficher la photo du mur actuel.
     */
    protected ImageView wall;
    /**
     * Le layout où sont disposés tous les éléments.
     */
    protected ConstraintLayout layout;
    /**
     * La dernière update.
     */
    protected long lastUpdateTime = 0;
    /**
     * TextView comportant l'orientation actuelle du téléphone.
     */
    protected TextView orientation;

    /**
     * Champ contenant le SensorManager.
     */
    protected SensorManager sensorManager;


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
                    h = Globals.getInstance().getDataHabitation();
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
                            m = new Mur(p, "N",p.getId()+"MUR_NORD" );
                            fos = openFileOutput(p.getId()+"MUR_NORD", MODE_PRIVATE);
                        } else if (orientation().equals("Sud")){
                            m = new Mur(p, "S",p.getId()+"MUR_SUD" );
                            fos = openFileOutput(p.getId()+"MUR_SUD", MODE_PRIVATE);
                        } else if (orientation().equals("Est")){
                            m = new Mur(p, "E",p.getId()+"MUR_EST" );
                            fos = openFileOutput(p.getId()+"MUR_EST", MODE_PRIVATE);
                        } else {
                            m = new Mur(p, "O",p.getId()+"MUR_OUEST" );
                            fos = openFileOutput(p.getId()+"MUR_OUEST", MODE_PRIVATE);
                        }
                        m.setHeurePhoto();
                        m.getMeteo(this);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();

                        Globals.getInstance().setDataHabitation(h);
                        Globals.getInstance().setmData(m);
                        Intent intent = new Intent(NewRoomActivity.this,SelectDoorActivity.class);
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
        setContentView(R.layout.activity_new_room);
        h = Globals.getInstance().getDataHabitation();
        p = new Piece(h.getId());

        TextView roomName = findViewById(R.id.room_name);
        DialogNameCustom.FullNameListener listener = new DialogNameCustom.FullNameListener() {
            @Override
            public void fullNameEntered(String fullName) {
                p.setNom(fullName);
                roomName.setText(p.getNom());
            }
        };
        final DialogNameCustom dialog = new DialogNameCustom(this, listener,h);
        dialog.setCancelable(false);
        dialog.show();


        listeButtonPorte = new ArrayList<>();
        wall = findViewById(R.id.wall);
        layout = findViewById(R.id.layout);
        orientation = findViewById(R.id.orientation);

        Button prendrePhoto = (Button) findViewById(R.id.take_picture);
        prendrePhoto.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager())!=null) {
                launcher.launch(intent);
            }
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(NewRoomActivity.this,sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(NewRoomActivity.this,sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);

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

            set3D();

            lastUpdateTime = System.currentTimeMillis();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
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

            if (p.getMurNord() != null){
                afficherMur(p.getMurNord());

            } else {
                wall.setImageBitmap(null);
            }
        } else if (orientation().equals("Est")){
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
        } else if (orientation().equals("Sud")){
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
     * Méthode onBackPressed. Save dans l'habitation en global & désenregistrement des sensors.
     */
    @Override
    public void onBackPressed(){
        sensorManager.unregisterListener(NewRoomActivity.this);
        h.addPiece(p);
        Globals.getInstance().setDataHabitation(h);

        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
        super.onBackPressed();
    }

    /**
     * Méthode onPause.
     */
    @Override
    protected void onPause(){
        sensorManager.unregisterListener(NewRoomActivity.this);
        super.onPause();
    }

    /**
     * Méthode onResume.
     */
    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(NewRoomActivity.this,sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(NewRoomActivity.this,sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    /**
     * Méthode finish.
     */
    @Override
    public void finish() {
        sensorManager.unregisterListener(NewRoomActivity.this);
        super.finish();
    }
}