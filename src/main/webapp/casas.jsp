<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <fmt:setLocale
                value="${param.lang != null ? param.lang : sessionScope.locale != null ? sessionScope.locale : 'pt_BR'}"
                scope="session" />
            <fmt:setBundle basename="messages" />
            <!DOCTYPE html>
            <html lang="pt-BR">

            <head>
                <meta charset="UTF-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1" />
                <title>
                    <fmt:message key="houses.title" /> |
                    <fmt:message key="app.title" />
                </title>
                <link rel="stylesheet" href="css/bootstrap.min.css" />
                <link rel="stylesheet" href="css/style.css" />
                <link rel="stylesheet" href="css/responsive.css" />
            </head>

            <body class="main-layout">
                <div class="container mt-5">
                    <header class="mb-4">
                        <h1>
                            <fmt:message key="houses.title" />
                        </h1>
                        <p>
                            <fmt:message key="houses.subtitle" />
                        </p>
                    </header>

                    <div class="btn-group mb-4" role="group">
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/index.jsp">
                            <fmt:message key="topnav.home" />
                        </a>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/pets">
                            <fmt:message key="button.view_pets" />
                        </a>
                        <c:if test="${not empty sessionScope.petLogado}">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/reservas">
                                <fmt:message key="button.my_reservations" />
                            </a>
                            <a class="btn btn-warning" href="${pageContext.request.contextPath}/pets/sair">
                                <fmt:message key="button.logout_pet" />
                            </a>
                        </c:if>
                        <c:if test="${not empty sessionScope.usuarioLogado}">
                            <a class="btn btn-danger" href="${pageContext.request.contextPath}/usuario/logout">
                                <fmt:message key="button.logout" />
                            </a>
                        </c:if>

                    </div>

                    <c:if test="${not empty sessionScope.petLogado}">
                        <div class="alert alert-info">
                            <strong>
                                <fmt:message key="label.selected_pet" />
                            </strong> ${sessionScope.petLogado.nome}
                        </div>
                    </c:if>


                    <c:if test="${empty casas}">
                        <div class="alert alert-info">
                            <fmt:message key="no.houses" />
                        </div>
                    </c:if>

                    <div class="row">
                        <c:forEach var="casa" items="${casas}">
                            <div class="col-md-6 mb-4">
                                <div class="card h-100">
                                    <div class="card-body">
                                        <h4>${casa.nome}</h4>
                                        <p><strong>
                                                <fmt:message key="label.address" />
                                            </strong> ${casa.endereco}</p>
                                        <p>${casa.descricao}</p>
                                        <p><strong>
                                                <fmt:message key="label.daily" />
                                            </strong> R$ ${casa.diaria}</p>
                                        <p><strong>
                                                <fmt:message key="label.capacity" />
                                            </strong> ${casa.capacidade}</p>
                                        <div class="mt-3">
                                            <c:choose>
                                                <c:when test="${not empty sessionScope.petLogado}">
                                                    <a class="btn btn-primary"
                                                        href="${pageContext.request.contextPath}/reservas/novo?casaId=${casa.id}">
                                                        <fmt:message key="button.reserve" />
                                                    </a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a class="btn btn-outline-primary"
                                                        href="${pageContext.request.contextPath}/pets">
                                                        <fmt:message key="button.select_pet" />
                                                    </a>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <c:if
                                            test="${not empty sessionScope.usuarioLogado && sessionScope.usuarioLogado.papel == 'ADMIN'}">
                                            <div class="mt-3">
                                                <a class="btn btn-secondary"
                                                    href="${pageContext.request.contextPath}/casas/edicao?id=${casa.id}">
                                                    <fmt:message key="button.edit" />
                                                </a>
                                                <a class="btn btn-danger"
                                                    href="${pageContext.request.contextPath}/casas/excluir?id=${casa.id}"
                                                    onclick="return confirm('<fmt:message key=" confirm.delete" />');">
                                                <fmt:message key="button.delete" /></a>
                                            </div>
                                        </c:if>
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