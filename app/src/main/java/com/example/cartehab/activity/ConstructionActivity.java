package com.example.cartehab.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Piece;
import com.example.cartehab.models.Porte;
import com.example.cartehab.outils.FabriqueNumero;
import com.example.cartehab.outils.SaveManager;
import com.example.cartehab.view.DialogNameCustom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ConstructionActivity extends AppCompatActivity {
    protected Habitation hab;
    protected ArrayList<String> listeHabitation;
    protected TextView nameHab;
    protected String nomLastHab;

    final ActivityResultLauncher<Intent> launcherNewRoom = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    String nomH = result.getData().getStringExtra("Hab");
                    hab = SaveManager.open(getApplicationContext(), nomH);
                    //Piece p = (Piece) result.getData().getSerializableExtra("Piece");
                    //hab.addPiece(p);
                }
            });

    final ActivityResultLauncher<Intent> launcherModificationRoom = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    String nomH = result.getData().getStringExtra("Hab");
                    hab = SaveManager.open(getApplicationContext(), nomH);
                    //Habitation h = (Habitation) result.getData().getSerializableExtra("Hab");
                    //hab = h;
                }

            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construction);

        nomLastHab = openListeHabitation();
        if (nomLastHab == null){
            hab = new Habitation();
            listeHabitation.add(hab.getName());
        } else {
            hab = SaveManager.open(getApplicationContext(),nomLastHab);
            if (hab == null){
                hab = new Habitation();
                listeHabitation.add(hab.getName());
                Toast.makeText(ConstructionActivity.this,"Impossible de récupérer la dernière habitation. Une erreur a eu lieu.", Toast.LENGTH_LONG).show();
            }
        }


        nameHab = findViewById(R.id.nom_hab);
        nameHab.setText(hab.getName());


        Button newRoom = (Button) findViewById(R.id.new_room);
        newRoom.setOnClickListener(view -> {
            Intent intent = new Intent(ConstructionActivity.this, NewRoomActivity.class);
            intent.putExtra("Hab",hab.getName());
            //intent.putExtra("hab",hab);
            launcherNewRoom.launch(intent);
        });

        Button modifRoom = (Button) findViewById(R.id.modif_room);
        modifRoom.setOnClickListener(view -> {
            if (hab.getListePieces().size() == 0){
                Toast.makeText(ConstructionActivity.this, "Il n'y a pas de pièces de créées.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ConstructionActivity.this, ModificationRoomActivity.class);
                intent.putExtra("Hab", hab.getName());
                //intent.putExtra("hab",hab);
                launcherModificationRoom.launch(intent);
            }
        });

        Button openB = findViewById(R.id.open);
        openB.setOnClickListener(view -> {
            if (listeHabitation.size() == 1){
                Toast.makeText(ConstructionActivity.this, "Il n'y a pas d'autres habitations enregistrées.", Toast.LENGTH_LONG).show();
            } else {
                SaveManager.save(getApplicationContext(),hab);
                saveListeHabitation(0);
                AlertDialog d = alertOpenHabitation();
                d.show();
            }

        });

        Button saveB = findViewById(R.id.save);
        saveB.setOnClickListener(view ->{
            SaveManager.save(getApplicationContext(),hab);
            saveListeHabitation(0);
        });

        Button newHabitation = findViewById(R.id.new_habitation);
        newHabitation.setOnClickListener(view -> {
            SaveManager.save(getApplicationContext(),hab);
            saveListeHabitation(0);
            newHabitation();
        });

        Button supprimer = findViewById(R.id.delete_habitation);
        supprimer.setOnClickListener(view->{
            supprimerHabitationEtOuvrirDerniereHabitation();

        });
    }

    public void saveListeHabitation(int s){
        try {
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(openFileOutput("listehabitation.json", Context.MODE_PRIVATE)));
            writer.beginObject();
            writer.name("CPTHABITATION");
            writer.value(FabriqueNumero.getInstance().getNumeroHabitationSansIncre());

            writer.name("LISTE_HABITATION");
            if (listeHabitation.size() != 0){
                writer.beginArray();
                for (String h : listeHabitation){
                    writer.beginObject();
                    writer.name("NOM_HABITATION");
                    writer.value(h);
                    writer.endObject();
                }
                writer.endArray();
            }


            writer.name("LAST_HAB");
            if (s == 0){ //Habitation actuelle
                writer.value(hab.getName());
            }
            if (s == 1){ //Habitation actuelle étant suppr, on mets en dernière habitation la dernière créée.
                if (listeHabitation.size() != 0) {
                    writer.value(listeHabitation.get(listeHabitation.size() - 1));
                }
            }
            writer.endObject();

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String openListeHabitation(){
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

            String nomLastHab = obj.getString("LAST_HAB");
            return nomLastHab;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void newHabitation(){
        hab = new Habitation();
        nameHab.setText(hab.getName());
        listeHabitation.add(hab.getName());
        FabriqueNumero.getInstance().resetCompteurPiece();
    }

    protected AlertDialog alertOpenHabitation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisissez l'habitation à ouvrir : ");
        String[] noms = new String[listeHabitation.size()];
        noms = listeHabitation.toArray(noms);
        final String[] finalNoms = noms;
        builder.setSingleChoiceItems(noms,0, null).setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int n = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                hab = SaveManager.open(getApplicationContext(),finalNoms[n]);
                nameHab.setText(hab.getName());
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }

    public void supprimerHabitationEtOuvrirDerniereHabitation(){
        getApplicationContext().deleteFile(hab.getName()+".data");
        listeHabitation.remove(hab.getName());
        String nomLastHab1 = null;

        if (listeHabitation.size() != 0) { //Pas besoin de save ni de réouvrir la dernière hab si la liste est vide
            saveListeHabitation(1);
            listeHabitation = new ArrayList<>();
            nomLastHab1 = openListeHabitation();

        }
        if (nomLastHab1 == null){
            hab = new Habitation();
            listeHabitation.add(hab.getName());
            FabriqueNumero.getInstance().resetCompteurPiece();
        } else {
            hab = SaveManager.open(getApplicationContext(),nomLastHab1);
        }
        nameHab.setText(hab.getName());
    }

    /**
     * Méthode de création du menu.
     * @param menu Le menu.
     * @return true.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_construction,menu);
        return true;
    }

    /**
     * Méthode qui gère les actions des items du menu.
     * @param item Les items du menu.
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modifier_nom:
                DialogNameCustom.FullNameListener listener = new DialogNameCustom.FullNameListener() {
                    @Override
                    public void fullNameEntered(String fullName) {
                        if (fullName != null){
                            listeHabitation.remove(hab.getName());
                            getApplicationContext().deleteFile(hab.getName()+".data");
                            hab.setName(fullName);
                            nameHab.setText(fullName);
                            listeHabitation.add(fullName);
                        }
                    }
                };
                final DialogNameCustom dialog = new DialogNameCustom(this, listener,listeHabitation);
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onPause(){
        SaveManager.save(getApplicationContext(),hab);
        saveListeHabitation(0);
        super.onPause();
    }

    @Override
    public void onBackPressed(){
        SaveManager.save(getApplicationContext(),hab);
        saveListeHabitation(0);
        super.onBackPressed();
    }

    @Override
    public void finish(){
        SaveManager.save(getApplicationContext(),hab);
        saveListeHabitation(0);
        super.finish();
    }


}