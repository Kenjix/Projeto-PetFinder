<?php
    include("connector.php");
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {   
        $json = file_get_contents('php://input'); //recebe o JSON enviado via POST
        $data = json_decode($json, true); //converte o JSON em um array associativo
        
        if ($data !== null && isset($data['email']) && isset($data['senha'])) { //verifica se o JSON foi convertido corretamente e se os campos email e senha estão presentes
            $email = filter_var($data['email'], FILTER_SANITIZE_SPECIAL_CHARS); //filtra o email para evitar possíveis ataques de injeção de SQL
            $senha = filter_var($data['senha'], FILTER_SANITIZE_SPECIAL_CHARS); //filtra a senha para evitar possíveis ataques de injeção de SQL
        
            $sql = "SELECT * FROM usuario WHERE email = '$email'";
        
            if (!mysqli_query($conexao, $sql)) {
                die("Erro ao consultar" . mysqli_error($conexao));
            }
        
            $resultado = $conexao->query($sql);
                    
            if ($resultado->num_rows > 0) {
                $linha = $resultado->fetch_assoc();
                if(password_verify($senha, $linha['senha'])){   
                    $nome = $linha['nome'];  
                    $nivelAcesso = $linha['nivelAcesso'];                 
                    echo json_encode(array("resultado" => 0, "nome" => $nome, "nivelAcesso" => $nivelAcesso));
                } else {                    
                    $sql = "UPDATE usuario SET tentativasAcesso = tentativasAcesso+1 WHERE id = '{$linha['id']}'";                    
                    if (!mysqli_query($conexao, $sql)) {
                        die("Erro ao consultar" . mysqli_error($conexao));
                    }
                    echo json_encode(array("resultado" => 1, "nome" => null, "nivelAcesso" => 0));
                }
            } else {
                echo json_encode(array("resultado" => 2, "nome" => null, "nivelAcesso" => 0));
            }
        } else {
            die('JSON inválido ou campos email e senha não presentes');
        }  

        //fecha a conexão com o banco de dados
        mysqli_close($conexao);           
    } else {
        die('Chamada inválida');
    }
?>