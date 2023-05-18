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

        if (Auth::attempt($credenciais)) {
            //autenticação bem-sucedida
            $user = Auth::user(); // Obter o usuário autenticado
            return response()->json([
                'user' => $user,
                'resultado' => 0
            ], 200);
        } else {
            //autenticação falhou
            $email = $request->input('email');

            // Buscar o usuário pelo email
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
