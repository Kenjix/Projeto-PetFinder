<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {   
        Schema::create('publicacoes', function (Blueprint $table) {
            $table->bigIncrements('id');
            $table->string('nomePet', 60);
            $table->string('porte', 60);
            $table->integer('idade');
            $table->string('vacinas', 255);
            $table->boolean('castrado');
            $table->string('genero', 20);
            $table->string('especie', 60);
            $table->string('descricao', 255);
            $table->string('image_path', 120);
            $table->unsignedBigInteger('user_id');
            $table->foreign('user_id')->references('id')->on('users');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('publicacoes');
    }
};