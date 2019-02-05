<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8" />
    <title>ExchangeRate-Service</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/css/style.css"/>
</head>
<body>
<h1>ExchangeRate-Service</h1>
<div>
    <a href="${pageContext.request.contextPath}/index">[Index]</a>
    <a href="${pageContext.request.contextPath}/combs">[Combs]</a>
    <a href="${pageContext.request.contextPath}/currencies">[Currencies]</a>
</div>

<div>
    <table border="1">
        <tr>
            <th>Идентификатор</th>
            <th>Наименование</th>
            <th>Описание</th>
            <th>Дата обновление</th>
        </tr>
        <c:forEach  items="${listCurrnecies}" var ="listC">
            <tr>
                <td>${listC.id}</td>
                <td>${listC.name}</td>
                <td>${listC.description}</td>
                <td>${listC.lastUpdate}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>