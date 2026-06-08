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
    <title><fmt:message key="reservations.title"/> | <fmt:message key="app.title"/></title>
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/style.css" />
    <link rel="stylesheet" href="css/responsive.css" />
</head>
<body class="main-layout">
<div class="container mt-5">
    <header class="text-center mb-4">
        <h1><fmt:message key="reservations.title"/></h1>
        <p><fmt:message key="reservations.subtitle"/></p>
    </header>

    <div class="mb-3">
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/casas"><fmt:message key="button.view_houses"/></a>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/pets"><fmt:message key="button.select_pet"/></a>
    </div>

    <c:if test="${empty reservas}">
        <div class="alert alert-info"><fmt:message key="no.reservations"/></div>
    </c:if>

    <div class="row">
        <c:forEach var="reserva" items="${reservas}">
            <div class="col-md-6 mb-4">
                <div class="card">
                    <div class="card-body">
                        <h4>${reserva.casa.nome}</h4>
                        <p><strong><fmt:message key="label.pet"/></strong> ${reserva.pet.nome}</p>
                        <p><strong><fmt:message key="label.address"/></strong> ${reserva.casa.endereco}</p>
                        <p><strong><fmt:message key="label.arrival"/></strong> ${reserva.dataInicio}</p>
                        <p><strong><fmt:message key="label.departure"/></strong> ${reserva.dataFim}</p>
                        <p><strong><fmt:message key="label.total"/></strong> R$ ${reserva.total}</p>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/main.js"></script>
</body>
</html>
