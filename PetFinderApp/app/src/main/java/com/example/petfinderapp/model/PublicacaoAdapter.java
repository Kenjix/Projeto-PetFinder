package com.example.petfinderapp.model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petfinderapp.R;
import com.example.petfinderapp.VerMaisPublicacaoActivity;
import com.google.gson.Gson;

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
        holder.textGenero.setText(publicacao.getGenero());
        holder.textDescricao.setText(publicacao.getDescricao());
        holder.textIdadePet.setText(publicacao.getIdade());
        //listeneer botao favorito
        holder.buttonCurtir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "FAV! id:" + publicacao.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        //listener botao ver mais
        holder.buttonVerMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //serializa objeto para JSON usando Gson
                Gson gson = new Gson();
                String json = gson.toJson(publicacao);
                Intent intent = new Intent(v.getContext(), VerMaisPublicacaoActivity.class);
                intent.putExtra("publicacaoJson", json);
                //inicia a activity usando o contexto da View
                v.getContext().startActivity(intent);
            }
        });
        Glide.with(holder.itemView)
                .load(publicacao.getUser().getAvatar())
                .placeholder(R.drawable.fotoperfil)
                .into(holder.imageViewPerfil);

        Glide.with(holder.itemView)
                .load(publicacao.getImagem())
                .placeholder(R.drawable.cachorro)
                .into(holder.imageViewFoto);
    }

    //sobreescreve o metodo getItemCount para retornar o número de itens na lista
    @Override
    public int getItemCount() {
        return publicacoes.size();
    }

    //classe ViewHolder para representar os itens da lista
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //elementos do layout
        private TextView textNomePet, textNomeUser, textDescricao, textIdadePet, textGenero;
        private ImageView imageViewFoto, imageViewPerfil;
        private Button buttonVerMais;
        private ImageButton buttonCurtir;


        public ViewHolder(View itemView) {
            super(itemView);
            //inicializa os elementos de layout
            textNomePet = itemView.findViewById(R.id.textNomePet);
            imageViewFoto = itemView.findViewById(R.id.imageViewFoto);
            imageViewPerfil = itemView.findViewById(R.id.imageViewPerfil);
            textNomeUser = itemView.findViewById(R.id.textNomeUser);
            textDescricao = itemView.findViewById(R.id.textDescricao);
            textGenero = itemView.findViewById(R.id.textGenero);
            textIdadePet = itemView.findViewById(R.id.textIdadePet);
            buttonVerMais = itemView.findViewById(R.id.buttonVerMais);
            buttonCurtir = itemView.findViewById(R.id.buttonCurtir);
        }
    }
}
