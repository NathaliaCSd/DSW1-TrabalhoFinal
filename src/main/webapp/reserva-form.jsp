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
    <title><fmt:message key="reservation.form.title"/> | <fmt:message key="app.title"/></title>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/style.css" />
    <link rel="stylesheet" href="css/responsive.css" />
</head>
<body class="main-layout">
<div class="container reservation-page">
    <header class="text-center mb-4 reservation-title">
        <h1><fmt:message key="reservation.form.title"/></h1>
        <p><fmt:message key="reservation.form.subtitle"/></p>
    </header>
    <c:if test="${not empty erro}">
        <div class="alert alert-danger">${erro}</div>
    </c:if>
    <div class="reservation-card card shadow-sm">
        <div class="reservation-header">
            <h2>${casa.nome}</h2>
            <p>${casa.descricao}</p>
        </div>
        <div class="card-body reservation-info">
            <div class="reservation-meta mb-4">
                <span><strong><fmt:message key="label.daily"/></strong> R$ ${casa.diaria}</span>
            </div>
            <form action="${pageContext.request.contextPath}/reservas/salvar" method="post" class="reservation-form">
                <input type="hidden" name="casaId" value="${casa.id}" />
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label><fmt:message key="label.arrival"/></label>
                        <input class="form-control" type="date" name="dataInicio" required />
                    </div>
                    <div class="form-group col-md-6">
                        <label><fmt:message key="label.departure"/></label>
                        <input class="form-control" type="date" name="dataFim" required />
                    </div>
                </div>
                <div class="reservation-actions">
                    <button class="btn btn-primary btn-lg" type="submit"><fmt:message key="button.reserve"/></button>
                    <a class="btn btn-outline-secondary btn-lg" href="${pageContext.request.contextPath}/casas"><fmt:message key="button.back"/></a>
                </div>
            </form>
        </div>
    </div>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>
