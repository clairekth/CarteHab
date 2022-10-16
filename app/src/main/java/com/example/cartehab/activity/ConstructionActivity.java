package com.example.cartehab.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Mur;
import com.example.cartehab.models.Piece;

public class ConstructionActivity extends AppCompatActivity {
    protected Habitation hab;
    final ActivityResultLauncher<Intent> launcherNewRoom = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Piece p = (Piece) result.getData().getSerializableExtra("Piece");
                    hab.addPiece(p);
                    Log.i("Hab", hab.toString());
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construction);
        hab = new Habitation();

        Button newRoom = (Button) findViewById(R.id.new_room);
        newRoom.setOnClickListener(view -> {
            Intent intent = new Intent(ConstructionActivity.this, NewRoomActivity.class);
            intent.putExtra("hab",hab);
            launcherNewRoom.launch(intent);
        });
    }
}