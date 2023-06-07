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

        if (auth::attempt($credenciais)) {               
            /** @var \App\Models\User $user **/       
            $user = Auth::user();
            $user->tokens()->delete();            
            $token = $user->createToken('auth_token');       
            
            if ($user->ativo == 0) {
                return response()->json(['message' => 'Conta Desativada. \\nRedefina a senha para reabilitar.'], 401);
            } 
            if($user->tentativasAcesso >= 5) {
                return response()->json(['message' => 'Tentativas de acesso excedidas. \\nConta bloqueada.'], 401);            
            } else {                
                $user->tentativasAcesso = 0;               
                $user->save();
                
                $response = [
                    'user' => $user->toArray(),
                ];
                $response['user']['avatar'] = asset($user->avatar);     
                $response['user']['auth_token'] = $token->plainTextToken;      
                return response()->json($response, 200);
            }
        } else { //autenticação falhou
            //busca o usuário pelo email
            $user = User::where('email', $credenciais['email'])->first();
            if ($user && $user->ativo == 1) {
                //incrementa o número de tentativas de acesso
                $user->tentativasAcesso++;
                $user->save();
                if ($user->tentativasAcesso >= 5) {
                    return response()->json(['message' => 'Tentativas de acesso excedidas. \\nConta bloqueada.'], 401);
                }
                return response()->json(['message' => 'Credenciais inválidas. \\nTentativas de acesso restantes: '. (5 - $user->tentativasAcesso)], 401);
            } else {
                return response()->json(['message' => 'Credenciais inválidas.'], 401);
            }
        }
    }

    public function logout(Request $request)
    {
        if (!Auth::check()) {
            return response()->json(['error' => 'Usuário não autenticado.'], 401);
        }
        $user = Auth::user();
        $user->currentAccessToken()->delete();    
        return response()->json(['message' => 'Logout realizado com sucesso.'], 200);
    }
}
?>