<?php
namespace App\Http\Controllers\Auth;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use App\Models\User;

class LoginController extends Controller
{    
    public function login(Request $request)
    {
        $credenciais = $request->only('email', 'password');

        if (Auth::once($credenciais)) {
            // Autenticação bem-sucedida
            $user = Auth::user();
            
            if ($user->ativo == 0 || $user->tentativasAcesso >= 5) {
                return response()->json(['message' => 'Usuário bloqueado. Redefina a senha.'], 401);
            } else {
                $user = User::where('email', $credenciais['email'])->first();
                $user->tentativasAcesso = 0;
                $user->save();
                return response()->json([
                    'user' => $user,
                ], 200);
            }
        } else { // Autenticação falhou
            // Buscar o usuário pelo email
            $user = User::where('email', $credenciais['email'])->first();
            if ($user && $user->ativo == 1) {
                //incrementa o número de tentativas de acesso
                $user->tentativasAcesso++;
                $user->save();
                if ($user->tentativasAcesso >= 5) {
                    return response()->json(['message' => 'Tentativas de acesso excedidas. Usuário bloqueado.'], 401);
                }
                return response()->json(['message' => 'Credenciais inválidas. Tentativas de acesso restantes: '. (5 - $user->tentativasAcesso)], 401);
            } else {
                return response()->json(['message' => 'Credenciais inválidas.'], 401);
            }
        }
    }
}

?>
