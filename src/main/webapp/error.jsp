<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Erro | PetBnB</title>
    <link rel="stylesheet" href="style.css" />
</head>
<body>
<div class="container">
    <header>
        <h1>Ops! Algo deu errado</h1>
        <p>Ocorreu um problema ao processar sua solicitação.</p>
    </header>

    <div class="box">
        <p><strong>Status:</strong> ${status}</p>
        <p><strong>Erro:</strong> ${error}</p>
        <p><strong>Mensagem:</strong> ${message}</p>
        <p><strong>Caminho:</strong> ${path}</p>
    </div>

    <p><a class="button" href="/">Voltar para a página inicial</a></p>
</div>
</body>
</html>
