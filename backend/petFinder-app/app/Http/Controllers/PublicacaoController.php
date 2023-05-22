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
        $publicacoes = Publicacao::with('user')->get();
    
        //adiciona a propriedade virtual 'image_link' às publicações
        $publicacoes->each(function ($publicacao) {
            $publicacao->append('image_link');
        });
    
        // Retorna a resposta JSON com as publicações com a propriedade virtual 'image_link'
        return response()->json($publicacoes);
    }

    public function publicacaoCadastro(Request $request)
    {
        $validaDados = Validator::make($request->all(), [
            'nomePet' => 'required|string',
            'porte' => 'required|string',
            'idade' => 'required|string',
            'vacinas' => 'nullable|string',
            'castrado' => 'boolean',
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
            "image.required" => "A imagem é obrigatória",
        ]);

        if ($validaDados->fails()) {
            return response()->json(['errors' => $validaDados->errors()], 422);
        }

        $base64Image = $request->input('image');
        $decodedImage = base64_decode($base64Image);
        $fileName = 'imagens/' . uniqid() . '.png';
        Storage::disk('local')->put($fileName, $decodedImage);  
        $url = $fileName;
        
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
            return response()->json(['message' => 'Publicação cadastrada com sucesso'], 200);
        }
        return response()->json(['message' => 'Falha ao cadastrar a publicação'], 500);
    }
}
?>
