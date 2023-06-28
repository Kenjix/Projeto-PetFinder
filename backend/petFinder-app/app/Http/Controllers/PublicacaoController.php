<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Storage;
use App\Http\Resources\PublicacaoResource;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use App\Models\Publicacao;

class PublicacaoController extends Controller
{
    public function index()
    {
        $publicacoes = Publicacao::with('favoritos')->orderByDesc('updated_at')->get();
        return PublicacaoResource::collection($publicacoes);
    }

    public function store(Request $request)
    {
        $validaDados = Validator::make(
            $request->all(),
            [
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
            ]
        );

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

    public function update(Request $request, $id)
    {
        $validaDados = $request->validate([
            'petnome' => 'required',
            'porte' => 'required',
            'idade' => 'required',
            'castrado' => 'required',
            'genero' => 'required',
            'especie' => 'required',
            'descricao' => 'required',
        ]);

        try {
            $publicacao = Publicacao::findOrFail($id);

            $url = null;
            if ($publicacao->image_path) {
                $filePath = public_path($publicacao->image_path);
                unlink($filePath);
            }

            $base64Image = $request->input('image_path');
            if ($base64Image) {
                $base64Image = $request->input('image_path');
                $decodedImage = base64_decode($base64Image);
                $fileName = 'imagens/' . uniqid() . '.png';
                Storage::disk('public')->put($fileName, $decodedImage);
                $url = Storage::url($fileName);
            }

            $publicacao->petnome = $validaDados['petnome'];
            $publicacao->porte = $validaDados['porte'];
            $publicacao->idade = $validaDados['idade'];
            $publicacao->castrado = $validaDados['castrado'];
            $publicacao->genero = $validaDados['genero'];
            $publicacao->especie = $validaDados['especie'];
            $publicacao->descricao = $validaDados['descricao'];
            $publicacao->image_path = $base64Image ? $url : null;
            $publicacao->save();

            return response()->json(['message' => 'Dados atualizados com sucesso!', 'user' => $publicacao], 200);
        } catch (ModelNotFoundException $e) {
            return response()->json(['message' => 'Publicação não encontrada'], 404);
        } catch (\Exception $e) {
            return response()->json(['message' => 'Erro ao atualizar a publicação'], 500);
        }
    }

    public function favoritos($user_id)
    {
        $publicacoes = Publicacao::whereHas('favoritos', function ($query) use ($user_id) {
            $query->where('user_id', $user_id);
        })
        ->with('favoritos')
        ->orderByDesc('updated_at')
        ->get();

        return PublicacaoResource::collection($publicacoes);
    }

    public function show($user_id)
    {
        $publicacoes = Publicacao::whereHas('user', function ($query) use ($user_id) {
            $query->where('user_id', $user_id);
        })
            ->with('user')
            ->orderByDesc('updated_at')
            ->get();

        if ($publicacoes->isEmpty()) {
            return response()->json(['message' => 'Nenhuma publicação encontrada'], 404);
        }

        return PublicacaoResource::collection($publicacoes);
    }

    public function destroy($id)
    {
        try {
            $publicacao = Publicacao::findOrFail($id);
            $publicacao->ativo = false;
            $publicacao->save();

            return response()->json(['message' => 'Publicação desativada com sucesso'], 200);
        } catch (ModelNotFoundException $e) {
            return response()->json(['message' => 'Publicação não encontrada'], 404);
        } catch (\Exception $e) {
            return response()->json(['message' => 'Erro ao desativar a publicação'], 500);
        }
    }

    public function buscar(Request $request)
    {
        //filtros
        $especie = $request->input('especie');
        $genero = $request->input('genero');
        $porte = $request->input('porte');

        $publicacao = Publicacao::query()
            ->when($especie, function ($query) use ($especie) {
                return $query->where('especie', $especie);
            })
            ->when($genero, function ($query) use ($genero) {
                return $query->where('genero', $genero);
            })
            ->when($porte, function ($query) use ($porte) {
                return $query->where('porte', $porte);
            })
            ->get();

        return PublicacaoResource::collection($publicacao);
	}
}
