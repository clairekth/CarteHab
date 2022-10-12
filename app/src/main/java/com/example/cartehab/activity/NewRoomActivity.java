package com.example.cartehab.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Mur;
import com.example.cartehab.models.Piece;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewRoomActivity extends AppCompatActivity {
    protected Piece p;
    protected Mur m;
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
                        FileOutputStream fos = openFileOutput("image.png", MODE_PRIVATE);
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.flush();

                        m = new Mur(p, "image.png", "N");
                        Intent intent = new Intent(NewRoomActivity.this,SelectDoorActivity.class);
                        intent.putExtra("Mur",m);
                        startActivityForResult(intent,0);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 0) {
            m = (Mur) data.getSerializableExtra("Mur");
            Log.i("Mur", m.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_room);
        Intent i = getIntent();
        Habitation h = (Habitation) i.getSerializableExtra("hab");
        p = new Piece(h);

        Button prendrePhoto = (Button) findViewById(R.id.take_picture);
        prendrePhoto.setOnClickListener(view -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager())!=null) {
                launcher.launch(intent);
            }

        });


        ImageView wall = findViewById(R.id.wall);
        Button afficher = (Button) findViewById(R.id.print_picture);
        afficher.setOnClickListener(view -> {
            FileInputStream fis = null;
            try {
                fis = openFileInput("image.png");
                Bitmap bp = BitmapFactory.decodeStream(fis);
                wall.setImageBitmap(bp);
                Log.i("Mur", m.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        });



    }
}