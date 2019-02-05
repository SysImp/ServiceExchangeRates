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
    <ul>
        API ExchangeRate-Service:
        <li><a href="${pageContext.request.contextPath}/rest/rate">Show all currencies (JSON)</a></li>
        <li><a href="${pageContext.request.contextPath}/rest/rate/1">Show currency by ID(JSON)</a></li>
        <li><a href="${pageContext.request.contextPath}/rest/get/BTC_RUB">Show currency by NAME coupe(JSON)</a></li>
    </ul>
</div>
<div>
    <form method="post">
        <input type="text" value="${count}" name="count" placeholder="Enter amount">
        <select title="${from.description}" name="from">
            <c:forEach  items="${AllowCurrencies}" var ="listCurrencies">
                <c:set var="val" value="${from.name}"/>
                <option title="${listCurrencies.description}" ${listCurrencies.name == val ? "selected" : ""} value="${listCurrencies.name}">${listCurrencies.name}</option>
            </c:forEach>
        </select>
        <select title="${from.description}" name="to">
            <c:forEach  items="${AllowCurrencies}" var ="listCurrencies">
                <c:set var="val" value="${to.name}"/>
                <option title="${listCurrencies.description}" ${listCurrencies.name == val ? "selected" : ""} value="${listCurrencies.name}">${listCurrencies.name}</option>
            </c:forEach>
        </select>
        <input type="text" disabled value="${value}">
        <button type="submit">Convert</button>
    </form>
</div>
</body>
</html>