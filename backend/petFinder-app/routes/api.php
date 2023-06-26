<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Auth\LoginController;
use App\Http\Controllers\FavoritoController;
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

Route::prefix('auth')->group(function(){
    Route::post('/login', [LoginController::class, 'login']);
    Route::post('/logout', [LoginController::class, 'logout'])->middleware('auth:sanctum');       
});

Route::middleware('auth:sanctum')->group(function() {
    Route::get('/users', [UserController::class, 'index']);
    Route::post('/users', [UserController::class, 'store'])->withoutMiddleware('auth:sanctum');
    Route::get('/users/{id}', [UserController::class, 'show']);
    Route::put('/users/{id}', [UserController::class, 'update']);
    Route::put('/users/redefinirSenha/{id}', [UserController::class, 'redefinirSenha']);
    Route::delete('/users/{id}', [UserController::class, 'destroy'])->withoutMiddleware('auth:sanctum');;
});

Route::middleware('auth:sanctum')->group(function() {
    Route::get('/publicacao', [PublicacaoController::class, 'index']);
    Route::get('/publicacao/favoritos/{id}', [PublicacaoController::class, 'favoritos']);
    Route::post('/publicacao', [PublicacaoController::class, 'store']);
    Route::get('/publicacao/{id}', [PublicacaoController::class, 'show']);
    Route::put('/publicacao/{id}', [PublicacaoController::class, 'update']);
    Route::delete('/publicacao/{id}', [PublicacaoController::class, 'destroy']);
});

Route::middleware('auth:sanctum')->group(function() {
    Route::post('favoritos', [FavoritoController::class, 'store']);
    Route::delete('favoritos/{publicacaoId}', [FavoritoController::class, 'destroy']);
});