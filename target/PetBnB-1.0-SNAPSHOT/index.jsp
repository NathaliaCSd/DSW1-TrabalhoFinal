<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${param.lang != null ? param.lang : sessionScope.locale != null ? sessionScope.locale : 'pt_BR'}" scope="session" />
<fmt:setBundle basename="messages"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><fmt:message key="app.title"/></title>
    <link rel="stylesheet" href="style.css" />
</head>
<body>
<div class="container">
    <header>
        <h1><fmt:message key="index.header"/></h1>
        <p><fmt:message key="index.subtitle"/></p>
        <div class="lang-selector">
            <strong><fmt:message key="lang.select"/>:</strong>
            <a href="?lang=pt_BR"><fmt:message key="lang.portuguese"/></a> |
            <a href="?lang=en"><fmt:message key="lang.english"/></a>
        </div>
    </header>

    <c:choose>
        <c:when test="${not empty sessionScope.usuarioLogado}">
            <div class="box">
                <h2><fmt:message key="topnav.hello"><fmt:param value="${sessionScope.usuarioLogado.nome}"/></fmt:message></h2>
                <p><fmt:message key="home.description"/></p>
                <p>
                    <a class="button" href="${pageContext.request.contextPath}/casas"><fmt:message key="button.view_houses"/></a>
                    <a class="button" href="${pageContext.request.contextPath}/pets"><fmt:message key="button.view_pets"/></a>
                    <c:if test="${sessionScope.usuarioLogado.papel == 'ADMIN'}">
                        <a class="button" href="${pageContext.request.contextPath}/usuario/lista"><fmt:message key="button.manage_users"/></a>
                    </c:if>
                    <a class="button" href="${pageContext.request.contextPath}/usuario/logout"><fmt:message key="button.logout"/></a>
                </p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="box">
                <h2><fmt:message key="home.guest.title"/></h2>
                <p><fmt:message key="home.guest.description"/></p>
                <p>
                    <a class="button" href="${pageContext.request.contextPath}/login.jsp"><fmt:message key="button.login"/></a>
                    <a class="button" href="${pageContext.request.contextPath}/usuario/cadastro"><fmt:message key="button.register"/></a>
                </p>
            </div>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
