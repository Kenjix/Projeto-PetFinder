<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Storage;
use Illuminate\Database\Eloquent\ModelNotFoundException;
use Illuminate\Support\Facades\Hash;
use App\Models\User;


class UserController extends Controller
{
    //INDEX (GET ALL)
    public function index()
    {
        //retorna a lista de usuarios
        $users = User::all();
        return response()->json($users);
    }

    //GET ONE
    public function show($id)
    {
        $user = User::find($id);
        $user->append('avatar_link');

        if (!$user) {
            return response()->json(['message' => 'Usuário não encontrado'], 404);
        }
        return response()->json(['user' => $user]);
    }


    //STORE (CREATE)
    public function store(Request $request)
    {
        $validaDados = Validator::make(
            $request->all(),
            [
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
            ]
        );

        if ($validaDados->fails()) {
            return response()->json(['errors' => $validaDados->errors()], 422);
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

    //PUT (UPDATE)
    public function update(Request $request, $id)
    {
        $validaDados = $request->validate([
            'name' => 'required',
            'telefone' => 'required',
            'password' => 'required',
            'dataNasc' => 'required',
            'genero' => 'required',
        ]);

        try {
            $user = User::findOrFail($id);
            if (!Hash::check($request->password, $user->password)) {
                return response()->json(['message' => 'Credenciais inválidas'], 401);
            }
            $url = null;
            if ($user->avatar) {
                $filePath = public_path($user->avatar);
                unlink($filePath);
            }

            $base64Image = $request->input('avatar');
            if ($base64Image) {
                $base64Image = $request->input('avatar');
                $decodedImage = base64_decode($base64Image);
                $fileName = 'avatares/' . uniqid() . '.png';
                Storage::disk('public')->put($fileName, $decodedImage);
                $url = Storage::url($fileName);
            }

            $user->name = $validaDados['name'];
            $user->telefone = $validaDados['telefone'];
            $user->dataNasc = $validaDados['dataNasc'];
            $user->genero = $validaDados['genero'];
            $user->avatar = $base64Image ? $url : null;
            $user->save();

            return response()->json(['message' => 'Dados atualizados com sucesso!', 'user' => $user], 200);
        } catch (ModelNotFoundException $e) {
            return response()->json(['message' => 'Erro ao atualizar'], 403);
        }
    }

    public function redefinirSenha(Request $request, $id)
    {

        $validaDados = Validator::make(
            $request->all(),
            [
                'password' => 'required',
                'nova_senha' => 'required',
                'nova_senha' => 'required|min:6',
            ],
            [
                "password.required" => "A senha atual é obrigatória",
                "nova_senha.required" => "A nova senha é obrigatória",
                "nova_senha.min" => "A nova senha não atende os requisitos mínimos",
            ]
        );

        if ($validaDados->fails()) {
            return response()->json(['errors' => $validaDados->errors()], 422);
        }

        try {
            $user = User::findOrFail($id);
            if (!Hash::check($request->password, $user->password)) {
                return response()->json(['message' => 'Credenciais inválidas'], 401);
            }
            $user->password = Hash::make($request->nova_senha);
            $user->save();
            return response()->json(['message' => 'Senha atualizada com sucesso!'], 200);
        } catch (ModelNotFoundException $e) {
            return response()->json(['message' => 'Erro ao atualizar a senha'], 403);
        }
    }
}
