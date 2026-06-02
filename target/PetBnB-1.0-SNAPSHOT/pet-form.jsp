<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang != null ? param.lang : sessionScope.locale != null ? sessionScope.locale : 'pt_BR'}" scope="session" />
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="app.title"/> | <c:choose><c:when test="${not empty pet}"><fmt:message key="pet.form.title.edit"/></c:when><c:otherwise><fmt:message key="pet.form.title.create"/></c:otherwise></c:choose></title>
    <link rel="stylesheet" href="style.css" />
</head>
<body>
<div class="container">
    <header>
        <h1><c:choose><c:when test="${not empty pet}"><fmt:message key="pet.form.title.edit"/></c:when><c:otherwise><fmt:message key="pet.form.title.create"/></c:otherwise></c:choose></h1>
        <div class="lang-selector">
            <strong><fmt:message key="lang.select"/>:</strong>
            <a href="?lang=pt_BR"><fmt:message key="lang.portuguese"/></a> |
            <a href="?lang=en"><fmt:message key="lang.english"/></a>
        </div>
    </header>

    <c:if test="${not empty erro}">
        <div class="alert alert-error">${erro}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/pets/salvar" method="post" class="form">
        <c:if test="${not empty pet}">
            <input type="hidden" name="id" value="${pet.id}" />
        </c:if>

        <label><fmt:message key="pet.form.name"/></label>
        <input type="text" name="nome" value="${pet.nome}" required />

        <label><fmt:message key="pet.form.breed"/></label>
        <input type="text" name="raca" value="${pet.raca}" required />

        <label><fmt:message key="pet.form.age"/></label>
        <input type="number" name="idade" value="${pet.idade}" required />

        <label><fmt:message key="pet.form.size"/></label>
        <input type="text" name="porte" value="${pet.porte}" required />

        <label><fmt:message key="pet.form.neutered"/></label>
        <input type="checkbox" name="castrado" ${pet.castrado ? 'checked' : ''} />

        <label><fmt:message key="pet.form.description"/></label>
        <textarea name="descricao" rows="4">${pet.descricao}</textarea>

        <button type="submit"><fmt:message key="button.save"/></button>
    </form>
    <p><a href="${pageContext.request.contextPath}/pets"><fmt:message key="button.back"/></a></p>
</div>
</body>
</html>
