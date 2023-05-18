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

        $user = User::where('email', $credenciais['email'])->first();

        if ($user && password_verify($credenciais['password'], $user->password)) {
            //autenticação bem-sucedida
            return response()->json([
                'user' => $user,                           
            ], 200);
        } else { //autenticação falhou
            // Buscar o usuário pelo email
            $email = $request->input('email');
            $user = User::where('email', $email)->first();

            if ($user) {
                // Incrementar o número de tentativas de acesso
                $user->tentativasAcesso++;
                $user->save();
                return response()->json(['message' => 'Credenciais inválidas. Tentaivas de acesso restantes: '. $user->tentativasAcesso], 401);
            }

            return response()->json(['message' => 'Credenciais inválidas'], 401);
        }
    }
}
?>
