package com.a.marvel_personagens.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a.marvel_personagens.R;
import com.a.marvel_personagens.model.modelo;
import com.bumptech.glide.Glide;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {




    private List<modelo> listaPersonagens;


    public Adapter(List<modelo> lista) {
        this.listaPersonagens = lista;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lista_principal,parent,false);

        return new MyViewHolder(itemLista);

    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {



        modelo modelo = listaPersonagens.get(position);

        holder.name_ID.setText(modelo.getName());
        Glide.with(holder.path_ID).load(modelo.getPath()).into(holder.path_ID);

/*
        holder.name_ID.setText("teste");
        Glide.with(holder.path_ID).load("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg").into(holder.path_ID);

 */
     }


    @Override
    public int getItemCount() {


        //return 1;
        return listaPersonagens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView   name_ID;
        ImageView  path_ID;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            name_ID              = itemView.findViewById(R.id.name_ID);
            path_ID              = itemView.findViewById(R.id.path_ID);

        }
    }


}//fim do adaptador
