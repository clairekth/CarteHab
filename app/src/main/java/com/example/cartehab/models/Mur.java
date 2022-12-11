package com.example.cartehab.models;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * La classe Mur représente un Mur d'une Piece.
 * @author Claire Kurth
 */

public class Mur implements Serializable {
    /**
     * L'identifiant du mur.
     */
    protected String id;
    /**
     * La pièce auquel le mur appartient.
     */
    protected Piece piece;
    /**
     * L'orientation du mur (Nord, Sud, Est ou Ouest)
     */
    protected String orientation;
    /**
     * La liste des portes du mur.
     */
    protected ArrayList<Porte> listePortes;
    /**
     * L'heure à laquelle a été prise la photo du mur.
     */
    protected LocalTime heurePhoto;
    /**
     * Le temps qu'il faisait lors de la prise de la photo du mur.
     */
    protected String temps;

    /**
     * Constructeur.
     * @param p La pièce contenant le mur.
     * @param o L'orientation du mur.
     * @param id L'identifiant du mur.
     */
    public Mur(Piece p, String o, String id) {
        piece = p;
        orientation = o;
        listePortes = new ArrayList<>();
        this.id = id;
    }

    /**
     * Cette méthode permet de récupérer le temps qu'il faisait lors de la prise de la photo du mur.
     * @param a L'activité parente.
     */
    public void getMeteo(Activity a) {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(a);
        if (ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(a, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(a, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(a, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double lat = location.getLatitude();
                    double longi = location.getLongitude();
                    ExecutorService service = Executors.newSingleThreadExecutor();
                    service.execute(() -> {
                        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + longi + "&appid=3ac1d55ec65699a4ba5ba55e7fd93887&lang=fr";
                        InputStream in = null;
                        try {
                            in = new java.net.URL(url).openStream();
                            JSONObject res = readStream(in);

                            String description_data_tmp = res.getJSONArray("weather").getJSONObject(0).getString("description");
                            StringBuilder sb = new StringBuilder(description_data_tmp);
                            sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
                            setTemps(sb.toString());

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    /**
     * Cette méthode permet de set le temps lors de la prise de la photo passé en paramètre.
     * @param t Le temps qu'il faisait lors de la prise de la photo.
     */
    public void setTemps(String t){
        temps = t;
    }

    /**
     * Cette méthode permet de récupérer le temps qu'il faisait lors de la prise de la photo du mur.
     * @return Le temps qu'il faisait.
     */
    public String getTemps(){
        return temps;
    }

    @SuppressLint("NewApi")
    /**
     * Cette méthode permet de set l'heure lors de la prise de la photo du mur.
     */
    public void setHeurePhoto(){
        heurePhoto = LocalTime.now();
        heurePhoto = heurePhoto.minusNanos(heurePhoto.getNano()); //enlève les nano secondes
    }

    /**
     * Cette méthode permet de récupérer l'heure de la prise de la photo à été prise.
     * @return L'heure au format HH:MM:SS.
     */
    public LocalTime getHeurePhoto(){
        return heurePhoto;
    }

    /**
     * Cette méthode permet d'ajouter une porte à la liste des portes du mur.
     * @param p La porte à ajouter.
     */
    public void addPorte(Porte p){
        listePortes.add(p);
    }

    /**
     * Cette méthode permet d'afficher les attributs de la classe pour le débugg.
     * @return Un string avec les attributs de la classe.
     */
    @Override
    public String toString() {
        return "Mur{" +
                "id='" + id + '\'' +
                ", piece=" + piece.getNom() +
                ", orientation='" + orientation + '\'' +
                ", listePortes" + listePortes +
                "}\n";
    }

    /**
     * Cette méthode permet de retourner la pièce contenant le mur.
     * @return La pièce.
     */
    public Piece getPiece(){
        return piece;
    }

    /**
     * Cette méthode permet de retourner la liste des portes du mur.
     * @return La liste des portes.
     */
    public ArrayList<Porte> getListePortes(){
        return listePortes;
    }

    /**
     * Cette méthode permet de récupérer l'identifiant du mur.
     * @return l'identifiant.
     */
    public String getId(){
        return id;
    }

    /**
     * Cette méthode permet de récupérer l'orientation du mur.
     * @return L'orientation du mur.
     */
    public String getOrientation(){
        return orientation;
    }

    /**
     * Cette méthode permet de supprimer toutes les portes du mur.
     */
    public void deletePortes(){
        listePortes.clear();
    }

    /**
     * Cette méthode permet de vérifier que le mur a été construit correctement, c'est à dire que
     * toutes les portes du mur sont construites correctement.
     * @return true si il a été construit correctement, false sinon.
     */
    public boolean murEstOK(){
        for (Porte p : listePortes){
            if (p.porteEstOK() != 0){
                return false; //Une des portes a une erreur
            }
        }
        return true;
    }

    /**
     * Cette méthode permet de savoir si le mur contient des portes.
     * @return true si le mur contient des portes, false sinon.
     */
    public boolean murADesPortes(){
        return listePortes.size() != 0;
    }

    /**
     * Cette méthode permet de savoir si le mur possède une porte vers une pièce.
     * @param dst La pièce de destination.
     * @return true si le mur comporte une porte vers dst, false sinon.
     */
    public boolean porteVers(Piece dst){
        for (Porte p : listePortes){
            if (p.porteVers(dst)){
                return true;
            }
        }
        return false;
    }

    /**
     * Méthode qui permet de lire le flot et d'en faire un objet JSON.
     * @param is Le flot de données.
     * @return L'objet JSON obtenu à partir du flot.
     * @throws IOException
     * @throws JSONException
     */
    private JSONObject readStream(InputStream is) throws IOException, JSONException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is), 1000);
        for (String line = r.readLine(); line != null; line = r.readLine()){
            sb.append(line);
        }
        is.close();
        return new JSONObject(sb.toString());
    }


}
