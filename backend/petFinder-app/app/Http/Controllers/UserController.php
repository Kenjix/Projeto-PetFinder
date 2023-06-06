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
    public function index()
    {
        // Retorna a lista de usuários
        $users = User::all();
        return response()->json($users);
    }


    public function store(Request $request)
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
            if (!Hash::check($request->password, $user->password)){
                return response()->json(['message' => 'Credenciais inválidas'], 401);
            }


           /* $base64Image = $request->input('avatar');
            if ($base64Image) {
                $decodedImage = base64_decode($base64Image);
                $fileName = 'avatares/' . uniqid() . '.png';
                Storage::disk('public')->put($fileName, $decodedImage);
                $url = Storage::url($fileName);
                $user->avatar = $url;
            } else {

            }
        
            $base64Image = $request->input('avatar');
            if ($base64Image) {
                if ($user->avatar) {
                    // Remover a imagem anterior se existir
                    Storage::disk('public')->delete($user->avatar);
                }
            

            
                
            }*/
            $user->name = $validaDados['name'];
            $user->telefone = $validaDados['telefone'];
            $user->dataNasc = $validaDados['dataNasc'];
            $user->genero = $validaDados['genero'];            
            $user->save();
            return response()->json(['message' => 'Dados atualizados com sucesso!'], 200);
        } catch (ModelNotFoundException $e) {
            return response()->json(['message' => 'Erro ao atualizar'], 403);
        }
    }

    public function show($id)
    {
        $user = User::find($id);
        $user->append('avatar_link');

        if (!$user) {
            return response()->json(['message' => 'Usuário não encontrado'], 404);
        }
        return response()->json(['user' => $user]);
    }
}
