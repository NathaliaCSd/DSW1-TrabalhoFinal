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
    <title><fmt:message key="app.title"/> | <c:choose><c:when test="${not empty pet}"><fmt:message key="pet.form.title.edit"/></c:when><c:otherwise><fmt:message key="pet.form.title.create"/></c:otherwise></c:choose></title>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/style.css" />
    <link rel="stylesheet" href="css/responsive.css" />
</head>
<body class="main-layout">
<div class="container mt-5">
    <header class="mb-4">
        <h1><c:choose><c:when test="${not empty pet}"><fmt:message key="pet.form.title.edit"/></c:when><c:otherwise><fmt:message key="pet.form.title.create"/></c:otherwise></c:choose></h1>
    </header>

    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/pets/salvar" method="post">
        <c:if test="${not empty pet}">
            <input type="hidden" name="id" value="${pet.id}" />
        </c:if>

        <div class="form-group">
            <label><fmt:message key="pet.form.name"/></label>
            <input class="form-control" type="text" name="nome" value="${pet.nome}" required />
        </div>
        <div class="form-group">
            <label><fmt:message key="pet.form.breed"/></label>
            <input class="form-control" type="text" name="raca" value="${pet.raca}" required />
        </div>
        <div class="form-group">
            <label><fmt:message key="pet.form.age"/></label>
            <input class="form-control" type="number" name="idade" value="${pet.idade}" required />
        </div>
        <div class="form-group">
            <label><fmt:message key="pet.form.size"/></label>
            <input class="form-control" type="text" name="porte" value="${pet.porte}" required />
        </div>
        <div class="form-group form-check">
            <input class="form-check-input" type="checkbox" name="castrado" id="castrado" ${pet.castrado ? 'checked' : ''} />
            <label class="form-check-label" for="castrado"><fmt:message key="pet.form.neutered"/></label>
        </div>
        <div class="form-group">
            <label><fmt:message key="pet.form.description"/></label>
            <textarea class="form-control" name="descricao" rows="4">${pet.descricao}</textarea>
        </div>
        <button class="btn btn-primary" type="submit"><fmt:message key="button.save"/></button>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/pets"><fmt:message key="button.back"/></a>
    </form>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>
