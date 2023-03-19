package com.example.seriesehu;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdaptadorListView
        extends RecyclerView.Adapter<AdaptadorListView.ViewHolderDatos>
        implements View.OnClickListener{

    ArrayList<listaCompra> listaDatos;
    private View.OnClickListener listener;

    public AdaptadorListView( ArrayList<listaCompra> listaDatos) {
        this.listaDatos = listaDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista,null,false);

        view.setOnClickListener(this);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.etiqueta.setText(listaDatos.get(position).getNombre());

        holder.imagen.setImageResource(listaDatos.get(position).getFoto());
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    public void setOnclickLisrener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View view) {
        if (listener != null){
            listener.onClick((view));
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView etiqueta;
        ImageView imagen;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            etiqueta = itemView.findViewById(R.id.etiqueta);
            imagen = itemView.findViewById(R.id.imagen);

        }


    }

}