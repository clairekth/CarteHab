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
import com.example.cartehab.view.DialogChooseRoomModification;
import com.example.cartehab.view.DialogChooseRoomNext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ModificationRoomActivity extends AppCompatActivity implements SensorEventListener {
    protected Habitation hab;
    protected Piece piece;
    protected Mur m;
    protected ArrayList<Button> listeButtonPorte;
    protected float degree;
    protected ImageView wall;
    protected ConstraintLayout layout;
    protected long lastUpdateTime = 0;
    protected SensorManager sensorManager;
    protected TextView roomName;
    private boolean dialogDismiss;


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

    final ActivityResultLauncher<Intent> launcherSelectDoor = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    m = (Mur) result.getData().getSerializableExtra("Mur");
                    piece.setMur(m);
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
                            m = new Mur(piece, "N",piece.getNom()+"MUR_NORD" );
                            fos = openFileOutput(piece.getNom()+"MUR_NORD", MODE_PRIVATE);
                        } else if (orientation().equals("Sud")){
                            m = new Mur(piece, "S",piece.getNom()+"MUR_SUD" );
                            fos = openFileOutput(piece.getNom()+"MUR_SUD", MODE_PRIVATE);
                        } else if (orientation().equals("Est")){
                            m = new Mur(piece, "E",piece.getNom()+"MUR_EST" );
                            fos = openFileOutput(piece.getNom()+"MUR_EST", MODE_PRIVATE);
                        } else {
                            m = new Mur(piece, "O",piece.getNom()+"MUR_OUEST" );
                            fos = openFileOutput(piece.getNom()+"MUR_OUEST", MODE_PRIVATE);
                        }
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();


                        Intent intent = new Intent(ModificationRoomActivity.this,SelectDoorActivity.class);
                        intent.putExtra("Mur",m);
                        intent.putExtra("Hab", hab);
                        launcherSelectDoor.launch(intent);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification_room);
        Intent i= getIntent();
        hab = (Habitation) i.getSerializableExtra("hab");

        roomName = findViewById(R.id.room_name);
        listeButtonPorte = new ArrayList<>();
        wall = findViewById(R.id.wall);
        layout = findViewById(R.id.layout);

        dialogDismiss = false;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorMagnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(ModificationRoomActivity.this,sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(ModificationRoomActivity.this,sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);

        DialogChooseRoomModification.NameRoomToModifyListener listener = new DialogChooseRoomModification.NameRoomToModifyListener() {
            @Override
            public void nameRoomToModify(String fullName) {
                setPiece(fullName);
                roomName.setText(fullName);
                dialogDismiss = true;
            }
        };
        final DialogChooseRoomModification dialog = new DialogChooseRoomModification(ModificationRoomActivity.this, hab,listener);
        dialog.showAlertDialog();



        Button prendrePhoto = (Button) findViewById(R.id.take_picture);
        prendrePhoto.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager())!=null) {
                launcher.launch(intent);
            }

        });

        Button modification = (Button) findViewById(R.id.modification_button);
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
                Toast.makeText(ModificationRoomActivity.this, "Impossible de modifier. Prenez d'abord une photo.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ModificationRoomActivity.this,SelectDoorActivity.class);
                intent.putExtra("Mur",m);
                intent.putExtra("Hab", hab);
                launcherSelectDoor.launch(intent);
            }
        });

    }

    private void setPiece(String name){
        piece = hab.getPiece(name);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
            //Log.i("COmpass", "" + d);

            ImageView compass = findViewById(R.id.compass);
            compass.setRotation(degree);
            if (dialogDismiss){
                set3D();
            }
            lastUpdateTime = System.currentTimeMillis();
        }

    }

    @Override
    protected void onPause(){
        sensorManager.unregisterListener(ModificationRoomActivity.this);
        Intent data = new Intent();
        data.putExtra("Piece", piece);
        setResult(RESULT_OK, data);
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(ModificationRoomActivity.this,sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(ModificationRoomActivity.this,sensorMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public String orientation(){
        if (degree < 45 && degree > -45){
            return "Nord";
        } else if (degree > 45 && degree < 135){
            return "Ouest";
        } else if (degree < -45 && degree > -135){
            return "Est";
        }
        return "Sud";
    }

    public void set3D(){
        if (orientation().equals("Nord")){
            for (Button b : listeButtonPorte){
                layout.removeView(b);
            }
            listeButtonPorte.clear();

            if (piece.getMurNord() != null){
                afficherMur(piece.getMurNord());
            }
        } else if (orientation().equals("Est")){
            for (Button b : listeButtonPorte){
                layout.removeView(b);
            }
            listeButtonPorte.clear();

            if (piece.getMurEst() != null){
                afficherMur(piece.getMurEst());

            } else {
                wall.setImageBitmap(null);
            }
        } else if (orientation().equals("Sud")){
            for (Button b : listeButtonPorte){
                layout.removeView(b);
            }
            listeButtonPorte.clear();
            if (piece.getMurSud() != null){
                afficherMur(piece.getMurSud());

            } else {
                wall.setImageBitmap(null);
            }
        } else {
            for (Button b : listeButtonPorte){
                layout.removeView(b);
            }
            listeButtonPorte.clear();
            if (piece.getMurOuest() != null){
                afficherMur(piece.getMurOuest());

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
            for (Porte p : m.getListePortes()){
                Button b = new Button(this);
                b.setX(p.getLeft());
                b.setY(p.getTop());
                b.setHeight(p.getBottom() - p.getTop());
                b.setWidth(p.getRight() - p.getLeft());
                Log.i("Porte", "NewRoom : " + p.toString());

                if (p.getPieceSuivante() == null){
                    b.setText("Pièce suivante non créée.");
                } else {
                    b.setText(p.getPieceSuivante().getNom());
                }
                b.setBackgroundColor(Color.argb(60,50,156,123));
                b.setOnClickListener(viewB -> {
                    if (p.getPieceSuivante() != null) {
                        Toast.makeText(ModificationRoomActivity.this, p.getPieceSuivante().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ModificationRoomActivity.this, "Pas de pièces suivantes", Toast.LENGTH_SHORT).show();
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
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Piece", piece);
        setResult(RESULT_OK, data);
        super.finish();
    }

}