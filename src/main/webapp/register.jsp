<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang != null ? param.lang : sessionScope.locale != null ? sessionScope.locale : 'pt_BR'}" scope="session" />
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="register.title"/> | <fmt:message key="app.title"/></title>
    <link rel="stylesheet" href="style.css" />
</head>
<body>
<div class="container">
    <header>
        <h1><fmt:message key="register.title"/></h1>
        <p><fmt:message key="register.subtitle"/></p>
        <div class="lang-selector">
            <strong><fmt:message key="lang.select"/>:</strong>
            <a href="?lang=pt_BR"><fmt:message key="lang.portuguese"/></a> |
            <a href="?lang=en"><fmt:message key="lang.english"/></a>
        </div>
    </header>

    <c:if test="${not empty erro}">
        <div class="alert alert-error">${erro}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/usuario/cadastro" method="post" class="form">
        <label><fmt:message key="register.name"/></label>
        <input type="text" name="nome" required />
        <label><fmt:message key="label.login"/></label>
        <input type="text" name="login" required />
        <label><fmt:message key="register.password"/></label>
        <input type="password" name="senha" required />
        <button type="submit"><fmt:message key="button.create_account"/></button>
    </form>
    <p class="center"><fmt:message key="register.already_account"/> <a href="${pageContext.request.contextPath}/login.jsp"><fmt:message key="button.login"/></a></p>
</div>
</body>
</html>
