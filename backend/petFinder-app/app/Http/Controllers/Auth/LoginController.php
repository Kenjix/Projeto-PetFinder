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
            //autenticação bem-sucedida
            $user = Auth::user();
            if ($user->ativo == 0) {
                return response()->json(['message' => 'Usuário bloquado. Redefina a senha.'], 401);
            } else{
                return response()->json([
                    'user' => $user,                           
                ], 200);
            }
        } else { //autenticação falhou
            // Buscar o usuário pelo email
            $email = $credenciais['email'];
            $user = User::where('email', $email)->first();
            if ($user && $user->ativo == 1) {
                // Incrementar o número de tentativas de acesso
                $user->tentativasAcesso++;
                $user->save();
                return response()->json(['message' => 'Credenciais inválidas. Tentaivas de acesso restantes: '. $user->tentativasAcesso], 401);
            } else {
                return response()->json(['message' => 'Usuário bloquado. Redefina a senha.'], 401);
            }
            return response()->json(['message' => 'Credenciais inválidas'], 401);
        }
    }
}
?>
