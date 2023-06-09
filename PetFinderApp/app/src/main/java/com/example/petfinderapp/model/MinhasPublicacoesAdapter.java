package com.example.petfinderapp.model;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.petfinderapp.EditarPublicacaoActivity;
import com.example.petfinderapp.R;
import com.google.gson.Gson;

import java.util.List;

public class MinhasPublicacoesAdapter extends RecyclerView.Adapter<MinhasPublicacoesAdapter.ViewHolder> {
    private List<Publicacao> publicacoes;
    private PublicacaoAdapter.OnImageClickListener imageClickListener;

    //construtor para receber a lista de publicações
    public MinhasPublicacoesAdapter(List<Publicacao> publicacoes, PublicacaoAdapter.OnImageClickListener imageClickListener) {
        this.publicacoes = publicacoes;
        this.imageClickListener = imageClickListener;
    }

    public void setPublicacoes(List<Publicacao> publicacoes) {
        this.publicacoes = publicacoes;
        notifyDataSetChanged();
    }

    //sobrescreve o método onCreateViewHolder para inflar o layout do item da lista
    @Override
    public MinhasPublicacoesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_minha_publicacao, parent, false);
        return new MinhasPublicacoesAdapter.ViewHolder(view);
    }

    //sobreescreve o metodo getItemCount para retornar o número de itens na lista
    @Override
    public int getItemCount() {
        return publicacoes.size();
    }

    //sobrescreve o método onBindViewHolder para associar os dados do item aos elementos de layout
    @Override
    public void onBindViewHolder(MinhasPublicacoesAdapter.ViewHolder holder, int position) {
        Publicacao publicacao = publicacoes.get(position);
        holder.textNomePet.setText(publicacao.getNomePet());
        holder.textIdadePet.setText(publicacao.getIdade());
        Glide.with(holder.itemView)
                .load(publicacao.getImagem())
                .placeholder(R.drawable.cachorro)
                .into(holder.imageViewPetFoto);

        //listeneer botao favorito
        holder.buttoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //serializa objeto para JSON usando Gson
                Gson gson = new Gson();
                String json = gson.toJson(publicacao);
                Intent intent = new Intent(v.getContext(), EditarPublicacaoActivity.class);
                intent.putExtra("publicacaoJson", json);
                v.getContext().startActivity(intent);
            }
        });
        holder.buttonExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getBindingAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION && imageClickListener != null) {
                    imageClickListener.onImageClick(clickedPosition);
                }
            }
        });
    }

    public interface OnImageClickListener {
        void onImageClick(int position);

        void onImageViewClick(int position);
    }

    //classe ViewHolder para representar os itens da lista
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //elementos do layout
        private TextView textNomePet, textIdadePet;
        private ImageView imageViewPetFoto;
        private ImageButton buttoEditar, buttonExcluir;

        public ViewHolder(View itemView) {
            super(itemView);
            //inicializa os elementos de layout
            textNomePet = itemView.findViewById(R.id.textNomePet);
            textIdadePet = itemView.findViewById(R.id.textIdadePet);
            imageViewPetFoto = itemView.findViewById(R.id.imageViewPetFoto);
            buttoEditar = itemView.findViewById(R.id.buttoEditar);
            buttonExcluir = itemView.findViewById(R.id.buttonExcluir);
        }
    }
}
