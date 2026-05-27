<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang != null ? param.lang : sessionScope.locale != null ? sessionScope.locale : 'pt_BR'}" scope="session" />
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="houses.title"/> | <fmt:message key="app.title"/></title>
    <link rel="stylesheet" href="style.css" />
</head>
<body>
<div class="container">
    <header>
        <h1><fmt:message key="houses.title"/></h1>
        <p><fmt:message key="houses.subtitle"/></p>
        <div class="lang-selector">
            <strong><fmt:message key="lang.select"/>:</strong>
            <a href="?lang=pt_BR"><fmt:message key="lang.portuguese"/></a> |
            <a href="?lang=en"><fmt:message key="lang.english"/></a>
        </div>
    </header>

    <div class="topnav">
        <span><fmt:message key="topnav.hello"><fmt:param value="${sessionScope.usuarioLogado.nome}"/></fmt:message></span>
        <a href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="topnav.home"/></a>
        <a href="${pageContext.request.contextPath}/usuario/logout"><fmt:message key="button.logout"/></a>
        <c:if test="${sessionScope.usuarioLogado.papel == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/casas/novo"><fmt:message key="topnav.create_house"/></a>
        </c:if>
    </div>

    <c:if test="${empty casas}">
        <div class="box"><fmt:message key="no.houses"/></div>
    </c:if>

    <div class="cards">
        <c:forEach var="casa" items="${casas}">
            <div class="card">
                <h2>${casa.nome}</h2>
                <p><strong><fmt:message key="label.address"/></strong> ${casa.endereco}</p>
                <p>${casa.descricao}</p>
                <p><strong><fmt:message key="label.daily"/></strong> R$ ${casa.diaria}</p>
                <p><strong><fmt:message key="label.capacity"/></strong> ${casa.capacidade} <fmt:message key="label.dogs"/></p>
                <c:if test="${sessionScope.usuarioLogado.papel == 'ADMIN'}">
                    <div class="actions">
                        <a class="button small" href="${pageContext.request.contextPath}/casas/edicao?id=${casa.id}"><fmt:message key="button.edit"/></a>
                        <a class="button small button-danger" href="${pageContext.request.contextPath}/casas/excluir?id=${casa.id}" onclick="return confirm('<fmt:message key="confirm.delete"/>');"><fmt:message key="button.delete"/></a>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
