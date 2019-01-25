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
    <table>
        <tr><td><center>Значения валют относительно USD</center></td></tr>
        <tr>
            <td>
                <table border="1">
                    <tr>
                        <th>Наименование</th>
                        <th>Описание</th>
                        <th>Значение</th>
                        <th>Обратное значение</th>
                        <th>Дата последнего обновления</th>
                    </tr>
                    <c:forEach  items="${listAllowCurrencies}" var ="listC">
                        <tr>
                            <td>${listC.name}</td>
                            <td>${listC.description}</td>
                            <td>${listC.name}_USD: <b>${listC.value}</b></td>
                            <td>USD_${listC.name}: <b>${1/listC.value}</b></td>
                            <td>${listC.lastUpdate}</td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
    </table>
</div>
<a href="${pageContext.request.contextPath}/combs">Combs List</a>
<a href="${pageContext.request.contextPath}/search">Search</a>

</body>

</html>