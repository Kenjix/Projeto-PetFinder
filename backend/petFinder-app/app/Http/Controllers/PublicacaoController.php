<?php
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Storage;
use App\Models\Publicacao;

class PublicacaoController extends Controller
{
    public function index()
    {
        $publicacoes = Publicacao::with('user')->orderByDesc('updated_at')->get();
        return response()->json($publicacoes);
    }

    public function store(Request $request)
    {
        $validaDados = Validator::make($request->all(), [
            'nomePet' => 'required|string',
            'porte' => 'required|string',
            'idade' => 'string',
            'vacinas' => 'nullable|string',
            'castrado' => 'required|boolean',
            'genero' => 'required|string',
            'especie' => 'required|string',
            'descricao' => 'nullable|string',
            'user_id' => 'integer',
            'image' => 'required',
        ],
        [
            "nomePet.required" => "O nome é obrigatório",
            "porte.required" => "O porte é obrigatório",
            "genero.required" => "O gênero é obrigatório",
            "especie.required" => "O tipo é obrigatório",
            "descricao.required" => "A descrição é obrigatória",
            "image.required" => "A imagem é obrigatória",
        ]);

        if ($validaDados->fails()) {
            return response()->json(['errors' => $validaDados->errors()], 422);
        }

        $base64Image = $request->input('image');
        $decodedImage = base64_decode($base64Image);
        $fileName = 'images/' . uniqid() . '.png';        
        $url = Storage::url($fileName);
        
        $publicacao = Publicacao::create([
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
            Storage::disk('public')->put($fileName, $decodedImage);
            return response()->json(['message' => 'Publicação cadastrada com sucesso'], 200);
        }
        return response()->json(['message' => 'Falha ao cadastrar a publicação'], 500);
    }
}
?>
