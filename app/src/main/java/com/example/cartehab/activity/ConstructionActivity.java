package com.example.cartehab.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Mur;
import com.example.cartehab.models.Piece;
import com.example.cartehab.models.Porte;
import com.example.cartehab.outils.FabriqueNumero;
import com.example.cartehab.view.DialogChooseHabitation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConstructionActivity extends AppCompatActivity {
    protected Habitation hab;
    protected HashMap<String,String> listeHabitation;

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
                    Piece p = (Piece) result.getData().getSerializableExtra("Piece");
                    hab.remove(p);
                    hab.addPiece(p);
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construction);
        hab = new Habitation();

        /*listeHabitation = new HashMap<>();
        String idLastHab = openListeHabitation();
        if (idLastHab == null ){
            hab = new Habitation();
            listeHabitation.put(hab.getId(),"hab");
        } else {
            open(idLastHab);
            FabriqueNumero.getInstance().setCptPiece(hab.getListePieces().size());
        }*/

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
        });

        Button newHabitation = findViewById(R.id.new_habitation);
        newHabitation.setOnClickListener(view -> {
            hab = new Habitation();
            FabriqueNumero.getInstance().resetCompteurPiece();
        });
    }

    @Override
    public void onBackPressed(){
        saving();
        saveListeHabitation();
        super.onBackPressed();
    }

    protected void saving() {
        try {
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(openFileOutput(hab.getId() + ".json", Context.MODE_PRIVATE)));
            writer.beginObject();
            writer.name("ID_HABITATION");
            writer.value(hab.getId());

            writer.name("PIECES");
            writer.beginArray();
            for (Piece p : hab.getListePieces().values()){
                writer.beginObject();
                writer.name("ID_PIECE");
                writer.value(p.getId());

                writer.name("NOM_PIECE");
                writer.value(p.getNom());

                writer.name("MUR_NORD");
                writer.beginObject();
                if (p.getMurNord() != null) {
                    writer.name("ID_MUR_NORD");
                    writer.value(p.getMurNord().getId());
                    writer.name("PORTES");
                    writer.beginArray();
                    for (Porte po : p.getMurNord().getListePortes()) {
                        writer.beginObject();
                        writer.name("LEFT");
                        writer.value(po.getLeft());
                        writer.name("TOP");
                        writer.value(po.getTop());
                        writer.name("RIGHT");
                        writer.value(po.getRight());
                        writer.name("BOTTOM");
                        writer.value(po.getBottom());

                        writer.name("ID_PIECE_ACTUELLE");
                        writer.value(po.getPieceActuelle().getId());
                        writer.name("ID_PIECE_SUIVANTE");
                        if (po.getPieceSuivante() != null) {
                            writer.value(po.getPieceSuivante().getId());
                        } else {
                            writer.value("null");
                        }

                        writer.endObject();
                    }
                    writer.endArray();
                } else {
                    writer.name("ID_MUR_NORD");
                    writer.value("null");
                }
                writer.endObject();


                writer.name("MUR_SUD");
                writer.beginObject();
                if (p.getMurSud() != null) {
                    writer.name("ID_MUR_SUD");
                    writer.value(p.getMurSud().getId());
                    writer.name("PORTES");
                    writer.beginArray();
                    for (Porte po : p.getMurSud().getListePortes()) {
                        writer.beginObject();
                        writer.name("LEFT");
                        writer.value(po.getLeft());
                        writer.name("TOP");
                        writer.value(po.getTop());
                        writer.name("RIGHT");
                        writer.value(po.getRight());
                        writer.name("BOTTOM");
                        writer.value(po.getBottom());

                        writer.name("ID_PIECE_ACTUELLE");
                        writer.value(po.getPieceActuelle().getId());
                        writer.name("ID_PIECE_SUIVANTE");
                        if (po.getPieceSuivante() != null) {
                            writer.value(po.getPieceSuivante().getId());
                        } else {
                            writer.value("null");
                        }

                        writer.endObject();
                    }
                    writer.endArray();
                } else {
                    writer.name("ID_MUR_SUD");
                    writer.value("null");
                }
                writer.endObject();

                writer.name("MUR_EST");
                writer.beginObject();
                if (p.getMurEst() != null) {
                    writer.name("ID_MUR_EST");
                    writer.value(p.getMurEst().getId());
                    writer.name("PORTES");
                    writer.beginArray();
                    for (Porte po : p.getMurEst().getListePortes()) {
                        writer.beginObject();
                        writer.name("LEFT");
                        writer.value(po.getLeft());
                        writer.name("TOP");
                        writer.value(po.getTop());
                        writer.name("RIGHT");
                        writer.value(po.getRight());
                        writer.name("BOTTOM");
                        writer.value(po.getBottom());

                        writer.name("ID_PIECE_ACTUELLE");
                        writer.value(po.getPieceActuelle().getId());
                        writer.name("ID_PIECE_SUIVANTE");
                        if (po.getPieceSuivante() != null) {
                            writer.value(po.getPieceSuivante().getId());
                        } else {
                            writer.value("null");
                        }

                        writer.endObject();
                    }
                    writer.endArray();
                } else {
                    writer.name("ID_MUR_EST");
                    writer.value("null");
                }
                writer.endObject();

                writer.name("MUR_OUEST");
                writer.beginObject();
                if (p.getMurOuest() != null) {
                    writer.name("ID_MUR_OUEST");
                    writer.value(p.getMurOuest().getId());
                    writer.name("PORTES");
                    writer.beginArray();
                    for (Porte po : p.getMurOuest().getListePortes()) {
                        writer.beginObject();
                        writer.name("LEFT");
                        writer.value(po.getLeft());
                        writer.name("TOP");
                        writer.value(po.getTop());
                        writer.name("RIGHT");
                        writer.value(po.getRight());
                        writer.name("BOTTOM");
                        writer.value(po.getBottom());

                        writer.name("ID_PIECE_ACTUELLE");
                        writer.value(po.getPieceActuelle().getId());
                        writer.name("ID_PIECE_SUIVANTE");
                        if (po.getPieceSuivante() != null) {
                            writer.value(po.getPieceSuivante().getId());
                        } else {
                            writer.value("null");
                        }

                        writer.endObject();
                    }
                    writer.endArray();
                } else {
                    writer.name("ID_MUR_OUEST");
                    writer.value("null");
                }
                writer.endObject();

                writer.endObject();
            }


            writer.endArray();
            writer.endObject();
            writer.close();

        }  catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void open(String name){
        File file = new File(getApplicationContext().getFilesDir(),name + ".json");
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
            String id = obj.get("ID_HABITATION").toString();
            hab = new Habitation(id);
           // Log.i("HAB", id);

            JSONArray listePieces = obj.getJSONArray("PIECES");
            for (int i = 0 ; i < listePieces.length(); i ++){
                JSONObject piece = listePieces.getJSONObject(i);
                String idPiece = piece.getString("ID_PIECE");
                String nomPiece = piece.getString("NOM_PIECE");
                Piece p = new Piece(hab,nomPiece, idPiece);
                Log.i("HAB", id +  " : " + nomPiece);

                JSONObject murNord = piece.getJSONObject("MUR_NORD");
                String idMurNord = murNord.getString("ID_MUR_NORD");
                if (!idMurNord.equals("null")){
                    Mur mNord = new Mur(p,"N",idMurNord);
                    JSONArray listePortes = obj.getJSONArray("PORTES");
                    for (int j = 0 ; j < listePortes.length() ; j ++){
                        JSONObject porte = listePortes.getJSONObject(i);
                        int top = porte.getInt("TOP");
                        int left = porte.getInt("LEFT");
                        int right = porte.getInt("RIGHT");
                        int bottom = porte.getInt("BOTTOM");

                        String idPieceSuivante = porte.getString("ID_PIECE_SUIVANTE");

                        Porte por = new Porte(mNord,left,top,right,bottom);
                        por.setIdPieceSuivante(idPieceSuivante);
                        mNord.addPorte(por);
                    }
                    p.setMur(mNord);
                }
                hab.addPiece(p);
            }

        } catch (IOException | JSONException e) {
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
            for (Map.Entry<String, String> h : listeHabitation.entrySet()){
                writer.beginObject();
                writer.name("ID_HABITATION");
                writer.value(h.getKey());
                writer.name("NOM_HABITATION");
                writer.value(h.getValue());
                writer.endObject();
            }
            writer.endArray();

            writer.name("LAST_HAB");
            writer.value(hab.getId());
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
                String id = piece.getString("ID_HABITATION");
                String nom = piece.getString("NOM_HABITATION");
                listeHabitation.put(id, nom);
            }

            String idLastHab = obj.getString("LAST_HAB");
            return idLastHab;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}