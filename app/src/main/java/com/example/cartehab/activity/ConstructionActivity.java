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

public class ConstructionActivity extends AppCompatActivity {
    protected Habitation hab;
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

        Button open = (Button) findViewById(R.id.open);
        open.setOnClickListener(view -> {
            open();
        });
    }

    @Override
    public void onBackPressed(){
        saving();
        super.onBackPressed();
    }

    protected void saving() {
        try {
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(openFileOutput("test.json", Context.MODE_PRIVATE)));
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

                writer.name("ID_MUR_NORD");
                if (p.getMurNord() != null) {
                    writer.value(p.getMurNord().getId());
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
                    writer.value("null");
                }

                writer.name("ID_MUR_SUD");
                if (p.getMurSud() != null) {
                    writer.value(p.getMurSud().getId());
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
                    writer.value("null");
                }

                writer.name("ID_MUR_EST");
                if (p.getMurEst() != null) {
                    writer.value(p.getMurEst().getId());
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
                    writer.value("null");
                }

                writer.name("ID_MUR_OUEST");
                if (p.getMurOuest() != null) {
                    writer.value(p.getMurOuest().getId());
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
                    writer.value("null");
                }

                writer.endObject();
            }


            writer.endArray();
            writer.endObject();
            writer.close();

        }  catch (IOException e) {
            e.printStackTrace();
        }

    }

    protected void open(){
        File file = new File(getApplicationContext().getFilesDir(),"test.json");
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
            Log.i("HAB", id);

            JSONArray listePieces = obj.getJSONArray("PIECES");
            for (int i = 0 ; i < listePieces.length(); i ++){
                JSONObject piece = listePieces.getJSONObject(i);
                String idPiece = piece.getString("ID_PIECE");
                String nomPiece = piece.getString("NOM_PIECE");
                Log.i("HAB", id +  " : " + nomPiece);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}