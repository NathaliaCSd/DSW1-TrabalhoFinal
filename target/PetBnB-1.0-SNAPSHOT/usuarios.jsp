<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang != null ? param.lang : sessionScope.locale != null ? sessionScope.locale : 'pt_BR'}" scope="session" />
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="users.title"/> | <fmt:message key="app.title"/></title>
    <link rel="stylesheet" href="style.css" />
</head>
<body>
<div class="container">
    <header>
        <h1><fmt:message key="users.title"/></h1>
        <p><fmt:message key="users.subtitle"/></p>
        <div class="lang-selector">
            <strong><fmt:message key="lang.select"/>:</strong>
            <a href="?lang=pt_BR"><fmt:message key="lang.portuguese"/></a> |
            <a href="?lang=en"><fmt:message key="lang.english"/></a>
        </div>
    </header>

    <div class="topnav">
        <span><fmt:message key="topnav.hello"><fmt:param value="${sessionScope.usuarioLogado.nome}"/></fmt:message></span>
        <a href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="topnav.home"/></a>
        <a href="${pageContext.request.contextPath}/pets"><fmt:message key="button.view_pets"/></a>
        <a href="${pageContext.request.contextPath}/usuario/logout"><fmt:message key="button.logout"/></a>
    </div>

    <p><a class="button" href="${pageContext.request.contextPath}/usuario/novo"><fmt:message key="button.new_user"/></a></p>

    <c:if test="${empty usuarios}">
        <div class="box"><fmt:message key="no.users"/></div>
    </c:if>

    <table class="table">
        <thead>
            <tr>
                <th>ID</th>
                <th><fmt:message key="user.form.name"/></th>
                <th><fmt:message key="user.form.login"/></th>
                <th><fmt:message key="user.form.role"/></th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="usuario" items="${usuarios}">
                <tr>
                    <td>${usuario.id}</td>
                    <td>${usuario.nome}</td>
                    <td>${usuario.login}</td>
                    <td>${usuario.papel}</td>
                    <td>
                        <a class="button small" href="${pageContext.request.contextPath}/usuario/edicao?id=${usuario.id}"><fmt:message key="button.edit"/></a>
                        <a class="button small button-danger" href="${pageContext.request.contextPath}/usuario/excluir?id=${usuario.id}" onclick="return confirm('<fmt:message key="confirm.delete"/>');"><fmt:message key="button.delete"/></a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
