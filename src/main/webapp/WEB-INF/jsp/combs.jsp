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
        <tr><td><center>Комбинация курсов</center></td></tr>
        <tr>
            <td>
                <table border="1">
                    <tr>
                        <th>Отношение</th>
                        <th>Значение</th>
                    </tr>
                    <c:forEach  items="${listCombs}" var ="listC">
                        <tr>
                            <td>${listC.fromCurrency}_${listC.toCurrency}</td>
                            <td>${listC.value}</td>
                        </tr>
                    </c:forEach>

                </table>
            </td>
        </tr>
    </table>
</div>


<a href="${pageContext.request.contextPath}/main">Main</a>

</body>

</html>