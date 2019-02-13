<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="UTF-8" />
    <title>ExchangeRate-Service</title>
</head>
<body>
<h1>ExchangeRate-Service</h1>
<div>
    <a href="${pageContext.request.contextPath}/index">[Index]</a>
    <a href="${pageContext.request.contextPath}/rate">[Rate]</a>
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
