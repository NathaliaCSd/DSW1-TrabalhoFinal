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
    <title><fmt:message key="pets.title"/> | <fmt:message key="app.title"/></title>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/style.css" />
    <link rel="stylesheet" href="css/responsive.css" />
</head>
<body class="main-layout">
<div class="container mt-5">
    <header class="mb-4">
        <h1><fmt:message key="pets.title"/></h1>
        <p><fmt:message key="pets.subtitle"/></p>
    </header>

    <div class="mb-3">
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="topnav.home"/></a>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/casas"><fmt:message key="button.view_houses"/></a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/pets/novo"><fmt:message key="button.new_pet"/></a>
        <c:if test="${not empty sessionScope.petLogado}">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/reservas"><fmt:message key="button.my_reservations"/></a>
            <a class="btn btn-warning" href="${pageContext.request.contextPath}/pets/sair"><fmt:message key="button.logout_pet"/></a>
        </c:if>
    </div>

    <c:if test="${not empty sessionScope.petLogado}">
        <div class="alert alert-info">
            <strong><fmt:message key="label.selected_pet"/></strong> ${sessionScope.petLogado.nome}
        </div>
    </c:if>

    <c:if test="${empty pets}">
        <div class="alert alert-info"><fmt:message key="no.pets"/></div>
    </c:if>

    <div class="row">
        <c:forEach var="pet" items="${pets}">
            <div class="col-md-6 mb-4">
                <div class="card h-100">
                    <div class="card-body">
                        <h4>${pet.nome}</h4>
                        <c:if test="${not empty sessionScope.petLogado && sessionScope.petLogado.id == pet.id}">
                            <span class="badge bg-success mb-2"><fmt:message key="button.selected"/></span>
                        </c:if>
                        <p><strong><fmt:message key="pet.form.breed"/></strong> ${pet.raca}</p>
                        <p><strong><fmt:message key="pet.form.age"/></strong> ${pet.idade}</p>
                        <p><strong><fmt:message key="pet.form.size"/></strong> ${pet.porte}</p>
                        <p><strong><fmt:message key="pet.form.neutered"/></strong> ${pet.castrado ? 'Sim' : 'Não'}</p>
                        <p>${pet.descricao}</p>
                        <div class="mt-3">
                            <c:choose>
                                <c:when test="${not empty sessionScope.petLogado && sessionScope.petLogado.id == pet.id}">
                                    <button type="button" class="btn btn-success" disabled><fmt:message key="button.selected"/></button>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/pets/selecionar?id=${pet.id}"><fmt:message key="button.select_pet"/></a>
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${(not empty sessionScope.usuarioLogado && sessionScope.usuarioLogado.papel == 'ADMIN') || (not empty sessionScope.petLogado && sessionScope.petLogado.id == pet.id)}">
                                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/pets/edicao?id=${pet.id}"><fmt:message key="button.edit"/></a>
                                <a class="btn btn-danger" href="${pageContext.request.contextPath}/pets/excluir?id=${pet.id}" onclick="return confirm('<fmt:message key="confirm.delete"/>')"><fmt:message key="button.delete"/></a>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>