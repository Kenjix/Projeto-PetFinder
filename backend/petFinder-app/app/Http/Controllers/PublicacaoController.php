<?php
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Models\Publicacoes;
use Illuminate\Support\Facades\Storage;

class PublicacaoController extends Controller
{
    public function publicacaoCadastro(Request $request)
    {
        $validaDados = Validator::make($request->all(), [
            'nomePet' => 'required|string',
            'porte' => 'required|string',
            'idade' => 'required|integer',
            'vacinas' => 'string',
            'castrado' => 'boolval',
            'genero' => 'required|string',
            'especie' => 'required|string',
            'descricao' => 'string',  
            'user_id' => 'long',
            'image' => 'required'
        ],
        [
            "nomePet.required" => "O nome é obrigatório",
            "porte.required" => "O porte é obrigatório",
            "genero.required" => "O gênero é obrigatório",
            "especie.required" => "O tipo é obrigatório",
            "image.required" => "A imagem é obrigatória",
        ]);

        if ($validaDados->fails()) {
            return response()->json(['errors' => $validaDados->errors()], 422);
        }

        $base64Image = $request->input('image');
        $decodedImage = base64_decode($base64Image);
        $fileName = uniqid() . '.png';
        Storage::disk('local')->put($fileName, $decodedImage);        
        $url = asset('storage/' . $fileName);        

        $publicacao = Publicacoes::create([
            'nomePet' => $request->input('nomePet'),
            'porte' => $request->input('porte'),
            'idade' => $request->input('idade'),
            'vacinas' => $request->input('vacinas'),
            'castrado' => $request->input('castrado'),
            'genero' => $request->input('genero'),
            'especie' => $request->input('especie'),
            'descricao' => $request->input('descricao'),  
            'image_path' => $url,
            'user_id' => $request->input('user_id'),
        ]);

        if ($publicacao) {
            return response()->json(['message' => 'Publicação cadastrada com sucesso'], 200);
        }

        return response()->json(['message' => 'Falha ao cadastrar a publicação'], 500);
    }
}
?>
