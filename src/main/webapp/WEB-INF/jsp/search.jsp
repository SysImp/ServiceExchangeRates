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
<table><tr><td><h2>Allowed currencies:</h2></td>
    <c:forEach  items="${allowRequest}" var ="listC">
        <td><b><h2>${listC}</h2></b></td>
    </c:forEach>
</tr></table>

<div><h2>Example: USD_RUB</h2></div>

<div>
    <table>
        <tr><td><h2>Result: ${value}</h2></td></tr>
        <tr><td><form method="post">
            <input type="text" name="request" placeholder="Введите запрос" />
            <button type="submit">Добавить</button>
        </form></td></tr>
    </table>
    <div><h2>Request: ${request}</h2></div>
</div>


<a href="${pageContext.request.contextPath}/main">Main</a>

</body>

</html>