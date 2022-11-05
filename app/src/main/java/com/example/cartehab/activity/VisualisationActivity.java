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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class VisualisationActivity extends AppCompatActivity implements SensorEventListener {
    protected Piece p;
    protected ArrayList<String> listeHabitation;
    protected TextView roomName;
    protected Mur m;
    protected Habitation h;
    protected ArrayList<Button> listeButtonPorte;
    protected float degree;
    protected ImageView wall;
    protected ConstraintLayout layout;
    protected long lastUpdateTime = 0;
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

    private boolean dialogDismiss;

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
        dialogDismiss = false;

        AlertDialog d = alertOpenHabitation();
        d.show();


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(VisualisationActivity.this,sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(VisualisationActivity.this,sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);

    }


    protected AlertDialog alertOpenHabitation(){
        Log.i("TEST","CC");
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
                    Toast.makeText(VisualisationActivity.this,"NN",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    p = h.getListePieces().get(0);
                    roomName.setText(p.getNom());
                    dialogDismiss = true;
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
            //Log.i("COmpass", "" + d);

            //Permet d'avoir un degree un peu plus stable
            degree = (float) Math.toDegrees((orientationAngles[0] + Math.PI*2) % (Math.PI*2));
            if (dialogDismiss) {
                set3D();
            }
            //Log.i("ORIEN", orientation() + " : " + degree);

            lastUpdateTime = System.currentTimeMillis();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public String orientation(){
        /*if (degree < 45 && degree >= -45){
            return "Nord";
        } else if (degree >= 45 && degree < 135){
            return "Ouest";
        } else if (degree < -45 && degree >= -135){
            return "Est";
        }
        return "Sud";*/

        if (degree < 90) {
            return "Nord";
        } else if (degree >=90 && degree < 180) {
            return "Est";
        } else if (degree >=180 && degree < 270){
            return "Sud";
        }
        return "Ouest";
    }

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
                    }
                });
                listeButtonPorte.add(b);
                layout.addView(b);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed(){
        sensorManager.unregisterListener(VisualisationActivity.this);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onPause(){
        sensorManager.unregisterListener(VisualisationActivity.this);

        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(VisualisationActivity.this,sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(VisualisationActivity.this,sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void finish() {

        super.finish();
    }
}