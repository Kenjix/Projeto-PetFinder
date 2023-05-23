# Projeto Extensionista da Disciplina de Computação Móvel

## Pet Finder
Pet Finder é um aplicativo Android que será desenvolvido por estudantes do 5º Semestre do Curso de Sistemas de Informação, da Universidade Franciscana (UFN). Tem o objetivo de auxiliar a comunidade na doação e adoção de animais.

Os protótipos de tela foram desenvolvidos no Figma:  
[Link do Figma](https://www.figma.com/file/4UYmxjJ30Wu5QA8CacLTsG/Untitled?type=design&node-id=0%3A1&t=zqqFHQqMbJukmqUj-1)
![Imagem geral do protótipo das Telas](PrototipoTela/prototipo.png)

### Ferramentas utilizadas

- PHP 8.2.6;
- Laravel 10.11.0;
- AndroidStudio (Android 8 [Nougat]);
- MySQL Workbench

### Instalação

## Primeiros passos

Antes de mais nada você deve clonar o repositório

### #1 PHP 8.2.6

Você pode acessar este [link](https://windows.php.net/download/) para baixar. E também assistir ao [video](https://www.youtube.com/watch?v=KwEilZK5d04) para instalar.

### #2 Composer

Após ter instalado a versão do PHP 8.2.6, agora você pode instalar o [Composer](https://getcomposer.org/download/) e selecionar o caminho do php instalado anteriormente quando solicitado, e então, OK para todos os passos.

### #3 Abrindo o Projeto no VSCODE

> Primeiramente você deve ir até a raiz do seu PHP 8.2.6 e procurar o arquivo php.ini e descomentar os seguintes itens:

```
extension=fileinfo
extension=pdo_msql
extension=zip
```
> Depois você deve ir até a pasta Projeto-PetFinder/backend/petFinder-app e EXCLUIR o arquivo composer.lock

Após ter realizado esses passos, você pode ir até a pasta que foi clonada e abrir a pasta Projeto-PetFinder/backend -> e selecionar a pasta petFinder-app e abrir com o VS Code

### #4 Instalando o Composer

No terminal do VS Code você deve executar o comando:

```
composer install
```

```
composer self-update
```
e repetir o comando abaixo 

```
composer install
```
### #5 Abrindo o MySQL Workbench

Para a criação das tabelas, abra o seu MySQL

### #6 Fazendo a conexão do migrations com o MySQL

Para fazer a criação das tabelas no banco de dados você deve criar um arquivo .env na raiz da pasta petFinder-app

Em seguida abrir o arquivo .env.example e copiar tudo

Colar no arquivo .env que você a recém criou

Para conectar com o seu banco de dados você deve mudar essa parte do código no arquivo .env e colocar seu username e senha para o seu banco de dados no MySQL

```
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=petfinder
DB_USERNAME=aqui voce coloca o username do seu banco
DB_PASSWORD=aqui voce coloca a senha do seu banco
```

### #7 Criando as tabelas

Finalizando, para a criação das tabelas você executa esse comando:

```
php artisan migrate
```

### #8 Conectando os Android Studio no PHP

Recomendo você iniciar o servidor no terminal do VS Code dessa forma:

```
php artisan serve --host=seuip --port=8000
```
Exemplo: Se meu IP é igual a 192.168.0.02 então eu inicio o server assim: 
```
php artisan serve --host=192.168.0.02 --port=8000
```

### Observação

Lembre-se de trocar a url de requisição nos arquivos do Android Studio são eles (Por enquanto):

- Login
- CadastroUsuario
- CriarPublicacaoFragment

### Desenvolvedores:
- Douglas Kenji Kihara
- Gabriel Castagna Henrique
- Carlos Marcelo M. Filho
- Leonardo Pereira
- Matheus Freitas
- Júnior Ferreira
- Lucas Schaefer

### Professor Avaliador:
- Guilherme Chagas Kurtz
