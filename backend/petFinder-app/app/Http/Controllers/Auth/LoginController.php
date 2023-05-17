<?php

namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class LoginController extends Controller
{
    
    public function login(Request $request)
    {
        // Validar os dados recebidos na requisição
        $validatedData = $request->validate([
            'email' => 'required|email',
            'password' => 'required',
        ]);

        // Obter os dados do objeto JSON enviado
        $email = $validatedData['email'];
        $password = $validatedData['password'];

        // Tentar autenticar o usuário
        if (Auth::attempt(['email' => $email, 'password' => $password])) {
            // Autenticação bem-sucedida             
            $token = $request->user()->createToken($request->token_name);

            // Retornar uma resposta com o token de autenticação
            return response()->json(['token' => $token], 200);
        } else {
            // Autenticação falhou
            return response()->json(['message' => 'Credenciais inválidas'], 401);
        }
        return response()->json(['message' => 'Credenciais inválidas'], 401);
    }
}
