package com.example.cartehab.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.cartehab.R;
import com.example.cartehab.models.Habitation;
import com.example.cartehab.models.Mur;
import com.example.cartehab.models.Piece;
import com.example.cartehab.models.Porte;
import com.example.cartehab.outils.Globals;
import com.example.cartehab.view.DialogChooseRoomNext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
/**
 * Classe représentant une activité permettant la séléction des portes d'un mur.
 * @author Claire Kurth
 */
public class SelectDoorActivity extends AppCompatActivity {
    /**
     * Champ contenant l'imageView.
     */
    protected ImageView im;

    /**
     * Champ contenant le rectangle créé par l'utilisateur.
     */
    protected Rect rectangle;

    /**
     * Champ contenant le surfaceHolder.
     */
    protected SurfaceHolder holder;

    /**
     * Champ contenant la SurfaceView.
     */
    protected SurfaceView surface;

    /**
     * Champ contenant le Canvas utilisé pour dessiner le rectangle de sélection.
     */
    protected Canvas canvas;

    /**
     * Champs contenants les coordonnées du rectangle dessiné par l'utilisateur.
     */
    protected int left, top, right, bottom ;

    /**
     * Champ contenants les coordoonées de l'imageView.
     */
    protected int xIm, yIm;

    /**
     * La liste des portes sous forme de rectangle.
     */
    protected ArrayList<Rect> listePorte;
    /**
     * Le mur contenant les portes.
     */
    protected Mur m;
    /**
     * L'habitation actuelle.
     */
    protected Habitation hab;
    /**
     * Le nom de la pièce suivante de la porte venant d'être créée.
     */
    protected String pieceSuivanteNom;
    /**
     * Méthode onCreate.
     * @param savedInstanceState
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_door);

        hab = Globals.getInstance().getDataHabitation();
        m = Globals.getInstance().getmData();

        Button delete = (Button) findViewById(R.id.delete_button);

        listePorte = new ArrayList<>();
        m.deletePortes();
        im = (ImageView) findViewById(R.id.im_selectActivity);

        FileInputStream fis = null;
        try {
            fis = openFileInput(m.getId());
            Bitmap bp = BitmapFactory.decodeStream(fis);
            im.setImageBitmap(bp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        surface = (SurfaceView) findViewById(R.id.surface);
        holder = surface.getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);
        surface.setZOrderOnTop(true);

        Paint myPaint = new Paint();
        myPaint.setColor(Color.BLUE);
        myPaint.setStyle(Paint.Style.FILL);
        myPaint.setAlpha(70);

        canvas = new Canvas();

        im.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                int count = event.getPointerCount();

                if (count == 2) {
                    /*Récupère les coordonnées de l'imageView*/
                    xIm = (int) im.getX();
                    yIm = (int) im.getY();

                    /*Récupère les coordonnées de la photo des 2 doigts + ajout des coordonnées de l'image pour avoir les coordonnées par rapport à la vue*/
                    left = (int) event.getX(0) + xIm;
                    top = (int) event.getY(0) + yIm;

                    right = (int) event.getX(1) + xIm;
                    bottom = (int) event.getY(1) + yIm;

                    /*Gestion sortie du rectangle de sélection de l'image*/
                    if (top < yIm){
                        top = yIm + 1;
                    }
                    if (bottom < yIm){
                        bottom = yIm + 1;
                    }
                    if (bottom > yIm + im.getHeight()){
                        bottom = yIm + im.getHeight() - 1;
                    }
                    if (top > yIm + im.getHeight()){
                        top = yIm + im.getHeight() - 1;
                    }

                    if (top == bottom){
                        bottom = bottom - 1;
                    }
                    if (left == right){
                        right = left -1;
                    }

                    /*Création du rectangle + tri des valeurs*/
                    rectangle = new Rect(left, top, right, bottom);
                    rectangle.sort();

                    canvas = holder.lockCanvas();
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //Clear les anciens rectangle
                    canvas.drawRect(rectangle, myPaint);
                    for (Rect r : listePorte){
                        canvas.drawRect(r,myPaint);
                    }
                    holder.unlockCanvasAndPost(canvas);

                }
            } else if (event.getAction() == MotionEvent.ACTION_UP && rectangle != null){
                Porte p = new Porte(m, rectangle.left, rectangle.top, rectangle.right, rectangle.bottom);
                listePorte.add(rectangle);

                DialogChooseRoomNext.NameRoomNextListener listener = fullName -> {
                    pieceSuivanteNom = fullName;
                    Piece piece = hab.getPiece(pieceSuivanteNom);
                    p.setPieceSuivante(piece);
                };
                final DialogChooseRoomNext dialog = new DialogChooseRoomNext(SelectDoorActivity.this, hab,m,listener);
                dialog.showAlertDialog();
                m.addPorte(p);
            }
            return true;
        });

        Button ok = findViewById(R.id.validation_button);
        ok.setOnClickListener(view -> {
           finish();
        });

        delete.setOnClickListener( view -> {
            listePorte.clear();
            m.deletePortes();
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //Clear les anciens rectangle
            holder.unlockCanvasAndPost(canvas);
        });
    }

    /**
     * Méthode finish. Save de l'habitation en global pour la récupérer dans les autres activités.
     */
    @Override
    public void finish() {
        Intent data = new Intent();
        m.getPiece().setMur(m);
        Globals.getInstance().setDataHabitation(hab);
        setResult(RESULT_OK, data);
        super.finish();
    }
}