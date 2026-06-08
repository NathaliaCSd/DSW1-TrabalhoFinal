<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang != null ? param.lang : sessionScope.locale != null ? sessionScope.locale : 'pt_BR'}" scope="session" />
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title><fmt:message key="app.title"/></title>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/style.css" />
    <link rel="stylesheet" href="css/style1.css" />
    <link rel="stylesheet" href="css/responsive.css" />
</head>
<body class="main-layout">
<header>
    <div class="header">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-3 logo_section">
                    <div class="logo">
                        <a href="index.jsp"><img src="images/supercao.png" alt="PetBnb" /></a>
                    </div>
                </div>
                <div class="col-md-9">
                    <div class="navigation">
                        <ul class="nav">
                            <li class="nav-item"><a class="nav-link" href="index.jsp">Home</a></li>
                            <li class="nav-item"><a class="nav-link" href="#about">Sobre nós</a></li>
                            <li class="nav-item"><a class="nav-link" href="casas">Espaços disponíveis</a></li>
                            <li class="nav-item"><a class="nav-link" href="login.jsp">Login</a></li>
                            <li class="nav-item"><a class="nav-link" href="register.jsp">Registro</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</header>

<section class="home-banner-area" id="home">
    <div class="container h-100">
        <div class="home-banner text-center">
            <div class="text-center">
                <h4>Encontre um espaço seguro para o seu pet</h4>
                <h1>Onde <em>ele</em> quiser</h1>
                <a class="button home-banner-btn" href="casas"><fmt:message key="button.view_houses"/></a>
            </div>
        </div>
    </div>
</section>

<div class="container mt-5">
    <div class="row">
        <div class="col-md-12 text-center">
            <c:choose>
                <c:when test="${not empty sessionScope.petLogado}">
                    <h2>Pet logado: ${sessionScope.petLogado.nome}</h2>
                    <p>Reserve casas e gerencie sua estadia diretamente como pet.</p>
                    <a class="button" href="pets">Meus Pets</a>
                    <a class="button" href="casas">Ver Casas</a>
                    <a class="button" href="reservas">Minhas Reservas</a>
                    <a class="button" href="pets/sair">Sair do Pet</a>
                </c:when>
                <c:when test="${not empty sessionScope.usuarioLogado}">
                    <h2>Olá, ${sessionScope.usuarioLogado.nome}</h2>
                    <p>Gerencie casas, pets e reservas como administrador.</p>
                    <a class="button" href="casas">Ver Casas</a>
                    <a class="button" href="pets">Ver Pets</a>
                    <c:if test="${sessionScope.usuarioLogado.papel == 'ADMIN'}">
                        <a class="button" href="usuario/lista">Gerenciar Usuários</a>
                    </c:if>
                    <a class="button" href="usuario/logout">Logout</a>
                </c:when>
                <c:otherwise>
                    <h2>Seja bem-vindo ao PetBnB</h2>
                    <p>Cadastre seu pet e reserve a casa ideal para a estadia.</p>
                    <a class="button" href="pets/novo">Cadastrar Pet</a>
                    <a class="button" href="casas">Ver Casas</a>
                    <a class="button" href="login.jsp">Login Admin</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>
