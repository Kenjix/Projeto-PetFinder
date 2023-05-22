<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Auth\LoginController;
use App\Http\Controllers\UserController;
use App\Http\Controllers\PublicacaoController;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});

Route::post('/login', [LoginController::class, 'login'])->name('login');
Route::post('/cadastroUser', [UserController::class, 'userCadastro'])->name('user_cadastro');
Route::post('/cadastroPublicacao', [PublicacaoController::class, 'publicacaoCadastro'])->name('publicacao_cadastro');
Route::get('publicacoes', [PublicacaoController::class, 'index']);
Route::post('/atualizarUsuario', [UserController::class, 'atualizar'])->name('atualizar_usuario');
