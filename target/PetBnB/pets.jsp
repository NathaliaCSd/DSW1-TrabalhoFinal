<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang != null ? param.lang : sessionScope.locale != null ? sessionScope.locale : 'pt_BR'}" scope="session" />
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="pets.title"/> | <fmt:message key="app.title"/></title>
    <link rel="stylesheet" href="style.css" />
</head>
<body>
<div class="container">
    <header>
        <h1><fmt:message key="pets.title"/></h1>
        <p><fmt:message key="pets.subtitle"/></p>
        <div class="lang-selector">
            <strong><fmt:message key="lang.select"/>:</strong>
            <a href="?lang=pt_BR"><fmt:message key="lang.portuguese"/></a> |
            <a href="?lang=en"><fmt:message key="lang.english"/></a>
        </div>
    </header>

    <div class="topnav">
        <span><fmt:message key="topnav.hello"><fmt:param value="${sessionScope.usuarioLogado.nome}"/></fmt:message></span>
        <a href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="topnav.home"/></a>
        <a href="${pageContext.request.contextPath}/usuario/lista"><fmt:message key="button.manage_users"/></a>
        <a href="${pageContext.request.contextPath}/usuario/logout"><fmt:message key="button.logout"/></a>
    </div>

    <p><a class="button" href="${pageContext.request.contextPath}/pets/novo"><fmt:message key="button.new_pet"/></a></p>

    <c:if test="${empty pets}">
        <div class="box"><fmt:message key="no.pets"/></div>
    </c:if>

    <div class="cards">
        <c:forEach var="pet" items="${pets}">
            <div class="card">
                <h2>${pet.nome}</h2>
                <p><strong><fmt:message key="pet.form.breed"/></strong> ${pet.raca}</p>
                <p><strong><fmt:message key="pet.form.age"/></strong> ${pet.idade}</p>
                <p><strong><fmt:message key="pet.form.size"/></strong> ${pet.porte}</p>
                <p><strong><fmt:message key="pet.form.neutered"/></strong> ${pet.castrado ? 'Sim' : 'Não'}</p>
                <p>${pet.descricao}</p>
                <p><strong><fmt:message key="pet.owner"/></strong> ${pet.usuario.nome}</p>
                <c:if test="${sessionScope.usuarioLogado.papel == 'ADMIN' || sessionScope.usuarioLogado.id == pet.usuario.id}">
                    <div class="actions">
                        <a class="button small" href="${pageContext.request.contextPath}/pets/edicao?id=${pet.id}"><fmt:message key="button.edit"/></a>
                        <a class="button small button-danger" href="${pageContext.request.contextPath}/pets/excluir?id=${pet.id}" onclick="return confirm('<fmt:message key="confirm.delete"/>');"><fmt:message key="button.delete"/></a>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
