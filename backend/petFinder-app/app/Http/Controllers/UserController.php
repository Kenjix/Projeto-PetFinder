<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Storage;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use App\Models\User;


class UserController extends Controller
{
    public function userCadastro(Request $request)
    {
        $validaDados = Validator::make($request->all(), [     
            'name' => 'required|string',
            'email' => 'required|email|unique:users',
            'password' => 'required|min:6',
            'dataNasc' => 'date',
            'genero' => 'string',
            'telefone' => 'string|required'           
        ],
        [   
            "name.required" => "O nome é obrigatório",        
            "email" => "Email inválido",
            "email.unique" => "Email já cadastrado",
            "email.required" => "O email é obrigatório",
            "password.required" => "A senha é obrigatória",
            "password.min" => "A senha não atende os requisitos mínimos",
            "telefone.required" => "O número de celular é obrigatório",
        ]);

        if ($validaDados->fails()) {  
            return response()->json(['errors'=>$validaDados->errors()], 422);
        }

        $base64Image = $request->input('avatar');
        if ($base64Image) {
            $decodedImage = base64_decode($base64Image);
            $fileName = 'avatares/' . uniqid() . '.png';
            Storage::disk('public')->put($fileName, $decodedImage);
            $url = Storage::url($fileName);
        }

        $user = User::create([
            'name' => $request->input('name'),
            'email' => $request->input('email'),
            'password' => $request->input('password'),
            'dataNasc' => $request->input('dataNasc'),
            'genero' => $request->input('genero'),
            'telefone' => $request->input('telefone'),
            'avatar' => $base64Image ? $url : null,
        ]);

        if ($user) {
            return response()->json(['message' => 'Usuário cadastrado com sucesso'], 200);
        }

        return response()->json(['message' => 'Falha ao cadastrar o usuário'], 500);        
    }

    public function userAtualizar(Request $request, $id)
    {       
        $validaDados = $request->validate([
            'name' => 'required',
            'telefone' => 'required',
            'password' => 'required',
            'dataNasc' => 'required',
            'genero' => 'required',
            'telefone' => 'required',
            'avatar' => 'required',
        ]);
 
        try {
            $user = User::findOrFail($id);
            $user->fill($validaDados); 
            $user->save();
            return response()->json(['message' => 'Dados atualizados com sucesso']);
        } catch (ModelNotFoundException $e) {
            return response()->json(['message' => 'Erro ao atualizar']);
        }
    }

    public function getUser($id)
    {
        $user = User::find($id);
        $user->append('avatar_link');

        if (!$user) {
            return response()->json(['message' => 'Usuário não encontrado'], 404);
        }
        return response()->json(['user' => $user]);
    }
}
