<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang != null ? param.lang : sessionScope.locale != null ? sessionScope.locale : 'pt_BR'}" scope="session" />
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title><fmt:message key="login.title"/> | <fmt:message key="app.title"/></title>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/style.css" />
    <link rel="stylesheet" href="css/responsive.css" />
</head>
<body>
<div class="container">
    <header>
        <h1><fmt:message key="login.title"/></h1>
        <p><fmt:message key="login.subtitle"/></p>
        <div class="lang-selector">
            <strong><fmt:message key="lang.select"/>:</strong>
            <a href="?lang=pt_BR"><fmt:message key="lang.portuguese"/></a> |
            <a href="?lang=en"><fmt:message key="lang.english"/></a>
        </div>
    </header>

    <c:if test="${not empty erro}">
        <div class="alert alert-error">${erro}</div>
    </c:if>
    <c:if test="${not empty mensagem}">
        <div class="alert alert-success">${mensagem}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/usuario/login" method="post" class="form">
        <div class="form-group">
            <label><fmt:message key="label.login"/></label>
            <input class="form-control" type="text" name="login" required />
        </div>
        <div class="form-group">
            <label><fmt:message key="label.password"/></label>
            <input class="form-control" type="password" name="senha" required />
        </div>
        <button class="btn btn-primary" type="submit"><fmt:message key="button.login"/></button>
    </form>
    <p class="center"><fmt:message key="login.no_account"/> <a href="${pageContext.request.contextPath}/usuario/cadastro"><fmt:message key="button.register"/></a></p>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
</body>
</html>
