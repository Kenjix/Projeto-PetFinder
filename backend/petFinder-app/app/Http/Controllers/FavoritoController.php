<?php

namespace App\Http\Controllers;
use App\Models\Favorito;
use App\Models\Publicacao;
use Illuminate\Http\Request;

class FavoritoController extends Controller
{
    public function store(Request $request)
    {
        $usuarioId = $request->user()->id;
        $publicacaoId = $request->input('publicacao_id');
    
        //verificar se a publicacao existe
        $publicacao = Publicacao::find($publicacaoId);
    
        if (!$publicacao) {
            return response()->json(['message' => 'A publicação não foi encontrada'], 404);
        }
    
        //verifica se ja existe um registro na tabela favoritos com esse usuario e publicacao
        $favoritoExistente = Favorito::where('user_id', $usuarioId)
            ->where('publicacao_id', $publicacaoId)
            ->first();
    
        if (!$favoritoExistente) {
            //cria o novo registro na tabela favoritos
            $favorito = new Favorito();
            $favorito->user_id = $usuarioId;
            $favorito->publicacao_id = $publicacaoId;
            $favorito->save();
            return response()->json(['message' => 'Publicação adicionada aos favoritos']);
        } else {
            return response()->json(['message' => 'Publicação já adicionada aos favoritos']); 
        }
    }

    public function destroy(Request $request, $publicacaoId)
    {
        $usuarioId = $request->user()->id;
    
        //verificar se a publicação esta vinculada ao usuario
        $favoritoExistente = Favorito::where('user_id', $usuarioId)
            ->where('publicacao_id', $publicacaoId)
            ->first();
    
        if (!$favoritoExistente) {
            return response()->json(['message' => 'A publicação não está vinculada aos favoritos do usuário'], 404);
        }
    
        //remove o registro da tabela favoritos com esse usuário e publicação
        $favoritoExistente->delete();
    
        return response()->json(['message' => 'Publicação removida dos favoritos']);
    }
}
