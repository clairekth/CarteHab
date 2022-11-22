package com.example.cartehab.models;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

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
import java.net.MalformedURLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Mur implements Serializable {
    protected String id;
    protected Piece piece;
    protected String orientation;
    protected ArrayList<Porte> listePortes;
    protected LocalTime heurePhoto;
    protected String temps;

    public Mur(Piece p, String o, String id) {
        piece = p;
        orientation = o;
        listePortes = new ArrayList<>();
        this.id = id;
    }

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
                        Log.i("TEMPS","CC");
                        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + longi + "&appid=3ac1d55ec65699a4ba5ba55e7fd93887&lang=fr";
                        InputStream in = null;
                        try {
                            in = new java.net.URL(url).openStream();
                            JSONObject res = readStream(in);

                            String description_data_tmp = res.getJSONArray("weather").getJSONObject(0).getString("description");
                            StringBuilder sb = new StringBuilder(description_data_tmp);
                            sb.replace(0, 1, sb.substring(0, 1).toUpperCase());
                            setTemps(sb.toString());
                            Log.i("TEMPS0", description_data_tmp);
                            Log.i("TEMPS1", temps);

                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    public void setTemps(String t){
        temps = t;
    }
    public String getTemps(){
        return temps;
    }

    @SuppressLint("NewApi")
    public void setHeurePhoto(){
        heurePhoto = LocalTime.now();
        heurePhoto = heurePhoto.minusNanos(heurePhoto.getNano()); //enlève les nano secondes
    }

    public LocalTime getHeurePhoto(){
        return heurePhoto;
    }

    public void addPorte(Porte p){
        listePortes.add(p);
    }

    @Override
    public String toString() {
        return "Mur{" +
                "id='" + id + '\'' +
                ", piece=" + piece.getNom() +
                ", orientation='" + orientation + '\'' +
                ", listePortes" + listePortes +
                "}\n";
    }

    public Piece getPiece(){
        return piece;
    }

    public ArrayList<Porte> getListePortes(){
        return listePortes;
    }

    public String getId(){
        return id;
    }

    public String getOrientation(){
        return orientation;
    }

    public void deletePortes(){
        listePortes.clear();
    }

    public boolean murEstOK(){
        for (Porte p : listePortes){
            if (p.porteEstOK() != 0){
                return false; //Une des portes a une erreur
            }
        }
        return true;
    }

    public boolean murADesPortes(){
        return listePortes.size() != 0;
    }

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
