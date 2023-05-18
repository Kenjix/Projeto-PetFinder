<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Models\User;

class UserController extends Controller
{
    public function userCadastro(Request $request)
    {
        $validaDados = Validator::make($request->all(), [     
            'name' => 'required|string',
            'email' => 'required|email|unique:users',
            'password' => 'required|min:6',
            'dataNasc' => 'string',
            'genero' => 'string',
            'telefone' => 'string'           
        ],
        [   
            "name.required" => "O nome é obrigatório",        
            "email" => "Email inválido",
            "email.unique" => "Email já cadastrado",
            "email.required" => "O email é obrigatório",
            "password.required" => "A senha é obrigatória",
            "password.min" => "A senha não atende os requisitos minimos",
        ]);

        if ($validaDados->fails()) {  
            return response()->json(['errors'=>$validaDados->errors()], 422);
        }

    $user = User::create([
        'name' => $validaDados['name'],
        'email' => $validaDados['email'],
        'password' => bcrypt($validaDados['password']),
        'dataNasc' => $validaDados['dataNasc'],
        'genero' => $validaDados['genero'],
        'telefone' => $validaDados['telefone'],
    ]);
    if ($user) {
        return response()->json(['message' => 'Usuário cadastrado com sucesso'], 200);
    }
    return response()->json(['message' => 'Falha ao cadastrar o usuário'], 500);        
    }
}
