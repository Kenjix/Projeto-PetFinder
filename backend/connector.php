<?php
    //define as constantes de conexão
    define("DB_HOST", "localhost");
    define("DB_USER", "root");
    define("DB_PASSWORD", "laboratorio");
    define("DB_NAME", "petfinder");

    //conecta ao banco de dados
    $conexao = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_NAME);

    //verifica se a conexão foi bem sucedida
    if (mysqli_connect_errno()) {
        die("Falha na conexão com o banco de dados: " . mysqli_connect_error());
    }

    //seleciona o banco de dados
    if (!mysqli_select_db($conexao, DB_NAME)) {
        die("Não foi possível selecionar o banco de dados: " . mysqli_error($conexao));
    }
?>