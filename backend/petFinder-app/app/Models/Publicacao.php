<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Concerns\HasTimestamps;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Publicacao extends Model
{
    use HasFactory, HasTimestamps;
    protected $table = 'publicacoes';
    protected $fillable = [
        'nomePet',
        'porte',
        'idade',
        'vacinas',
        'castrado',
        'genero',
        'especie',
        'descricao',        
        'image_path',
        'user_id',
    ];
}
