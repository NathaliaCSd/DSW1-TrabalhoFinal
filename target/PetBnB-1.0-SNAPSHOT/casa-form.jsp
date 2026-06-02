<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang != null ? param.lang : sessionScope.locale != null ? sessionScope.locale : 'pt_BR'}" scope="session" />
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="app.title"/> | <fmt:message key="house.form.title.create"/></title>
    <link rel="stylesheet" href="style.css" />
</head>
<body>
<div class="container">
    <header>
        <h1><c:choose><c:when test="${not empty casa}"><fmt:message key="house.form.title.edit"/></c:when><c:otherwise><fmt:message key="house.form.title.create"/></c:otherwise></c:choose></h1>
        <div class="lang-selector">
            <strong><fmt:message key="lang.select"/>:</strong>
            <a href="?lang=pt_BR"><fmt:message key="lang.portuguese"/></a> |
            <a href="?lang=en"><fmt:message key="lang.english"/></a>
        </div>
    </header>

    <form action="${pageContext.request.contextPath}/casas/salvar" method="post" class="form">
        <c:if test="${not empty casa}">
            <input type="hidden" name="id" value="${casa.id}" />
        </c:if>
        <label><fmt:message key="house.form.name"/></label>
        <input type="text" name="nome" value="${casa.nome}" required />

        <label><fmt:message key="house.form.address"/></label>
        <input type="text" name="endereco" value="${casa.endereco}" required />

        <label><fmt:message key="house.form.description"/></label>
        <textarea name="descricao" rows="4">${casa.descricao}</textarea>

        <label><fmt:message key="house.form.daily"/></label>
        <input type="number" name="diaria" step="0.01" value="${casa.diaria}" required />

        <label><fmt:message key="house.form.capacity"/></label>
        <input type="number" name="capacidade" value="${casa.capacidade}" required />

        <button type="submit"><fmt:message key="button.save"/></button>
    </form>
    <p><a href="${pageContext.request.contextPath}/casas">Voltar para lista</a></p>
</div>
</body>
</html>
