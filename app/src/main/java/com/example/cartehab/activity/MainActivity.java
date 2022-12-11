package com.example.cartehab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.cartehab.R;
import com.example.cartehab.outils.FabriqueNumero;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe représentant une activité permettant soit de dirigé vers ConstructionActivity soit vers VisualisationActivity.
 * @author Claire Kurth
 */
public class MainActivity extends AppCompatActivity {
    /**
     * La listes des habitations déjà créaient.
     */
    protected ArrayList<String> listeHabitation;

    /**
     * Méthode onCreate.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openListeHabitation();
        Button constructionButton = (Button) findViewById(R.id.construction_button);
        constructionButton.setOnClickListener( view -> {
            Intent intent = new Intent(MainActivity.this, ConstructionActivity.class);
            startActivity(intent);
        });

        Button visualisationButton = (Button) findViewById(R.id.visualisation_button);
        visualisationButton.setOnClickListener(view -> {
            if (listeHabitation == null || listeHabitation.size() == 0){
                Toast.makeText(MainActivity.this,"Il n'y a pas d'habitation enregistrées.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(MainActivity.this, VisualisationActivity.class);
                intent.putExtra("Liste", listeHabitation);
                startActivity(intent);
            }

        });
    }

    /**
     * Cette méthode permet de lire la liste des noms des habitations déjà créée.
     */
    public void openListeHabitation(){
        listeHabitation = new ArrayList<>();
        File file = new File(getApplicationContext().getFilesDir(),"listehabitation.json");
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            String res = stringBuilder.toString();
            JSONObject obj  = new JSONObject(res);
            int cpthabitation = obj.getInt("CPTHABITATION");
            FabriqueNumero.getInstance().setCptHabitation(cpthabitation);

            JSONArray liste = obj.getJSONArray("LISTE_HABITATION");
            for (int i = 0 ; i < liste.length(); i ++){
                JSONObject piece = liste.getJSONObject(i);
                String nom = piece.getString("NOM_HABITATION");
                listeHabitation.add(nom);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode onResume.
     */
    @Override
    protected void onResume(){
        openListeHabitation();
        super.onResume();
    }

}