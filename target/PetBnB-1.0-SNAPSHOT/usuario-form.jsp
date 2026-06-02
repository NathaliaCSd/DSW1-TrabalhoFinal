<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang != null ? param.lang : sessionScope.locale != null ? sessionScope.locale : 'pt_BR'}" scope="session" />
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="app.title"/> | <c:choose><c:when test="${not empty usuario}"><fmt:message key="user.form.title.edit"/></c:when><c:otherwise><fmt:message key="user.form.title.create"/></c:otherwise></c:choose></title>
    <link rel="stylesheet" href="style.css" />
</head>
<body>
<div class="container">
    <header>
        <h1><c:choose><c:when test="${not empty usuario}"><fmt:message key="user.form.title.edit"/></c:when><c:otherwise><fmt:message key="user.form.title.create"/></c:otherwise></c:choose></h1>
        <div class="lang-selector">
            <strong><fmt:message key="lang.select"/>:</strong>
            <a href="?lang=pt_BR"><fmt:message key="lang.portuguese"/></a> |
            <a href="?lang=en"><fmt:message key="lang.english"/></a>
        </div>
    </header>

    <c:if test="${not empty erro}">
        <div class="alert alert-error">${erro}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/usuario/salvar" method="post" class="form">
        <c:if test="${not empty usuario}">
            <input type="hidden" name="id" value="${usuario.id}" />
        </c:if>

        <label><fmt:message key="user.form.name"/></label>
        <input type="text" name="nome" value="${usuario.nome}" required />

        <label><fmt:message key="user.form.login"/></label>
        <input type="text" name="login" value="${usuario.login}" required />

        <label><fmt:message key="user.form.password"/></label>
        <input type="password" name="senha" ${not empty usuario ? "" : "required"} />

        <label><fmt:message key="user.form.role"/></label>
        <select name="papel" required>
            <option value="USER" ${usuario.papel == 'USER' ? 'selected' : ''}><fmt:message key="user.form.role.user"/></option>
            <option value="ADMIN" ${usuario.papel == 'ADMIN' ? 'selected' : ''}><fmt:message key="user.form.role.admin"/></option>
        </select>

        <button type="submit"><fmt:message key="button.save"/></button>
    </form>
    <p><a href="${pageContext.request.contextPath}/usuario/lista"><fmt:message key="button.back"/></a></p>
</div>
</body>
</html>
