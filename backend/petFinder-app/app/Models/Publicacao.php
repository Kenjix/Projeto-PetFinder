<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Concerns\HasTimestamps;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Support\Facades\Storage;

class Publicacao extends Model
{
    use HasFactory, HasTimestamps;

    protected $table = 'publicacoes';
    protected $appends = ['base64_imagem'];
    protected $hidden = ['image_path'];

    public function getBase64ImagemAttribute()
    {
        $path = $this->image_path;
        $conteudo = Storage::get($path);
        $base64 = base64_encode($conteudo);
        return $base64;
    }

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
