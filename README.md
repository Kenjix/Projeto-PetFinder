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

> Primeiramente você deve ir até a raiz do seu PHP 8.2.6 e procurar o arquivo php.ini e verificar os seguintes itens, eles devem estar descomentados:

```
extension=fileinfo
extension=pdo_msql
extension=zip
```

### #4 Instalando o Composer

Agora você deve abrir a pasta backend/petFinder-app no VS Code e executar o comando no terminal:

```
composer install
```

### #5 Fazendo a conexão do migrations com o MySQL

Para fazer a criação das tabelas no banco de dados você deve criar um arquivo .env na raiz da pasta petFinder-app

Em seguida abrir o arquivo .env.example e copiar tudo

Colar no arquivo .env que você a recém criou

Para conectar com o seu banco de dados você deve mudar a parte do código abaixo no arquivo .env e colocar seu username e senha para o seu banco de dados no MySQL

```
DB_CONNECTION=mysql
DB_HOST=127.0.0.1
DB_PORT=3306
DB_DATABASE=petfinder
DB_USERNAME=aqui voce coloca o username do seu banco
DB_PASSWORD=aqui voce coloca a senha do seu banco
```

### #6 Criando as tabelas

Finalizando, para a criação das tabelas você executa esse comando:

```
php artisan migrate
```
### #7 Criando o Link simbólico para armazenar as imagens das postagem e perfil

```
php artisan storage:link
```
### #8 Conectando o Android Studio no PHP

Recomendo você iniciar o servidor no terminal do VS Code dessa forma:

```
php artisan serve --host=seuip --port=8000
```
Exemplo: Se meu IP é igual a 192.168.0.02 então eu inicio o server assim: 
```
php artisan serve --host=192.168.0.02 --port=8000
```

### Observação

Para conectar o Android Studio à API, você deve colocar o mesmo endereço IP que usou acima no arquivo no arquivo do Android Studio, localizado na pasta res/values/string.xml

```
<string name="base_url">http://192.168.0.02:8000</string>
```
nessa string você coloca o seu IP e isso ira configurar para todas os arquivos
