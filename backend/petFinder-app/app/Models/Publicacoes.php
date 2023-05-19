<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Concerns\HasTimestamps;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Publicacoes extends Model
{
    use HasFactory, HasTimestamps;

    protected $fillable = [
        'nomePet',
        'porte',
        'idade',
        'vacinas',
        'castrado',
        'genero',
        'especie',
        'descricao',
        'user_id',
    ];
}
