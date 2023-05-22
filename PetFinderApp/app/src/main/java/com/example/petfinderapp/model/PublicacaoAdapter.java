package com.example.petfinderapp.model;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.petfinderapp.R;
import com.example.petfinderapp.VerMaisPublicacaoActivity;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class PublicacaoAdapter extends RecyclerView.Adapter<PublicacaoAdapter.ViewHolder> {
    private List<Publicacao> publicacoes;

    //construtor para receber a lista de publicações
    public PublicacaoAdapter(List<Publicacao> publicacoes) {
        this.publicacoes = publicacoes;
    }

    public void setPublicacoes(List<Publicacao> publicacoes) {
        this.publicacoes = publicacoes;
        notifyDataSetChanged();
    }

    //classe ViewHolder para representar os itens da lista
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //elementos do layout
        private TextView textNomePet, textNomeUser, textDescricao, textIdadePet;
        private ImageView imageViewFoto, imageViewPerfil;
        private Button buttonVerMais;


        public ViewHolder(View itemView) {
            super(itemView);
            //inicializa os elementos de layout
            textNomePet = itemView.findViewById(R.id.textNomePet);
            imageViewFoto = itemView.findViewById(R.id.imageViewFoto);
            imageViewPerfil = itemView.findViewById(R.id.imageViewPerfil);
            textNomeUser = itemView.findViewById(R.id.textNomeUser);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textIdadePet = itemView.findViewById(R.id.textIdadePet);
            buttonVerMais = itemView.findViewById(R.id.buttonVerMais);
        }
    }

    //sobrescreve o método onCreateViewHolder para inflar o layout do item da lista
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_publicacao, parent, false);
        return new ViewHolder(view);
    }

    //sobrescreve o método onBindViewHolder para associar os dados do item aos elementos de layout
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Publicacao publicacao = publicacoes.get(position);
        holder.textNomePet.setText(publicacao.getNomePet());
        holder.textNomeUser.setText(publicacao.getUser().getName());
        holder.textDescricao.setText(publicacao.getDescricao());
        holder.textIdadePet.setText(publicacao.getIdade());
        holder.buttonVerMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //serializar objeto para JSON usando Gson
                Gson gson = new Gson();
                String json = gson.toJson(publicacao);
                Intent intent = new Intent(v.getContext(), VerMaisPublicacaoActivity.class);
                intent.putExtra("publicacaoJson", json);
                //iniciar a atividade usando o contexto da View
                v.getContext().startActivity(intent);
            }
        });
        Glide.with(holder.itemView)
                .load(publicacao.getUser().getAvatar())
                .placeholder(R.drawable.fotoperfil)
                .into(holder.imageViewPerfil);

        Glide.with(holder.itemView)
                .load(publicacao.getImagem())
                .placeholder(R.drawable.fotoperfil)
                .placeholder(R.drawable.cachorro)
                .into(holder.imageViewFoto);
    }

    // Sobrescreva o método getItemCount para retornar o número de itens na lista
    @Override
    public int getItemCount() {
        return publicacoes.size();
    }
}

