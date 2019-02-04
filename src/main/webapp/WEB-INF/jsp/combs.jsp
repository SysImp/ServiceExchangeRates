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
</div>
<div>
    <table>
        <tr><td><center>Комбинация курсов</center></td></tr>
        <tr>
            <td>
                    <c:forEach  items="${listCombs}" var ="listC" varStatus="status">
                            <c:set var="val" value="${countCurrency}"/>
                            <c:choose>
                                <c:when test="${status.getIndex() % (val) == 0 && status.getIndex() != 0}">
                                    </table>
                                </c:when>
                            </c:choose>
                            <c:choose>
                                <c:when test="${status.getIndex() % val == 0 }">
                                    <table width="500" border="1">
                                        <tr>
                                            <th>Отношение</th>
                                            <th>Значение</th>
                                            <th>Дата обновления</th>
                                        </tr>
                                </c:when>
                            </c:choose>
                            <tr>
                                <td>${listC.fromCurrency}_${listC.toCurrency}</td>
                                <td>${listC.value}</td>
                                <td>${listC.lastUpdate}</td>
                            </tr>
                    </c:forEach>
            </td>
        </tr>
    </table>
</div>
</body>
</html>