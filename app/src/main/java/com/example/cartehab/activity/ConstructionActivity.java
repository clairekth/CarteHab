package com.example.cartehab.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Mur;
import com.example.cartehab.models.Piece;
import com.example.cartehab.models.Porte;
import com.example.cartehab.outils.FabriqueNumero;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConstructionActivity extends AppCompatActivity {
    protected Habitation hab;
    protected ArrayList<String> listeHabitation;
    protected TextView nameHab;

    final ActivityResultLauncher<Intent> launcherNewRoom = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Piece p = (Piece) result.getData().getSerializableExtra("Piece");
                    hab.addPiece(p);
                }
            });

    final ActivityResultLauncher<Intent> launcherModificationRoom = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Habitation h = (Habitation) result.getData().getSerializableExtra("Hab");
                    hab = h;
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construction);
        listeHabitation = new ArrayList<>();
        String nomLastHab = openListeHabitation();
        if (nomLastHab == null){
            Log.i("CONS", "error");

            hab = new Habitation();
            listeHabitation.add(hab.getName());
        } else {
            Log.i("CONS", nomLastHab);
            open(nomLastHab);
        }


        nameHab = findViewById(R.id.nom_hab);
        nameHab.setText(hab.getName());


        Button newRoom = (Button) findViewById(R.id.new_room);
        newRoom.setOnClickListener(view -> {
            Intent intent = new Intent(ConstructionActivity.this, NewRoomActivity.class);
            intent.putExtra("hab",hab);
            launcherNewRoom.launch(intent);
        });

        Button modifRoom = (Button) findViewById(R.id.modif_room);
        modifRoom.setOnClickListener(view -> {
            if (hab.getListePieces().size() == 0){
                Toast.makeText(ConstructionActivity.this, "Il n'y a pas de pièces de créées.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ConstructionActivity.this, ModificationRoomActivity.class);
                intent.putExtra("hab", hab);
                launcherModificationRoom.launch(intent);
            }
        });

        Button openB = findViewById(R.id.open);
        openB.setOnClickListener(view -> {
            open("HAB1");
            nameHab.setText(hab.getName());
            Log.i("Cons",hab.toString());
        });

        Button saveB = findViewById(R.id.save);
        saveB.setOnClickListener(view ->{
            save();
            saveListeHabitation();
        });

        Button newHabitation = findViewById(R.id.new_habitation);
        newHabitation.setOnClickListener(view -> {
            save();
            saveListeHabitation();
            hab = new Habitation();
            nameHab.setText(hab.getName());
            listeHabitation.add(hab.getName());
            FabriqueNumero.getInstance().resetCompteurPiece();
        });
    }

    public void save(){
        FileOutputStream fos = null;
        ObjectOutputStream o = null;
        try {
            fos = openFileOutput(hab.getName()+".data", MODE_PRIVATE);
            o = new ObjectOutputStream(fos);
            o.writeObject(hab);
            o.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void open(String name){
        try {
            FileInputStream fis =  getApplicationContext().openFileInput(name + ".data");
            ObjectInputStream o = new ObjectInputStream(fis);
            hab = (Habitation) o.readObject();
            o.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void saveListeHabitation(){
        try {
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(openFileOutput("listehabitation.json", Context.MODE_PRIVATE)));
            writer.beginObject();
            writer.name("CPTHABITATION");
            writer.value(FabriqueNumero.getInstance().getNumeroHabitationSansIncre());

            writer.name("LISTE_HABITATION");
            writer.beginArray();
            for (String h : listeHabitation){
                Log.i("CONS", "SAVE" + h);
                writer.beginObject();
                writer.name("NOM_HABITATION");
                writer.value(h);
                writer.endObject();
            }
            writer.endArray();

            writer.name("LAST_HAB");
            writer.value(hab.getName());
            writer.endObject();

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String openListeHabitation(){
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

            String responce = stringBuilder.toString();
            JSONObject obj  = new JSONObject(responce);
            int cpthabitation = obj.getInt("CPTHABITATION");
            FabriqueNumero.getInstance().setCptHabitation(cpthabitation);

            JSONArray liste = obj.getJSONArray("LISTE_HABITATION");
            for (int i = 0 ; i < liste.length(); i ++){
                JSONObject piece = liste.getJSONObject(i);
                String nom = piece.getString("NOM_HABITATION");
                Log.i("CONS", "open " + nom);
                listeHabitation.add(nom);
            }

            String nomLastHab = obj.getString("LAST_HAB");
            return nomLastHab;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }






    @Override
    protected void onPause(){
        save();
        saveListeHabitation();
        super.onPause();
    }

    @Override
    public void onBackPressed(){
        save();
        saveListeHabitation();
        super.onBackPressed();
    }




}