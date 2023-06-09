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
            $table->string('nomePet', 60)->nullable(false);
            $table->string('porte', 60)->nullable(false);
            $table->string('idade')->nullable(true);
            $table->string('vacinas', 255)->nullable(true);
            $table->boolean('castrado')->nullable(false);
            $table->string('genero', 20)->nullable(false);
            $table->string('especie', 60)->nullable(false);
            $table->string('descricao', 255)->nullable(false);
            $table->string('image_path')->nullable(false);
            $table->boolean('ativo')->default(true);
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
