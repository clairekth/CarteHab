package com.example.cartehab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button constructionButton = (Button) findViewById(R.id.construction_button);
        constructionButton.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, ConstructionActivity.class);
            startActivity(intent);
        });
    }



}