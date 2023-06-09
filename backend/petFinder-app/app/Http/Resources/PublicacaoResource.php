<?php

namespace App\Http\Resources;


use Illuminate\Http\Resources\Json\JsonResource;

class PublicacaoResource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @return array<string, mixed>
     */
    public function toArray($request)
    {
        return [
            'publicacao' => [
            'id' => $this->id,
            'nomePet' => $this->nomePet,
            'porte' => $this->porte,
            'idade' => $this->idade,
            'vacinas' => $this->vacinas,
            'castrado' => $this->castrado,
            'genero' => $this->genero,
            'especie' => $this->especie,
            'descricao' => $this->descricao,
            'image_link' => $this->image_link,
            ],
            'user' => [
                'id' => $this->user->id,
                'name' => $this->user->name,
                'genero' => $this->user->genero,
                'dataNasc' => $this->user->dataNasc,
                'telefone' => $this->user->telefone,
                'avatar_link' => $this->user->avatar_link,
            ],
            'favoritos' => $this->favoritos->map(function ($favorito) {
                return [                    
                    'user_id' => $favorito->pivot->user_id,
                ];
            }),
        ];
    }
}

