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
        Schema::create('users', function (Blueprint $table) {
            $table->bigIncrements('id');
            $table->string('name', 30)->nullable(false);
            $table->string('email', 80)->unique()->nullable(false);
            $table->string('password', 80)->nullable(false);
            $table->date('dataNasc')->nullable();
            $table->char('genero')->nullable();
            $table->string('telefone', 11)->nullable(false);
            $table->binary('avatar')->nullable();
            $table->integer('nivelAcesso')->default(0);
            $table->integer('tentativasAcesso')->default(0);
            $table->boolean('ativo')->default(true);
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('users');
    }
};
