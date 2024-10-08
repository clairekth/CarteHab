package com.example.cartehab.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cartehab.R;
import com.example.cartehab.models.Piece;

import java.util.ArrayList;

public class AdapterListRoom extends ArrayAdapter<Piece> {

    private ArrayList<Piece> listePieces;

    public AdapterListRoom(@NonNull Context context, @NonNull ArrayList<Piece> pieces) {
        super(context, R.layout.room_cell, pieces);
        listePieces = pieces;

    }

    public View getView(int position, View convertView, ViewGroup parent){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View roomItemView = inflater.inflate(R.layout.room_cell, parent, false);
        TextView textView = (TextView) roomItemView.findViewById(R.id.room_name);
        textView.setText(listePieces.get(position).getNom());

        ImageView imError = (ImageView) roomItemView.findViewById(R.id.imageView_error);
        if (listePieces.get(position).pieceEstOK()){
            imError.setVisibility(View.INVISIBLE);
        } else {
            imError.setVisibility(View.VISIBLE);
        }
        return roomItemView;

    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);

    }

    public Piece getListePieces(int i ){
        return listePieces.get(i);
    }


}
