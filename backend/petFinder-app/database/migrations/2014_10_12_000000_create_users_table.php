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
            $table->string('name', 30)->nullable(false);;
            $table->string('email', 80)->unique()->nullable(false);
            $table->string('password', 80)->nullable(false);;
            $table->dateTime('dataNasc')->nullable();;
            $table->char('genero')->nullable();
            $table->string('telefone', 14)->nullable();;;
            $table->binary('avatar')->nullable();
            $table->integer('nivelAcesso')->default(0);
            $table->integer('tentativasAcesso')->default(0);
            $table->boolean('ativo')->default(true);
            $table->timestamp('created_at')->nullable();
            $table->timestamp('updated_at')->nullable();
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
