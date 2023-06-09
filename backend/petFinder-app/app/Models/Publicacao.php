<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Concerns\HasTimestamps;
use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Publicacao extends Model
{
    use HasFactory, HasTimestamps;

    protected $table = 'publicacoes';
    protected $hidden = ['image_path'];
    
    public function user()
    {
        return $this->belongsTo(User::class, 'user_id')->select('id', 'name', 'avatar', 'genero', 'dataNasc', 'telefone');
    }
   
    public function favoritos()
    {
        return $this->belongsToMany(User::class, 'favoritos', 'publicacao_id', 'user_id');
    }

    protected $appends = ['image_link'];
    public function getImageLinkAttribute()
    {
        return asset($this->image_path);
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
