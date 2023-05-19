<?php
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Models\Publicacoes;

class PublicacaoController extends Controller
{
    public function publicacaoCadastro(Request $request)
    {
        $validaDados = Validator::make($request->all(), [     
            'nomePet' => 'required|string',
            'porte' => 'required|string',
            'idade' => 'required|integer',
            'vacinas' => 'string',
            'genero' => 'required|string',
            'especie' => 'required|string',
            'descricao' => 'string',
            'user_id' => 'required|integer',
            //a imagem vem em formato de String do JAVA
            'imagem' => 'required|string'         
        ],
        [   
            "nomePet.required" => "O nome é obrigatório",        
            "porte.required" => "O porte é obrigatório",
            "genero.required" => "O gênero é obrigatório",
            "especie.required" => "O tipo é obrigatório",
            "imagem.required" => "A imagem é obrigatória",
            "imagem.string" => "A imagem deve estar em formato de string"
        ]);

        if ($validaDados->fails()) {
            return response()->json(['errors' => $validaDados->errors()], 422);
        }

        $decodedImagem = base64_decode($request->input('imagem'));

        $publicacao = Publicacoes::create([        
            'nomePet' => $request->input('nomePet'),
            'porte' => $request->input('porte'),
            'idade' => $request->input('idade'),
            'vacinas' => $request->input('vacinas'),
            'castrado' => $request->input('castrado'),
            'genero' => $request->input('genero'),
            'especie' => $request->input('especie'),
            'descricao' => $request->input('descricao'),
            'user_id' => $request->input('user_id'),
            'imagem' => $decodedImagem
        ]);

        if ($publicacao) {
            return response()->json(['message' => 'Publicação cadastrada com sucesso'], 200);
        }

        return response()->json(['message' => 'Falha ao cadastrar a publicação'], 500);
    }
}
