<?php
    include("connector.php");

    function criptografarSenha($senha) {
        $custo = 10;
        $salt = random_bytes(22);
        $hash = password_hash($senha, PASSWORD_ARGON2ID, [
            'memory_cost' => 1 << $custo,
            'time_cost' => 4,
            'threads' => 2
        ]);
        return $hash;
    }
    
    $hash = '';
    
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {        
        if (!empty(trim($_POST['nome'])) 
            || !empty(trim($_POST['sobreNome'])) 
            || !empty(trim($_POST['email'])) 
            || !empty(trim($_POST['senha'])) 
            || !empty(trim($_POST['repeteSenha']))) {
                
            $nome = filter_input(INPUT_POST, 'nome', FILTER_SANITIZE_SPECIAL_CHARS);
            $sobreNome = filter_input(INPUT_POST, 'sobreNome', FILTER_SANITIZE_SPECIAL_CHARS);
            $email = filter_input(INPUT_POST, 'email', FILTER_SANITIZE_SPECIAL_CHARS);
            $senha = filter_input(INPUT_POST, 'senha', FILTER_SANITIZE_SPECIAL_CHARS);
            $repeteSenha = filter_input(INPUT_POST, 'repeteSenha', FILTER_SANITIZE_SPECIAL_CHARS);

            if ($senha != $repeteSenha) {
                die('Senha inválida');
            } else {                
                $hash = criptografarSenha($senha);                
            }

            $sql = "INSERT INTO usuario (nome, sobrenome, email, senha) VALUES ('$nome','$sobreNome', '$email', '$hash')";            

            if (mysqli_query($conexao, $sql)) {
                echo "Usuário cadastrado com sucesso!";
            } else {
                echo "Erro ao cadastrar usuário: " . mysqli_error($conexao);
            }
            
            //Fecha a conexão com o banco de dados
            mysqli_close($conexao);
        }
    } else {
        die('Chamada inválida');
    }
?>