package com.example.cartehab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;

public class ConstructionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construction);
        Habitation h = new Habitation();

        Button newRoom = (Button) findViewById(R.id.new_room);
        newRoom.setOnClickListener(view -> {
            Intent intent = new Intent(ConstructionActivity.this, NewRoomActivity.class);
            intent.putExtra("hab",h);
            startActivity(intent);
        });
    }
}